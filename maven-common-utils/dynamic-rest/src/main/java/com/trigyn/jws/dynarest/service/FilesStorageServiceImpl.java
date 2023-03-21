package com.trigyn.jws.dynarest.service;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.ByteStreams;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;
import com.trigyn.jws.dbutils.spi.PropertyMasterKeyVO;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.vo.FileInfo;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynarest.dao.FileUploadConfigDAO;
import com.trigyn.jws.dynarest.dao.FileUploadConfigRepository;
import com.trigyn.jws.dynarest.entities.FileUpload;
import com.trigyn.jws.dynarest.entities.FileUploadConfig;
import com.trigyn.jws.dynarest.entities.FileUploadTemp;
import com.trigyn.jws.dynarest.repository.FileUploadRepository;
import com.trigyn.jws.dynarest.repository.FileUploadTempRepository;
import com.trigyn.jws.dynarest.utils.Constants;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;

@Component
@Qualifier(value = "file-system-storage")
@Transactional
public class FilesStorageServiceImpl implements FilesStorageService {

	private static final Logger			logger						= LogManager.getLogger(FilesStorageServiceImpl.class);

	@Autowired
	private PropertyMasterService		propertyMasterService		= null;

	@Autowired
	private PropertyMasterDetails		propertyMasterDetails		= null;

	@Autowired
	private IUserDetailsService			userdetailsService			= null;

	@Autowired
	private FileUploadRepository		fileUploadRepository		= null;

	@Autowired
	private FileUploadTempRepository	fileUploadTempRepository	= null;

	@Autowired
	private FileUploadConfigRepository	fileUploadConfigRepository	= null;

	@Autowired
	private FileUploadConfigDAO			fileUploadConfigDAO			= null;

	@Autowired
	private TemplatingUtils				templatingUtils				= null;

	private final static String			JWS_SALT					= "main alag duniya";

	@Autowired
	private DBTemplatingService			templatingService			= null;

	@Autowired
	private ActivityLog					activitylog					= null;

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
		logger.debug("Inside FilesStorageServiceImpl.save(fileBinId: {}, fileAssociationId: {})", fileBinId,
				fileAssociationId);

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
			FileUploadTemp	fileUpload	= saveFileTempDetails(location.toString(), file.getOriginalFilename(),
					fileBinId, fileAssociationId, UUID.randomUUID().toString(), 1, null, UUID.randomUUID().toString());
			Path			root		= Paths.get(location.toString());
			Files.copy(file.getInputStream(), root.resolve(fileUpload.getPhysicalFileName()));
			CryptoUtils.encrypt(JWS_SALT, root.resolve(fileUpload.getPhysicalFileName()).toFile(),
					root.resolve(fileUpload.getPhysicalFileName()).toFile());
			return fileUpload.getFileUploadId();
		} catch (Exception a_exc) {
			logger.error("Could not store the file. Error: ", a_exc);
			return null;
		}
	}

	public FileUploadTemp saveFileTempDetails(String location, String originalFilename, String fileBinId,
			String fileAssociationId, String fileUploadId, Integer action, String fileUploadTempId, String physicalFileName) {
		logger.debug(
				"Inside FilesStorageServiceImpl.saveFileTempDetails(location: {}, originalFilename: {}, fileBinId: {}, fileAssociationId: {})",
				location, originalFilename, fileBinId, fileAssociationId);

		UserDetailsVO	userDetailsVO	= userdetailsService.getUserDetails();
		FileUploadTemp	fileUpload		= new FileUploadTemp();
		fileUpload.setFileUploadTempId(fileUploadTempId);
		fileUpload.setFileUploadId(fileUploadId);
		fileUpload.setFilePath(location);
		fileUpload.setOriginalFileName(originalFilename);
		fileUpload.setPhysicalFileName(physicalFileName);
		fileUpload.setUpdatedBy(userDetailsVO.getUserName());
		fileUpload.setFileBinId(fileBinId);
		fileUpload.setFileAssociationId(fileAssociationId);
		fileUpload.setAction(action);
		return fileUploadTempRepository.save(fileUpload);
	}

	public FileUpload saveFileDetails(String location, String originalFilename, String fileBinId,
			String fileAssociationId) {
		logger.debug(
				"Inside FilesStorageServiceImpl.saveFileDetails(location: {}, originalFilename: {}, fileBinId: {}, fileAssociationId: {})",
				location, originalFilename, fileBinId, fileAssociationId);

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
	public Map<String, Object> load(String fileUploadId) throws Exception {
		logger.debug("Inside FilesStorageServiceImpl.load(fileUploadId: {})", fileUploadId);

		try {
			Map<String, Object>	details				= new HashMap<>();
			FileUpload			fileUploadDetails	= fileUploadRepository.findById(fileUploadId).orElse(null);
			if (fileUploadDetails == null) {
				List<FileUploadTemp> fileUploadTempDetails = fileUploadTempRepository
						.findAllTempFileUpload(fileUploadId);
				if (fileUploadTempDetails != null && fileUploadTempDetails.isEmpty() == false) {
					fileUploadDetails = new FileUpload();
					fileUploadDetails.setFilePath(fileUploadTempDetails.get(0).getFilePath());
					fileUploadDetails.setPhysicalFileName(fileUploadTempDetails.get(0).getPhysicalFileName());
					fileUploadDetails.setOriginalFileName(fileUploadTempDetails.get(0).getOriginalFileName());
				}
			}
			if (fileUploadDetails == null) {
				throw (new Exception("file not found with id : " + fileUploadId));
			}
			Path		root		= Paths.get(fileUploadDetails.getFilePath());
			Path		filePath	= root.resolve(fileUploadDetails.getPhysicalFileName());
			Resource	resource	= new UrlResource(filePath.toUri());
			String		mimeType	= URLConnection.guessContentTypeFromName(fileUploadDetails.getOriginalFileName());
			if (resource.exists() || resource.isReadable()) {
				File	newFile		= resource.getFile();
				byte[]	newFiles	= CryptoUtils.decrypt(JWS_SALT, newFile, null);
				details.put("file", newFiles);

			} else {
				String		filePathStr	= fileUploadDetails.getFilePath() + "/"
						+ fileUploadDetails.getPhysicalFileName();
				InputStream	in			= FilesStorageServiceImpl.class.getResourceAsStream(filePathStr);
				if (in == null) {
					throw new Exception("Could not read the file with name - " + fileUploadDetails.getOriginalFileName()
							+ " : fileBinId : " + fileUploadDetails.getFileBinId() + " : fileUploadId : "
							+ fileUploadDetails.getFileUploadId());
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
			logger.error("Error: " + a_exc.getMessage());
			throw new Exception(a_exc.getMessage());
		}
	}

	@Override
	public void deleteAll() {
		logger.debug("Inside FilesStorageServiceImpl.deleteAll()");

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
		logger.debug("Inside FilesStorageServiceImpl.loadAll()");

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
	public List<FileInfo> getFileDetailsByFileUploadIds(List<String> fileUploadIdList) throws Exception {
		logger.debug("Inside FilesStorageServiceImpl.getFileDetailsByFileUploadIds(fileUploadIdList: {})",
				fileUploadIdList);

		List<FileUpload>	fileUploadList	= fileUploadRepository.findAllByFileUploadIds(fileUploadIdList);
		List<FileInfo>		fileInfoList	= convertFileUploadToFileInfo(fileUploadList);
		return fileInfoList;
	}

	@Override
	public List<FileInfo> getFileDetailsByConfigId(String fileBinId, String fileAssociationId,
			Map<String, Object> requestParamMap) throws Exception {
		logger.debug(
				"Inside FilesStorageServiceImpl.getFileDetailsByConfigId(fileBinId: {}, fileAssociationId: {}, requestParamMap: {})",
				fileBinId, fileAssociationId, requestParamMap);

		List<FileUpload>	fileUploadList		= new ArrayList<>();
		FileUploadConfig	fileUploadConfig	= fileUploadConfigRepository.getFileUploadConfig(fileBinId);
		if (fileUploadConfig != null) {
			String queryContent = fileUploadConfig.getSelectQueryContent();
			if (StringUtils.isBlank(queryContent) == false) {
				requestParamMap.put("fileAssociationId", fileAssociationId);
				String query = templatingUtils.processTemplateContents(queryContent, "fileViewQuery", requestParamMap);
				fileUploadList = fileUploadConfigDAO.executeSelectQuery(null, query, requestParamMap);
			} else {
				fileUploadList = fileUploadRepository.findAllFilesByConfigId(fileBinId, fileAssociationId);
			}
		}
		List<FileInfo> fileInfoList = convertFileUploadToFileInfo(fileUploadList);
		return fileInfoList;
	}

	private List<FileInfo> convertFileUploadToFileInfo(List<FileUpload> fileUploads) throws Exception {
		logger.debug("Inside FilesStorageServiceImpl.convertFileUploadToFileInfo(fileUploads: {})", fileUploads);
		List<FileInfo> fileInfos = new ArrayList<>();
		for (FileUpload fileUploadEntity : fileUploads) {
			String	filePath	= fileUploadEntity.getFilePath() + File.separator
					+ fileUploadEntity.getPhysicalFileName();
			File	file		= new File(filePath);
			if (file.length() == 0) {
				InputStream in = FilesStorageServiceImpl.class.getResourceAsStream(filePath);
				if (in == null) {
					System.out.println("File Not Found : fileBinId: " + fileUploadEntity.getFileBinId()
							+ " : filePath : " + filePath);
					FileInfo fileInfo = new FileInfo();
					fileInfo.setFileName(fileUploadEntity.getOriginalFileName());
					fileInfo.setFileId(fileUploadEntity.getFileUploadId());
					// byte[] byteArray = null;
					// byteArray = ByteStreams.toByteArray(in);
					fileInfo.setSizeInBytes(file.length());
					fileInfo.setWarningMessage("File Not Found with name - " + fileUploadEntity.getOriginalFileName());
					fileInfos.add(fileInfo);
					// throw new ResponseStatusException(HttpStatus.NOT_FOUND);
				} else {
					byte[] byteArray = null;
					byteArray = ByteStreams.toByteArray(in);
					fileInfos.add(new FileInfo(fileUploadEntity.getFileUploadId(),
							fileUploadEntity.getOriginalFileName(), Long.valueOf(byteArray.length)));
				}
			} else {
				fileInfos.add(new FileInfo(fileUploadEntity.getFileUploadId(), fileUploadEntity.getOriginalFileName(),
						file.length()));
			}
		}
		return fileInfos;
	}

	@Override
	public void deleteFileByFileUploadId(String fileUploadId) throws Exception {
		logger.debug("Inside FilesStorageServiceImpl.deleteFileByFileUploadId(fileUploadId: {})", fileUploadId);

		FileUpload fileUploadDetails = fileUploadRepository.findFileBinIdByUploadId(fileUploadId);
		if (fileUploadDetails != null) {
			saveFileTempDetails(fileUploadDetails.getFilePath().toString(), fileUploadDetails.getOriginalFileName(),
					fileUploadDetails.getFileBinId(), fileUploadDetails.getFileAssociationId(),
					fileUploadDetails.getFileUploadId(), -1, null, fileUploadDetails.getPhysicalFileName());
		} else {
			List<FileUploadTemp> tempFileUploadDetails = fileUploadTempRepository.findAllTempFileUpload(fileUploadId);
			if (tempFileUploadDetails != null) {
				for (FileUploadTemp fileTemp : tempFileUploadDetails) {
					saveFileTempDetails(fileTemp.getFilePath().toString(), fileTemp.getOriginalFileName(),
							fileTemp.getFileBinId(), fileTemp.getFileAssociationId(), fileTemp.getFileUploadId(), -1,
							fileTemp.getFileUploadTempId(), fileTemp.getPhysicalFileName());
				}
			}
		}
	}

	@Override
	public Map<String, Object> createRequestParam(HttpServletRequest httpServletRequest) {
		logger.debug("Inside FilesStorageServiceImpl.convertFileUploadToFileInfo()");

		Map<String, Object> requestParams = new HashMap<>();
		if (httpServletRequest != null && CollectionUtils.isEmpty(httpServletRequest.getParameterMap()) == false) {
			for (String requestParamKey : httpServletRequest.getParameterMap().keySet()) {
				requestParams.put(requestParamKey, httpServletRequest.getParameter(requestParamKey));
			}
		}
		return requestParams;
	}

	@Override
	public Integer hasPermission(String fileBinId, String fileAssociationId, String fileUploadId, Integer queryType,
			Map<String, Object> requestParamMap) throws Exception {
		logger.debug(
				"Inside FilesStorageServiceImpl.hasPermission(fileBinId: {}, fileAssociationId: {}, queryType: {})",
				fileBinId, fileAssociationId, queryType);

		Integer hasPermission = Constants.IS_ALLOWED;
		requestParamMap.put("queryType", queryType);
		requestParamMap.put("moduleName", Constants.MODULE_NAME);

		List<Map<String, Object>> resultSetList = validateFilePermission(fileBinId, fileAssociationId, fileUploadId,
				requestParamMap);

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

	public String isValidQueries(MultiValueMap<String, String> formData) throws Exception {
		Map<String, Object>	invalidQueryMap	= validateFileQueries(formData);
		StringBuilder		errorMessage	= new StringBuilder();
		Map<String, String>	queryNameMap	= Constants.getQueryName();

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

	@Override
	public Map<String, Object> validateFileQueries(MultiValueMap<String, String> formData) throws Exception {
		logger.debug("Inside FilesStorageServiceImpl.validateFileQueries(formData: {})", formData);

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
		logger.debug("Inside FilesStorageServiceImpl.validateSelectQuery(fileBinId: {}, selectQuery: {})", fileBinId,
				selectQuery);

		Map<String, Object>	invalidQueryMap		= new HashMap<>();
		Map<String, String>	invalidColumnMap	= new HashMap<>();
		Map<String, Object>	requestParamMap		= new HashMap<>();

		requestParamMap.put("fileBinId", fileBinId);
		requestParamMap.put("moduleName", "File Bin");
		requestParamMap.put("fileUploadId", "");
		requestParamMap.put("fileAssociationId", "");

		Map<String, String> requiredColumnMap = Constants.getSelectRequiredColumnMap();
		try {
			if (StringUtils.isBlank(selectQuery) == false) {
				String				query					= templatingUtils.processTemplateContents(selectQuery,
						"fileViewQuery", requestParamMap);
				Map<String, String>	resultSetMetadataMap	= fileUploadConfigDAO.validateFileQuery(null, query,
						requestParamMap);

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
		logger.debug("Inside FilesStorageServiceImpl.validateDMLQueries(fileBinId: {}, fileValidatorQueries: {})",
				fileBinId, fileValidatorQueries);

		Map<String, Object>	invalidQueryMap	= new HashMap<>();
		Map<String, Object>	parameterMap	= new HashMap<>();

		parameterMap.put("fileBinId", fileBinId);
		parameterMap.put("moduleName", "File Bin");
		parameterMap.put("fileUploadId", "");
		parameterMap.put("fileAssociationId", "");

		if (CollectionUtils.isEmpty(fileValidatorQueries) == false) {
			for (Map.Entry<String, String> validatorQueryMap : fileValidatorQueries.entrySet()) {
				Map<String, String>	invalidColumnMap	= new HashMap<>();
				Map<String, String>	requiredColumnMap	= Constants.getFileValidatorColumnMap();

				if (StringUtils.isBlank(validatorQueryMap.getValue()) == false) {
					String query = templatingUtils.processTemplateContents(validatorQueryMap.getValue(),
							"fileViewQuery", parameterMap);
					try {
						Map<String, String> resultSetMetadataMap = fileUploadConfigDAO.validateFileQuery(null, query,
								parameterMap);

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
		logger.debug(
				"Inside FilesStorageServiceImpl.validateFilePermission(fileBinId: {}, fileAssociationId: {}, fileUploadId: {}, parameterMap: {})",
				fileBinId, fileAssociationId, fileUploadId, parameterMap);

		String		queryContent	= "";

		FileUpload	fileUpload		= fileUploadRepository.findFileBinIdByUploadId(fileUploadId);
		if (fileUpload != null && StringUtils.isBlank(fileBinId) == true) {
			fileBinId = fileUpload.getFileBinId();
		}
		if (StringUtils.isBlank(fileAssociationId) == true && fileUpload != null
				&& StringUtils.isBlank(fileUpload.getFileAssociationId()) == false) {
			fileAssociationId = fileUpload.getFileAssociationId();
		}

		if (fileUpload == null && fileBinId == null && fileAssociationId == null) {
			List<FileUploadTemp> fileUploadTempDetails = fileUploadTempRepository.findAllTempFileUpload(fileUploadId);
			if (fileUploadTempDetails != null && fileUploadTempDetails.isEmpty() == false) {
				fileBinId			= fileUploadTempDetails.get(0).getFileBinId();
				fileAssociationId	= fileUploadTempDetails.get(0).getFileAssociationId();
			}
		}

		parameterMap.put("fileBinId", fileBinId);
		parameterMap.put("fileUploadId", fileUploadId);
		parameterMap.put("fileAssociationId", fileAssociationId);

		FileUploadConfig	fileUploadConfig	= fileUploadConfigRepository.getFileUploadConfig(fileBinId);

		Integer				queryType			= parameterMap.get("queryType") == null
				? Constants.SELECT_FILE_VALIDATOR
				: (Integer) parameterMap.get("queryType");

		String				templateName		= "fileSelectValidatorQuery";
		int					queryTypeValidator	= 1;
		if (fileUploadConfig != null) {
			if (queryType.equals(Constants.UPLOAD_FILE_VALIDATOR)) {
				queryContent	= fileUploadConfig.getUploadQueryContent();
				templateName	= "fileUploadValidatorQuery";
			} else if (queryType.equals(Constants.VIEW_FILE_VALIDATOR)) {
				queryContent	= fileUploadConfig.getViewQueryContent();
				templateName	= "fileViewValidatorQuery";
			} else {
				queryContent	= fileUploadConfig.getDeleteQueryContent();
				templateName	= "fileDeleteValidatorQuery";
			}
		}
		// Below code is added to handle the null pointer exception if queryContent is
		// not passed.
		if (queryContent == null || queryContent.isBlank() || queryContent.isEmpty()) {
			queryContent = "select 1";
		}

		List<Map<String, Object>>	resultSet	= new ArrayList<>();
		if (StringUtils.isBlank(queryContent) == false
				&& Constants.QueryType.SELECT.getQueryType() == queryTypeValidator) {
			String						query		= templatingUtils.processTemplateContents(queryContent, templateName,
			      						     		                       				parameterMap);
			resultSet = fileUploadConfigDAO.executeQueries(null, query, parameterMap);
		} else {
			TemplateVO		templateVO			= templatingService.getTemplateByName("script-util");
			StringBuilder	resultStringBuilder	= new StringBuilder();
			resultStringBuilder.append(templateVO.getTemplate()).append("\n");
			ScriptEngineManager	scriptEngineManager	= new ScriptEngineManager();
			ScriptEngine		scriptEngine		= scriptEngineManager.getEngineByName("nashorn");
			
			parameterMap.forEach((key, value) -> scriptEngine.put(key, value));
			UserDetailsVO detailsVO = userdetailsService.getUserDetails();
			if (detailsVO != null) {
				scriptEngine.put("loggedInUserName", detailsVO.getUserName());
				scriptEngine.put("loggedInUserRoleList", detailsVO.getRoleIdList());
				scriptEngine.put("loggedInUserId", detailsVO.getUserId());
				scriptEngine.put("fullName", detailsVO.getFullName());
				scriptEngine.put("userObject", detailsVO);
			}
			Map<PropertyMasterKeyVO, String>	propertyMasterMap	= propertyMasterDetails.getAllProperties();
			
			Map systemProperties = new HashMap();
			propertyMasterMap.forEach((key, value) -> systemProperties.put(key.getPropertyName(), value));
			
			scriptEngine.put("systemProperties", propertyMasterMap);
			
			HttpServletRequest requestObject = getRequest();
			
			if(requestObject != null) {
				Map<String, String> headerMap = new HashMap<>();
				Enumeration<String> headerNames = requestObject.getHeaderNames();
				if (headerNames != null) {
					while (headerNames.hasMoreElements()) {
						String header = headerNames.nextElement();
						headerMap.put(header, requestObject.getHeader(header));
					}
				}
				
				scriptEngine.put("httpRequestObject", requestObject);
				scriptEngine.put("requestHeaders", headerMap);
				scriptEngine.put("session", requestObject.getSession());
			}
			
			resultStringBuilder.append(queryContent.toString());
			Object				result			= scriptEngine.eval(resultStringBuilder.toString());
			Map<String, Object>	resultSetMap	= new HashMap<>();
			resultSetMap.put("isAllowed", result);
			if (resultSetMap.size() > 0) {
				resultSet = new ArrayList<>();
				resultSet.add(resultSetMap);
			}
		}
		return resultSet;
	}
	
	private HttpServletRequest getRequest() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if(sra != null) {
			return sra.getRequest();
		} else 
			return null;
	}

	@Override
	public void commitChanges(String fileBinId, String fileAssociationId) throws Exception {
		commitChanges(fileBinId, fileAssociationId, null);
	}

	/**
	 * Purpose of this method is to log activities</br>
	 * in File Bins-Common File Module.
	 * 
	 * @author              Bibhusrita.Nayak
	 * @param  returnResult
	 * @throws Exception
	 */
	private void logActivity(Map<String, List<Object[]>> returnResult) throws Exception {
		Map<String, String>	requestParams		= new HashMap<>();
		UserDetailsVO		detailsVO			= userdetailsService.getUserDetails();
		Date				activityTimestamp	= new Date();
		for (Map.Entry<String, List<Object[]>> entry : returnResult.entrySet()) {
			String			key	= entry.getKey();
			List<Object[]>	val	= entry.getValue();
			for (Object[] queryRes : val) {
				if (queryRes != null) {
					if (key == "INSERT") {
						requestParams.put("action", Constants.ACTION_UPL);
					} else {
						requestParams.put("action", Constants.ACTION_DEL);
					}
					requestParams.put("entityName", (String) queryRes[0] + "-" + (String) queryRes[1]);
					requestParams.put("masterModuleType", Constants.MODULE_NAME);
					requestParams.put("typeSelect", Constants.CHANGETYPE);
					requestParams.put("userName", detailsVO.getUserName());
					requestParams.put("message", "");
					requestParams.put("date", activityTimestamp.toString());
				}
				activitylog.activitylog(requestParams);
			}
		}
	}

	@Override
	public boolean chkAndDeleteFromTemp(String fileUploadId) throws Exception {
		List<FileUploadTemp> tempFileUploadDetails = fileUploadTempRepository.findAllTempFileUpload(fileUploadId);
		if (tempFileUploadDetails != null) {
			for (FileUploadTemp fileUploadTemp : tempFileUploadDetails) {
				String	filePath	= fileUploadTemp.getFilePath() + File.separator
						+ fileUploadTemp.getPhysicalFileName();
				File	file		= new File(filePath);
				if(file.exists()) {
					file.delete();
				}
				fileUploadTempRepository.deleteById(fileUploadTemp.getFileUploadTempId());
				return true;
			}
		}
		return false;
	}

	@Override
	public void commitChanges(String fileBinId, String fileAssociationId, String fileUploadTempId) throws Exception {
		List<FileUploadTemp>		fileUploadDetails	= fileUploadTempRepository
				.getAllTempDeletedFileUploadId(fileBinId, fileAssociationId, fileUploadTempId);

		List<FileUploadTemp>	fileUploadTempDetails	= fileUploadTempRepository
				.getAllTempDeletedFileUploadTempId(fileBinId, fileAssociationId, fileUploadTempId);
		
		List<FileUploadTemp> fileList = new ArrayList<>();
		if(fileUploadDetails != null && fileUploadDetails.isEmpty() == false) {
			fileList.addAll(fileUploadDetails);
		}
		if(fileUploadTempDetails != null && fileUploadTempDetails.isEmpty() == false) {
			fileList.addAll(fileUploadTempDetails);
		}
		
		Map<String, List<Object[]>>	returnResult		= fileUploadConfigDAO.commitChanges(fileBinId,
				fileAssociationId, fileUploadTempId);
		/* Method called for implementing Activity Log */
		logActivity(returnResult);
		fileUploadConfigDAO.clearTempFileBin(fileBinId, fileAssociationId, fileUploadTempId);
		for (FileUploadTemp fileUpload : fileList) {
			if (fileUpload != null) {
				File file = new File(fileUpload.getFilePath());
				if (file.exists()) {
					Path		root		= Paths.get(fileUpload.getFilePath());
					Path		filePath	= root.resolve(fileUpload.getPhysicalFileName());
					Resource	resource	= new UrlResource(filePath.toUri());
					if (resource.exists() || resource.isReadable()) {
						resource.getFile().delete();
					}
				}
			}
		}

	}

	@Override
	public String update(MultipartFile file, String fileUploadId) throws Exception {
		UserDetailsVO	userDetailsVO		= userdetailsService.getUserDetails();
		FileUpload		fileUploadDetail	= fileUploadRepository.findById(fileUploadId)
				.orElseThrow(() -> new Exception("file not found with id : " + fileUploadId));
		fileUploadDetail.setUpdatedBy(userDetailsVO.getUserName());
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
			fileUploadDetail.setFilePath(location.toString());
			fileUploadRepository.save(fileUploadDetail);
			Path root = Paths.get(location.toString());
			Files.copy(file.getInputStream(), root.resolve(fileUploadDetail.getPhysicalFileName()),
					StandardCopyOption.REPLACE_EXISTING);
			CryptoUtils.encrypt(JWS_SALT, root.resolve(fileUploadDetail.getPhysicalFileName()).toFile(),
					root.resolve(fileUploadDetail.getPhysicalFileName()).toFile());
		} catch (Exception a_exc) {
			logger.error("Could not store the file. Error: ", a_exc);
			return fileUploadId;
		}
		return fileUploadId;
	}

	@Override
	public void clearTempFileBin(String fileBinId, String fileAssociationId) throws Exception {
		List<FileUploadTemp> fileUploadDetails = fileUploadTempRepository.getAllTempDeletedFileUploadId(fileBinId,
				fileAssociationId, null);
		if (fileUploadDetails != null && fileUploadDetails.size() > 0) {
			String[]	fileTempIds	= new String[fileUploadDetails.size()];
			int			fileCounter	= 0;
			for (FileUploadTemp fileUpload : fileUploadDetails) {
				if (fileUpload != null) {
					fileTempIds[fileCounter] = fileUpload.getFileUploadId();
				}
			}
			clearTempFileBin(fileBinId, fileAssociationId, fileTempIds);
		}
	}

	@Override
	public Boolean isExtensionSupported(String fileBinId, MultipartFile[] files) {
		if (fileBinId != null) {
			FileUploadConfig fileUploadConfig = fileUploadConfigRepository.getFileUploadConfig(fileBinId);
			if (fileUploadConfig != null) {
				String supportedExtensions = fileUploadConfig.getFileTypSupported();
				if (supportedExtensions != null
						&& (supportedExtensions.equals("*") || supportedExtensions.equals(".*"))) {
					return true;
				}

				if (supportedExtensions != null && supportedExtensions.isEmpty() == false && files != null
						&& files.length > 0 && files[0] != null) {
					MultipartFile	file		= files[0];
					String			fileName	= file.getOriginalFilename();
					if (fileName.contains(".")) {
						String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
						if (supportedExtensions.contains(fileExtension) == false) {
							return false;
						}
					}
				}
			}

		}
		return true;
	}

	@Override
	public Boolean isMaxFileSizeExceed(String fileBinId, MultipartFile[] files) {
		if (fileBinId != null) {
			FileUploadConfig fileUploadConfig = fileUploadConfigRepository.getFileUploadConfig(fileBinId);
			if (fileUploadConfig != null) {
				BigDecimal maxFileSize = fileUploadConfig.getMaxFileSize();
				if (files != null && files.length > 0 && files[0] != null) {
					MultipartFile	file		= files[0];
					long			fileSize	= file.getSize();
					if (fileSize > maxFileSize.longValue()) {
						return false;
					}
				}
			}

		}
		return true;
	}

	@Override
	public void clearTempFileBin(String fileBinId, String fileAssociationId, String[] fileUploadTempIds)
			throws Exception {

		if (fileUploadTempIds == null || fileBinId == null || fileAssociationId == null) {
			logger.error("Error: Parameters are empty. Could not clear the bile bin's.");
			return;
		}
		for (int iFileCounter = 0; iFileCounter < fileUploadTempIds.length; iFileCounter++) {
			String					fileUploadTempId	= fileUploadTempIds[iFileCounter];
			List<FileUploadTemp>	fileUploadDetails	= fileUploadTempRepository
					.getAllTempDeletedFileUploadId(fileBinId, fileAssociationId, fileUploadTempId);
			List<FileUploadTemp>	fileUploadTempDetails	= fileUploadTempRepository
					.getAllTempDeletedFileUploadTempId(fileBinId, fileAssociationId, fileUploadTempId);
			
			List<FileUploadTemp> fileList = new ArrayList<>();
			if(fileUploadDetails != null && fileUploadDetails.isEmpty() == false) {
				fileList.addAll(fileUploadDetails);
			}
			if(fileUploadTempDetails != null && fileUploadTempDetails.isEmpty() == false) {
				fileList.addAll(fileUploadTempDetails);
			}
			
			fileUploadConfigDAO.clearTempFileBin(fileBinId, fileAssociationId, fileUploadTempId);

			for (FileUploadTemp fileUpload : fileList) {
				if (fileUpload != null) {
					File file = new File(fileUpload.getFilePath());
					if (file.exists()) {
						file.delete();
					}
				}
			}
		}
	}

	@Override
	public boolean checkFileExistByUploadId(String fileUploadId) throws Exception {
		List<FileUploadTemp> tempFileUploadDetails = fileUploadTempRepository.findAllTempFileUpload(fileUploadId);
		if (tempFileUploadDetails != null && tempFileUploadDetails.size() > 0)
			return true;
		else
			return false;
	}

	@Override
	public String updateFileUploadTemp(MultipartFile file, String fileUploadId, String fileBinId,
			String fileAssociationId) throws Exception {
		logger.debug("Inside FilesStorageServiceImpl.updateFileUploadTemp(fileBinId: {}, fileAssociationId: {})",
				fileBinId, fileAssociationId);

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
			List<FileUploadTemp> fileUploadTempDetails = fileUploadTempRepository.findAllTempFileUpload(fileUploadId);
			if (fileUploadTempDetails.size() > 0) {
				FileUploadTemp	fileUpload	= saveFileTempDetails(location.toString(), file.getOriginalFilename(),
						fileBinId, fileAssociationId, fileUploadTempDetails.get(0).getFileUploadId(), 1,
						fileUploadTempDetails.get(0).getFileUploadTempId(), UUID.randomUUID().toString());
				Path			root		= Paths.get(location.toString());
				Files.copy(file.getInputStream(), root.resolve(fileUpload.getPhysicalFileName()));
				CryptoUtils.encrypt(JWS_SALT, root.resolve(fileUpload.getPhysicalFileName()).toFile(),
						root.resolve(fileUpload.getPhysicalFileName()).toFile());
				return fileUpload.getFileUploadId();
			}
		} catch (Exception a_exc) {
			logger.error("Could not store the file. Error: ", a_exc);
			return null;
		}
		return null;
	}

}