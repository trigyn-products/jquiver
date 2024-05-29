package com.trigyn.jws.dynarest.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dynarest.dao.FileUploadConfigDAO;
import com.trigyn.jws.dynarest.dao.FileUploadConfigRepository;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.dynarest.entities.FileUploadConfig;
import com.trigyn.jws.dynarest.utils.Constants;
import com.trigyn.jws.dynarest.vo.FileUploadConfigVO;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibrary;

@Service
@Transactional
public class FileUploadConfigService {

	private static final Logger logger = LogManager.getLogger(FileUploadConfigService.class);

	@Autowired
	private FileUploadConfigRepository fileUploadConfigRepository = null;

	@Autowired
	private PropertyMasterService propertyMasterService 		  = null;

	@Autowired
	private FileUploadConfigDAO fileUploadConfigDAO 			  = null;

	@Autowired
	private JwsDynarestDAO dynarestDAO 							  = null;

	public FileUploadConfig getFileUploadConfigByBinId(String fileBinId) throws Exception {
		logger.debug("Inside FileUploadConfigService.getFileUploadConfigByBinId(fileBinId: {})", fileBinId);
		return fileUploadConfigRepository.getFileUploadConfig(fileBinId);
	}

	public void saveFileUploadConfig(FileUploadConfig fileUploadConfig) throws Exception {
		logger.debug("Inside FileUploadConfigService.saveFileUploadConfig(fileUploadConfig: {})", fileUploadConfig);
		fileUploadConfigRepository.save(fileUploadConfig);
	}

	public FileUploadConfigVO convertFileUploadEntityToVO(FileUploadConfig entity) {
		logger.debug("Inside FileUploadConfigService.convertFileUploadEntityToVO(entity: {})", entity);

		FileUploadConfigVO config = new FileUploadConfigVO(entity.getFileBinId(), entity.getFileTypSupported(),
				entity.getMaxFileSize(), entity.getNoOfFiles(), entity.getUploadQueryContent(),
				entity.getViewQueryContent(), entity.getDeleteQueryContent(), entity.getIsDeleted(),
				entity.getLastUpdatedBy(), entity.getLastUpdatedTs(), entity.getCreatedBy(), entity.getCreatedDate(),
				entity.getUploadQueryType(), entity.getViewQueryType(), entity.getDeleteQueryType(),
				entity.getDatasourceViewValidator(), entity.getDatasourceUploadValidator(),
				entity.getDatasourceDeleteValidator(), entity.getIsCustomUpdated(), entity.getUploadScriptLibraryId(),
				entity.getViewScriptLibraryId(), entity.getDeleteScriptLibraryId());
		return config;

	}

	public FileUploadConfig convertFileUploadVOToEntity(FileUploadConfigVO vo)
			throws JsonMappingException, JsonProcessingException {
		logger.debug("Inside FileUploadConfigService.convertFileUploadVOToEntity(vo: {})", vo);
		FileUploadConfig config = new FileUploadConfig();

		config.setFileTypSupported(vo.getFileTypSupported());
		config.setFileBinId(vo.getFileBinId());
		config.setIsDeleted(vo.getIsDeleted());
		config.setMaxFileSize(vo.getMaxFileSize());
		config.setNoOfFiles(vo.getNoOfFiles());
		config.setLastUpdatedBy(vo.getUpdatedBy());
		config.setLastUpdatedTs(vo.getUpdatedDate());
		config.setDatasourceId(vo.getDatasourceId());
		config.setDeleteQueryType(vo.getDeleteQueryType());
		config.setDeleteQueryContent(vo.getDeleteQueryContent());
		config.setDatasourceDeleteValidator(vo.getDatasourceDeleteValidator());
		config.setUploadQueryType(vo.getUploadQueryType());
		config.setUploadQueryContent(vo.getUploadQueryContent());
		config.setDatasourceUploadValidator(vo.getDatasourceUploadValidator());
		config.setViewQueryType(vo.getViewQueryType());
		config.setViewQueryContent(vo.getViewQueryContent());
		config.setDatasourceViewValidator(vo.getDatasourceViewValidator());
		config.setIsCustomUpdated(vo.getIsCustomUpdated());
		config.setUploadScriptLibraryId(
				vo.getUploadScriptLibraryId() != null ? vo.getUploadScriptLibraryId().trim() : "");
		config.setViewScriptLibraryId(vo.getViewScriptLibraryId() != null ? vo.getViewScriptLibraryId().trim() : "");
		config.setDeleteScriptLibraryId(
				vo.getDeleteScriptLibraryId() != null ? vo.getDeleteScriptLibraryId().trim() : "");

		return config;
	}

	public String getFileUploadJson(String entityId) throws Exception {
		logger.debug("Inside FileUploadConfigService.getFileUploadJson(entityId: {})", entityId);

		FileUploadConfig fileUploadConfig = getFileUploadConfigByBinId(entityId);
		List<String> scriptLibUploadList =  fileUploadConfigDAO.getFileBinScriptLibId("upload_" + entityId);
		List<String> scriptLibViewList   = fileUploadConfigDAO.getFileBinScriptLibId("view_" + entityId);
		List<String> scriptLibDeleteList = fileUploadConfigDAO.getFileBinScriptLibId("delete_" + entityId);
		List<String> scriptLibUploadId   = new ArrayList<>();
		List<String> scriptLibViewId     = new ArrayList<>();
		List<String> scriptLibDeleteId   = new ArrayList<>();
		for (int iScriptUploadCounter = 0; iScriptUploadCounter < scriptLibUploadList.size(); iScriptUploadCounter++) {
			scriptLibUploadId.add("\"" + scriptLibUploadList.get(iScriptUploadCounter) + "\"");
			fileUploadConfig.setUploadScriptLibraryId(scriptLibUploadId.toString());
		}
		for (int iScriptViewCounter = 0; iScriptViewCounter < scriptLibViewList.size(); iScriptViewCounter++) {
			scriptLibViewId.add("\"" + scriptLibViewList.get(iScriptViewCounter) + "\"");
			fileUploadConfig.setViewScriptLibraryId(scriptLibViewId.toString());
		}
		for (int iScriptDeleteCounter = 0; iScriptDeleteCounter < scriptLibDeleteList.size(); iScriptDeleteCounter++) {
			scriptLibDeleteId.add("\"" + scriptLibDeleteList.get(iScriptDeleteCounter) + "\"");
			fileUploadConfig.setDeleteScriptLibraryId(scriptLibDeleteId.toString());
		}

		String jsonString = "";

		if (fileUploadConfig != null) {
			fileUploadConfig = fileUploadConfig.getObject();
			FileUploadConfigVO fileUploadConfigVO = convertFileUploadEntityToVO(fileUploadConfig);

			Gson gson = new Gson();

			ObjectMapper objectMapper = new ObjectMapper();
			String dbDateFormat = propertyMasterService.getDateFormatByName(Constant.PROPERTY_MASTER_OWNER_TYPE,
					Constant.PROPERTY_MASTER_OWNER_ID, Constant.JWS_DATE_FORMAT_PROPERTY_NAME,
					com.trigyn.jws.dbutils.utils.Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);
			DateFormat dateFormat = new SimpleDateFormat(dbDateFormat);
			objectMapper.setDateFormat(dateFormat);
			Map<String, Object> objectMap = objectMapper.convertValue(fileUploadConfigVO, TreeMap.class);
			jsonString = gson.toJson(objectMap);
		}

		return jsonString;
	}

	public void scriptLibSave(FileUploadConfig fileUploadConfig) throws Exception {
		logger.debug("Inside FileUploadConfigService.scriptLibSave(fileUploadConfig: {})", fileUploadConfig);
		ObjectMapper objectMapper = new ObjectMapper();
		String entityId;
		String scriptUploadId;
		String scriptViewId;
		String scriptDeleteId;
		String moduleId = Constants.FILE_BIN_MOD_ID;
		List<String> entityIdList = new ArrayList<>();
		List<String> scriptUploadIdList = new ArrayList<>();
		List<String> scriptViewIdList = new ArrayList<>();
		List<String> scriptDeleteIdList = new ArrayList<>();
		List<ScriptLibrary> scriptLibInsert = new ArrayList<>();
		if (null == fileUploadConfig.getDeleteScriptLibraryId()) {
			fileUploadConfig.setDeleteScriptLibraryId("");
		} else if (null == fileUploadConfig.getUploadScriptLibraryId()) {
			fileUploadConfig.setUploadScriptLibraryId("");
		} else if (null == fileUploadConfig.getViewScriptLibraryId()) {
			fileUploadConfig.setViewScriptLibraryId("");
		}
		if (null != fileUploadConfig && fileUploadConfig.getUploadScriptLibraryId() != null && fileUploadConfig.getUploadScriptLibraryId().isEmpty() == false) {
			entityId = "upload_" + fileUploadConfig.getFileBinId();
			dynarestDAO.scriptLibDeleteById(entityId);
			entityIdList = new ArrayList<String>(Arrays.asList(entityId));
			scriptUploadId = fileUploadConfig.getUploadScriptLibraryId();
			scriptUploadIdList = objectMapper.readValue(scriptUploadId, List.class);
			fileUploadConfigDAO.scriptLibSave(entityIdList, scriptUploadIdList, scriptLibInsert, moduleId, null);
		}
		if (null != fileUploadConfig && fileUploadConfig.getViewScriptLibraryId() != null && fileUploadConfig.getViewScriptLibraryId().isEmpty() == false) {
			entityId = "view_" + fileUploadConfig.getFileBinId();
			dynarestDAO.scriptLibDeleteById(entityId);
			entityIdList = new ArrayList<String>(Arrays.asList(entityId));
			scriptViewId = fileUploadConfig.getViewScriptLibraryId();
			scriptViewIdList = objectMapper.readValue(scriptViewId, List.class);
			fileUploadConfigDAO.scriptLibSave(entityIdList, scriptViewIdList, scriptLibInsert, moduleId, null);
		}
		if (null != fileUploadConfig && fileUploadConfig.getDeleteScriptLibraryId() != null && fileUploadConfig.getDeleteScriptLibraryId().isEmpty() == false) {
			entityId = "delete_" + fileUploadConfig.getFileBinId();
			dynarestDAO.scriptLibDeleteById(entityId);
			entityIdList = new ArrayList<String>(Arrays.asList(entityId));
			scriptDeleteId = fileUploadConfig.getDeleteScriptLibraryId();
			scriptDeleteIdList = objectMapper.readValue(scriptDeleteId, List.class);
			fileUploadConfigDAO.scriptLibSave(entityIdList, scriptDeleteIdList, scriptLibInsert, moduleId, null);
		}
	}

}
