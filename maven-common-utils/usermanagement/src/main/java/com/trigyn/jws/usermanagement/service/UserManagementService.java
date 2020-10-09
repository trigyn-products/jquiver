package com.trigyn.jws.usermanagement.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.entities.PropertyMasterPK;
import com.trigyn.jws.dbutils.repository.PropertyMasterRepository;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.entities.JwsMasterModules;
import com.trigyn.jws.usermanagement.entities.JwsRole;
import com.trigyn.jws.usermanagement.entities.JwsRoleMasterModulesAssociation;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.entities.JwsUserRoleAssociation;
import com.trigyn.jws.usermanagement.repository.JwsMasterModulesRepository;
import com.trigyn.jws.usermanagement.repository.JwsRoleMasterModulesAssociationRepository;
import com.trigyn.jws.usermanagement.repository.JwsRoleRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.repository.UserManagementDAO;
import com.trigyn.jws.usermanagement.vo.JwsRoleMasterModulesAssociationVO;
import com.trigyn.jws.usermanagement.vo.JwsRoleVO;
import com.trigyn.jws.usermanagement.vo.JwsUserVO;

@Service
@Transactional
public class UserManagementService {

	@Autowired
	private JwsRoleRepository jwsRoleRepository = null;

	@Autowired
	private JwsMasterModulesRepository jwsmasterModuleRepository = null;

	@Autowired
	private JwsRoleMasterModulesAssociationRepository roleModuleRepository = null;

	@Autowired
	private MenuService menuService = null;

	@Autowired
	private JwsUserRepository jwsUserRepository = null;

	@Autowired
	private UserManagementDAO userManagementDAO = null;

	@Autowired
	private JwsUserRoleAssociationRepository userRoleRepository = null;
	
	@Autowired
	private PropertyMasterRepository propertyMasterRepository = null;

	public String addEditRole(String roleId) throws Exception {

		Map<String, Object> templateMap = new HashMap<>();
		JwsRole jwsRole = new JwsRole();
		if (StringUtils.isNotEmpty(roleId)) {

			jwsRole = jwsRoleRepository.findById(roleId).get();
		}
		templateMap.put("jwsRole", jwsRole);
		return menuService.getTemplateWithSiteLayout("addEditRole", templateMap);
	}

	public void saveRoleData(JwsRoleVO roleData) {
		JwsRole jwsRole = roleData.convertVOToEntity(roleData);
		jwsRoleRepository.save(jwsRole);

	}

	public String manageRoleModules() throws Exception {

		Map<String, Object> mapDetails = new HashMap<>();

		List<JwsRole> roles = new ArrayList<>();
		roles = jwsRoleRepository.findAll();

		List<JwsMasterModules> masterModules = new ArrayList<>();
		masterModules = jwsmasterModuleRepository.findAll();

		List<JwsRoleMasterModulesAssociation> roleModulesAssociations = new ArrayList<>();
		roleModulesAssociations = roleModuleRepository.findAll();

		mapDetails.put("roles", roles);
		mapDetails.put("masterModules", masterModules);
		mapDetails.put("roleModulesAssociations", roleModulesAssociations);

		return menuService.getTemplateWithSiteLayout("manageRoleModule", mapDetails);
	}

	public String saveRoleModules(List<JwsRoleMasterModulesAssociationVO> roleModulesList) {

		for (JwsRoleMasterModulesAssociationVO jwsRoleMasterModulesAssociationVO : roleModulesList) {
			JwsRoleMasterModulesAssociation masterModuleAssociation = jwsRoleMasterModulesAssociationVO
					.convertVOToEntity(jwsRoleMasterModulesAssociationVO);

			roleModuleRepository.save(masterModuleAssociation);
		}

		return null;
	}

	public void saveUserData(JwsUserVO userData) {
		JwsUser jwsUser = userData.convertVOToEntity(userData);
		jwsUserRepository.save(jwsUser);
		if (jwsUser.getUserId() != null) {
			userManagementDAO.deleteUserRoleAssociation(jwsUser.getUserId());
		}
		for (String roleId : userData.getRoleIds()) {
			Date currentDate = new Date();
			JwsUserRoleAssociation userRoleAssociation = new JwsUserRoleAssociation();
			userRoleAssociation.setRoleId(roleId);
			userRoleAssociation.setUserId(jwsUser.getUserId());
			userRoleAssociation.setUpdatedDate(currentDate);
			userRoleRepository.save(userRoleAssociation);
		}
	}

	public String addEditUser(String userId) throws Exception {

		Map<String, Object> templateMap = new HashMap<>();
		JwsUser jwsUser = new JwsUser();
		if (StringUtils.isNotEmpty(userId)) {

			jwsUser = jwsUserRepository.findById(userId).get();
			List<String> userRoleIds = userManagementDAO.getRoleIdsByUserId(userId);
			templateMap.put("userRoleIds", userRoleIds);
		}

		List<JwsRole> roles = new ArrayList<>();
		roles = jwsRoleRepository.findAll();
		templateMap.put("roles", roles);
		templateMap.put("jwsUser", jwsUser);
		return menuService.getTemplateWithSiteLayout("addEditJwsUser", templateMap);
	}

	public void updatePropertyMasterValues(String authenticationEnabled, String authenticationTypeId) {
		
		propertyMasterRepository.updatePropertyValueByName(authenticationEnabled, "enable-user-management");
		
		propertyMasterRepository.updatePropertyValueByName(authenticationTypeId, "authentication-type");
		
		
	}

	public String loadUserManagement() throws Exception {
		Map<String, Object> mapDetails = new HashMap<>();
		PropertyMasterPK id = new PropertyMasterPK("system", "system", "enable-user-management");
		PropertyMaster propertyMaster = propertyMasterRepository.findById(id).orElse(new PropertyMaster());
		mapDetails.put("authEnabled", Boolean.parseBoolean(propertyMaster.getPropertyValue()));
		PropertyMasterPK idAuthType = new PropertyMasterPK("system", "system", "authentication-type");
		PropertyMaster propertyMasterAuthType = propertyMasterRepository.findById(idAuthType).orElse(new PropertyMaster());
		mapDetails.put("authTypeId", propertyMasterAuthType.getPropertyValue());
		return menuService.getTemplateWithSiteLayout("user-management", mapDetails);
		
	}

}
