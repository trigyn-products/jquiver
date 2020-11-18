package com.trigyn.jws.dbutils.service;

import java.util.Date;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.repository.PropertyMasterRepository;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;

@Service
@Transactional
public class PropertyMasterService {

	@Autowired
	private PropertyMasterDAO propertyMasterDAO = null;
	
	@Autowired
	private IUserDetailsService usersDetailsService = null;
	
	@Autowired
	private PropertyMasterRepository propertyMasterRepository = null;
	
	@Autowired
    private PropertyMasterDetails propertyMasterDetails = null;
	
	public String findPropertyMasterValue(String propertyName) throws Exception {
		return propertyMasterDetails.getSystemPropertyValue(propertyName);
	}
	
	public String findPropertyMasterValue(String ownerType, String ownerId, String propertyName) throws Exception {
		return propertyMasterDetails.getPropertyValueFromPropertyMaster(ownerId, ownerType, propertyName);
	}
	
	private void savePropertyMasterDetails(Map<String, Object> parameterMap) {
		String ownerType = parameterMap.get("ownerType").toString();
		String ownerId = parameterMap.get("ownerId").toString();
		String propertyName = parameterMap.get("propertyName").toString();
		String propertyValue = parameterMap.get("propertyValue").toString();
		String comments = parameterMap.get("comments").toString();
		PropertyMaster propertyMaster = propertyMasterRepository.findByOwnerTypeAndOwnerIdAndPropertyName(ownerType, ownerId, propertyName);
		propertyMaster.setIsDeleted(0);
		propertyMaster.setPropertyValue(propertyValue);
		propertyMaster.setLastModifiedDate(new Date());
		UserDetailsVO userDetailsVO = usersDetailsService.getUserDetails();
		propertyMaster.setModifiedBy(userDetailsVO.getUserId());
		propertyMaster.setComments(comments);
		propertyMasterRepository.save(propertyMaster);
	}

	@Transactional(readOnly = false)
	public void savePropertyDetails(String propertyName, String propertyValue) {
		PropertyMaster propertyMaster = propertyMasterRepository.findByOwnerTypeAndOwnerIdAndPropertyName("system", "system", propertyName);
		propertyMaster.setIsDeleted(0);
		propertyMaster.setPropertyValue(propertyValue);
		propertyMaster.setLastModifiedDate(new Date());
		propertyMaster.setModifiedBy("admin");
		propertyMaster.setAppVersion(1.0);
		propertyMaster.setComments("Dynarest property details");
		propertyMasterDAO.save(propertyMaster);
	}
	
	
	public String getDateFormatByName(String ownerType, String ownerId, String propertyName, String formatName) throws Exception {
		Gson gson = new Gson();
		String jwsDateFormat = propertyMasterDAO.findPropertyMasterValue(ownerType, ownerId, propertyName);
		Map<Object,Object> dateFormatMap = gson.fromJson(jwsDateFormat,Map.class);
		String dbDateFormat = (String) dateFormatMap.get(formatName);
		return dbDateFormat;
	}
	
}
