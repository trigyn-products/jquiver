package com.trigyn.jws.dbutils.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dbutils.entities.JwsTemplateVersion;
import com.trigyn.jws.dbutils.repository.TemplateVersionDAO;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;

@Service
@Transactional
public class TemplateVersionService {

	@Autowired
	private TemplateVersionDAO templateVersionDAO = null;

	@Autowired
	private IUserDetailsService userDetailsService = null;

	@Transactional(readOnly = false)
	public void saveTemplateVersion(Object entityData, Object parentEntityIdObj, Object entityTypeIdObj, String entityTypeName) throws Exception{
		ObjectMapper objectMapper 				= new ObjectMapper();
		String parentEntityId 					= parentEntityIdObj == null ? null : parentEntityIdObj.toString();
		String entityTypeId 					= entityTypeIdObj.toString();
		JwsTemplateVersion templateVersion 		= new JwsTemplateVersion();
		
		String templateJson 					= objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(entityData);
		UserDetailsVO userDetailsVO 			= userDetailsService.getUserDetails();
		
		Double versionId = getVersionNumber(entityTypeId);
		templateVersion.setParentEntityId(parentEntityId);
        templateVersion.setEntityId(entityTypeId);
        templateVersion.setEntityName(entityTypeName);
        templateVersion.setTemplateJson(templateJson);
	    templateVersion.setVersionId(versionId);
        templateVersion.setUpdatedDate(new Date());
        templateVersion.setUpdatedBy(userDetailsVO.getUserId());
		templateVersionDAO.save(templateVersion);
	}


	private Double getVersionNumber(String entityTypeId) throws Exception {
		Double versionId = templateVersionDAO.getVersionIdByEntityId(entityTypeId);
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
	
	
	public Map<Double, String> getVersionDetails(String entityId) throws Exception{
	   Map<Double, String> versionDetailsMap = new LinkedHashMap<>();
	   List<JwsTemplateVersion> jwsTemplateVersions = templateVersionDAO.getTemplateVersionByEntityId(entityId);
	   String dateFormatPattern = "dd/MM/yyyy HH:mm:ss";
	   DateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);
	   if(!CollectionUtils.isEmpty(jwsTemplateVersions)) {
		   for (JwsTemplateVersion jwsTemplateVersion : jwsTemplateVersions) {
			   String updatedDateString = dateFormat.format(jwsTemplateVersion.getUpdatedDate());
			   versionDetailsMap.put(jwsTemplateVersion.getVersionId(), updatedDateString);
		   }
	   }
	   return versionDetailsMap;
	}
	   
	public String getTemplateData(String entityId, Double versionId) throws Exception{
	   String templateJson = templateVersionDAO.getTemplateData(entityId, versionId);
	   ObjectMapper objectMapper = new ObjectMapper();
	   String templateData = objectMapper.readValue(templateJson, String.class);
	   return templateData;
	}
	   
}
