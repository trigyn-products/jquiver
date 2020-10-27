package com.trigyn.jws.webstarter.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.entities.PropertyMaster;
import com.trigyn.jws.dbutils.entities.PropertyMasterPK;
import com.trigyn.jws.dbutils.repository.PropertyMasterRepository;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.entities.JwsAuthenticationType;
import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;
import com.trigyn.jws.usermanagement.entities.JwsMasterModules;
import com.trigyn.jws.usermanagement.entities.JwsRole;
import com.trigyn.jws.usermanagement.entities.JwsRoleMasterModulesAssociation;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.entities.JwsUserRoleAssociation;
import com.trigyn.jws.usermanagement.repository.JwsAuthenticationTypeRepository;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationRepository;
import com.trigyn.jws.usermanagement.repository.JwsMasterModulesRepository;
import com.trigyn.jws.usermanagement.repository.JwsRoleMasterModulesAssociationRepository;
import com.trigyn.jws.usermanagement.repository.JwsRoleRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.repository.UserManagementDAO;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.JwsAuthenticationTypeVO;
import com.trigyn.jws.usermanagement.vo.JwsEntityRoleAssociationVO;
import com.trigyn.jws.usermanagement.vo.JwsEntityRoleVO;
import com.trigyn.jws.usermanagement.vo.JwsMasterModulesVO;
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
	
	@Autowired
	private JwsAuthenticationTypeRepository authenticationTypeRepository = null;
	
	@Autowired
	private JwsEntityRoleAssociationRepository entityRoleAssociationRepository = null;
	
    @Autowired
    private DBTemplatingService templatingService     = null;

	@Autowired
	private TemplatingUtils templatingUtils = null; 
	
	@Autowired
	private IUserDetailsService userDetailsService = null;

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
		roles = jwsRoleRepository.findAllRoles();

		List<JwsMasterModulesVO> masterModulesVO = new ArrayList<>();
		List<JwsMasterModules> masterModules = new ArrayList<>();
		masterModules = jwsmasterModuleRepository.findAll();
		
		for (JwsMasterModules jwsMasterModule : masterModules) {
			masterModulesVO.add(new JwsMasterModulesVO().convertEntityToVO(jwsMasterModule));
		}

		List<JwsRoleMasterModulesAssociation> roleModulesAssociations = new ArrayList<>();
		roleModulesAssociations = roleModuleRepository.findAll();

		mapDetails.put("roles", roles);
		mapDetails.put("masterModules", masterModulesVO);
		mapDetails.put("roleModulesAssociations", roleModulesAssociations);
		
		TemplateVO templateVO =  templatingService.getTemplateByName("manageRoleModule");
		return	templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), mapDetails);
	}

	public Boolean saveRoleModules(JwsRoleMasterModulesAssociationVO roleModule) {

			JwsRoleMasterModulesAssociation masterModuleAssociation = roleModule
					.convertVOToEntity(roleModule);

			roleModuleRepository.save(masterModuleAssociation);

		return true;
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
		roles = jwsRoleRepository.findAllRoles();
		templateMap.put("roles", roles);
		templateMap.put("jwsUser", jwsUser);
		return menuService.getTemplateWithSiteLayout("addEditJwsUser", templateMap);
	}


	public String loadUserManagement() throws Exception {
		Map<String, Object> mapDetails = new HashMap<>();
		PropertyMasterPK id = new PropertyMasterPK("system", "system", "enable-user-management");
		PropertyMaster propertyMaster = propertyMasterRepository.findById(id).orElse(new PropertyMaster());
		mapDetails.put("authEnabled", Boolean.parseBoolean(propertyMaster.getPropertyValue()));
		PropertyMasterPK idAuthType = new PropertyMasterPK("system", "system", "authentication-type");
		PropertyMaster propertyMasterAuthType = propertyMasterRepository.findById(idAuthType).orElse(new PropertyMaster());
		mapDetails.put("authTypeId", propertyMasterAuthType.getPropertyValue());
		
		List<JwsAuthenticationType> authenticationTypes = authenticationTypeRepository.findAll();
		List<JwsAuthenticationTypeVO> authenticationTypesVO = new ArrayList<>();
		
		for (JwsAuthenticationType authenticationType : authenticationTypes) {
			authenticationTypesVO.add(new JwsAuthenticationTypeVO().convertEntityToVO(authenticationType));
		}
		mapDetails.put("authenticationTypesVO", authenticationTypesVO);
		
		return menuService.getTemplateWithSiteLayout("user-management", mapDetails);
		
	}

	public void updatePropertyMasterValuesAndAuthProperties(String authenticationEnabled, String authenticationTypeId,
			String propertyJson) {
		
		propertyMasterRepository.updatePropertyValueByName(authenticationEnabled, "enable-user-management");
		propertyMasterRepository.updatePropertyValueByName(authenticationTypeId, "authentication-type");
		
		authenticationTypeRepository.updatePropertyById(Integer.parseInt(authenticationTypeId),propertyJson);
	}

	public String manageEntityRoles() throws Exception {
		Map<String, Object> mapDetails = new HashMap<>();
		List<JwsRole> roles = new ArrayList<>();
		roles = jwsRoleRepository.findAllRoles();
		mapDetails.put("roles", roles);
		
		TemplateVO templateVO =  templatingService.getTemplateByName("manageEntityRoles");
		return	templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), mapDetails);
	}

	public void saveUpdateEntityRole(List<JwsEntityRoleAssociationVO> entityRoleAssociations) {
		
		for (JwsEntityRoleAssociationVO jwsEntityRoleAssociationVO : entityRoleAssociations) {
			
			JwsEntityRoleAssociation jwsEntityRoleAssociation = jwsEntityRoleAssociationVO.convertVOtoEntity(jwsEntityRoleAssociationVO);
			jwsEntityRoleAssociation.setLastUpdatedBy("admin");
			String entityRoleId = entityRoleAssociationRepository.getEntityRoleIdByEntityAndRoleId(jwsEntityRoleAssociation.getEntityId(),jwsEntityRoleAssociation.getRoleId());
			jwsEntityRoleAssociation.setEntityRoleId(entityRoleId);
			entityRoleAssociationRepository.save(jwsEntityRoleAssociation);
		}
	}

	public List<JwsMasterModulesVO> getModules() {
		
		List<JwsMasterModulesVO> masterModulesVO = new ArrayList<>();
		List<JwsMasterModules> masterModules = new ArrayList<>();
		masterModules = jwsmasterModuleRepository.findAll();
		
		for (JwsMasterModules jwsMasterModule : masterModules) {
			masterModulesVO.add(new JwsMasterModulesVO().convertEntityToVO(jwsMasterModule));
		}
		
		return masterModulesVO;
	}

	public void deleteAndSaveEntityRole(JwsEntityRoleVO entityRoles) {
		
		
		if(!entityRoles.getRoleIds().contains(Constants.ADMIN_ROLE_ID)) {
			entityRoles.getRoleIds().add(Constants.ADMIN_ROLE_ID);
			entityRoles.setRoleIds(entityRoles.getRoleIds());
		}
		
		List<String> newRoleIds = new ArrayList<>(entityRoles.getRoleIds());
		
		List<JwsEntityRoleAssociation> entityRoleAssociations =  entityRoleAssociationRepository.getEntityRoles(entityRoles.getEntityId(),entityRoles.getModuleId());
		for (JwsEntityRoleAssociation jwsEntityRoleAssociation : entityRoleAssociations) {
			//inactive
			if(!entityRoles.getRoleIds().contains(jwsEntityRoleAssociation.getRoleId()))
			{
				newRoleIds.remove(jwsEntityRoleAssociation.getRoleId());
				jwsEntityRoleAssociation.setIsActive(Constants.INACTIVE);
				jwsEntityRoleAssociation.setLastUpdatedDate(new Date());
				jwsEntityRoleAssociation.setLastUpdatedBy(userDetailsService.getUserDetails().getUserId());
				entityRoleAssociationRepository.save(jwsEntityRoleAssociation);
			}else {
				newRoleIds.remove(jwsEntityRoleAssociation.getRoleId());
				jwsEntityRoleAssociation.setIsActive(Constants.ISACTIVE);
				jwsEntityRoleAssociation.setLastUpdatedDate(new Date());
				jwsEntityRoleAssociation.setLastUpdatedBy(userDetailsService.getUserDetails().getUserId());
				entityRoleAssociationRepository.save(jwsEntityRoleAssociation);
			}
		}
		
		for (String roleId : newRoleIds) {
			JwsEntityRoleAssociation entityRoleAssociation = new JwsEntityRoleAssociation();
			entityRoleAssociation.setRoleId(roleId);
			entityRoleAssociation.setEntityId(entityRoles.getEntityId());
			entityRoleAssociation.setEntityName(entityRoles.getEntityName());
			entityRoleAssociation.setModuleId(entityRoles.getModuleId());
			entityRoleAssociation.setIsActive(Constants.ISACTIVE);
			entityRoleAssociation.setLastUpdatedDate(new Date());
			entityRoleAssociation.setLastUpdatedBy(userDetailsService.getUserDetails().getUserId());
			entityRoleAssociationRepository.save(entityRoleAssociation);
			
		}
		
	}

	public List<JwsRoleVO> getEntityRoles(String entityId, String moduleId) {
		List<JwsRoleVO> roles =  entityRoleAssociationRepository.getRoles(entityId,moduleId,Constants.ISACTIVE);
		return roles;
	}

}
