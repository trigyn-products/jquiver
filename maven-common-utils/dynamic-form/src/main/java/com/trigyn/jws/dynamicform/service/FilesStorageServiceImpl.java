package com.trigyn.jws.dynamicform.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.ByteStreams;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.FileInfo;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynamicform.dao.FileUploadConfigRepository;
import com.trigyn.jws.dynamicform.dao.FileUploadRepository;
import com.trigyn.jws.dynamicform.entities.FileUpload;
import com.trigyn.jws.dynamicform.entities.FileUploadConfig;
import com.trigyn.jws.dynamicform.entities.FileUploadConfigDAO;
import com.trigyn.jws.dynamicform.utils.Constant;
import com.trigyn.jws.dynamicform.utils.CryptoUtils;
import com.trigyn.jws.templating.utils.TemplatingUtils;

@Component
@Qualifier(value = "file-system-storage")
@Transactional
public class FilesStorageServiceImpl implements FilesStorageService {

	private static final Logger			logger						= LogManager
			.getLogger(FilesStorageServiceImpl.class);

	@Autowired
	private PropertyMasterService		propertyMasterService		= null;

	@Autowired
	private IUserDetailsService			userdetailsService			= null;

	@Autowired
	private FileUploadRepository		fileUploadRepository		= null;

	@Autowired
	private FileUploadConfigRepository	fileUploadConfigRepository	= null;

	@Autowired
	private FileUploadConfigDAO			fileUploadConfigDAO			= null;

	@Autowired
	private TemplatingUtils				templatingUtils				= null;

	private final static String			JWS_SALT					= "main alag duniya";

	@Override
	public void init() {
		String fileUploadDir = null;
		try {
			fileUploadDir = propertyMasterService.findPropertyMasterValue("file-upload-location");
			Files.createDirectory(Paths.get(fileUploadDir));
		} catch (Exception a_exc) {
			logger.warn("Error while init of file storage ", a_exc.getMessage());
		}

	}

	@Override
	public String save(MultipartFile file, String fileBinId, String fileAssociationId) {
		try {
			LocalDate		localDate		= LocalDate.now();
			Integer			year			= localDate.getYear();
			Integer			month			= localDate.getMonthValue();
			Integer			date			= localDate.getDayOfMonth();
			String			fileUploadDir	= propertyMasterService.findPropertyMasterValue("file-upload-location");
			StringJoiner	location		= new StringJoiner("" + File.separatorChar);
			location.add(fileUploadDir);
			location.add(year.toString()).add(month.toString()).add(date.toString());
			if (Boolean.FALSE.equals(new File(location.toString()).exists())) {
				Files.createDirectories(Paths.get(location.toString()));
			}
			FileUpload	fileUpload	= saveFileDetails(location.toString(), file.getOriginalFilename(), fileBinId,
					fileAssociationId);
			Path		root		= Paths.get(location.toString());
			Files.copy(file.getInputStream(), root.resolve(fileUpload.getPhysicalFileName()));
			CryptoUtils.encrypt(JWS_SALT, root.resolve(fileUpload.getPhysicalFileName()).toFile(),
					root.resolve(fileUpload.getPhysicalFileName()).toFile());
			return fileUpload.getFileUploadId();
		} catch (Exception a_exc) {
			logger.error("Could not store the file. Error: ", a_exc);
			return null;
		}
	}

	private FileUpload saveFileDetails(String location, String originalFilename, String fileBinId,
			String fileAssociationId) {
		UserDetailsVO	userDetailsVO	= userdetailsService.getUserDetails();
		FileUpload		fileUpload		= new FileUpload();
		fileUpload.setFilePath(location);
		fileUpload.setOriginalFileName(originalFilename);
		fileUpload.setPhysicalFileName(UUID.randomUUID().toString());
		fileUpload.setUpdatedBy(userDetailsVO.getUserName());
		fileUpload.setFileBinId(fileBinId);
		fileUpload.setFileAssociationId(fileAssociationId);
		return fileUploadRepository.save(fileUpload);
	}

	@Override
	public Map<String, Object> load(String fileUploadId) {
		try {
			Map<String, Object>	details				= new HashMap<>();
			FileUpload			fileUploadDetails	= fileUploadRepository.findById(fileUploadId)
					.orElseThrow(() -> new Exception("file not found with id : " + fileUploadId));
			Path				root				= Paths.get(fileUploadDetails.getFilePath());
			Path				filePath			= root.resolve(fileUploadDetails.getPhysicalFileName());
			Resource			resource			= new UrlResource(filePath.toUri());
			String				mimeType			= URLConnection
					.guessContentTypeFromName(fileUploadDetails.getOriginalFileName());
			if (resource.exists() || resource.isReadable()) {
				File	newFile		= resource.getFile();
				byte[]	newFiles	= CryptoUtils.decrypt(JWS_SALT, newFile, null);
				details.put("file", newFiles);

			} else {
				String		filePathStr	= fileUploadDetails.getFilePath() + "/"
						+ fileUploadDetails.getPhysicalFileName();
				InputStream	in			= FilesStorageServiceImpl.class.getResourceAsStream(filePathStr);
				if (in == null) {
					throw new RuntimeException("Could not read the file!");
				} else {
					byte[] byteArray = ByteStreams.toByteArray(in);
					in.close();
					byte[] fileByteArray = CryptoUtils.decrypt(JWS_SALT, byteArray, null);
					details.put("file", fileByteArray);
					details.put("fileName", fileUploadDetails.getOriginalFileName());
				}
			}
			details.put("fileName", fileUploadDetails.getOriginalFileName());
			details.put("mimeType", mimeType);
			return details;
		} catch (Exception a_exc) {
			logger.error("Error: ", a_exc.getMessage());
		}
		return null;
	}

	@Override
	public void deleteAll() {
		try {
			fileUploadRepository.deleteAll();
			String	fileUploadDir	= propertyMasterService.findPropertyMasterValue("file-upload-location");
			Path	root			= Paths.get(fileUploadDir);
			FileSystemUtils.deleteRecursively(root.toFile());
		} catch (Exception a_exc) {
			logger.error("Error: ", a_exc.getMessage());
		}
	}

	@Override
	public List<FileInfo> loadAll() {
		try {
			List<FileUpload>	fileUploads	= fileUploadRepository.findAll();
			List<FileInfo>		fileInfos	= fileUploads.stream().map(files -> {
												File file = new File(files.getFilePath());
												return new FileInfo(files.getFileUploadId(),
														files.getOriginalFileName(), file.length());
											})
					.collect(Collectors.toList());
			return fileInfos;
		} catch (Exception a_exc) {
			logger.error("Could not load the files!");
		}
		return null;
	}

	@Override
	public List<FileInfo> getFileDetailsByFileUploadIds(List<String> fileUploadIdList) {
		List<FileUpload>	fileUploadList	= fileUploadRepository.findAllByFileUploadIds(fileUploadIdList);
		List<FileInfo>		fileInfoList	= convertFileUploadToFileInfo(fileUploadList);
		return fileInfoList;
	}

	@Override
	public List<FileInfo> getFileDetailsByConfigId(String fileBinId, String fileAssociationId,
			Map<String, Object> requestParamMap) throws Exception {
		List<FileUpload>	fileUploadList		= new ArrayList<>();
		FileUploadConfig	fileUploadConfig	= fileUploadConfigRepository.getFileUploadConfig(fileBinId);
		String				queryContent		= fileUploadConfig.getSelectQueryContent();
		if (StringUtils.isBlank(queryContent) == false) {
			requestParamMap.put("fileAssociationId", fileAssociationId);
			String query = templatingUtils.processTemplateContents(queryContent, "fileViewQuery", requestParamMap);
			fileUploadList = fileUploadConfigDAO.executeSelectQuery(null, query, requestParamMap);
		} else {
			fileUploadList = fileUploadRepository.findAllFilesByConfigId(fileBinId, fileAssociationId);
		}
		List<FileInfo> fileInfoList = convertFileUploadToFileInfo(fileUploadList);
		return fileInfoList;
	}

	private List<FileInfo> convertFileUploadToFileInfo(List<FileUpload> fileUploads) {
		List<FileInfo> fileInfos = fileUploads.stream().map(files -> {
			String	filePath	= files.getFilePath() + File.separator + files.getPhysicalFileName();
			File	file		= new File(filePath);
			if (file.length() == 0) {
				// Trying to read the file from the jar
				InputStream	in			= FilesStorageServiceImpl.class.getResourceAsStream(filePath);
				byte[]		byteArray	= null;
				try {
					byteArray = ByteStreams.toByteArray(in);
				} catch (Throwable exception) {
					logger.error("Error while retrieving the file", exception);
				}
				return new FileInfo(files.getFileUploadId(), files.getOriginalFileName(),
						Long.valueOf(byteArray.length));
			} else {
				return new FileInfo(files.getFileUploadId(), files.getOriginalFileName(), file.length());
			}
		}).collect(Collectors.toList());
		return fileInfos;
	}

	@Override
	public void deleteFileByFileUploadId(String fileUploadId) throws Exception {
		FileUpload	fileUploadDetails	= fileUploadRepository.findById(fileUploadId)
				.orElseThrow(() -> new Exception("file not found with id : " + fileUploadId));
		Path		root				= Paths.get(fileUploadDetails.getFilePath());
		Path		filePath			= root.resolve(fileUploadDetails.getPhysicalFileName());
		Resource	resource			= new UrlResource(filePath.toUri());
		if (resource.exists() || resource.isReadable()) {
			fileUploadRepository.deleteById(fileUploadId);
			resource.getFile().delete();
		}
	}

	@Override
	public Map<String, Object> createRequestParam(HttpServletRequest httpServletRequest) {
		Map<String, Object> requestParams = new HashMap<>();
		if (httpServletRequest != null && CollectionUtils.isEmpty(httpServletRequest.getParameterMap()) == false) {
			for (String requestParamKey : httpServletRequest.getParameterMap().keySet()) {
				requestParams.put(requestParamKey, httpServletRequest.getParameter(requestParamKey));
			}
		}
		requestParams.put("moduleName", "File Bin");
		return requestParams;
	}

	@Override
	public Integer hasPermission(String fileBinId, String fileAssociationId, String fileUploadId, Integer queryType,
			HttpServletRequest httpServletRequest) throws Exception {
		Integer				hasPermission	= Constant.IS_ALLOWED;
		Map<String, Object>	requestParams	= createRequestParam(httpServletRequest);
		requestParams.put("queryType", queryType);
		List<Map<String, Object>> resultSetList = validateFilePermission(fileBinId, fileAssociationId, fileUploadId,
				requestParams);

		if (CollectionUtils.isEmpty(resultSetList) == false) {
			Map<String, Object> resultSetMap = resultSetList.get(0);
			if (resultSetMap.get("isAllowed") != null) {
				if (resultSetMap.get("isAllowed") instanceof Integer) {
					hasPermission = (Integer) (resultSetMap.get("isAllowed"));
				} else if (resultSetMap.get("isAllowed") instanceof Long) {
					Long fileBinIdCount = (Long) resultSetMap.get("isAllowed");
					hasPermission = fileBinIdCount.intValue();
				}
			}
		}
		return hasPermission;
	}

	@Override
	public Map<String, Object> validateFileQueries(MultiValueMap<String, String> formData) throws Exception {
		Map<String, Object> invalidQueryMap = new HashMap<>();
		if (CollectionUtils.isEmpty(formData) == false) {
			String				fileBinId				= formData.getFirst("fileBinId");
			String				selectFileQuery			= formData.getFirst("selectValidator_query");
			String				uploadFileQuery			= formData.getFirst("uploadValidator_query");
			String				viewFileQuery			= formData.getFirst("viewValidator_query");
			String				deleteFileQuery			= formData.getFirst("deleteValidator_query");

			Map<String, String>	fileValidatorQueries	= new HashMap<>();
			fileValidatorQueries.put("uploadValidator_query", uploadFileQuery);
			fileValidatorQueries.put("viewValidator_query", viewFileQuery);
			fileValidatorQueries.put("deleteValidator_query", deleteFileQuery);
			invalidQueryMap = validateSelectQuery(fileBinId, selectFileQuery);
			invalidQueryMap.putAll(validateDMLQueries(fileBinId, fileValidatorQueries));
		}
		return invalidQueryMap;
	}

	private Map<String, Object> validateSelectQuery(String fileBinId, String selectQuery) throws Exception {
		Map<String, Object>	invalidQueryMap		= new HashMap<>();
		Map<String, String>	invalidColumnMap	= new HashMap<>();
		Map<String, Object>	requestParamMap		= new HashMap<>();
		requestParamMap.put("fileBinId", fileBinId);
		requestParamMap.put("moduleName", "File Bin");
		requestParamMap.put("fileUploadId", "");
		requestParamMap.put("fileAssociationId", "");
		Map<String, String> requiredColumnMap = Constant.getSelectRequiredColumnMap();
		try {
			if (StringUtils.isBlank(selectQuery) == false) {
				String				query					= templatingUtils.processTemplateContents(selectQuery,
						"fileViewQuery", requestParamMap);
				Map<String, String>	resultSetMetadataMap	= fileUploadConfigDAO.validateFileQuery(null,
						query, requestParamMap);

				if (CollectionUtils.isEmpty(resultSetMetadataMap) == false) {

					for (Map.Entry<String, String> requiredColumn : requiredColumnMap.entrySet()) {
						String columnType = resultSetMetadataMap.get(requiredColumn.getKey());
						if (StringUtils.isBlank(columnType) == true
								|| columnType.equals(requiredColumn.getValue()) == false) {
							invalidColumnMap.put(requiredColumn.getKey(), requiredColumn.getValue());
						}
					} // End of forEach

				}
				invalidQueryMap.put("selectValidator_query", invalidColumnMap);
			}
			return invalidQueryMap;
		} catch (Exception exception) {
			logger.error("Error while validating queries ", exception.getMessage());
			Map<String, String> errorCode = new HashMap<>();
			errorCode.put("syntaxError", "You have an error in your SQL syntax ");
			invalidQueryMap.put("selectValidator_query", errorCode);
			return invalidQueryMap;
		}
	}

	private Map<String, Object> validateDMLQueries(String fileBinId, Map<String, String> fileValidatorQueries)
			throws Exception {
		Map<String, Object>	invalidQueryMap	= new HashMap<>();
		Map<String, Object>	parameterMap	= new HashMap<>();

		parameterMap.put("fileBinId", fileBinId);
		parameterMap.put("moduleName", "File Bin");
		parameterMap.put("fileUploadId", "");
		parameterMap.put("fileAssociationId", "");

		if (CollectionUtils.isEmpty(fileValidatorQueries) == false) {
			for (Map.Entry<String, String> validatorQueryMap : fileValidatorQueries.entrySet()) {
				Map<String, String>	invalidColumnMap	= new HashMap<>();
				Map<String, String>	requiredColumnMap	= Constant.getFileValidatorColumnMap();

				if (StringUtils.isBlank(validatorQueryMap.getValue()) == false) {
					String query = templatingUtils.processTemplateContents(validatorQueryMap.getValue(),
							"fileViewQuery", parameterMap);
					try {
						Map<String, String> resultSetMetadataMap = fileUploadConfigDAO.validateFileQuery(null,
								query, parameterMap);

						if (CollectionUtils.isEmpty(resultSetMetadataMap) == false) {

							for (Map.Entry<String, String> requiredColumn : requiredColumnMap.entrySet()) {
								String columnType = resultSetMetadataMap.get(requiredColumn.getKey());
								if (StringUtils.isBlank(columnType) == true
										|| columnType.contains(requiredColumn.getValue()) == false) {
									invalidColumnMap.put(requiredColumn.getKey(), requiredColumn.getValue());
								}
							} // End of forEach

						}
						invalidQueryMap.put(validatorQueryMap.getKey(), invalidColumnMap);
					} catch (Exception exception) {
						logger.error("Error while validating queries ", exception.getMessage());
						Map<String, String> errorCode = new HashMap<>();
						errorCode.put("syntaxError", "You have an error in your SQL syntax ");
						invalidQueryMap.put(validatorQueryMap.getKey(), errorCode);
					}
				}

			} // End of forEach
		}
		return invalidQueryMap;
	}

	@Override
	public List<Map<String, Object>> validateFilePermission(String fileBinId, String fileAssociationId,
			String fileUploadId, Map<String, Object> parameterMap) throws Exception {
		String		queryContent;

		FileUpload	fileUpload	= fileUploadRepository.findFileBinIdByUploadId(fileUploadId);
		if (StringUtils.isBlank(fileBinId) == true) {
			fileBinId = fileUpload.getFileBinId();
		}
		if (StringUtils.isBlank(fileAssociationId) == true && fileUpload != null
				&& StringUtils.isBlank(fileUpload.getFileAssociationId()) == false) {
			fileAssociationId = fileUpload.getFileAssociationId();
		}

		parameterMap.put("fileBinId", fileBinId);
		parameterMap.put("fileUploadId", fileUploadId);
		parameterMap.put("fileAssociationId", fileAssociationId);

		FileUploadConfig	fileUploadConfig	= fileUploadConfigRepository.getFileUploadConfig(fileBinId);

		Integer				queryType			= parameterMap.get("queryType") == null ? Constant.SELECT_FILE_VALIDATOR
				: (Integer) parameterMap.get("queryType");
		if (queryType.equals(Constant.UPLOAD_FILE_VALIDATOR)) {
			queryContent = fileUploadConfig.getUploadQueryContent();
		} else if (queryType.equals(Constant.VIEW_FILE_VALIDATOR)) {
			queryContent = fileUploadConfig.getViewQueryContent();
		} else {
			queryContent = fileUploadConfig.getDeleteQueryContent();
		}

		if (StringUtils.isBlank(queryContent) == false) {
			String						query		= templatingUtils.processTemplateContents(queryContent,
					"fileViewQuery", parameterMap);
			List<Map<String, Object>>	resultSet	= new ArrayList<>();
			resultSet = fileUploadConfigDAO.executeQueries(null, query, parameterMap);
			return resultSet;
		}
		return null;
	}

}
