package com.trigyn.jws.dynarest.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dynarest.dao.FileUploadConfigRepository;
import com.trigyn.jws.dynarest.entities.FileUploadConfig;
import com.trigyn.jws.dynarest.vo.FileUploadConfigVO;

@Service
@Transactional
public class FileUploadConfigService {

	private static final Logger			logger						= LogManager.getLogger(FileUploadConfigService.class);

	@Autowired
	private FileUploadConfigRepository	fileUploadConfigRepository	= null;

	@Autowired
	private PropertyMasterService		propertyMasterService		= null;

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
		
		FileUploadConfigVO config = new FileUploadConfigVO(entity.getFileBinId(), entity.getFileTypSupported(), entity.getMaxFileSize(),
				entity.getNoOfFiles(), entity.getUploadQueryContent(), entity.getViewQueryContent(),
				entity.getDeleteQueryContent(), entity.getIsDeleted(), entity.getLastUpdatedBy(), entity.getLastUpdatedTs(), entity.getCreatedBy(),
				entity.getCreatedDate(), entity.getUploadQueryType(), entity.getViewQueryType(), entity.getDeleteQueryType(), entity.getDatasourceViewValidator(),
				entity.getDatasourceUploadValidator(), entity.getDatasourceDeleteValidator(), entity.getIsCustomUpdated());
		return config;

	}

	public FileUploadConfig convertFileUploadVOToEntity(FileUploadConfigVO vo) {
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

		return config;
	}

	public String getFileUploadJson(String entityId) throws Exception {
		logger.debug("Inside FileUploadConfigService.getFileUploadJson(entityId: {})", entityId);

		FileUploadConfig	fileUploadConfig	= getFileUploadConfigByBinId(entityId);
		String				jsonString			= "";

		if (fileUploadConfig != null) {
			fileUploadConfig = fileUploadConfig.getObject();
			FileUploadConfigVO	fileUploadConfigVO	= convertFileUploadEntityToVO(fileUploadConfig);
			Gson				gson				= new Gson();

			ObjectMapper		objectMapper		= new ObjectMapper();
			String				dbDateFormat		= propertyMasterService.getDateFormatByName(Constant.PROPERTY_MASTER_OWNER_TYPE,
					Constant.PROPERTY_MASTER_OWNER_ID, Constant.JWS_DATE_FORMAT_PROPERTY_NAME,
					com.trigyn.jws.dbutils.utils.Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);
			DateFormat			dateFormat			= new SimpleDateFormat(dbDateFormat);
			objectMapper.setDateFormat(dateFormat);
			Map<String, Object> objectMap = objectMapper.convertValue(fileUploadConfigVO, TreeMap.class);
			jsonString = gson.toJson(objectMap);
		}

		return jsonString;
	}
}
