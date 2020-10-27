package com.trigyn.jws.dbutils.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dbutils.entities.JwsModuleVersion;
import com.trigyn.jws.dbutils.repository.ModuleVersionDAO;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;

@Service
@Transactional
public class ModuleVersionService {

	@Autowired
	private ModuleVersionDAO moduleVersionDAO 		= null;

	@Autowired
	private IUserDetailsService userDetailsService 	= null;
	
	@Autowired
	private FileUtilities fileUtilities  			= null;
	
	@Autowired
	private PropertyMasterDAO propertyMasterDAO		= null;

	@Transactional(readOnly = false)
	public void saveModuleVersion(Object entityData, Object parentEntityIdObj, Object entityTypeIdObj, String entityTypeName) throws Exception{
		ObjectMapper objectMapper 				= new ObjectMapper();
		String parentEntityId 					= parentEntityIdObj == null ? null : parentEntityIdObj.toString();
		String entityTypeId 					= entityTypeIdObj.toString();
		JwsModuleVersion moduleVersion 			= new JwsModuleVersion();
		
		String moduleJson 						= objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(entityData);
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
	
	public Map<Double, String> getVersionDetails(String entityId) throws Exception{
	   Map<Double, String> versionDetailsMap = new LinkedHashMap<>();
	   List<JwsModuleVersion> jwsModuleVersions = moduleVersionDAO.getModuleVersionByEntityId(entityId);
	   String dateFormatPattern = "dd/MM/yyyy HH:mm:ss";
	   DateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
	   if(!CollectionUtils.isEmpty(jwsModuleVersions)) {
		   for (JwsModuleVersion jwsModuleVersion : jwsModuleVersions) {
			   String updatedDateString = dateFormat.format(jwsModuleVersion.getUpdatedDate());
			   versionDetailsMap.put(jwsModuleVersion.getVersionId(), updatedDateString);
		   }
	   }
	   return versionDetailsMap;
	}
	
	public String getModuleData(String entityId, Double versionId) throws Exception{
	   String moduleJson = moduleVersionDAO.getModuleData(entityId, versionId);
	   ObjectMapper objectMapper = new ObjectMapper();
	   String templateData = objectMapper.readValue(moduleJson, String.class);
	   return templateData;
	}

	
}
