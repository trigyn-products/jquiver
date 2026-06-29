package com.trigyn.jws.webstarter.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.trigyn.jws.dashboard.entities.Dashboard;
import com.trigyn.jws.dashboard.entities.DashboardDashletAssociation;
import com.trigyn.jws.dashboard.repository.interfaces.IDashboardDashletAssociationRepository;
import com.trigyn.jws.dashboard.service.DashletModule;
import com.trigyn.jws.dbutils.entities.AdditionalDatasource;
import com.trigyn.jws.dbutils.entities.AdditionalDatasourceRepository;
import com.trigyn.jws.dbutils.entities.JwsBusinessModule;
import com.trigyn.jws.dbutils.entities.JwsBusinessModuleEntity;
import com.trigyn.jws.dbutils.repository.JwsBusinessModuleEntityRepository;
import com.trigyn.jws.dbutils.repository.JwsBusinessModuleRepository;
import com.trigyn.jws.dbutils.repository.ModuleVersionDAO;
import com.trigyn.jws.dbutils.repository.ScriptLibraryConnRepository;
import com.trigyn.jws.dbutils.repository.ScriptLibraryRepository;
import com.trigyn.jws.dbutils.vo.JwsBusinessModuleEntityVO;
import com.trigyn.jws.dbutils.vo.ScriptLibConnectVO;
import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.service.DynamicFormService;
import com.trigyn.jws.dynarest.dao.JwsDynamicRestDAORepository;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.formio.entities.FormIO;
import com.trigyn.jws.formio.service.FormIOService;
import com.trigyn.jws.formio.vo.FormIOVO;
import com.trigyn.jws.gridutils.entities.GridDetails;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryConnection;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryDetails;
import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;
import com.trigyn.jws.usermanagement.entities.JwsMasterModules;
import com.trigyn.jws.usermanagement.entities.JwsRole;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationRepository;
import com.trigyn.jws.usermanagement.repository.JwsMasterModulesRepository;
import com.trigyn.jws.usermanagement.repository.JwsRoleRepository;
import com.trigyn.jws.webstarter.utils.Constant;
import com.trigyn.jws.webstarter.utils.XMLUtil;
import com.trigyn.jws.webstarter.utils.Constant.EntityNameModuleTypeEnum;
import com.trigyn.jws.webstarter.utils.Constant.ModuleType;
import com.trigyn.jws.webstarter.xml.AdditionalDatasourceXMLVO;
import com.trigyn.jws.webstarter.xml.BusinessModuleXMLVO;
import com.trigyn.jws.webstarter.xml.DashboardXMLVO;
import com.trigyn.jws.webstarter.xml.FormIOXMLVO;
import com.trigyn.jws.webstarter.xml.GridXMLVO;
import com.trigyn.jws.webstarter.xml.PermissionXMLVO;
import com.trigyn.jws.webstarter.xml.RoleXMLVO;
import com.trigyn.jws.webstarter.xml.ScriptLibraryXMLVO;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.JwsEntityRoleAssociationVO;
import com.trigyn.jws.usermanagement.vo.JwsRoleVO;

@Service
public class ExportDependencyService {

	@Autowired
	private JwsMasterModulesRepository jwsmasterModuleRepository = null;

	@Autowired
	private JwsEntityRoleAssociationRepository entityRoleAssociationRepository = null;

	@Autowired
	@Qualifier("dashlet")
	private DashletModule dashletDownloadUploadModule = null;

	@Autowired
	private IDashboardDashletAssociationRepository dashboardDashletAssociationRepository = null;

	@Autowired
	private DynamicFormCrudService dynamicService = null;

	@Autowired
	private FormIOService formIOService;

	@Autowired
	private JwsRoleRepository jwsRoleRepository;

	@Autowired
	private JwsBusinessModuleEntityRepository businessModuleEntityRepository = null;

	@Autowired
	private JwsBusinessModuleRepository jwsBusinessModuleRepository = null;

	@Autowired
	private ScriptLibraryConnRepository scriptLibraryConnRepository;

	@Autowired
	private ScriptLibraryRepository scriptLibraryRepository;

	@Autowired
	private JwsDynarestDAO jwsDynarestDAO = null;

	@Autowired
	private AdditionalDatasourceRepository additionalDatasourceRepository;

	@Autowired
	private JwsDynamicRestDAORepository jwsDynamicRestDAORepository = null;

	@Autowired
	private ModuleVersionDAO moduleVersionDAO = null;

	public void exportPermissionDependencies(Map<String, List<String>> exportedEntityIds, String tempDownloadPath,
			Map<String, String> moduleListMap, Map<String, List<ExportInfo>> exportedEntityInfo) throws Exception {

		PermissionXMLVO permissionXMLVO = new PermissionXMLVO();

		List<String> gridIds = exportedEntityIds.get(Constant.MasterModuleType.GRID.getModuleType());
		List<String> exportedPermissionIds = new ArrayList<>();
		if (gridIds != null) {

			JwsMasterModules module = jwsmasterModuleRepository
					.findBymoduleName(com.trigyn.jws.usermanagement.utils.Constants.Modules.GRIDUTILS.getModuleName());

			collectPermissions(gridIds, module.getModuleId(), permissionXMLVO, exportedPermissionIds);
		}

		List<String> dashboardIds = exportedEntityIds.get(Constant.MasterModuleType.DASHBOARD.getModuleType());

		if (dashboardIds != null) {

			JwsMasterModules module = jwsmasterModuleRepository
					.findBymoduleName(com.trigyn.jws.usermanagement.utils.Constants.Modules.DASHBOARD.getModuleName());

			collectPermissions(dashboardIds, module.getModuleId(), permissionXMLVO, exportedPermissionIds);
		}

		List<String> dashletIds = exportedEntityIds.get(Constant.MasterModuleType.DASHLET.getModuleType());

		if (dashletIds != null) {

			JwsMasterModules module = jwsmasterModuleRepository.findBymoduleName("Dashlet");

			collectPermissions(dashletIds, module.getModuleId(), permissionXMLVO, exportedPermissionIds);
		}

		List<String> autocompleteIds = exportedEntityIds.get(Constant.MasterModuleType.AUTOCOMPLETE.getModuleType());

		if (autocompleteIds != null && !autocompleteIds.isEmpty()) {

			JwsMasterModules module = jwsmasterModuleRepository.findBymoduleName(
					com.trigyn.jws.usermanagement.utils.Constants.Modules.AUTOCOMPLETE.getModuleName());

			collectPermissions(autocompleteIds, module.getModuleId(), permissionXMLVO, exportedPermissionIds);
		}

		List<String> routerIds = exportedEntityIds.get(Constant.MasterModuleType.ROUTER.getModuleType());

		if (routerIds != null && !routerIds.isEmpty()) {

			JwsMasterModules module = jwsmasterModuleRepository
					.findBymoduleName(com.trigyn.jws.usermanagement.utils.Constants.Modules.ROUTER.getModuleName());

			collectPermissions(routerIds, module.getModuleId(), permissionXMLVO, exportedPermissionIds);
		}

		List<String> formIoIds = exportedEntityIds.get(Constant.MasterModuleType.FORMIO.getModuleType());

		if (formIoIds != null) {

			JwsMasterModules module = jwsmasterModuleRepository
					.findBymoduleName(Constants.Modules.FORMIO.getModuleName());

			collectPermissions(formIoIds, module.getModuleId(), permissionXMLVO, exportedPermissionIds);
		}

		List<String> templateIds = exportedEntityIds.get(Constant.MasterModuleType.TEMPLATES.getModuleType());

		if (templateIds != null && !templateIds.isEmpty()) {

			JwsMasterModules module = jwsmasterModuleRepository
					.findBymoduleName(Constants.Modules.TEMPLATING.getModuleName());

			collectPermissions(templateIds, module.getModuleId(), permissionXMLVO, exportedPermissionIds);
		}

		List<String> formIds = exportedEntityIds.get(Constant.MasterModuleType.DYNAMICFORM.getModuleType());

		if (formIds != null) {

			JwsMasterModules module = jwsmasterModuleRepository
					.findBymoduleName(Constants.Modules.DYNAMICFORM.getModuleName());

			collectPermissions(formIds, module.getModuleId(), permissionXMLVO, exportedPermissionIds);
		}

		List<String> restIds = exportedEntityIds.get(Constant.MasterModuleType.DYNAREST.getModuleType());

		if (restIds != null) {

			JwsMasterModules module = jwsmasterModuleRepository.findBymoduleName("REST API");

			collectPermissions(restIds, module.getModuleId(), permissionXMLVO, exportedPermissionIds);
		}

		List<String> helpmanualIds = exportedEntityIds.get(Constant.MasterModuleType.HELPMANUAL.getModuleType());

		if (helpmanualIds != null) {

			JwsMasterModules module = jwsmasterModuleRepository.findBymoduleName("Help Manual");

			collectPermissions(helpmanualIds, module.getModuleId(), permissionXMLVO, exportedPermissionIds);
		}

		List<String> fileBinIds = exportedEntityIds.get(Constant.MasterModuleType.FILEMANAGER.getModuleType());
		if (fileBinIds != null) {

			JwsMasterModules module = jwsmasterModuleRepository.findBymoduleName("File Bin");

			collectPermissions(fileBinIds, module.getModuleId(), permissionXMLVO, exportedPermissionIds);
		}
		if (!permissionXMLVO.getJwsRoleDetails().isEmpty()) {

			XMLUtil.marshaling(permissionXMLVO, Constant.MasterModuleType.PERMISSION.getModuleType(), tempDownloadPath);

			moduleListMap.put(Constant.MasterModuleType.PERMISSION.getModuleType(), Constant.XML_EXPORT_TYPE);
			exportedEntityIds.put(Constant.MasterModuleType.PERMISSION.getModuleType(), exportedPermissionIds);

			for (JwsEntityRoleAssociation permission : permissionXMLVO.getJwsRoleDetails()) {

				exportedEntityInfo
						.computeIfAbsent(Constant.MasterModuleType.PERMISSION.getModuleType(), k -> new ArrayList<>())
						.add(new ExportInfo(permission.getEntityRoleId(), permission.getEntityName(), "1.0")); // Permissions
																												// are
																												// XML
																												// modules
			}
		}
	}

	public List<JwsEntityRoleAssociation> getEntityPermissionEntities(String entityId, String moduleId) {
		return entityRoleAssociationRepository.getEntityRoles(entityId, moduleId);
	}

//	private void collectGridPermissions(GridXMLVO gridXMLVO, PermissionXMLVO permissionXMLVO) throws Exception {
//
//		JwsMasterModules	mastermodule	= jwsmasterModuleRepository
//				.findBymoduleName(com.trigyn.jws.usermanagement.utils.Constants.Modules.GRIDUTILS.getModuleName());
//
//		String				moduleId		= mastermodule.getModuleId();
//
//		for (GridDetails grid : gridXMLVO.getGridDetails()) {
//
//			List<JwsEntityRoleAssociation> permissions = getEntityPermissionEntities(grid.getGridId(), moduleId);
//
//			addPermissions(permissionXMLVO, permissions);
//		}
//	}

//	private void addPermissions(PermissionXMLVO permissionXMLVO, List<JwsEntityRoleAssociation> permissions) {
//
//		if (permissions == null) {
//			return;
//		}
//
//		for (JwsEntityRoleAssociation permission : permissions) {
//
//			JwsEntityRoleAssociation permissionObj = permission.getObject();
//
//			boolean exists = permissionXMLVO.getJwsRoleDetails().stream()
//					.anyMatch(existing -> existing.getEntityRoleId().equals(permissionObj.getEntityRoleId()));
//
//			if (!exists) {
//				permissionXMLVO.getJwsRoleDetails().add(permissionObj);
//			}
//		}
//	}

//	private void collectDashboardPermissions(DashboardXMLVO dashboardXMLVO, PermissionXMLVO permissionXMLVO)
//			throws Exception {
//
//		JwsMasterModules	mastermodule	= jwsmasterModuleRepository
//				.findBymoduleName(Constants.Modules.DASHBOARD.getModuleName());
//
//		String				moduleId		= mastermodule.getModuleId();
//
//		for (Dashboard dashboard : dashboardXMLVO.getDashboardDetails()) {
//
//			List<JwsEntityRoleAssociation> permissions = getEntityPermissionEntities(dashboard.getDashboardId(),
//					moduleId);
//
//			addPermissions(permissionXMLVO, permissions);
//		}
//	}

//	private void collectDashletPermissions(PermissionXMLVO permissionXMLVO) throws Exception {
//
//		Map<String, Map<String, Object>> moduleDetailsMap = dashletDownloadUploadModule.getModuleDetailsMap();
//
//		if (moduleDetailsMap == null || moduleDetailsMap.isEmpty()) {
//			return;
//		}
//
//		JwsMasterModules	masterModule	= jwsmasterModuleRepository.findBymoduleName("Dashlet");
//
//		String				moduleId		= masterModule.getModuleId();
//
//		for (String dashletId : moduleDetailsMap.keySet()) {
//
//			List<JwsEntityRoleAssociation> permissions = getEntityPermissionEntities(dashletId, moduleId);
//
//			addPermissions(permissionXMLVO, permissions);
//		}
//		System.out.println("Dashlets Exported : " + dashletDownloadUploadModule.getModuleDetailsMap().size());
//	}

	private void collectPermissions(List<String> entityIds, String moduleId, PermissionXMLVO permissionXMLVO,
			List<String> exportedPermissionIds) throws Exception {

		for (String entityId : entityIds) {

			List<JwsEntityRoleAssociation> permissions = getEntityPermissionEntities(entityId, moduleId);

			if (permissions == null) {
				continue;
			}

			for (JwsEntityRoleAssociation permission : permissions) {

				JwsEntityRoleAssociation permissionObj = permission.getObject();

				boolean exists = false;

				for (JwsEntityRoleAssociation existing : permissionXMLVO.getJwsRoleDetails()) {

					if (existing.getEntityRoleId().equals(permissionObj.getEntityRoleId())) {

						exists = true;
						break;
					}
				}

				if (!exists) {
					permissionXMLVO.getJwsRoleDetails().add(permissionObj);
					exportedPermissionIds.add(permissionObj.getEntityRoleId());
				}
			}
		}
	}

	public void exportDashletDependencies(Map<String, List<String>> exportedEntityIds) throws Exception {

		List<String> dashboardIds = exportedEntityIds.get(Constant.MasterModuleType.DASHBOARD.getModuleType());

		if (dashboardIds == null || dashboardIds.isEmpty()) {
			return;
		}

		List<String> dashletIds = new ArrayList<>();

		for (String dashboardId : dashboardIds) {

			List<DashboardDashletAssociation> associations = dashboardDashletAssociationRepository
					.findByIdDashboardId(dashboardId);

			for (DashboardDashletAssociation association : associations) {

				if (!dashletIds.contains(association.getId().getDashletId())) {

					String dashletId = association.getId().getDashletId();

					dashletIds.add(dashletId);
				}
			}
		}

		if (!dashletIds.isEmpty()) {
			exportedEntityIds.put(Constant.MasterModuleType.DASHLET.getModuleType(), dashletIds);
		}
	}

	public void exportRoleDependencies(Map<String, List<String>> exportedEntityIds, String tempDownloadPath,
			Map<String, String> moduleListMap, Map<String, List<ExportInfo>> exportedEntityInfo) throws Exception {

		RoleXMLVO roleXMLVO = new RoleXMLVO();

		List<String> exportedRoleIds = new ArrayList<>();
		collectRoles(exportedEntityIds.get(Constant.MasterModuleType.GRID.getModuleType()),
				Constants.Modules.GRIDUTILS.getModuleName(), roleXMLVO, exportedRoleIds);

		collectRoles(exportedEntityIds.get(Constant.MasterModuleType.DASHBOARD.getModuleType()),
				Constants.Modules.DASHBOARD.getModuleName(), roleXMLVO, exportedRoleIds);

		collectRoles(exportedEntityIds.get(Constant.MasterModuleType.DASHLET.getModuleType()), "Dashlet", roleXMLVO,
				exportedRoleIds);

		collectRoles(exportedEntityIds.get(Constant.MasterModuleType.AUTOCOMPLETE.getModuleType()),
				Constants.Modules.AUTOCOMPLETE.getModuleName(), roleXMLVO, exportedRoleIds);

		collectRoles(exportedEntityIds.get(Constant.MasterModuleType.ROUTER.getModuleType()),
				Constants.Modules.ROUTER.getModuleName(), roleXMLVO, exportedRoleIds);

		collectRoles(exportedEntityIds.get(Constant.MasterModuleType.FORMIO.getModuleType()),
				Constants.Modules.FORMIO.getModuleName(), roleXMLVO, exportedRoleIds);

		collectRoles(exportedEntityIds.get(Constant.MasterModuleType.FILEMANAGER.getModuleType()),
				Constants.Modules.FILEBIN.getModuleName(), roleXMLVO, exportedRoleIds);

		collectRoles(exportedEntityIds.get(Constant.MasterModuleType.TEMPLATES.getModuleType()),
				Constants.Modules.TEMPLATING.getModuleName(), roleXMLVO, exportedRoleIds);

		collectRoles(exportedEntityIds.get(Constant.MasterModuleType.DYNAMICFORM.getModuleType()),
				Constants.Modules.DYNAMICFORM.getModuleName(), roleXMLVO, exportedRoleIds);

		collectRoles(exportedEntityIds.get(Constant.MasterModuleType.DYNAREST.getModuleType()), "REST API", roleXMLVO,
				exportedRoleIds);
		collectRoles(exportedEntityIds.get(Constant.MasterModuleType.HELPMANUAL.getModuleType()),
				Constants.Modules.HELPMANUAL.getModuleName(), roleXMLVO, exportedRoleIds);

		if (!roleXMLVO.getRoleDetails().isEmpty()) {

			XMLUtil.marshaling(roleXMLVO, Constant.MasterModuleType.MANAGEROLES.getModuleType(), tempDownloadPath);

			moduleListMap.put(Constant.MasterModuleType.MANAGEROLES.getModuleType(), Constant.XML_EXPORT_TYPE);
			exportedEntityIds.put(Constant.MasterModuleType.MANAGEROLES.getModuleType(), exportedRoleIds);
			for (JwsRole role : roleXMLVO.getRoleDetails()) {

				exportedEntityInfo
						.computeIfAbsent(Constant.MasterModuleType.MANAGEROLES.getModuleType(), k -> new ArrayList<>())
						.add(new ExportInfo(role.getRoleId(), role.getRoleName(), "1.0"));
			}
		}
	}

	private void collectRoles(List<String> entityIds, String moduleName, RoleXMLVO roleXMLVO,
			List<String> exportedRoleIds) throws Exception {

		if (entityIds == null || entityIds.isEmpty()) {
			return;
		}

		JwsMasterModules module = jwsmasterModuleRepository.findBymoduleName(moduleName);

		Set<String> setExportedRoleIds = new HashSet<>();

		for (String entityId : entityIds) {

			List<JwsRoleVO> roles = getRoles(entityId, module.getModuleId());

			if (roles == null) {
				continue;
			}

			for (JwsRoleVO roleVO : roles) {

				if (setExportedRoleIds.add(roleVO.getRoleId())) {

					JwsRole role = jwsRoleRepository.findByRoleId(roleVO.getRoleId());

					if (role != null) {
						roleXMLVO.getRoleDetails().add(role.getObject());
						exportedRoleIds.add(role.getRoleId());
					}
				}
			}
		}
	}

	public List<JwsRoleVO> getRoles(String entityId, String moduleId) {
		List<JwsEntityRoleAssociation> entityRoleAssociations = entityRoleAssociationRepository.getEntityRoles(entityId,
				moduleId);
		List<JwsRoleVO> roleList = new ArrayList<>();
		if (entityRoleAssociations != null) {
			for (Iterator iterator = entityRoleAssociations.iterator(); iterator.hasNext();) {
				JwsEntityRoleAssociation jwsEntityRoleAssociation = (JwsEntityRoleAssociation) iterator.next();
				jwsEntityRoleAssociation = jwsEntityRoleAssociation.getObject();
				JwsEntityRoleAssociationVO vo = new JwsEntityRoleAssociationVO();
				vo = vo.convertEntityToVO(jwsEntityRoleAssociation);
				JwsRoleVO jwsRoleVo = new JwsRoleVO();

				JwsRole role = new JwsRole();
				role = jwsRoleRepository.findByRoleId(vo.getRoleId());
				jwsRoleVo = role.convertEntityToVO(role);
				roleList.add(jwsRoleVo);
			}

		}
		return roleList;
	}

	public void exportFormIODependencies(List<String> dynamicFormIds, String tempDownloadPath,
			Map<String, String> moduleListMap, Map<String, List<String>> exportedEntityIds,
			Map<String, List<ExportInfo>> exportedEntityInfo) throws Exception {

		if (dynamicFormIds == null || dynamicFormIds.isEmpty()) {
			return;
		}

		FormIOXMLVO formIOXMLVO = new FormIOXMLVO();

		List<String> exportedFormIOIds = new ArrayList<>();

		for (String formId : dynamicFormIds) {

			DynamicForm dynamicForm = dynamicService.getDynamicFormById(formId);

			if (dynamicForm == null || StringUtils.isBlank(dynamicForm.getFormIoId())) {
				continue;
			}

			List<FormIOVO> formIOVOs = formIOService.findAutoFormIOByFormId(dynamicForm.getFormIoId());

			if (CollectionUtils.isEmpty(formIOVOs)) {
				continue;
			}

			for (FormIOVO formIOVO : formIOVOs) {

				exportedFormIOIds.add(formIOVO.getFormIoId());

				FormIO formIO = new FormIO();

				formIO.setFormIoId(formIOVO.getFormIoId());
				formIO.setFormName(formIOVO.getFormName());
				formIO.setFormIoJson(formIOVO.getFormIoJson());
				formIO.setFormDescription(formIOVO.getFormDescription());
				formIO.setFormIoType(formIOVO.getFormIoType());
				formIO.setPersistenceType(formIOVO.getPersistenceType());
				formIO.setIsCustomUpdated(formIOVO.getIsCustomUpdated());
				formIO.setLastUpdatedBy(formIOVO.getLastUpdatedBy());
				formIO.setLastUpdatedTs(formIOVO.getLastUpdatedTs());

				formIOXMLVO.getFormIODetails().add(formIO);
			}
		}

		if (!formIOXMLVO.getFormIODetails().isEmpty()) {

			XMLUtil.marshaling(formIOXMLVO, Constant.MasterModuleType.FORMIO.getModuleType(), tempDownloadPath);

			moduleListMap.put(Constant.MasterModuleType.FORMIO.getModuleType(), Constant.XML_EXPORT_TYPE);

			exportedEntityIds.put(Constant.MasterModuleType.FORMIO.getModuleType(), exportedFormIOIds);
			for (FormIO formIoDetails : formIOXMLVO.getFormIODetails()) {
				exportedEntityInfo
						.computeIfAbsent(Constant.MasterModuleType.FORMIO.getModuleType(), k -> new ArrayList<>())
						.add(new ExportInfo(formIoDetails.getFormIoId(), formIoDetails.getFormName(),
								getAutoExportVersion(Constant.MasterModuleType.FORMIO.getModuleType(),
										formIoDetails.getFormIoId())));
			}
		}

	}

	public void exportModuleDependencies(Map<String, List<String>> exportedEntityIds, String tempDownloadPath,
			Map<String, String> moduleListMap, Map<String, List<ExportInfo>> exportedEntityInfo) throws Exception {

		BusinessModuleXMLVO businessModuleXMLVO = new BusinessModuleXMLVO();
		List<String> exportedBusinessModuleIds = new ArrayList<>();
		collectBusinessModules(exportedEntityIds.get(Constant.MasterModuleType.GRID.getModuleType()),
				Constants.Modules.GRIDUTILS.getModuleName(), businessModuleXMLVO, exportedBusinessModuleIds);

		collectBusinessModules(exportedEntityIds.get(Constant.MasterModuleType.DASHBOARD.getModuleType()),
				Constants.Modules.DASHBOARD.getModuleName(), businessModuleXMLVO, exportedBusinessModuleIds);

		collectBusinessModules(exportedEntityIds.get(Constant.MasterModuleType.DASHLET.getModuleType()),
				ModuleType.DASHLET.toString(), businessModuleXMLVO, exportedBusinessModuleIds);

		collectBusinessModules(exportedEntityIds.get(Constant.MasterModuleType.AUTOCOMPLETE.getModuleType()),
				Constants.Modules.AUTOCOMPLETE.getModuleName(), businessModuleXMLVO, exportedBusinessModuleIds);

		collectBusinessModules(exportedEntityIds.get(Constant.MasterModuleType.FILEMANAGER.getModuleType()),
				Constants.Modules.FILEBIN.getModuleName(), businessModuleXMLVO, exportedBusinessModuleIds);

		collectBusinessModules(exportedEntityIds.get(Constant.MasterModuleType.FORMIO.getModuleType()),
				Constants.Modules.FORMIO.getModuleName(), businessModuleXMLVO, exportedBusinessModuleIds);

		collectBusinessModules(exportedEntityIds.get(Constant.MasterModuleType.DYNAMICFORM.getModuleType()),
				Constants.Modules.FORMIO.getModuleName(), businessModuleXMLVO, exportedBusinessModuleIds);

		collectBusinessModules(exportedEntityIds.get(Constant.MasterModuleType.ROUTER.getModuleType()),
				Constants.Modules.ROUTER.getModuleName(), businessModuleXMLVO, exportedBusinessModuleIds);

		collectBusinessModules(exportedEntityIds.get(Constant.MasterModuleType.DYNAREST.getModuleType()), "REST API",
				businessModuleXMLVO, exportedBusinessModuleIds);

		collectBusinessModules(exportedEntityIds.get(Constant.MasterModuleType.TEMPLATES.getModuleType()),
				Constants.Modules.TEMPLATING.getModuleName(), businessModuleXMLVO, exportedBusinessModuleIds);

		collectBusinessModules(exportedEntityIds.get(Constant.MasterModuleType.RESOURCEBUNDLE.getModuleType()),
				Constants.Modules.RESOURCEBUNDLE.getModuleName(), businessModuleXMLVO, exportedBusinessModuleIds);

		collectBusinessModules(exportedEntityIds.get(Constant.MasterModuleType.NOTIFICATION.getModuleType()),
				Constants.Modules.NOTIFICATION.getModuleName(), businessModuleXMLVO, exportedBusinessModuleIds);

		collectBusinessModules(
				exportedEntityIds.get(Constant.MasterModuleType.APPLICATIONCONFIGURATION.getModuleType()),
				Constants.Modules.APPLICATIONCONFIGURATION.getModuleName(), businessModuleXMLVO,
				exportedBusinessModuleIds);

		collectBusinessModules(exportedEntityIds.get(Constant.MasterModuleType.APICLIENTDETAILS.getModuleType()),
				Constants.Modules.APICLIENTS.getModuleName(), businessModuleXMLVO, exportedBusinessModuleIds);

		collectBusinessModules(exportedEntityIds.get(Constant.MasterModuleType.SCHEDULER.getModuleType()),
				Constants.Modules.SCHEDULER.getModuleName(), businessModuleXMLVO, exportedBusinessModuleIds);

		if (!businessModuleXMLVO.getBusinessModuleDetails().isEmpty()) {

			XMLUtil.marshaling(businessModuleXMLVO, Constant.MasterModuleType.BUSINESSMODULE.getModuleType(),
					tempDownloadPath);

			moduleListMap.put(Constant.MasterModuleType.BUSINESSMODULE.getModuleType(), Constant.XML_EXPORT_TYPE);
			exportedEntityIds.put(Constant.MasterModuleType.BUSINESSMODULE.getModuleType(), exportedBusinessModuleIds);
			for (JwsBusinessModule module : businessModuleXMLVO.getBusinessModuleDetails()) {

				exportedEntityInfo
						.computeIfAbsent(Constant.MasterModuleType.BUSINESSMODULE.getModuleType(),
								k -> new ArrayList<>())
						.add(new ExportInfo(module.getBusinessModuleId(), module.getModuleName(), "1.0"));
			}
		}
	}

	private void collectBusinessModules(List<String> entityIds, String moduleName,
			BusinessModuleXMLVO businessModuleXMLVO, List<String> exportedBusinessModuleIds) throws Exception {

		if (entityIds == null || entityIds.isEmpty()) {
			return;
		}

		JwsMasterModules module = jwsmasterModuleRepository.findBymoduleName(moduleName);

		if (module == null) {
			return;
		}

		Set<String> SetExportedBusinessModuleIds = new HashSet<>();

		for (String entityId : entityIds) {

			List<JwsBusinessModuleEntityVO> modules = getBusinessModules(entityId, module.getModuleId());

			if (modules == null) {
				continue;
			}

			for (JwsBusinessModuleEntityVO moduleVO : modules) {

				if (SetExportedBusinessModuleIds.add(moduleVO.getBusinessModuleId())) {

					JwsBusinessModule businessModule = jwsBusinessModuleRepository
							.findById(moduleVO.getBusinessModuleId()).orElse(null);

					if (businessModule != null) {
						businessModuleXMLVO.getBusinessModuleDetails().add(businessModule.getObject());
						exportedBusinessModuleIds.add(businessModule.getBusinessModuleId());
					}
				}
			}
		}
	}

	public List<JwsBusinessModuleEntityVO> getBusinessModules(String entityId, String moduleId) {
		List<JwsBusinessModuleEntity> businessModules = businessModuleEntityRepository.getBusinessModules(entityId,
				moduleId);
		List<JwsBusinessModuleEntityVO> moduleList = new ArrayList<>();
		if (businessModules != null) {
			for (Iterator iterator = businessModules.iterator(); iterator.hasNext();) {
				JwsBusinessModuleEntity businessModuleEntity = (JwsBusinessModuleEntity) iterator.next();
				businessModuleEntity = businessModuleEntity.getObject();
				JwsBusinessModuleEntityVO vo = new JwsBusinessModuleEntityVO();
				vo = vo.convertEntityToVO(businessModuleEntity);
				moduleList.add(vo);
			}
		}
		return moduleList;
	}

	public void exportScriptLibraryDependencies(Map<String, List<String>> exportedEntityIds, String tempDownloadPath,
			Map<String, String> moduleListMap, Map<String, List<ExportInfo>> exportedEntityInfo) throws Exception {

		ScriptLibraryXMLVO xmlVO = new ScriptLibraryXMLVO();
		List<String> exportedScriptIds = new ArrayList<>();
		collectScriptLibraries(exportedEntityIds.get(Constant.MasterModuleType.FILEMANAGER.getModuleType()),
				Constant.FILEBINMODID, xmlVO, exportedScriptIds);

		collectScriptLibraries(exportedEntityIds.get(Constant.MasterModuleType.DYNAMICFORM.getModuleType()),
				Constant.DYNAFORM_MOD_ID, xmlVO, exportedScriptIds);

		collectScriptLibraries(exportedEntityIds.get(Constant.MasterModuleType.DYNAREST.getModuleType()),
				Constant.DYNA_REST_MOD_ID, xmlVO, exportedScriptIds);

		if (!xmlVO.getScriptLibraryDetails().isEmpty()) {

			XMLUtil.marshaling(xmlVO, Constant.MasterModuleType.SCRIPTLIBRARY.getModuleType(), tempDownloadPath);

			moduleListMap.put(Constant.MasterModuleType.SCRIPTLIBRARY.getModuleType(), Constant.XML_EXPORT_TYPE);
			exportedEntityIds.put(Constant.MasterModuleType.SCRIPTLIBRARY.getModuleType(), exportedScriptIds);
			for (ScriptLibraryDetails scrlib : xmlVO.getScriptLibraryDetails()) {
				exportedEntityInfo
						.computeIfAbsent(Constant.MasterModuleType.SCRIPTLIBRARY.getModuleType(),
								k -> new ArrayList<>())
						.add(new ExportInfo(scrlib.getScriptLibId(), scrlib.getLibraryName(), getAutoExportVersion(
								Constant.MasterModuleType.SCRIPTLIBRARY.getModuleType(), scrlib.getScriptLibId())));
			}
		}
	}

	private void collectScriptLibraries(List<String> entityIds, String moduleId, ScriptLibraryXMLVO xmlVO,
			List<String> exportedScriptIds) throws Exception {

		if (entityIds == null || entityIds.isEmpty()) {
			return;
		}

		Set<String> setExportedScriptIds = new HashSet<>();

		for (String entityId : entityIds) {

			List<ScriptLibConnectVO> connections = getscriptLibraries(entityId, moduleId);

			if (connections == null || connections.isEmpty()) {
				continue;
			}

			for (ScriptLibConnectVO vo : connections) {

				String scriptLibId = vo.getScriptlibId();

				if (scriptLibId == null) {
					continue;
				}

				if (setExportedScriptIds.add(scriptLibId)) {

					ScriptLibraryDetails scriptLibrary = scriptLibraryRepository.findById(scriptLibId).orElse(null);

					if (scriptLibrary != null) {

						xmlVO.getScriptLibraryDetails().add(scriptLibrary.getObject());
						exportedScriptIds.add(scriptLibrary.getScriptLibId());
					}
				}
			}
		}
	}

	public List<ScriptLibConnectVO> getscriptLibraries(String entityId, String moduleId) {
		List<ScriptLibraryConnection> scriptLibConns = new ArrayList<>();

		if (moduleId.equals(Constant.FILEBINMODID)) {
			List<String> entityIds = Arrays.asList("upload_" + entityId, "view_" + entityId, "delete_" + entityId);
			scriptLibConns = scriptLibraryConnRepository.getScriptLibraryConnIds(entityIds, moduleId);
		} else if (moduleId.equals(Constant.DYNAFORM_MOD_ID)) {
			List<String> dynamicFormSaveQueryIdList = jwsDynarestDAO.getdynamicFormQueryID(entityId);
			for (String dynEntityId : dynamicFormSaveQueryIdList) {
				List<ScriptLibraryConnection> conns = scriptLibraryConnRepository
						.getScriptLibraryConnectionIds(dynEntityId, moduleId);
				if (conns != null) {
					scriptLibConns.addAll(conns);
				}
			}
		} else if (moduleId.equals(Constant.DYNA_REST_MOD_ID)) {
			List<ScriptLibraryConnection> conns = scriptLibraryConnRepository.getScriptLibraryConnectionIds(entityId,
					moduleId);
			if (conns != null) {
				scriptLibConns.addAll(conns);
			}
		}

		List<ScriptLibConnectVO> scriptLibIdList = new ArrayList<>();
		if (scriptLibConns != null) {
			for (ScriptLibraryConnection scriptLibraryConnection : scriptLibConns) {
				scriptLibraryConnection = scriptLibraryConnection.getObject();
				ScriptLibConnectVO vo = new ScriptLibConnectVO();
				vo = vo.convertEntityToVO(scriptLibraryConnection);
				scriptLibIdList.add(vo);
			}
		}
		return scriptLibIdList;
	}

	public void exportAdditionalDatasourceDependencies(Map<String, List<String>> exportedEntityIds,
			String tempDownloadPath, Map<String, String> moduleListMap,
			Map<String, List<ExportInfo>> exportedEntityInfo) throws Exception {

		AdditionalDatasourceXMLVO xmlVO = new AdditionalDatasourceXMLVO();

		Set<String> setExportedDatasourceIds = new HashSet<>();
		List<String> exportedDatasourceIds = new ArrayList<>();
		/*
		 * REST API
		 */
		collectAdditionalDatasources(exportedEntityIds.get(Constant.MasterModuleType.DYNAREST.getModuleType()),
				Constant.DYNA_REST_MOD_ID, setExportedDatasourceIds, xmlVO, exportedDatasourceIds);

		/*
		 * Dynamic Form
		 */
		collectAdditionalDatasources(exportedEntityIds.get(Constant.MasterModuleType.DYNAMICFORM.getModuleType()),
				Constant.DYNAFORM_MOD_ID, setExportedDatasourceIds, xmlVO, exportedDatasourceIds);

		/*
		 * File Bin
		 */
		collectAdditionalDatasources(exportedEntityIds.get(Constant.MasterModuleType.FILEMANAGER.getModuleType()),
				Constant.FILEBINMODID, setExportedDatasourceIds, xmlVO, exportedDatasourceIds);

		/*
		 * Auto Complete
		 */
		collectAdditionalDatasources(exportedEntityIds.get(Constant.MasterModuleType.AUTOCOMPLETE.getModuleType()),
				Constant.AUTOCOMPLETEMODID, setExportedDatasourceIds, xmlVO, exportedDatasourceIds);

		/*
		 * Dashlet
		 */
		collectAdditionalDatasources(exportedEntityIds.get(Constant.MasterModuleType.DASHLET.getModuleType()),
				Constant.DASHLETMODID, setExportedDatasourceIds, xmlVO, exportedDatasourceIds);

		/*
		 * Notification
		 */
		collectAdditionalDatasources(exportedEntityIds.get(Constant.MasterModuleType.NOTIFICATION.getModuleType()),
				Constant.NOTIFICATIONMODID, setExportedDatasourceIds, xmlVO, exportedDatasourceIds);

		/*
		 * Grid Utils
		 */
		collectAdditionalDatasources(exportedEntityIds.get(Constant.MasterModuleType.GRID.getModuleType()),
				Constant.GRIDMODID, setExportedDatasourceIds, xmlVO, exportedDatasourceIds);

		if (!xmlVO.getAdditionalDatasource().isEmpty()) {

			XMLUtil.marshaling(xmlVO, Constant.MasterModuleType.ADDITIONALDATASOURCE.getModuleType(), tempDownloadPath);

			moduleListMap.put(Constant.MasterModuleType.ADDITIONALDATASOURCE.getModuleType(), Constant.XML_EXPORT_TYPE);
			exportedEntityIds.put(Constant.MasterModuleType.ADDITIONALDATASOURCE.getModuleType(),
					exportedDatasourceIds);
			for (AdditionalDatasource datasource : xmlVO.getAdditionalDatasource()) {

				exportedEntityInfo
						.computeIfAbsent(Constant.MasterModuleType.ADDITIONALDATASOURCE.getModuleType(),
								k -> new ArrayList<>())
						.add(new ExportInfo(datasource.getAdditionalDatasourceId(), datasource.getDatasourceName(),
								"1.0"));
			}
		}
	}

	private void collectAdditionalDatasources(List<String> entityIds, String moduleId,
			Set<String> setExportedDatasourceIds, AdditionalDatasourceXMLVO xmlVO, List<String> exportedDatasourceIds)
			throws Exception {

		if (entityIds == null || entityIds.isEmpty()) {
			return;
		}

		for (String entityId : entityIds) {

			List<String> datasourceIds = getAdditionalDataSources(entityId, moduleId);

			if (datasourceIds == null) {
				continue;
			}

			for (String datasourceId : datasourceIds) {

				if (!setExportedDatasourceIds.add(datasourceId)) {
					continue;
				}

				AdditionalDatasource datasource = additionalDatasourceRepository.findById(datasourceId).orElse(null);

				if (datasource != null) {
					xmlVO.getAdditionalDatasource().add(datasource.getObject());
					exportedDatasourceIds.add(datasource.getAdditionalDatasourceId());
				}
			}
		}
	}

	public List<String> getAdditionalDataSources(String entityId, String moduleId) {
		List<String> jwsDynamicRestDaoDetail = new ArrayList<>();
		if (moduleId.equals(Constant.DYNA_REST_MOD_ID)) {
			jwsDynamicRestDaoDetail = jwsDynamicRestDAORepository.getRestApiDaoDataSourceByApiId(entityId);
		}

		return jwsDynamicRestDaoDetail;
	}

	private String getAutoExportVersion(String moduleType, String entityId) {

		try {

			switch (moduleType.toUpperCase()) {

			case "FILEMANAGER":
			case "PERMISSION":
			case "ROUTER":
			case "MANAGEUSERS":
			case "MANAGEROLES":
			case "HELPMANUAL":
			case "BUSINESSMODULE":
			case "BUSINESSENTITYMODULES":
			case "ADDITIONALDATASOURCE":
			case "APICLIENTDETAILS":
			case "SCHEDULER":
			case "FILEIMPEXPDETAILS":
				return "1.0";

			default:
				String entityName = EntityNameModuleTypeEnum.valueOf(moduleType.toUpperCase()).geTableName();
				String version = "1.0";
				version = String.valueOf(moduleVersionDAO.getVersionIdByEntityIdAndName(entityId, entityName));

				if (version == null || version.trim().isEmpty() || "null".equalsIgnoreCase(version)) {
					version = "1.0";
				}
				return version;
			}

		} catch (Exception e) {
			return "1.0";
		}
	}
}
