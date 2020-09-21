package app.trigyn.common.dbutils.service;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.trigyn.common.dbutils.configurations.JwsAuthenticationFilter;
import app.trigyn.common.dbutils.entities.PropertyMaster;
import app.trigyn.common.dbutils.entities.PropertyMasterPK;
import app.trigyn.common.dbutils.repository.PropertyMasterDAO;
import app.trigyn.common.dbutils.repository.PropertyMasterRepository;
import app.trigyn.common.dbutils.spi.IUserDetailsService;
import app.trigyn.common.dbutils.vo.UserDetailsVO;

@Service
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
	
	public void savePropertyMasterDetails(Map<String, Object> parameterMap) {
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
}
