package com.trigyn.jws.dynarest.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.vo.FileInfo;
import com.trigyn.jws.dbutils.vo.FileInfo.FileType;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;
import com.trigyn.jws.dynarest.service.JwsDynamicRestDetailService;
import com.trigyn.jws.dynarest.vo.RestApiDetails;
import com.trigyn.jws.usermanagement.security.config.Authorized;
import com.trigyn.jws.usermanagement.utils.Constants;

import freemarker.core.StopException;

@RestController
public class DynamicRestController {

	private static final Logger LOGGER = LogManager.getLogger(DynamicRestController.class);

	private static final String METHOD_SIGNATURE_MESSAGE = "Make sure you have the method signature correct. Signature should be similar to : - public T methodName(HttpServletRequest request, Map<String, Object> requestParameters, Map<String, Object> resultSetParameters, UserDetailsVO, details) {}";

	private static final String FILE_METHOD_SIGNATURE_MESSAGE = "Make sure you have the method signature correct. Signature should be similar to : - public FileInfo methodName(MultipartFile [] files, HttpServletRequest request, Map<String, Object> requestParameters, Map<String, Object> resultSetParameters, UserDetailsVO, details) {}";

	@Autowired
	private JwsDynamicRestDetailService jwsService = null;

	@Autowired
	private PropertyMasterService propertyMasterService = null;

	@Autowired
	private FileUploadController fileUploadController = null;

	@Autowired
	private SessionLocaleResolver sessionLocaleResolver = null;

	@Autowired
	private IUserDetailsService userDetailsService = null;

	@Autowired
	private ActivityLog activitylog = null;

	@RequestMapping(value = { "/api/**", "/japi/**" })
	@Authorized(moduleName = Constants.DYNAMICREST)
	@ResponseBody
	public ResponseEntity<?> callDynamicEntity(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws Exception {
		String requestUri = httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length());

		Map<String, FileInfo> fileMap = new HashMap<>();
		if (requestUri.startsWith("/japi/")) {
			requestUri = requestUri.replaceFirst("/japi/", "");
		} else {
			requestUri = requestUri.replaceFirst("/api/", "");
		}
		RestApiDetails restApiDetails = jwsService.getRestApiDetails(requestUri);
		Map<String, Object> requestParams;
		if (restApiDetails == null) {
			logActivity(restApiDetails, false, null);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND);
		}
		String requestType = httpServletRequest.getMethod();
		if (Boolean.FALSE.equals(requestType.equals(restApiDetails.getMethodType()))) {
			logActivity(restApiDetails, false, null);
			return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), HttpStatus.METHOD_NOT_ALLOWED);
		}
		requestParams = validateAndProcessRequestParams(httpServletRequest, restApiDetails);
		try {
			Object response = null;
			FileInfo fileInfoObject = null;
			try {

				if (httpServletRequest instanceof StandardMultipartHttpServletRequest) {
					StandardMultipartHttpServletRequest multipartRequest = (StandardMultipartHttpServletRequest) httpServletRequest;
					int iFileCounter = 0;
					String fileCopyPath = propertyMasterService.findPropertyMasterValue("file-copy-path");
					for (Map.Entry<String, MultipartFile> uf : multipartRequest.getFileMap().entrySet()) {
						String absolutePath = fileCopyPath + File.separator + UUID.randomUUID().toString();
						FileInfo fileInfo = new FileInfo();
						fileInfo.setFileId(uf.getValue().getName());
						fileInfo.setFileName(uf.getValue().getOriginalFilename());
						fileInfo.setFileType(FileType.Physical);
						fileInfo.setSizeInBytes(uf.getValue().getSize());
						fileInfo.setAbsolutePath(absolutePath);
						fileInfo.setCreatedTime(new Date().getTime());
						fileMap.put("file" + (iFileCounter++), fileInfo);
						/**
                         * This needs to be changed in future. 
                         * Below line is kept to support backward compatibility in HRS,
                         * as we have already supported code with key having uf.getKey().
                         * 
                         * uf.getKey() returns the input type file's id.
                         */
                        fileMap.put(uf.getKey(), fileInfo);
						uf.getValue().transferTo(new File(absolutePath));
					}
				}

				Map<String, Object> queriesResponse = jwsService.executeDAOQueries(restApiDetails.getDynamicId(),
						requestParams, fileMap);

				response = jwsService.createSourceCodeAndInvokeServiceLogic(fileMap, httpServletRequest, requestParams,
						queriesResponse, restApiDetails);
				String responseType = restApiDetails.getReponseType();
				if (StringUtils.isBlank(responseType) == false && responseType.equals("email/xml")) {
					Map<String, Object> combParam = new HashMap<>();
					combParam.putAll(requestParams);
					combParam.putAll(queriesResponse);
					response = jwsService.executeSendMail(response, combParam);
				}
				
				if((response instanceof FileInfo) == false
						&& restApiDetails.getReponseType().equalsIgnoreCase("application/octet-stream")) {
					if(response != null && "404".equalsIgnoreCase(response.toString())) {
						httpServletResponse.sendError(HttpStatus.NOT_FOUND.value(),
								"Invalid file information.");
						return new ResponseEntity<>(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND);
					} else {
						httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(),
								"Invalid file information.");
						return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED, HttpStatus.PRECONDITION_FAILED);
					}
				}
				
				if (response instanceof FileInfo
						&& restApiDetails.getReponseType().equalsIgnoreCase("application/octet-stream")) {
					fileInfoObject = (FileInfo) response;
				}

			} catch (IllegalArgumentException a_exception) {
				LOGGER.error("Error occured while invoking the method ", a_exception);
				httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(), METHOD_SIGNATURE_MESSAGE);
			} catch (InvocationTargetException a_exception) {
				LOGGER.error("Error occured while invoking the method ", a_exception);
			} catch (NoSuchMethodException a_exception) {
				LOGGER.error("Error occured while invoking the method ", a_exception);
				httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(), METHOD_SIGNATURE_MESSAGE);
			} catch (ClassNotFoundException a_exception) {
				LOGGER.error("Error occured while invoking the method ", a_exception);
				httpServletResponse.sendError(HttpStatus.NOT_FOUND.value(),
						"The class was not found in the mentioned package.");
			}
			buildResponseEntity(httpServletRequest, httpServletResponse, restApiDetails);
			if (restApiDetails.getReponseType().equalsIgnoreCase("application/octet-stream")) {
				if (fileInfoObject == null) {
					httpServletResponse.sendError(HttpStatus.UNPROCESSABLE_ENTITY.value(),
							FILE_METHOD_SIGNATURE_MESSAGE);
					logActivity(restApiDetails, false, (String) requestParams.get("isFromRestAPI"));
					return new ResponseEntity<>(FILE_METHOD_SIGNATURE_MESSAGE, HttpStatus.UNPROCESSABLE_ENTITY);
				}

				String filePathStr = "", fileName = "", mimeType = "";
				byte[] file;
				if (FileType.FileBin.equals(fileInfoObject.getFileType())) {
					Map<String, Object> fileBinMap = fileUploadController.getFileFromFileUploadId(
							fileInfoObject.getFileId(), httpServletRequest, httpServletResponse);
					fileName = fileBinMap.get("fileName").toString();
					file = (byte[]) fileBinMap.get("file");
					mimeType = (String) fileBinMap.get("mimeType");
				} else {
					filePathStr = fileInfoObject.getAbsolutePath();
					fileName = fileInfoObject.getFileName();

					InputStream in = new FileInputStream(filePathStr);
					file = ByteStreams.toByteArray(in);
					in.close();
					if (fileInfoObject.getMimeType() != null) {
						mimeType = fileInfoObject.getMimeType();
					} else {
						mimeType = httpServletRequest.getSession().getServletContext().getMimeType(filePathStr);

					}
				}

				String downloadType = "attachment";
				if (fileInfoObject.getReturnAction() == 2) {
					downloadType = "inline";
				}

				httpServletResponse.setContentType(mimeType);
				httpServletResponse.setHeader("Content-Disposition", downloadType + "; filename=\"" + fileName + "\"");
				httpServletResponse.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				httpServletResponse.setCharacterEncoding("utf-8");
				if (StringUtils.isBlank(httpServletResponse.getContentType()) == false) {
					httpServletResponse.flushBuffer();
				}

				InputStreamResource streamResource = new InputStreamResource(new ByteArrayInputStream(file));
				logActivity(restApiDetails, true, (String) requestParams.get("isFromRestAPI"));
				return new ResponseEntity<InputStreamResource>(streamResource, HttpStatus.OK);
			} else {
				if (StringUtils.isBlank(restApiDetails.getHeaderJson()) == false
						&& StringUtils.isBlank(httpServletResponse.getContentType()) == false) {
					httpServletResponse.flushBuffer();
				}
				logActivity(restApiDetails, true, (String) requestParams.get("isFromRestAPI"));
				if (response instanceof ResponseEntity<?>) {
					return (ResponseEntity<?>) response;
				}
				return new ResponseEntity<>(response, HttpStatus.OK);
			}

		} catch (StopException a_exception) {
			logActivity(restApiDetails, false, (String) requestParams.get("isFromRestAPI"));
			return new ResponseEntity<>(a_exception.getMessageWithoutStackTop(), HttpStatus.EXPECTATION_FAILED);
		} catch (Throwable a_throwable) {
			logActivity(restApiDetails, false, (String) requestParams.get("isFromRestAPI"));
			LOGGER.error("Error occurred while processing request: ", a_throwable);
			Objects.requireNonNull(a_throwable);
			Throwable rootCause = a_throwable;
			while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
				if (rootCause instanceof StopException) {
					return new ResponseEntity<>(((StopException) rootCause).getMessageWithoutStackTop(),
							HttpStatus.EXPECTATION_FAILED);
				}
				rootCause = rootCause.getCause();
			}
			if (a_throwable instanceof FileNotFoundException) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND.getReasonPhrase(),
						HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Purpose of this method is to log activities</br>
	 * in REST API EXECUTION Module.
	 * 
	 * @author Bibhusrita.Nayak
	 * @param restApiDetails
	 * @param action
	 * @param isFromRestAPI
	 * @throws Exception
	 */
	private void logActivity(RestApiDetails restApiDetails, Boolean action, String isFromRestAPI) throws Exception {
		Map<String, String> requestParams = new HashMap<>();
		UserDetailsVO detailsVO = userDetailsService.getUserDetails();
		requestParams.put("entityName", restApiDetails.getDynamicRestUrl());
		Date activityTimestamp = new Date();
		if (action == true) {
			requestParams.put("action", Constants.Action.APIEXECUTED.getAction());
		} else if (action == false) {
			requestParams.put("action", Constants.Action.APIEXECFAILED.getAction());
		}
		JwsDynamicRestDetail query = jwsService.getDynamicRestDetailsByName(restApiDetails.getMethodName());
		if (query.getJwsDynamicRestTypeId() == Constants.Changetype.CUSTOM.getChangeTypeInt()) {
			requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
		} else if (query.getJwsDynamicRestTypeId() == Constants.Changetype.SYSTEM.getChangeTypeInt()) {
			requestParams.put("typeSelect", Constants.Changetype.SYSTEM.getChangetype());
		}
		requestParams.put("masterModuleType", Constants.Modules.DYNAMICREST.getModuleName());
		requestParams.put("userName", detailsVO.getUserName());
		requestParams.put("message", "");
		requestParams.put("date", activityTimestamp.toString());
		if (isFromRestAPI != null && ("false".equalsIgnoreCase(isFromRestAPI)) == false ) {
			activitylog.activitylog(requestParams, isFromRestAPI);
		}
	}

	private Map<String, Object> validateAndProcessRequestParams(HttpServletRequest httpServletRequest,
			RestApiDetails restDetails) {
		Map<String, Object> requestParams = new HashMap<>();

		for (String requestParamKey : httpServletRequest.getParameterMap().keySet()) {

			requestParams.put(requestParamKey, httpServletRequest.getParameter(requestParamKey));
		}
		return requestParams;
	}

	private void buildResponseEntity(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			RestApiDetails restDetails) throws IOException {
		httpServletResponse.setHeader("content-type", restDetails.getReponseType());
		String localeId = sessionLocaleResolver.resolveLocale(httpServletRequest).toString();
		httpServletResponse.setHeader("Content-Language", localeId);

		Gson gson = new Gson();
		if (StringUtils.isBlank(restDetails.getHeaderJson()) == false) {
			Map<String, String> headerConfig = gson.fromJson(restDetails.getHeaderJson(), Map.class);
			headerConfig.forEach((key, value) -> {
				if ("Content-Language".equals(key) == false) {
					httpServletResponse.setHeader(key, value);
				}
			});
		}

	}

}
