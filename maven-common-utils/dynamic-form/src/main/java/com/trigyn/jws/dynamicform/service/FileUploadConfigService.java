package com.trigyn.jws.dynamicform.service;

import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.trigyn.jws.dynamicform.dao.FileUploadConfigRepository;
import com.trigyn.jws.dynamicform.entities.FileUploadConfig;
import com.trigyn.jws.dynamicform.vo.FileUploadConfigVO;

@Service
@Transactional
public class FileUploadConfigService {

	@Autowired
	private FileUploadConfigRepository fileUploadConfigRepository = null;
	
	public FileUploadConfig getFileUploadConfigById(String fileConfigId) throws Exception {
		return fileUploadConfigRepository.findById(fileConfigId)
				.orElseThrow(() -> new Exception("data not found with id : " + fileConfigId));
	}

	public void saveFileUploadConfig(FileUploadConfig fileUploadConfig) throws Exception {
		fileUploadConfigRepository.save(fileUploadConfig);
	}
	
	public FileUploadConfigVO convertFileUploadEntityToVO(FileUploadConfig entity) {
		FileUploadConfigVO config = new FileUploadConfigVO(entity.getFileUploadConfigId(), 
				entity.getFileTypSupported(), entity.getMaxFileSize(), entity.getNoOfFiles(), 
				entity.getIsDeleted(), entity.getUpdatedBy());
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

		FileUploadConfig fileUploadConfig = getFileUploadConfigById(entityId);
		fileUploadConfig = fileUploadConfig.getObject();
		FileUploadConfigVO fileUploadConfigVO		= convertFileUploadEntityToVO(fileUploadConfig);
		Gson gson = new Gson();

		TreeMap<String, String> treeMap = gson.fromJson(gson.toJson(fileUploadConfigVO), TreeMap.class);
	    String jsonString = gson.toJson(treeMap);
		return jsonString;
	}
}
