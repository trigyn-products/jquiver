package com.trigyn.jws.dbutils.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.trigyn.jws.dbutils.entities.JwsModuleVersion;
import com.trigyn.jws.dbutils.repository.JwsTemplateVersionRepository;
import com.trigyn.jws.dbutils.repository.ModuleVersionDAO;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dbutils.vo.ModuleVersionVO;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;

@Service
@Transactional
public class ModuleVersionService {

	@Autowired
	private ModuleVersionDAO moduleVersionDAO 				= null;

	@Autowired
	private IUserDetailsService userDetailsService 			= null;
	
	@Autowired
	private FileUtilities fileUtilities  					= null;
	
	@Autowired
	private PropertyMasterDAO propertyMasterDAO				= null;
	
	@Autowired
	private JwsTemplateVersionRepository versionRepository 	= null;
	
	@Transactional(readOnly = false)
	public void saveModuleVersion(Object entityData, Object parentEntityIdObj
			, Object entityTypeIdObj, String entityTypeName, Integer sourceTypeId) throws Exception{
		Gson gson 								= new Gson();
		ObjectMapper objectMapper				= new ObjectMapper();
		String moduleJson 						= null;
		try {
			Map<String, Object> objectMap 			= objectMapper.convertValue(entityData, TreeMap.class);
			moduleJson = gson.toJson(objectMap);
		} catch (IllegalArgumentException e) {
			moduleJson = entityData.toString();
		}
		String parentEntityId 					= parentEntityIdObj == null ? null : parentEntityIdObj.toString();
		String entityTypeId 					= entityTypeIdObj.toString();
		JwsModuleVersion moduleVersion 			= new JwsModuleVersion();

		UserDetailsVO userDetailsVO 			= userDetailsService.getUserDetails();
		String moduleJsonChecksum				= generateJsonChecksum(moduleJson);
		Boolean isDataUpdated					= compareChecksum(entityTypeId, moduleJsonChecksum);
		
		if(isDataUpdated != null && isDataUpdated == true) {
			Double versionId = getVersionNumber(entityTypeId);
			moduleVersion.setParentEntityId(parentEntityId);
	        moduleVersion.setEntityId(entityTypeId);
	        moduleVersion.setEntityName(entityTypeName);
	        moduleVersion.setModuleJson(moduleJson);
		    moduleVersion.setVersionId(versionId);
		    moduleVersion.setModuleJsonChecksum(moduleJsonChecksum);
		    moduleVersion.setSourceTypeId(sourceTypeId);
	        moduleVersion.setUpdatedDate(new Date());
	        moduleVersion.setUpdatedBy(userDetailsVO.getUserId());
			moduleVersionDAO.save(moduleVersion);
			deleteOldRecords(entityTypeId);
		}
	}


	private Boolean compareChecksum(String entityTypeId, String generatedChecksum) throws Exception {
		Boolean isChecksumChanged = true;
		String previousChecksum = getJsonChecksum(entityTypeId);
		if(previousChecksum != null && previousChecksum.equals(generatedChecksum)) {
			isChecksumChanged = false;
		}
		return isChecksumChanged;
	}


	private Double getVersionNumber(String entityTypeId) throws Exception {
		Double versionId = moduleVersionDAO.getVersionIdByEntityId(entityTypeId);
        if(versionId != null) {
			String versionIdStr = versionId.toString();
			Integer versionIdInt = Integer.parseInt(versionIdStr.split("\\.")[0]);
			Integer versionIdFractional = Integer.parseInt(versionIdStr.split("\\.")[1]);
	        if(versionIdFractional.equals(Constant.MAX_DECIMAL_VERSION_NUMBER)) {
	        	versionIdInt = versionIdInt + 1;
	        	versionId = versionIdInt.doubleValue();
	        }else{
	        	versionId = versionId + 0.01;
	        }
        }else {
        	versionId = Constant.INITIAL_VERSION_NUMBER;
        }
		return versionId;
	}
	
	
	public String generateJsonChecksum(String jsonContent) throws Exception {
		return fileUtilities.generateChecksum(jsonContent);
	}
	
	public String getJsonChecksum(String entityId) throws Exception{
		return moduleVersionDAO.getModuleJsonChecksum(entityId);
	}
	
	
	public void deleteOldRecords(String entityId) throws Exception{
		String maxVersionId = propertyMasterDAO.findPropertyMasterValue(Constant.MODULE_VERSION_OWNER_TYPE,
				Constant.MODULE_VERSION_OWNER_ID, Constant.MODULE_VERSION_PROERTY_NAME);
		if(!StringUtils.isBlank(maxVersionId)) {
			Integer maxVersionIdInt = Integer.parseInt(maxVersionId);
			Integer versionIdCount = moduleVersionDAO.getVersionIdCount(entityId);
			if(versionIdCount != null && versionIdCount > maxVersionIdInt) {
				moduleVersionDAO.deleteOldRecords(entityId);
			}
		}
		
	}
	
	public String getModuleJsonById(String moduleVersionId) throws Exception{
		return moduleVersionDAO.getModuleJsonById(moduleVersionId);
	}


	public List<ModuleVersionVO> fetchModuleVersionDetails(String entityId) {
		return versionRepository.getModuleVersionById(entityId);
	}
	
	public String getLastUpdatedJsonData(String entityId) throws Exception {
		return moduleVersionDAO.getLastUpdatedJsonData(entityId);
	}

	
}
