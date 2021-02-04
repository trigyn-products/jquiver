package com.trigyn.jws.dynamicform.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dynamicform.dao.FileUploadConfigRepository;
import com.trigyn.jws.dynamicform.entities.FileUploadConfig;
import com.trigyn.jws.dynamicform.vo.FileUploadConfigVO;

@Service
@Transactional
public class FileUploadConfigService {

	@Autowired
	private FileUploadConfigRepository	fileUploadConfigRepository	= null;

	@Autowired
	private PropertyMasterService		propertyMasterService		= null;

	public FileUploadConfig getFileUploadConfigById(String fileConfigId) throws Exception {
		return fileUploadConfigRepository.getFileUploadConfig(fileConfigId);
	}

	public void saveFileUploadConfig(FileUploadConfig fileUploadConfig) throws Exception {
		fileUploadConfigRepository.save(fileUploadConfig);
	}

	public FileUploadConfigVO convertFileUploadEntityToVO(FileUploadConfig entity) {
		FileUploadConfigVO config = new FileUploadConfigVO(entity.getFileUploadConfigId(), entity.getFileTypSupported(),
				entity.getMaxFileSize(), entity.getNoOfFiles(), entity.getIsDeleted());
		return config;

	}

	public FileUploadConfig convertFileUploadVOToEntity(FileUploadConfigVO vo) {
		FileUploadConfig config = new FileUploadConfig();
		config.setFileTypSupported(vo.getFileTypSupported());
		config.setFileUploadConfigId(vo.getFileUploadConfigId());
		config.setIsDeleted(vo.getIsDeleted());
		config.setMaxFileSize(vo.getMaxFileSize());
		config.setNoOfFiles(vo.getNoOfFiles());
		config.setUpdatedBy(vo.getUpdatedBy());

		return config;
	}

	public String getFileUploadJson(String entityId) throws Exception {

		FileUploadConfig	fileUploadConfig	= getFileUploadConfigById(entityId);
		String				jsonString			= "";

		if (fileUploadConfig != null) {
			fileUploadConfig = fileUploadConfig.getObject();
			FileUploadConfigVO	fileUploadConfigVO	= convertFileUploadEntityToVO(fileUploadConfig);
			Gson				gson				= new Gson();

			ObjectMapper		objectMapper		= new ObjectMapper();
			String				dbDateFormat		= propertyMasterService.getDateFormatByName(
					Constant.PROPERTY_MASTER_OWNER_TYPE, Constant.PROPERTY_MASTER_OWNER_ID,
					Constant.JWS_DATE_FORMAT_PROPERTY_NAME,
					com.trigyn.jws.dbutils.utils.Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);
			DateFormat			dateFormat			= new SimpleDateFormat(dbDateFormat);
			objectMapper.setDateFormat(dateFormat);
			Map<String, Object> objectMap = objectMapper.convertValue(fileUploadConfigVO, TreeMap.class);
			jsonString = gson.toJson(objectMap);
		}

		return jsonString;
	}
}
