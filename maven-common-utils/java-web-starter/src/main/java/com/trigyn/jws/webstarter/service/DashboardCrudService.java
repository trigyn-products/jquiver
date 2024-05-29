package com.trigyn.jws.webstarter.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.trigyn.jws.dashboard.dao.DashletDAO;
import com.trigyn.jws.dashboard.entities.Dashboard;
import com.trigyn.jws.dashboard.entities.DashboardDashletAssociation;
import com.trigyn.jws.dashboard.entities.DashboardDashletAssociationPK;
import com.trigyn.jws.dashboard.entities.DashboardRoleAssociation;
import com.trigyn.jws.dashboard.entities.DashboardRoleAssociationPK;
import com.trigyn.jws.dashboard.entities.Dashlet;
import com.trigyn.jws.dashboard.entities.DashletProperties;
import com.trigyn.jws.dashboard.repository.interfaces.IContextMasterRepository;
import com.trigyn.jws.dashboard.repository.interfaces.IDashboardDashletAssociationRepository;
import com.trigyn.jws.dashboard.repository.interfaces.IDashboardRepository;
import com.trigyn.jws.dashboard.repository.interfaces.IDashboardRoleAssociationRepository;
import com.trigyn.jws.dashboard.repository.interfaces.IDashletPropertiesRepository;
import com.trigyn.jws.dashboard.repository.interfaces.IDashletRepository;
import com.trigyn.jws.dashboard.utility.Constants;
import com.trigyn.jws.dashboard.vo.ContextMasterVO;
import com.trigyn.jws.dashboard.vo.DashboardVO;
import com.trigyn.jws.dashboard.vo.DashletPropertyVO;
import com.trigyn.jws.dashboard.vo.DashletVO;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.DownloadUploadModule;
import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.usermanagement.entities.JwsRole;
import com.trigyn.jws.usermanagement.repository.JwsRoleRepository;
import com.trigyn.jws.webstarter.dao.DashboardCrudDAO;

@Service
@Transactional(readOnly = false)
public class DashboardCrudService {

	private final static Logger						logger							= LogManager
			.getLogger(DashboardCrudService.class);

	@Autowired
	private DashboardCrudDAO						dashboardCrudDAO				= null;

	@Autowired
	private IDashboardRepository					iDashboardRepository			= null;

	@Autowired
	private IDashboardRoleAssociationRepository		iDashboardRoleRepository		= null;

	@Autowired
	private IDashboardDashletAssociationRepository	iDashboardDashletRepository		= null;

	@Autowired
	private IContextMasterRepository				iContextMasterRepository		= null;

	@Autowired
	private IDashletPropertiesRepository			iDashletPropertiesRepository	= null;

	@Autowired
	private IDashletRepository						iDashletRepository				= null;
	
	@Autowired
	private DashletDAO                              dashletDAO=null;

	@Autowired
	private JwsRoleRepository						userRoleRepository				= null;

	@Autowired
	private IUserDetailsService						userDetailsService				= null;

	@Autowired
	private DownloadUploadModule<Dashlet>			downloadUploadModule			= null;

	@Autowired
	private PropertyMasterDAO						propertyMasterDAO				= null;

	@Autowired
	private ModuleVersionService					moduleVersionService			= null;

	@Autowired
	private ActivityLog								activitylog						= null;
	
	public Dashboard findDashboardByDashboardId(String dashboardId) throws Exception {
		return iDashboardRepository.findById(dashboardId).orElse(null);
	}

	public List<DashboardRoleAssociation> findDashboardRoleByDashboardId(String dashboardId) throws Exception {
		return dashboardCrudDAO.findDashboardRoleByDashboardId(dashboardId);
	}

	public List<JwsRole> getAllUserRoles() throws Exception {
		return userRoleRepository.findAllRoles();
	}

	public void deleteAllDashletFromDashboard(DashboardVO dashboardVO) throws Exception {
		String dashboardId = dashboardVO.getDashboardId();
		if (!StringUtils.isBlank(dashboardId)) {
			dashboardCrudDAO.deleteAllDashletFromDashboard(dashboardId);
		}
	}

	public void deleteAllDashboardRoles(DashboardVO dashboardVO) throws Exception {
		String dashboardId = dashboardVO.getDashboardId();
		if (!StringUtils.isBlank(dashboardId)) {
			dashboardCrudDAO.deleteAllDashboardRoles(dashboardId);
		}
	}
	public DashletVO findDashletByDashletId(String dashletId) throws Exception {
		return iDashletRepository.findDashletByDashletId(dashletId);
	}
	
	public List<DashletVO> getDashletVOFromDashboard(Dashboard dashboard) throws Exception {
		
		List<DashletVO> listDashletVO =new ArrayList();
		if (!CollectionUtils.isEmpty(dashboard.getDashboardDashlets())) {
			for (DashboardDashletAssociation dashboardDashletAssociation : dashboard.getDashboardDashlets()) {
				DashletVO dashletVO = new DashletVO();
				dashletVO.setDashletId(dashboardDashletAssociation.getDashlet().getDashletId());
				dashletVO.setDashletName(dashboardDashletAssociation.getDashlet().getDashletName());
				listDashletVO.add(dashletVO);
			}
			return listDashletVO;
		}
		return null;
	}

	@Transactional(readOnly = false)
	public String saveDashboardDetails(DashboardVO dashboardVO, String userId, Integer sourceTypeId) throws Exception {
		if (StringUtils.isBlank(userId)) {
			userId = userDetailsService.getUserDetails().getUserId();
		}
		Dashboard							dashboardEntity		= convertDashboarVOToEntity(dashboardVO, userId);
		List<DashboardRoleAssociation>		roleAssociations	= new ArrayList<>();
		List<DashboardDashletAssociation>	dashletAssociations	= new ArrayList<>();
		String								action				= "";
		String								masterModuleType	= Constants.Modules.DASHBOARD.getModuleName();
		if (dashboardEntity.getDashboardId() != null) {
			action = Constants.Action.EDIT.getAction();
		} else {
			action = Constants.Action.ADD.getAction();
		}
		if(dashboardEntity.getDashboardId()!=null) {
			dashboardCrudDAO.updateDashboard(dashboardEntity);}			
		else {
		dashboardEntity = iDashboardRepository.save(dashboardEntity);}
		dashboardVO.setDashboardId(dashboardEntity.getDashboardId());
		if (!CollectionUtils.isEmpty(dashboardVO.getRoleIdList())) {
			for (String roleId : dashboardVO.getRoleIdList()) {
				DashboardRoleAssociation	dashboardRoleAssociation	= new DashboardRoleAssociation();
				DashboardRoleAssociationPK	dashboardRoleAssociationPK	= new DashboardRoleAssociationPK();
				dashboardRoleAssociationPK.setDashboardId(dashboardEntity.getDashboardId());
				dashboardRoleAssociationPK.setRoleId(roleId);
				dashboardRoleAssociation.setId(dashboardRoleAssociationPK);
				iDashboardRoleRepository.save(dashboardRoleAssociation);
				roleAssociations.add(dashboardRoleAssociation);
			}
		}
		if (!CollectionUtils.isEmpty(dashboardVO.getDashletIdList())) {
			for (String dahsletId : dashboardVO.getDashletIdList()) {
				DashboardDashletAssociation		dashboardDashletAssociation		= new DashboardDashletAssociation();
				DashboardDashletAssociationPK	dashboardDashletAssociationPK	= new DashboardDashletAssociationPK();
				dashboardDashletAssociationPK.setDashboardId(dashboardEntity.getDashboardId());
				dashboardDashletAssociationPK.setDashletId(dahsletId);
				dashboardDashletAssociation.setId(dashboardDashletAssociationPK);
				iDashboardDashletRepository.save(dashboardDashletAssociation);
				dashletAssociations.add(dashboardDashletAssociation);
			}
		}
		dashboardEntity.setDashboardRoles(roleAssociations);
		dashboardEntity.setDashboardDashlets(dashletAssociations);
		moduleVersionService.saveModuleVersion(dashboardVO, null, dashboardEntity.getDashboardId(), "jq_dashboard",
				sourceTypeId);
		/* Method called for implementing Activity Log */
		logActivity(dashboardEntity.getDashboardName(), dashboardEntity.getDashboardType(), action, masterModuleType);
		return dashboardEntity.getDashboardId();
	}

	/**
	 * Purpose of this method is to log activities</br>
	 * in Dashboard Module.
	 * 
	 * @author              Bibhusrita.Nayak
	 * @param  templateName
	 * @param  typeSelect
	 * @throws Exception
	 */
	private void logActivity(String templateName, Integer typeSelect, String action, String masterModuleType)
			throws Exception {
		Map<String, String>	requestParams		= new HashMap<>();
		UserDetailsVO		detailsVO			= userDetailsService.getUserDetails();

		Date				activityTimestamp	= new Date();
		if (typeSelect == Constants.Changetype.CUSTOM.getChangeTypeInt()) {
			requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
		} else {
			requestParams.put("typeSelect", Constants.Changetype.SYSTEM.getChangetype());
		}
		requestParams.put("action", action);
		requestParams.put("entityName", templateName);
		requestParams.put("masterModuleType", masterModuleType);
		requestParams.put("userName", detailsVO.getUserName());
		requestParams.put("message", "");
		requestParams.put("date", activityTimestamp.toString());
		activitylog.activitylog(requestParams);
	}

	private Dashboard convertDashboarVOToEntity(DashboardVO dashboardVO, String userId) {
		Dashboard	dashboardEntity	= new Dashboard();
		Date		date			= new Date();

		if (dashboardVO.getDashboardId() != null && !dashboardVO.getDashboardId().isBlank()
				&& !dashboardVO.getDashboardId().isEmpty()) {
			dashboardEntity.setDashboardId(dashboardVO.getDashboardId());
		}
		dashboardEntity.setDashboardName(dashboardVO.getDashboardName());
		dashboardEntity.setIsExportable(dashboardVO.getIsExportable());
		dashboardEntity.setIsDraggable(dashboardVO.getIsDraggable());
		dashboardEntity.setLastUpdatedTs(date);
		dashboardEntity.setCreatedBy(userId);
		dashboardEntity.setCreatedDate(date);
		dashboardEntity.setLastUpdatedTs(date);
		dashboardEntity.setDashboardBody(dashboardVO.getDashboardBody());
		return dashboardEntity;
	}
	public DashboardVO convertDashboardEntityToVO(Dashboard dashboard) {
		DashboardVO dashboardVO = new DashboardVO();
		dashboardVO.setDashboardId(dashboard.getDashboardId());
		dashboardVO.setDashboardName(dashboard.getDashboardName());

		List<DashboardDashletAssociation>	listDDA			= dashboard.getDashboardDashlets();
		List<String>						dashletIDList	= new ArrayList<>();
		for (DashboardDashletAssociation dda : listDDA) {
			dashletIDList.add(dda.getId().getDashletId());
		}
		dashboardVO.setDashletIdList(dashletIDList);
		dashboardVO.setIsDraggable(dashboard.getIsDraggable());
		dashboardVO.setIsExportable(dashboard.getIsExportable());
		dashboardVO.setDashboardBody(dashboard.getDashboardBody());
		
		return dashboardVO;
	}


	public DashboardVO convertDashboarEntityToVO(Dashboard dashboard) {
		DashboardVO dashboardVO = new DashboardVO();
		dashboardVO.setDashboardId(dashboard.getDashboardId());
		dashboardVO.setDashboardName(dashboard.getDashboardName());

		List<DashboardDashletAssociation>	listDDA			= dashboard.getDashboardDashlets();
		List<String>						dashletIDList	= new ArrayList<>();
		for (DashboardDashletAssociation dda : listDDA) {
			dashletIDList.add(dda.getId().getDashletId());
		}
		dashboardVO.setDashletIdList(dashletIDList);
		dashboardVO.setIsDraggable(dashboard.getIsDraggable());
		dashboardVO.setIsExportable(dashboard.getIsExportable());

		List<DashboardRoleAssociation>	listDRA		= dashboard.getDashboardRoleAssociations();
		List<String>					roleIdList	= new ArrayList<>();
		for (DashboardRoleAssociation dra : listDRA) {
			roleIdList.add(dra.getId().getRoleId());
		}
		dashboardVO.setRoleIdList(roleIdList);
		dashboardVO.setDashboardBody(dashboard.getDashboardBody());
		return dashboardVO;
	}

	public void saveDashboardDashletAssociation(DashboardDashletAssociation dashboardDashletAssociation)
			throws Exception {
		dashboardCrudDAO.saveDashboardDashletAssociation(dashboardDashletAssociation);
	}

	public Map<String, String> findContextDetails() throws Exception {
		Map<String, String>		contextDetails		= new HashMap<>();
		List<ContextMasterVO>	contextMasterVOList	= iContextMasterRepository.findAllContext();
		for (ContextMasterVO contextMasterVO : contextMasterVOList) {
			contextDetails.put(contextMasterVO.getContextId(), contextMasterVO.getContextDescription());
		}
		return contextDetails;
	}

	public void deleteAllDashletProperty(DashletVO dashletVO) throws Exception {
		String			dashletId		= dashletVO.getDashletId();
		List<String>	propertyIdList	= new ArrayList<>();
		if (!StringUtils.isBlank(dashletId)) {
			if (!CollectionUtils.isEmpty(dashletVO.getDashletPropertVOList())) {
				for (DashletPropertyVO dashletPropertyVO : dashletVO.getDashletPropertVOList()) {
					propertyIdList.add(dashletPropertyVO.getPropertyId());
				}
			}
			dashboardCrudDAO.deleteAllDashletProperty(dashletId, propertyIdList);
		}
	}

	public void deleteAllDashletRoles(DashletVO dashletVO) throws Exception {
		String dashletId = dashletVO.getDashletId();
		if (!StringUtils.isBlank(dashletId)) {
			dashboardCrudDAO.deleteAllDashletRoles(dashletId);
		}
	}
	@Transactional(readOnly = false)
	public String saveDashlet(DashletVO dashletVO, Integer sourceTypeId) throws Exception {

		Dashlet dashlet = convertDashletVOToEntity(dashletVO);
		dashboardCrudDAO.saveDashlet(dashlet);
		String	action				= "";
		String	masterModuleType	= Constants.Modules.DASHLETS.getModuleName();
		if (dashletVO.getDashletId().isEmpty() == false) {
			action = Constants.Action.EDIT.getAction();
		} else {
			action = Constants.Action.ADD.getAction();
		}
		/* Method called for implementing Activity Log */
		logActivity(dashlet.getDashletName(), dashlet.getDashletTypeId(), action, masterModuleType);

		dashletVO.setDashletId(dashlet.getDashletId());
		List<DashletProperties> properties = new ArrayList<>();

		try {
			if (!CollectionUtils.isEmpty(dashletVO.getDashletPropertVOList())) {
				for (DashletPropertyVO dashletPropertyVO : dashletVO.getDashletPropertVOList()) {
					DashletProperties dashletProperties = convertDashletPropertyVOtoEntity(dashlet.getDashletId(),
							dashletPropertyVO);
				    //dashboardCrudDAO.saveDashletProperties(dashletProperties);
					iDashletPropertiesRepository.save(dashletProperties);
					properties.add(dashletProperties);
				}
			}
			dashlet.setProperties(properties);
			moduleVersionService.saveModuleVersion(dashletVO, null, dashlet.getDashletId(), "jq_dashlet", sourceTypeId);

			String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
			if (environment.equalsIgnoreCase("dev")) {
				String downloadFolderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system",
						"template-storage-path");
				downloadUploadModule.downloadCodeToLocal(dashlet, downloadFolderLocation);
			}

		} catch (Exception a_excep) {
			logger.error("Error ocurred while saving Dashlets : DashletId : " + dashlet.getDashletId(), a_excep);

		}
		return dashlet.getDashletId();
	}

	public Dashlet convertDashletVOToEntity(DashletVO dashletVO) throws Exception {
		Dashlet	dashlet	= new Dashlet();
		Date	date	= new Date();
		String	userId	= userDetailsService.getUserDetails().getUserName();

		try {
			if (dashletVO.getDashletId() != null && !dashletVO.getDashletId().isBlank()
					&& !dashletVO.getDashletId().isEmpty()) {
				dashlet.setDashletId(dashletVO.getDashletId());
				dashlet.setUpdatedBy(userId);
			} else {
				dashlet.setCreatedBy(userId);
				dashlet.setCreatedDate(date);
			}
			if (StringUtils.isBlank(dashletVO.getDataSourceId()) == false) {
				dashlet.setDatasourceId(dashletVO.getDataSourceId());
			}

			dashlet.setDashletName(dashletVO.getDashletName());
			dashlet.setDashletTitle(dashletVO.getDashletTitle());
			dashlet.setXCoordinate(dashletVO.getxCoordinate());
			dashlet.setYCoordinate(dashletVO.getyCoordinate());
			dashlet.setWidth(dashletVO.getWidth());
			dashlet.setHeight(dashletVO.getHeight());
			dashlet.setHeight(dashletVO.getHeight());
			dashlet.setDashletQuery(dashletVO.getDashletQuery());
			dashlet.setDashletBody(dashletVO.getDashletBody());
			dashlet.setShowHeader(dashletVO.getShowHeader());
			dashlet.setLastUpdatedTs(date);
			dashlet.setIsActive(dashletVO.getIsActive());
			dashlet.setDaoQueryType(dashletVO.getDaoQueryType());
			dashlet.setResultVariableName(dashletVO.getResultVariableName());
			dashlet.setDatasourceId(dashletVO.getDataSourceId());
			dashlet.setDashletTypeId(dashletVO.getDashletTypeId());
		} catch (Exception a_excep) {
			logger.error("Error ocurred.", a_excep);

		}
		return dashlet;
	}

	public DashletProperties convertDashletPropertyVOtoEntity(String dashletId, DashletPropertyVO dashletPropertyVO)
			throws Exception {
		DashletProperties dashletProperties = new DashletProperties();
		try {
			if (dashletPropertyVO.getPropertyId() != null && !dashletPropertyVO.getPropertyId().isBlank()
					&& !dashletPropertyVO.getPropertyId().isEmpty()) {
				dashletProperties.setPropertyId(dashletPropertyVO.getPropertyId());
			}
			dashletProperties.setSequence(dashletPropertyVO.getSequence());
			dashletProperties.setDashletId(dashletId);
			dashletProperties.setPlaceholderName(dashletPropertyVO.getPlaceholderName());
			dashletProperties.setDisplayName(dashletPropertyVO.getDisplayName());
			dashletProperties.setType(dashletPropertyVO.getType());
			dashletProperties.setValue(dashletPropertyVO.getValue());
			dashletProperties.setValidation(dashletPropertyVO.getValidation());
			dashletProperties.setDefaultValue(dashletPropertyVO.getDefaultValue());
			dashletProperties.setConfigurationScript(dashletPropertyVO.getConfigurationScript());
			dashletProperties.setToDisplay(dashletPropertyVO.getToDisplay());
			dashletProperties.setIsDeleted(dashletPropertyVO.getIsDeleted());
			return dashletProperties;
		} catch (Exception a_excep) {
			logger.error("Error occurred while converting instance of DashletPropertyVO to DashletProperties entity.",
					a_excep);
			throw new RuntimeException(
					"Error occurred while converting instance of DashletPropertyVO to DashletProperties entity.");
		}
	}

	public void downloadDashlets(String dashletId) throws Exception {
		String downloadFolderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system",
				"template-storage-path");
		if (!StringUtils.isBlank(dashletId)) {
			Dashlet dashlet = dashboardCrudDAO.findDashletByDashletId(dashletId);
			downloadUploadModule.downloadCodeToLocal(dashlet, downloadFolderLocation);
		} else {
			downloadUploadModule.downloadCodeToLocal(null, downloadFolderLocation);
		}
	}

	public void uploadDashlets(String dashletName) throws Exception {
		downloadUploadModule.uploadCodeToDB(dashletName);
	}

}
