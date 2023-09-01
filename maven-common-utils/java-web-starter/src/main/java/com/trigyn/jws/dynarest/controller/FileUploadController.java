package com.trigyn.jws.dynarest.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.vo.FileInfo;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynarest.entities.FileUploadTemp;
import com.trigyn.jws.dynarest.repository.FileUploadTempRepository;
import com.trigyn.jws.dynarest.service.FilesStorageService;
import com.trigyn.jws.dynarest.utils.Constants;
import com.trigyn.jws.usermanagement.security.config.Authorized;

import freemarker.core.StopException;

@RestController
@RequestMapping("/cf")
public class FileUploadController {

	private final static Logger			logger						= LogManager.getLogger(FileUploadController.class);

	@Autowired
	private IUserDetailsService			userDetailsService			= null;

	@Autowired
	private ActivityLog					activitylog					= null;

	@Autowired
	@Qualifier("file-system-storage")
	private FilesStorageService			storageService				= null;

	@Autowired
	private FileUploadTempRepository	fileUploadTempRepository	= null;

	@PostMapping(value = "/upload", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	@Authorized(moduleName = com.trigyn.jws.usermanagement.utils.Constants.FILEBIN)
	public String uploadFiles(@RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest) {

		String	message				= "";

		String	fileBinId			= httpServletRequest.getParameter("fileBinId");

		String	fileAssociationId	= httpServletRequest.getParameter("fileAssociationId");
		String	additionalParameter	= httpServletRequest.getParameter("additionalParameter");
		try {

			if (StringUtils.isBlank(additionalParameter) == false) {
				Gson				gson					= new Gson();
				Map<String, Object>	additionalParameterMap	= gson.fromJson(additionalParameter, Map.class);
				String				fileTypeIdStr			= (String) additionalParameterMap.get("fileTypeId");
				Integer				fileTypeId				= StringUtils.isBlank(fileTypeIdStr) == false
						? Integer.parseInt(fileTypeIdStr)
						: 1;

			}

			String				fileId			= storageService.save(file, fileBinId, fileAssociationId);

			Map<String, Object>	uploadDetails	= new HashMap<String, Object>();
			uploadDetails.put("fileId", fileId);
			uploadDetails.put("success", "1");

			return new ObjectMapper().writeValueAsString(uploadDetails);
		} catch (Exception a_exc) {
			message = "Fail to upload files!";
			logger.error(message + a_exc);
			return message;
		}
	}
	
	@PostMapping(value = "/m-upload", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public ResponseEntity<String> uploadFiles(@RequestParam("files[0]") MultipartFile[] files, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws Exception {
		String				fileBinId				= httpServletRequest.getParameter("fileBinId");
		String				fileAssociationId		= httpServletRequest.getParameter("fileAssociationId");
		String				isReplaceExistingFile	= httpServletRequest.getParameter("isReplaceExistingFile");
		String				existingFileUploadId	= httpServletRequest.getParameter("existingFileId");
		Map<String, Object>	requestParamsMap		= storageService.createRequestParam(httpServletRequest);
		Integer				isAllowed				= storageService.hasPermission(fileBinId, fileAssociationId, null,
				Constants.UPLOAD_FILE_VALIDATOR, requestParamsMap);
		try {
			if (isAllowed > 0) {
				Boolean	isExtensionSupported	= storageService.isExtensionSupported(fileBinId, files);
				Boolean	isMaxFileSizeExceed		= storageService.isMaxFileSizeExceed(fileBinId, files);

				if (isExtensionSupported == null || isExtensionSupported == false) {
					logger.error("File with specified extension is not allowed.");
					// httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), "You don't have
					// enough privileges to delete this file");
					return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("INVALID_EXTENSION");
				} else if (isMaxFileSizeExceed == null || isMaxFileSizeExceed == false) {
					logger.error("File size exceeds more than allowed size.");
					// httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), "You don't have
					// enough privileges to delete this file");
					return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body("INVALID_SIZE");
				}

				List<String>		fileNames			= new ArrayList<>();
				Map<String, String>	updateTempDetails	= new HashMap<>();

				Arrays.asList(files).stream().forEach(file -> {
					String					fileId					= "";
					List<FileUploadTemp>	tempFileUploadDetails	= null;
					String					fileUploadTempId		= "";
					try {
						//Replace Image : Below code is changed for fixing up the replace image
						if(isReplaceExistingFile!=null) {
							if (Boolean.valueOf(isReplaceExistingFile)) {
								boolean isTempFileExist = storageService.checkFileExistByUploadId(existingFileUploadId);
								//Replace Image : Below code is added for fixing up the replace image from FileUploadTemp table
								if (isTempFileExist) {
										fileId = storageService.updateFileUploadTemp(file, existingFileUploadId, fileBinId, fileAssociationId);
								}else {
									//Replace Image : Below code is changed to update the files for FileUpload
									fileId = storageService.update(file, existingFileUploadId);
								}
								fileNames.add(fileId);
								
							} else if (Boolean.valueOf(isReplaceExistingFile) == false) {
									fileId					= storageService.save(file, fileBinId, fileAssociationId);
									tempFileUploadDetails	= fileUploadTempRepository.findAllTempFileUpload(fileId);
									fileNames.add(fileId);
									fileUploadTempId = tempFileUploadDetails.get(0).getFileUploadTempId();
									updateTempDetails.put(fileId, fileUploadTempId);
							}
						}
						
					} catch (Exception exception) {
						String message = "Fail to upload files!";
						logger.error(message, exception);
					}

				});
				Map<String, Object> uploadDetails = new HashMap<String, Object>();
				uploadDetails.put("fileIds", fileNames);
				uploadDetails.put("success", "1");
				if (updateTempDetails != null) {
					uploadDetails.put("updateTempDetails", updateTempDetails);
				}
				return ResponseEntity.status(HttpStatus.OK).body(new ObjectMapper().writeValueAsString(uploadDetails));
			}

			logUnauthorizedAccess("upload");
			// httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), "You don't have
			// enough privileges to upload file");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have enough privileges to upload file");
		} catch (Exception exception) {
			String message = "Fail to upload files!";
			logger.error(message, exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			// httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(),
			// message);
			return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// @GetMapping("/files")
	// public ResponseEntity<List<FileInfo>> getListFiles() {
	// List<FileInfo> fileInfos = storageService.loadAll();
	// return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	// }

	@DeleteMapping("/files/{fileUploadId:.+}")
	public ResponseEntity<?> deleteFile(@PathVariable String fileUploadId, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws Exception {
		boolean isTempFileExist = storageService.chkAndDeleteFromTemp(fileUploadId);
		if (isTempFileExist) {
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		Map<String, Object>	requestParamsMap	= storageService.createRequestParam(httpServletRequest);

		Integer				isAllowed			= storageService.hasPermission(null, null, fileUploadId,
				Constants.DELETE_FILE_VALIDATOR, requestParamsMap);

		if (isAllowed > 0) {
			storageService.deleteFileByFileUploadId(fileUploadId);
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		logUnauthorizedAccess("delete");
		httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(),
				"You don't have enough privileges to delete this file");
		return null;
	}

	@GetMapping("/fileDetails")
	public ResponseEntity<List<FileInfo>> getListFilesByIds(HttpServletRequest httpServletRequest)
			throws JsonMappingException, JsonProcessingException {

		String			fileUploadIds		= httpServletRequest.getParameter("fileUploadId");
		List<String>	fileUploadIdList	= new ObjectMapper().readValue(fileUploadIds, List.class);
		List<FileInfo>	fileInfos			= null;
		try {
			fileInfos = storageService.getFileDetailsByFileUploadIds(fileUploadIdList);
		} catch (Exception e) {
			ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(fileInfos);
		}
		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}

	@GetMapping("/fdbbi")
	public ResponseEntity<List<FileInfo>> getListFilesByConfigId(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException, CustomStopException {
		String message = "";
		try {

			String		fileBinId			= httpServletRequest.getParameter("fileBinId");
			String		fileAssociationId	= httpServletRequest.getParameter("fileAssociationId");
			String		fileUploadCsvIds	= httpServletRequest.getParameter("fileUploadTempIds");
			if(fileUploadCsvIds!=null) {
				String[]	fileUploadTempIds	= fileUploadCsvIds.split(",");
				/*
				 * isEdit is true when the file bin is updated from multiple browser
				 * simultaneously. So if another person edits the same fileassocid, will not get
				 * temp files added by another person. Also it will clear temp files when page
				 * is refreshed by same person.
				 */
				String		strIsEdit			= httpServletRequest.getParameter("isEdit");
				if (strIsEdit == null) {
					strIsEdit = "true";
				}
				boolean isEdit = Boolean.parseBoolean(strIsEdit);
				if (isEdit && fileUploadTempIds != null) {
					storageService.clearTempFileBin(fileBinId, fileAssociationId, fileUploadTempIds);
				}
			}

			Map<String, Object>	requestParams	= storageService.createRequestParam(httpServletRequest);
			List<FileInfo>		fileInfoList	= storageService.getFileDetailsByConfigId(fileBinId, fileAssociationId,
					requestParams);
			return ResponseEntity.status(HttpStatus.OK).body(fileInfoList);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in getListFilesByConfigId.", custStopException);
			throw custStopException;
		} catch (Exception exception) {
			message = "Failed to retrieve files";
			logger.error(message, exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.NOT_FOUND.value(), message);
			return null;
		}
	}

	@GetMapping("/files/{fileUploadId:.+}")
	public ResponseEntity<InputStreamResource> getFile(@PathVariable String fileUploadId,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		String				message			= "";
		InputStreamResource	streamResource	= null;
		try {
			Map<String, Object>	requestParamsMap	= storageService.createRequestParam(httpServletRequest);
			Integer				isAllowed			= storageService.hasPermission(null, null, fileUploadId,
					Constants.VIEW_FILE_VALIDATOR, requestParamsMap);

			String				fileBinId			= (String) requestParamsMap.get("fileBinId");
			String				moduleName			= (String) requestParamsMap.get("moduleName");
			if (isAllowed > 0) {
				Map<String, Object>	fileInfo	= storageService.load(fileUploadId);
				HttpHeaders			headers		= new HttpHeaders();
				String mimeType = (String) fileInfo.get("mimeType");
				if (StringUtils.isBlank(mimeType) == false) {
					ContentDisposition contentDisposition = ContentDisposition.builder(mimeType).build();
					headers.setContentDisposition(contentDisposition);
					headers.setContentType(MediaType.parseMediaType((String) fileInfo.get("mimeType")));
				} else {
					headers.setContentDispositionFormData("attachment", fileInfo.get("fileName").toString());
				}
				headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
				byte[] file = (byte[]) fileInfo.get("file");
				streamResource = new InputStreamResource(new ByteArrayInputStream(file));
				/* Method called for implementing Activity Log */
				logActivity(fileBinId + '-' + moduleName);
				return new ResponseEntity<InputStreamResource>(streamResource, headers, HttpStatus.OK);
			}

			logUnauthorizedAccess("access/view");
			httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(),
					"You don't have enough privileges to view this file");
			return null;
		} catch (Exception exception) {
			message = "Fail to retrieve file! " + exception.getMessage();
			logger.error(message);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.NOT_FOUND.value(), message);
			return null;
		}
	}

	/**
	 * Purpose of this method is to log activities</br>
	 * in File Config Module.
	 * 
	 * @author              Bibhusrita.Nayak
	 * @param  templateName
	 * @throws Exception
	 */
	private void logActivity(String templateName) throws Exception {
		Map<String, String>	requestParams		= new HashMap<>();
		UserDetailsVO		detailsVO			= userDetailsService.getUserDetails();
		Date				activityTimestamp	= new Date();
		requestParams.put("entityName", templateName);
		requestParams.put("masterModuleType", Constants.MODULE_NAME);
		requestParams.put("typeSelect", Constants.CHANGETYPE);
		requestParams.put("userName", detailsVO.getUserName());
		requestParams.put("message", "");
		requestParams.put("date", activityTimestamp.toString());
		requestParams.put("action", Constants.ACTION_DOWN);
		activitylog.activitylog(requestParams);
	}

	public Map<String, Object> getFileFromFileUploadId(String fileUploadId, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) {
		String message = "";
		try {
			Map<String, Object>	requestParamsMap	= storageService.createRequestParam(httpServletRequest);
			Integer				isAllowed			= storageService.hasPermission(null, null, fileUploadId,
					Constants.VIEW_FILE_VALIDATOR, requestParamsMap);
			if (isAllowed > 0) {
				Map<String, Object> fileInfo = storageService.load(fileUploadId);
				return fileInfo;
			}

			logUnauthorizedAccess("access/view");
			httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(),
					"You don't have enough privileges to view this file");
			return null;
		} catch (Exception exception) {
			message = "Fail to retrieve file!";
			logger.error(message, exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			return null;
		}
	}
	
	@PostMapping("/smfa-all")
	@ResponseBody
	public String saveFileAssociations(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException {
		String message = "File updated successfully";
		try {

			String	fileBinId			= httpServletRequest.getParameter("fileBinId");
			List<FileUploadTemp> tempFileUploadDetails =   fileUploadTempRepository.findAllByFileTempBinId(fileBinId);
			for (FileUploadTemp fileUploadTemp : tempFileUploadDetails) {
				storageService.commitChanges(fileBinId, fileUploadTemp.getFileAssociationId());
			}
			Map<String, Object>	uploadDetails	= new HashMap<String, Object>();
			uploadDetails.put("success", "1");
			return new ObjectMapper().writeValueAsString(uploadDetails);
		} catch (Exception a_exc) {
			message = "Fail to upload files!";
			logger.error(message + a_exc);
			return message;
		}
	}
	
	@PostMapping("/smfa")
	@ResponseBody
	public ResponseEntity<?> saveSelectedFiles(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException {
		try {

			List<Map<String, String>>	formData	= new Gson().fromJson(httpServletRequest.getParameter("formData"),
					List.class);
			for (Map<String, String> formEntry : formData) {
				if (formEntry.containsKey("valueType") && formEntry.get("valueType").equalsIgnoreCase("fileBin")) {
					storageService.commitChanges(formEntry.get("FileBinID"), formEntry.get("fileAssociationID"), formEntry.get("fileUploadTempId"));
				}
			}
			return ResponseEntity.status(HttpStatus.OK).body(new ObjectMapper().writeValueAsString("Success"));
		} catch (Exception a_exc) {
			logger.error("Error occurred while processing request: ", a_exc);
			Throwable rootCause = a_exc;
			while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
				if (rootCause instanceof StopException) {
					return new ResponseEntity<>(((StopException) rootCause).getMessageWithoutStackTop(),
							HttpStatus.EXPECTATION_FAILED);
				}
				rootCause = rootCause.getCause();
			}

			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private void logUnauthorizedAccess(String operationType) {
		UserDetailsVO	userDetailsVO	= userDetailsService.getUserDetails();
		String			userName		= userDetailsVO.getUserName();
		logger.info("{}", userName, " is trying to {}", operationType, " file without required privilges.");
	}

}
