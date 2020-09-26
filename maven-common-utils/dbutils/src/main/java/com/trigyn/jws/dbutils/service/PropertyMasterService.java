package com.trigyn.jws.dbutils.service;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.entities.PropertyMasterPK;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.repository.PropertyMasterRepository;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
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
	
	public String findPropertyMasterValue(String propertyName) throws Exception {
		return propertyMasterDAO.findPropertyMasterValue("system", "system", propertyName);
	}
	
	public String findPropertyMasterValue(String ownerType, String ownerId, String propertyName) throws Exception {
		return propertyMasterDAO.findPropertyMasterValue(ownerType, ownerId, propertyName);
	}
	
	private void savePropertyMasterDetails(Map<String, Object> parameterMap) {
		String ownerType = parameterMap.get("ownerType").toString();
		String ownerId = parameterMap.get("ownerId").toString();
		String propertyName = parameterMap.get("propertyName").toString();
		String propertyValue = parameterMap.get("propertyValue").toString();
		String comments = parameterMap.get("comments").toString();
		PropertyMasterPK propertyMasterPK = new PropertyMasterPK(ownerType, ownerId, propertyName);
		PropertyMaster propertyMaster = propertyMasterRepository.findById(propertyMasterPK).orElse(new PropertyMaster());
		propertyMaster.setId(propertyMasterPK);
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
		PropertyMasterPK propertyMasterPK = new PropertyMasterPK("system", "system", propertyName);
		PropertyMaster propertyMaster = propertyMasterRepository.findById(propertyMasterPK).orElse(new PropertyMaster());
		propertyMaster.setId(propertyMasterPK);
		propertyMaster.setIsDeleted(0);
		propertyMaster.setPropertyValue(propertyValue);
		propertyMaster.setLastModifiedDate(new Date());
		propertyMaster.setModifiedBy("admin");
		propertyMaster.setAppVersion(1.0);
		propertyMaster.setComments("Dynarest property details");
		propertyMasterDAO.save(propertyMaster);
	}
}
