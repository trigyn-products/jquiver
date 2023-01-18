package com.trigyn.jws.dynarest.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.net.ssl.SSLException;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.CustomResponseEntity;
import com.trigyn.jws.dbutils.vo.FileInfo;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynarest.dao.JwsDynamicRestDAORepository;
import com.trigyn.jws.dynarest.dao.JwsDynamicRestDetailsRepository;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;
import com.trigyn.jws.dynarest.utils.Constants;
import com.trigyn.jws.dynarest.vo.AttachmentXMLVO;
import com.trigyn.jws.dynarest.vo.Email;
import com.trigyn.jws.dynarest.vo.EmailAttachedFile;
import com.trigyn.jws.dynarest.vo.EmailBodyXMLVO;
import com.trigyn.jws.dynarest.vo.EmailListXMLVO;
import com.trigyn.jws.dynarest.vo.EmailXMLVO;
import com.trigyn.jws.dynarest.vo.FailedRecipientXMLVO;
import com.trigyn.jws.dynarest.vo.FailedRecipientsXMLVO;
import com.trigyn.jws.dynarest.vo.RecepientXMLVO;
import com.trigyn.jws.dynarest.vo.RecepientsXMLVO;
import com.trigyn.jws.dynarest.vo.RestApiDaoQueries;
import com.trigyn.jws.dynarest.vo.RestApiDetails;
import com.trigyn.jws.dynarest.vo.WebClientAttacmentVO;
import com.trigyn.jws.dynarest.vo.WebClientParamVO;
import com.trigyn.jws.dynarest.vo.WebClientRequestVO;
import com.trigyn.jws.dynarest.vo.WebClientXMLVO;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;
import com.trigyn.jws.usermanagement.security.config.JwsUserDetailsService;
import com.trigyn.jws.usermanagement.security.config.JwtUtil;
import com.trigyn.jws.usermanagement.vo.JwsAuthenticationType;
import com.trigyn.jws.usermanagement.vo.MultiAuthSecurityDetailsVO;

import io.netty.handler.ssl.SslContextBuilder;
import net.minidev.json.JSONObject;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Service
@Transactional
@Lazy
public class JwsDynamicRestDetailService {

	private final static Logger logger = LogManager.getLogger(JwsDynamicRestDetailService.class);

	private static final String Logger = null;

	@Autowired
	private TemplatingUtils templatingUtils = null;

	@Autowired
	private JwsDynamicRestDAORepository dynamicRestDAORepository = null;

	@Autowired
	private JwsDynarestDAO dynarestDAO = null;

	@Autowired
	private JwsDynamicRestDetailsRepository dyanmicRestDetailsRepository = null;

	@Autowired
	private IUserDetailsService detailsService = null;

	@Autowired
	@Lazy
	private SendMailService sendMailService = null;

	@Autowired
	private ApplicationContext applicationContext = null;

	@Autowired
	@Qualifier("file-system-storage")
	private FilesStorageService storageService = null;

	@Autowired
	private DBTemplatingService templatingService = null;

	@Autowired
	@Lazy
	private JwtUtil jwtUtil = null;

	@Autowired
	@Lazy
	private JwsUserDetailsService jwsUserDetailsService = null;

	@Autowired
	@Lazy
	private UserDetailsService userDetailsService = null;

	@Autowired
	private ApplicationSecurityDetails applicationSecurityDetails = null;

	public Object createSourceCodeAndInvokeServiceLogic(Map<String, FileInfo> files,
			HttpServletRequest httpServletRequest, Map<String, Object> requestParameterMap,
			Map<String, Object> daoResultSets, RestApiDetails restApiDetails) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> apiDetails = objectMapper.convertValue(restApiDetails, Map.class);
		requestParameterMap.putAll(apiDetails);

		if (restApiDetails.getPlatformId().equals(Constants.Platforms.JAVA.getPlatform())) {
			return invokeAndExecuteOnFileJava(files, httpServletRequest, daoResultSets, restApiDetails);
		} else if (restApiDetails.getPlatformId().equals(Constants.Platforms.FTL.getPlatform())) {
			return invokeAndExecuteFTL(files, httpServletRequest, requestParameterMap, daoResultSets, restApiDetails);
		} else if (restApiDetails.getPlatformId().equals(Constants.Platforms.JAVASCRIPT.getPlatform())) {
			return invokeAndExecuteFileOnJavascript(files, httpServletRequest, requestParameterMap, daoResultSets,
					restApiDetails);
		} else {
			return null;
		}
	}

	private Object invokeAndExecuteFTL(Map<String, FileInfo> files, HttpServletRequest httpServletRequest,
			Map<String, Object> requestParameterMap, Map<String, Object> daoResultSets, RestApiDetails restApiDetails)
			throws Exception {
		if (restApiDetails.getServiceLogic() != null
				|| Boolean.FALSE.equals("".equals(restApiDetails.getServiceLogic()))) {
			requestParameterMap.putAll(daoResultSets);
			requestParameterMap.putAll(files);

			Map<String, String> headerMap = new HashMap<>();
			Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
			if (headerNames != null) {
				while (headerNames.hasMoreElements()) {
					String header = headerNames.nextElement();
					headerMap.put(header, httpServletRequest.getHeader(header));

				}
			}
			requestParameterMap.put("requestHeaders", headerMap);
			requestParameterMap.put("session", httpServletRequest.getSession());

			StringBuilder resultStringBuilder = new StringBuilder();
			resultStringBuilder.append(restApiDetails.getServiceLogic());

			return templatingUtils.processTemplateContents(resultStringBuilder.toString(), "service",
					requestParameterMap);
		}
		return null;
	}

	public Object invokeAndExecuteOnFileJava(Map<String, FileInfo> files, HttpServletRequest httpServletRequest,
			Map<String, Object> daoResultSets, RestApiDetails restApiDetails) throws Exception, ClassNotFoundException,
			NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		Class<?> serviceClass = Class.forName(restApiDetails.getServiceLogic(), Boolean.TRUE,
				this.getClass().getClassLoader());
		Object classInstance = serviceClass.getDeclaredConstructor().newInstance();

		Method serviceLogicMethod = serviceClass.getDeclaredMethod(restApiDetails.getMethodName(),
				HttpServletRequest.class, Map.class, Map.class, UserDetailsVO.class);
		try {
			Method applicationContextMethod = serviceClass.getDeclaredMethod("setApplicationContext",
					ApplicationContext.class);
			applicationContextMethod.invoke(classInstance, applicationContext);
		} catch (NoSuchMethodException a_nsme) {
			logger.warn(
					"No method found for setting application context. Create method setApplicationContext to set applicationContext",
					a_nsme);
		} catch (SecurityException a_se) {
			logger.error("Security exception occured while invoking setApplication context ", a_se);
		} catch (IllegalAccessException a_iae) {
			logger.error("IllegalAccessException occured while invoking setApplication context ", a_iae);
		} catch (IllegalArgumentException a_iae) {
			logger.error("IllegalArgumentException occured while invoking setApplication context ", a_iae);
		} catch (InvocationTargetException a_ite) {
			logger.error("InvocationTargetException occured while invoking setApplication context ", a_ite);
		}
		return serviceLogicMethod.invoke(classInstance, httpServletRequest, files, daoResultSets,
				detailsService.getUserDetails());
	}

	public Object invokeAndExecuteFileOnJavascript(Map<String, FileInfo> files, HttpServletRequest httpServletRequest,
			Map<String, Object> requestParameterMap, Map<String, Object> daoResultSets, RestApiDetails restApiDetails)
			throws Exception {
		TemplateVO templateVO = templatingService.getTemplateByName("script-util");
		StringBuilder resultStringBuilder = new StringBuilder();
		resultStringBuilder.append(templateVO.getTemplate()).append("\n");

		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

		ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("nashorn");
		scriptEngine.put("requestDetails", requestParameterMap);
		scriptEngine.put("daoResults", daoResultSets);
		try {
			if (!(httpServletRequest instanceof StandardMultipartHttpServletRequest)) {
				if (httpServletRequest != null) {
					BufferedReader reader = httpServletRequest.getReader();
					if (reader != null)
						scriptEngine.put("requestBody", IOUtils.toString(reader));
				}
			}
		} catch (Exception e) {
			logger.error("Error occured while invoking the method ", e);
		}

		Map<String, String> headerMap = new HashMap<>();
		Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
				String header = headerNames.nextElement();
				headerMap.put(header, httpServletRequest.getHeader(header));

			}
		}

		scriptEngine.put("requestHeaders", headerMap);
		scriptEngine.put("session", httpServletRequest.getSession());

		if (files != null && files.size() > 0) {
			scriptEngine.put("files", files);
		}
		resultStringBuilder.append(restApiDetails.getServiceLogic());
		return scriptEngine.eval(resultStringBuilder.toString());
	}

	public RestApiDetails getRestApiDetails(String requestUri) {
		return dyanmicRestDetailsRepository.findByJwsDynamicRestUrl(requestUri);
	}

	public Map<String, Object> executeDAOQueries(String dynarestId, Map<String, Object> parameterMap,
			Map<String, FileInfo> files) throws Exception {
		List<RestApiDaoQueries> apiDaoQueries = dynamicRestDAORepository.getRestApiDaoQueriesByApiId(dynarestId);
		Map<String, Object> resultSetMap = new HashMap<>();
		for (RestApiDaoQueries restApiDaoQueries : apiDaoQueries) {
			String dataSourceId = restApiDaoQueries.getDataSourceId();
			Integer queryType = restApiDaoQueries.getQueryType();
			String queryContent = templatingUtils.processTemplateContents(restApiDaoQueries.getJwsDaoQueryTemplate(),
					"apiQuery", parameterMap);
			/* Added for Rest Client Attachment */
			if (files != null && files.size() > 0) {
				resultSetMap.put("files", files);
			}
			// Ends here
			if (Constants.QueryType.WC.getQueryType() == queryType) {
				CustomResponseEntity customResponseEntity = new CustomResponseEntity();
				try {
					JAXBContext jaxbContext = JAXBContext.newInstance(WebClientXMLVO.class);
					Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
					StringReader reader = new StringReader(queryContent);
					WebClientXMLVO webClientXMLVO = (WebClientXMLVO) unmarshaller.unmarshal(reader);
					if (webClientXMLVO.getWebClientURL() != null
							&& webClientXMLVO.getWebClientURL().equalsIgnoreCase("about:blank") == false) {
						Date requestTime = new Date();
						Mono<ResponseEntity<String>> responseContent = getWebClientResponse(webClientXMLVO);
						ResponseEntity<String> responseString = responseContent.block();
						Date responTime = new Date();
						customResponseEntity = convertResponseToVO(responseString);
						customResponseEntity.setResponseDuration(responTime.getTime() - requestTime.getTime());
					}
					parameterMap.put(restApiDaoQueries.getJwsResultVariableName(), customResponseEntity);
					resultSetMap.put(restApiDaoQueries.getJwsResultVariableName(), customResponseEntity);
				} catch (Throwable a_thr) {
					logger.error("Error occurred while establishing connection ", a_thr);
					String stacktrace = ExceptionUtils.getStackTrace(a_thr);
					customResponseEntity.setResponseBody(stacktrace);
					customResponseEntity.setResponseStatusCode(500);
					parameterMap.put(restApiDaoQueries.getJwsResultVariableName() + "_error",
							ExceptionUtils.getStackTrace(a_thr));
					resultSetMap.put(restApiDaoQueries.getJwsResultVariableName(), customResponseEntity);
				}
			} else {
				List<Map<String, Object>> resultSet = new ArrayList<>();
				if (Constants.QueryType.DML.getQueryType() == queryType) {
					try {
						Integer affectedRowCount = dynarestDAO.executeDMLQueries(dataSourceId, queryContent,
								parameterMap);
						resultSetMap.put(restApiDaoQueries.getJwsResultVariableName(), affectedRowCount);
						parameterMap.put(restApiDaoQueries.getJwsResultVariableName(), affectedRowCount);
					} catch (Throwable a_thr) {
						resultSetMap.put(restApiDaoQueries.getJwsResultVariableName(), -1);
						parameterMap.put(restApiDaoQueries.getJwsResultVariableName(), -1);
						resultSetMap.put(restApiDaoQueries.getJwsResultVariableName() + "_error",
								ExceptionUtils.getStackTrace(a_thr));
						parameterMap.put(restApiDaoQueries.getJwsResultVariableName() + "_error",
								ExceptionUtils.getStackTrace(a_thr));
					}
				} else {
					try {
						resultSet = dynarestDAO.executeQueries(dataSourceId, queryContent, parameterMap);
						resultSetMap.put(restApiDaoQueries.getJwsResultVariableName(), resultSet);
						parameterMap.put(restApiDaoQueries.getJwsResultVariableName(), resultSet);
					} catch (Throwable a_thr) {
						resultSetMap.put(restApiDaoQueries.getJwsResultVariableName(), new ArrayList<>());
						parameterMap.put(restApiDaoQueries.getJwsResultVariableName(), new ArrayList<>());
						resultSetMap.put(restApiDaoQueries.getJwsResultVariableName() + "_error",
								ExceptionUtils.getStackTrace(a_thr));
						parameterMap.put(restApiDaoQueries.getJwsResultVariableName() + "_error",
								ExceptionUtils.getStackTrace(a_thr));
					}
				}
			}
		}
		return resultSetMap;
	}

	public ResponseEntity<?> executeSendMail(Object emailXMLObj, Map<String, Object> requestParams) {
		String emailXMLContent = (String) emailXMLObj;
		JAXBContext jaxbContext = null;
		Unmarshaller unmarshaller = null;
		EmailListXMLVO emailListObj = null;
		JsonArray jsonArray = new JsonArray();
		try {
			jaxbContext = JAXBContext.newInstance(EmailListXMLVO.class);
			unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(emailXMLContent);
			emailListObj = (EmailListXMLVO) unmarshaller.unmarshal(reader);

			if (emailListObj != null && emailListObj.getEmailXMLVO() != null) {
				for (EmailXMLVO emailObj : emailListObj.getEmailXMLVO()) {
					JsonObject json = new JsonObject();
					List<EmailAttachedFile> attachedFileList = new ArrayList<>();
					RecepientsXMLVO recepientsXMLVO = emailObj.getRecepientsXMLVO();
					List<RecepientXMLVO> recepientXMLVOList = recepientsXMLVO.getRecepientXMLVO();
					/* Written for Failure Mail Notification */
					FailedRecipientsXMLVO failedrecepientsXMLVO = emailObj.getFailedrecepientsXMLVO();
					List<FailedRecipientXMLVO> failedrecepientXMLVOList = failedrecepientsXMLVO
							.getFailedrecipientXMLVO();
					List<AttachmentXMLVO> attachmentXMLVOList = emailObj.getAttachmentXMLVO();
					if (CollectionUtils.isEmpty(attachmentXMLVOList) == false) {
						for (AttachmentXMLVO attachmentXMLVO : attachmentXMLVOList) {
							EmailAttachedFile emailAttachedFile = new EmailAttachedFile();
							File attachedFile = null;
							if (attachmentXMLVO.getType().equals(Constants.FILE_ATTACHMENT_FILEBIN)) {
								String fileUploadId = attachmentXMLVO.getFilePath();
								Integer isAllowed = storageService.hasPermission(null, null, fileUploadId,
										Constants.VIEW_FILE_VALIDATOR, new HashMap<>());
								if (isAllowed > 0) {
									Map<String, Object> fileInfo = storageService.load(fileUploadId);
									if (fileInfo != null) {
										byte[] fileByte = (byte[]) fileInfo.get("file");
										attachedFile = new File(attachmentXMLVO.getFileName());
										try (FileOutputStream fos = new FileOutputStream(attachedFile)) {
											fos.write(fileByte);
										} catch (Exception exception) {
											logger.error("Error occurred while accessing file", exception);
											return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
													.body("Error occurred while accessing file");
										}
									} else {
										logger.error("Error occurred while accessing file");
										return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
												.body("Error occurred while accessing file");
									}
								}

							} else if (attachmentXMLVO.getType().equals(Constants.FILE_ATTACHMENT_FILESYSTEM)
									|| attachmentXMLVO.getType().equals(Constants.FILE_ATTACHMENT_UPLOADEDFILE)) {
								File aFile = new File(attachmentXMLVO.getFilePath());
								if (aFile != null && aFile.exists()) {
									attachedFile = new File(attachmentXMLVO.getFileName());
									InputStream in = new FileInputStream(attachmentXMLVO.getFilePath());
									byte[] byteArray = ByteStreams.toByteArray(in);
									in.close();
									try (FileOutputStream fos = new FileOutputStream(attachedFile)) {
										fos.write(byteArray);
									} catch (Exception exception) {
										logger.error("Error occurred while accessing file", exception);
										return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
												.body("Error occurred while accessing file");
									}
								}
							}
							if (attachedFile != null) {
								emailAttachedFile.setFile(attachedFile);
								if (attachmentXMLVO.getHasEmbeddedImage() != null
										&& attachmentXMLVO.getHasEmbeddedImage() != "") {
									emailAttachedFile.setIsEmbeddedImage(true);
									emailAttachedFile.setEmbeddedImageValue(attachmentXMLVO.getHasEmbeddedImage());
								}
								attachedFileList.add(emailAttachedFile);
							}
						}
					}
					List<String> toEmailIdList = new ArrayList<>();
					List<String> ccEmailIdList = new ArrayList<>();
					List<String> bccEmailIdList = new ArrayList<>();
					List<String> frEmailIdList = new ArrayList<>();// Created for Failure Mail notification
					StringJoiner toEmailIdStrJoiner = new StringJoiner(", ");
					StringJoiner ccEmailIdStrJoiner = new StringJoiner(", ");
					StringJoiner bccEmailIdStrJoiner = new StringJoiner(", ");
					StringJoiner frEmailIdStrJoiner = new StringJoiner(", ");// Created for Failure Mail notification

					for (RecepientXMLVO recepientXMLVO : recepientXMLVOList) {
						if (recepientXMLVO.getRecepientType().equals("to") == true
								&& toEmailIdList.contains(recepientXMLVO.getMailId()) == false) {
							toEmailIdStrJoiner.add(recepientXMLVO.getMailId());
							toEmailIdList.add(recepientXMLVO.getMailId());
						}
						if (recepientXMLVO.getRecepientType().equals("cc") == true
								&& ccEmailIdList.contains(recepientXMLVO.getMailId()) == false) {
							ccEmailIdStrJoiner.add(recepientXMLVO.getMailId());
							ccEmailIdList.add(recepientXMLVO.getMailId());
						}
						if (recepientXMLVO.getRecepientType().equals("bcc") == true
								&& bccEmailIdList.contains(recepientXMLVO.getMailId()) == false) {
							bccEmailIdStrJoiner.add(recepientXMLVO.getMailId());
							bccEmailIdList.add(recepientXMLVO.getMailId());
						}
					}
					Email email = new Email();
					email.setIsAuthenticationEnabled(applicationSecurityDetails.getIsAuthenticationEnabled());
					email.setLoggedInUserRole(detailsService.getUserDetails().getRoleIdList());
					/* Written for Failure Mail Notification */
					for (FailedRecipientXMLVO failedrecepientXMLVO : failedrecepientXMLVOList) {
						frEmailIdStrJoiner.add(failedrecepientXMLVO.getMailId());
						frEmailIdList.add(failedrecepientXMLVO.getMailId());
					}
					email.setInternetAddressToArray(InternetAddress.parse(toEmailIdStrJoiner.toString()));
					email.setInternetAddressCCArray(InternetAddress.parse(ccEmailIdStrJoiner.toString()));
					email.setInternetAddressBCCArray(InternetAddress.parse(bccEmailIdStrJoiner.toString()));
					email.setFailedrecipient(InternetAddress.parse(frEmailIdStrJoiner.toString()));// Added for Failure
																									// Mail Notification
					if (emailObj.getSenderXMLVO() != null) {
						email.setMailFrom(InternetAddress.parse(emailObj.getSenderXMLVO().getMailId()));
						email.setMailFromName(emailObj.getSenderXMLVO().getName());
					}
					EmailBodyXMLVO emailBodyXMLVO = emailObj.getBody();
					String body = "";
					if (emailBodyXMLVO.getContent() != null
							&& emailBodyXMLVO.getContent() == Constants.EMAIL_CONTENT_TYPE_TWO) {
						TemplateVO templateVO = templatingService.getTemplateByName(emailBodyXMLVO.getData().trim());
						body = templatingUtils.processTemplateContents(templateVO.getTemplate(),
								templateVO.getTemplateName(), requestParams);
					} else if (emailBodyXMLVO.getContent() != null
							&& emailBodyXMLVO.getContent() == Constants.EMAIL_CONTENT_TYPE_THREE) {
						body = templatingUtils.processTemplateContents(emailBodyXMLVO.getData(), "", requestParams);
					} else {
						body = emailBodyXMLVO.getData();
					}
					email.setBody(body);
					email.setSubject(emailObj.getSubject());
					email.setAttachementsArray(attachedFileList);
					email.setSeparatemails(recepientsXMLVO.getSeparatemails());
					json.addProperty("to", toEmailIdStrJoiner.toString());
					json.addProperty("cc", ccEmailIdStrJoiner.toString());
					json.addProperty("bcc", bccEmailIdStrJoiner.toString());
					json.addProperty("failedrecipient", frEmailIdStrJoiner.toString());// Added for Failure Mail
																						// Notification
					json.addProperty("subject", emailObj.getSubject());
					json.addProperty("body", body);
					jsonArray.add(json);
					sendMailService.sendTestMail(email);
				}
			}

		} catch (JAXBException exception) {
			logger.error("Error occurred while unmarshalling XML string content ", exception);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Please provide valid XML content, received \r\n" + emailXMLContent);
		} catch (AddressException exception) {
			logger.error("Error occurred while converting email to InternetAddress ", exception);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide valid email address");
		} catch (Throwable a_thr) {
			logger.error("Error occurred while sending email ", a_thr);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while sending email");
		}
		return ResponseEntity.status(HttpStatus.OK).body(jsonArray.toString());
	}

	public CustomResponseEntity executeRestXML(String queryContent) {
		CustomResponseEntity customResponseEntity = new CustomResponseEntity();
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(WebClientXMLVO.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			StringReader reader = new StringReader(queryContent);
			WebClientXMLVO webClientXMLVO = (WebClientXMLVO) unmarshaller.unmarshal(reader);

			if (webClientXMLVO.getWebClientURL() != null
					&& webClientXMLVO.getWebClientURL().equalsIgnoreCase("about:blank") == false) {
				Date requestTime = new Date();
				Mono<ResponseEntity<String>> responseContent = getWebClientResponse(webClientXMLVO);
				ResponseEntity<String> responseString = responseContent.block();
				Date responTime = new Date();
				customResponseEntity = convertResponseToVO(responseString);
				customResponseEntity.setResponseDuration(responTime.getTime() - requestTime.getTime());
			}
		} catch (Throwable a_thr) {
			logger.error("Error occurred while establishing connection ", a_thr);
			String stacktrace = ExceptionUtils.getStackTrace(a_thr);
			if (a_thr instanceof WebClientResponseException) {
				WebClientResponseException wcre = (WebClientResponseException) a_thr;
				customResponseEntity.setResponseStatusCode(wcre.getStatusCode().value());
				customResponseEntity.setStatusCode(wcre.getStatusCode());
				customResponseEntity.setStatusText(wcre.getStatusText());
				 customResponseEntity.setResponseBody(wcre.getResponseBodyAsString());
				 customResponseEntity.setMessage(wcre.getMessage());
				customResponseEntity.setHeaders(wcre.getHeaders().toSingleValueMap());
			} else {
				customResponseEntity.setResponseBody(stacktrace);
				customResponseEntity.setResponseStatusCode(500);
				customResponseEntity.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			customResponseEntity.setResponseTimestamp(new Date());
		}
		return customResponseEntity;
	}

	private Mono<ResponseEntity<String>> getWebClientResponse(WebClientXMLVO webClientVO) throws Exception {

		Integer responseTimeout = webClientVO.getResponseTimeOut() != null ? webClientVO.getResponseTimeOut()
				: Constants.DEFAULT_RESPONSE_TIMEOUT;
		Integer sslHandshakeTimeout = webClientVO.getSslHandshakeTimeout() != null ? webClientVO.getResponseTimeOut()
				: Constants.SSL_HANDSHAKE_TIMEOUT;
		Integer sslFlushTimeout = webClientVO.getSslFlushTimeout() != null ? webClientVO.getResponseTimeOut()
				: Constants.SSL_CLOSE_NOTIFY_FLUSH_TIMEOUT;
		Integer sslReadTimeout = webClientVO.getResponseTimeOut() != null ? webClientVO.getResponseTimeOut()
				: Constants.SSL_CLOSE_NOTIFY_READ_TIMEOUT;

		HttpClient httpClient = HttpClient.create().secure(spec -> {
			try {
				spec.sslContext(SslContextBuilder.forClient().build())
						.handshakeTimeout(Duration.ofSeconds(sslHandshakeTimeout))
						.closeNotifyFlushTimeout(Duration.ofSeconds(sslFlushTimeout))
						.closeNotifyReadTimeout(Duration.ofSeconds(sslReadTimeout));
			} catch (SSLException exception) {
				logger.debug("SSLException ", exception);
			}
		}).responseTimeout(Duration.ofSeconds(responseTimeout));
		ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient.wiretap(true));
		Builder webClientBuilder = WebClient.builder().defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
				.defaultHeader(HttpHeaders.USER_AGENT, "JQuiver");
		WebClientRequestVO webClientRequestVO = webClientVO.getWebClientRequestVO();
		String rawBody = "";
		boolean hasHeader = false;
		boolean isJsonContentType = false;
		boolean hasAttachment = false;
		if (webClientRequestVO != null) {
			if (CollectionUtils.isEmpty(webClientRequestVO.getHeaderParamVOList()) == false) {
				for (WebClientParamVO paramVO : webClientRequestVO.getHeaderParamVOList()) {
					if (HttpHeaders.AUTHORIZATION.equals(paramVO.getParameterName())
							|| "NOAUTHORIZATION".equals(paramVO.getParameterName())) {
						hasHeader = true;
					} else if ("content-type".equals(paramVO.getParameterName().toLowerCase())
							&& paramVO.getParameterValue().toLowerCase().contains("json")) {
						isJsonContentType = true;
					}
				}
			}
		}

		Integer authType = getOneAuthenticationId();
		if (hasHeader == false && authType != null
				&& (authType == com.trigyn.jws.usermanagement.utils.Constants.AuthType.DAO.getAuthType()
						|| authType == com.trigyn.jws.usermanagement.utils.Constants.AuthType.LDAP.getAuthType()
						|| authType == com.trigyn.jws.usermanagement.utils.Constants.AuthType.OAUTH.getAuthType())) {

			webClientBuilder.defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtUtil.generateToken(
					userDetailsService.loadUserByUsername(jwsUserDetailsService.getUserDetails().getUserName())));

		}
		webClientBuilder.filter(logRequest()).clientConnector(connector);
		JSONObject json = new JSONObject();
		MultiValueMap<String, String> bodyMultipvalueMap = new LinkedMultiValueMap<String, String>();
		MultiValueMap<String, String> queryParamMultipvalueMap = new LinkedMultiValueMap<String, String>();
		Mono<ResponseEntity<String>> responseContent = null;

		if (webClientRequestVO != null) {
			List<WebClientParamVO> webClientParamList = webClientRequestVO.getHeaderParamVOList();

			if (CollectionUtils.isEmpty(webClientParamList) == false) {
				webClientParamList.forEach(headerParam -> webClientBuilder.defaultHeader(headerParam.getParameterName(),
						headerParam.getParameterValue()));
			}

			if (webClientRequestVO.getBody() != null) {
				if ("rawBody".equals(webClientRequestVO.getBody().getContentType())) {
					List<WebClientParamVO> bodyParamVOList = webClientRequestVO.getBody().getBodyParamVOList();
					if (bodyParamVOList != null) {
						for (WebClientParamVO webClientParamVO : bodyParamVOList) {
							rawBody = webClientParamVO.getParameterValue();
						}
					}

				} else if (webClientRequestVO.getBody().getContentType() == null
						|| webClientRequestVO.getBody().getContentType().isEmpty()
						|| "keyValue".equals(webClientRequestVO.getBody().getContentType())) {
					List<WebClientParamVO> bodyParamVOList = webClientRequestVO.getBody().getBodyParamVOList();
					if (bodyParamVOList != null) {
						for (WebClientParamVO webClientParamVO : bodyParamVOList) {
							List<String> list = new ArrayList<>();
							list.add(webClientParamVO.getParameterValue());
							bodyMultipvalueMap.put(webClientParamVO.getParameterName(), list);
							if ("json".equals(webClientParamVO.getDataType())) {
								org.json.JSONObject jsonObj = new org.json.JSONObject(
										webClientParamVO.getParameterValue());
								json.put(webClientParamVO.getParameterName(), jsonObj);
							} else {
								json.put(webClientParamVO.getParameterName(), webClientParamVO.getParameterValue());
							}
						}
					}
				}
			}

			if (CollectionUtils.isEmpty(webClientRequestVO.getQueryParamVOList()) == false) {
				for (WebClientParamVO webClientParamVO : webClientRequestVO.getQueryParamVOList()) {
					List<String> list = new ArrayList<>();
					list.add(webClientParamVO.getParameterValue());
					queryParamMultipvalueMap.put(webClientParamVO.getParameterName(), list);
				}
			}
			/* Added for Rest Client Attachment */
			byte[] fileByte = null;
			File attachedFile = null;
			Map<String, Object> fileInfo = new HashMap<>();
			ArrayList<File> filePaths = new ArrayList<>();
			if (CollectionUtils.isEmpty(webClientRequestVO.getAttachmnetParamVOList()) == false) {
				for (WebClientAttacmentVO webClientAttachVO : webClientRequestVO.getAttachmnetParamVOList()) {
					hasAttachment = true;
					// This is File Bin
					if (webClientAttachVO.getType().equals(Constants.FILE_ATTACHMENT_FILEBIN)) {
						String fileUploadId = webClientAttachVO.getFilePath();
						Integer isAllowed = storageService.hasPermission(null, null, fileUploadId,
								Constants.VIEW_FILE_VALIDATOR, new HashMap<>());
						if (isAllowed > 0) {
							fileInfo = storageService.load(fileUploadId);
							if (fileInfo != null) {
								fileByte = (byte[]) fileInfo.get("file");
								attachedFile = new File(webClientAttachVO.getFileName());
								try (FileOutputStream fos = new FileOutputStream(attachedFile)) {
									fos.write(fileByte);
									filePaths.add(attachedFile);
								} catch (Exception exception) {
									logger.error("Error occurred while accessing file", exception);
								}
							}
						}
					} else if (webClientAttachVO.getType().equals(Constants.FILE_ATTACHMENT_FILESYSTEM)
							|| webClientAttachVO.getType().equals(Constants.FILE_ATTACHMENT_UPLOADEDFILE)) { // This is
																												// filesystem.
						File aFile = new File(webClientAttachVO.getFilePath());
						if (aFile != null && aFile.exists()) {
							attachedFile = new File(webClientAttachVO.getFileName());
							InputStream in = new FileInputStream(webClientAttachVO.getFilePath());
							fileByte = ByteStreams.toByteArray(in);
							filePaths.add(attachedFile);
							in.close();
							try (FileOutputStream fos = new FileOutputStream(attachedFile)) {
								fos.write(fileByte);
							} catch (Exception exception) {
								logger.error("Error occurred while accessing file", exception);
							}
						}
					}
				}
			}
			WebClient webClient = webClientBuilder.build();
			RequestHeadersSpec reqHeaderSpec = null;
			RequestBodySpec reqBodySpec = webClient.method(HttpMethod.resolve(webClientVO.getRequestType()))
					.uri(webClientVO.getWebClientURL(), uri -> uri.queryParams(queryParamMultipvalueMap).build());
			if (hasAttachment == true) {
				MultipartBodyBuilder builder = new MultipartBodyBuilder();
				for (int iCounter = 0; iCounter < filePaths.size(); iCounter++) {
					try {
						File file = filePaths.get(iCounter);
						InputStream in = new FileInputStream(file);
						fileByte = ByteStreams.toByteArray(in);
						builder.part("file[]", fileByte).header("Content-Disposition",
								"form-data; name=\"" + file.getName() + "\"; filename=\"" + file.getName() + "\"");
					} catch (Exception exception) {
						logger.error("Error occurred while accessing file", exception);
					}
				}
				reqHeaderSpec = reqBodySpec.contentType(MediaType.MULTIPART_FORM_DATA)
						.body(BodyInserters.fromMultipartData(builder.build()));
			} else if (webClientRequestVO.getBody() != null
					&& "rawBody".equals(webClientRequestVO.getBody().getContentType())) {
				reqHeaderSpec = reqBodySpec.bodyValue(rawBody);
			} else if (isJsonContentType == true) {
				reqHeaderSpec = reqBodySpec.bodyValue(json);
			} else {
				reqHeaderSpec = reqBodySpec.bodyValue(bodyMultipvalueMap);
			}
			responseContent = reqHeaderSpec.retrieve().toEntity(String.class);
			/**
			 * Below code is commented because we are not able to handle the exception from
			 * the urls accessed through this request. Since we are trying to convert
			 * CustomRuntimeException to String.class, the system is giving us an unparsable
			 * error, from which we are not able to get the client response code. Below code
			 * should be uncommented, after discussion only.
			 */
//					.onStatus(HttpStatus::is4xxClientError, response -> {
//				return response.bodyToMono(CustomRuntimeException.class).flatMap(error -> {
//					return Mono.error(new CustomRuntimeException(error));
//				});
//			}).onStatus(HttpStatus::is5xxServerError, response -> {
//				return response.bodyToMono(CustomRuntimeException.class).flatMap(error -> {
//					return Mono.error(new CustomRuntimeException(error));
//				});
//			}).toEntity(String.class);
		}

		return responseContent;
	}

	private CustomResponseEntity convertResponseToVO(ResponseEntity<String> responseString) {
		CustomResponseEntity customResponseEntity = new CustomResponseEntity();
		try {
			if (null != responseString.getBody()) {
				String responseBody = responseString.getBody();
				Integer responseStatusCode = responseString.getStatusCode().value();
				Map<String, String> headerMap = responseString.getHeaders().toSingleValueMap();
				customResponseEntity.setResponseStatusCode(responseStatusCode);
				customResponseEntity.setResponseBody(responseBody);
				customResponseEntity.setHeaders(headerMap);
				customResponseEntity.setResponseTimestamp(new Date());
			}
		} catch (Throwable a_thr) {
			logger.error("NullPointerException here ", a_thr);
		}
		return customResponseEntity;
	}

	private ExchangeFilterFunction logRequest() {
		return (clientRequest, next) -> {
			logger.info("Request: {} {}", clientRequest.method(), clientRequest.url());
			clientRequest.headers()
					.forEach((name, values) -> values.forEach(value -> logger.info("{}={}", name, value)));
			return next.exchange(clientRequest);
		};
	}

	private HttpServletRequest getRequest() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (sra != null) {
			return sra.getRequest();
		}
		return null;
	}

	private Integer getOneAuthenticationId() {

		Map<String, Object> authenticationDetails = applicationSecurityDetails.getAuthenticationDetails();
		Boolean isAuthenticationEnabled = (Boolean) authenticationDetails.get("isAuthenticationEnabled");
		if (authenticationDetails.isEmpty() == false && isAuthenticationEnabled) {
			List<MultiAuthSecurityDetailsVO> multiAuthSecurityDetails = (List<MultiAuthSecurityDetailsVO>) authenticationDetails
					.get("authenticationDetails");
			for (MultiAuthSecurityDetailsVO multiAuthSecurityDetailsVO : multiAuthSecurityDetails) {
				JwsAuthenticationType authenticationType = multiAuthSecurityDetailsVO.getConnectionDetailsVO()
						.getAuthenticationType();
				if (authenticationType != null) {
					if (authenticationType.getValue().equalsIgnoreCase("true")) {
						return multiAuthSecurityDetailsVO.getAuthenticationTypeVO().getId();
					}
				}
			}
		}
		return null;
	}

	public JwsDynamicRestDetail getDynamicRestDetailsByName(String jwsMethodName) {
		return dynarestDAO.getDynamicRestDetailsByName(jwsMethodName);
	}
}
