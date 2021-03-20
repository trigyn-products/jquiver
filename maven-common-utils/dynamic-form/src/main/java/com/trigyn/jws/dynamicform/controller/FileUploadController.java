package com.trigyn.jws.dynamicform.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.trigyn.jws.dbutils.vo.FileInfo;
import com.trigyn.jws.dynamicform.service.FilesStorageService;
import com.trigyn.jws.dynamicform.utils.Constant;
import com.trigyn.jws.usermanagement.security.config.Authorized;

@RestController
@RequestMapping("/cf")
public class FileUploadController {

	private final static Logger	logger			= LogManager.getLogger(FileUploadController.class);

	@Autowired
	@Qualifier("file-system-storage")
	private FilesStorageService	storageService	= null;

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
	public String uploadFiles(@RequestParam("files[0]") MultipartFile[] files, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws Exception {
		String	fileBinId			= httpServletRequest.getParameter("fileBinId");
		String	fileAssociationId	= httpServletRequest.getParameter("fileAssociationId");
		Integer	isAllowed			= storageService.hasPermission(fileBinId, fileAssociationId, null,
				Constant.UPLOAD_FILE_VALIDATOR, httpServletRequest);
		try {
			if (isAllowed > 0) {
				List<String> fileNames = new ArrayList<>();

				Arrays.asList(files).stream().forEach(file -> {
					String fileId = storageService.save(file, fileBinId, fileAssociationId);
					fileNames.add(fileId);
				});
				Map<String, Object> uploadDetails = new HashMap<String, Object>();
				uploadDetails.put("fileIds", fileNames);
				uploadDetails.put("success", "1");
				return new ObjectMapper().writeValueAsString(uploadDetails);
			}
			httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(),
					"You don't have enough privileges to upload file");
			return null;
		} catch (Exception exception) {
			String message = "Fail to upload files!";
			logger.error(message, exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
			return null;
		}
	}

	@GetMapping("/files")
	public ResponseEntity<List<FileInfo>> getListFiles() {
		List<FileInfo> fileInfos = storageService.loadAll();
		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}

	@DeleteMapping("/files/{fileUploadId:.+}")
	public ResponseEntity<?> deleteFile(@PathVariable String fileUploadId, HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws Exception {
		Integer isAllowed = storageService.hasPermission(null, null, fileUploadId, Constant.DELETE_FILE_VALIDATOR,
				httpServletRequest);
		if (isAllowed > 0) {
			storageService.deleteFileByFileUploadId(fileUploadId);
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(),
				"You don't have enough privileges to delete this file");
		return null;
	}

	@GetMapping("/fileDetails")
	public ResponseEntity<List<FileInfo>> getListFilesByIds(HttpServletRequest httpServletRequest)
			throws JsonMappingException, JsonProcessingException {
		String			fileUploadIds		= httpServletRequest.getParameter("fileUploadId");
		List<String>	fileUploadIdList	= new ObjectMapper().readValue(fileUploadIds, List.class);
		List<FileInfo>	fileInfos			= storageService.getFileDetailsByFileUploadIds(fileUploadIdList);
		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}

	@GetMapping("/fdbbi")
	public ResponseEntity<List<FileInfo>> getListFilesByConfigId(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws IOException {
		String message = "";
		try {
			String				fileBinId			= httpServletRequest.getParameter("fileBinId");
			String				fileAssociationId	= httpServletRequest.getParameter("fileAssociationId");
			Map<String, Object>	requestParams		= storageService.createRequestParam(httpServletRequest);
			List<FileInfo>		fileInfoList		= storageService.getFileDetailsByConfigId(fileBinId,
					fileAssociationId, requestParams);
			return ResponseEntity.status(HttpStatus.OK).body(fileInfoList);
		} catch (Exception exception) {
			message = "Fail to upload files!";
			logger.error(message, exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
			return null;
		}
	}

	@GetMapping("/files/{fileUploadId:.+}")
	public ResponseEntity<InputStreamResource> getFile(@PathVariable String fileUploadId,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		String message = "";
		try {
			Integer isAllowed = storageService.hasPermission(null, null, fileUploadId, Constant.VIEW_FILE_VALIDATOR,
					httpServletRequest);
			if (isAllowed > 0) {
				Map<String, Object>	fileInfo	= storageService.load(fileUploadId);
				HttpHeaders			headers		= new HttpHeaders();
				String				mimeType	= (String) fileInfo.get("mimeType");
				if (StringUtils.isBlank(mimeType) == false) {
					ContentDisposition contentDisposition = ContentDisposition.builder(mimeType).build();
					headers.setContentDisposition(contentDisposition);
					headers.setContentType(MediaType.parseMediaType((String) fileInfo.get("mimeType")));
				} else {
					headers.setContentDispositionFormData("attachment", fileInfo.get("fileName").toString());
				}
				headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
				byte[]				file			= (byte[]) fileInfo.get("file");
				InputStreamResource	streamResource	= new InputStreamResource(new ByteArrayInputStream(file));
				return new ResponseEntity<InputStreamResource>(streamResource, headers, HttpStatus.OK);
			}

			httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(),
					"You don't have enough privileges to view this file");
			return null;
		} catch (Exception exception) {
			message = "Fail to retrieve file!";
			logger.error(message, exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
			return null;
		}
	}

	@PostMapping(value = "/vfq", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String validateFileQueries(@RequestBody MultiValueMap<String, String> formData) throws Exception {
		Map<String, Object>	invalidQueryMap	= storageService.validateFileQueries(formData);
		StringBuilder		errorMessage	= new StringBuilder();
		Map<String, String>	queryNameMap	= Constant.getQueryName();

		for (Map.Entry<String, Object> invalidQuery : invalidQueryMap.entrySet()) {

			Map<String, String> invalidColumn = (Map<String, String>) invalidQuery.getValue();
			if (CollectionUtils.isEmpty(invalidColumn) == false) {
				errorMessage.append("Invalid ").append(queryNameMap.get(invalidQuery.getKey())).append(" Query: ");
			}
			for (Map.Entry<String, String> requiredColumn : invalidColumn.entrySet()) {
				if (requiredColumn.getKey().equals("syntaxError")) {
					errorMessage.append(requiredColumn.getValue());
				} else {
					errorMessage.append(requiredColumn.getKey()).append(",");
				}
			} // End of inner loop

			if (CollectionUtils.isEmpty(invalidColumn) == false) {
				errorMessage = new StringBuilder(errorMessage.substring(0, errorMessage.length() - 1));
				errorMessage.append("<br> ");
			}

		} // End of outer Loop
		return errorMessage.toString();
	}

}
