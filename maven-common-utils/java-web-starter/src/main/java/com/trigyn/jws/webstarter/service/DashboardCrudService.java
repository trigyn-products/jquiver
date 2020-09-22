package com.trigyn.jws.webstarter.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.trigyn.jws.dashboard.entities.Dashboard;
import com.trigyn.jws.dashboard.entities.DashboardDashletAssociation;
import com.trigyn.jws.dashboard.entities.DashboardDashletAssociationPK;
import com.trigyn.jws.dashboard.entities.DashboardRoleAssociation;
import com.trigyn.jws.dashboard.entities.DashboardRoleAssociationPK;
import com.trigyn.jws.dashboard.entities.Dashlet;
import com.trigyn.jws.dashboard.entities.DashletProperties;
import com.trigyn.jws.dashboard.entities.DashletRoleAssociation;
import com.trigyn.jws.dashboard.entities.DashletRoleAssociationPK;
import com.trigyn.jws.dashboard.repository.interfaces.IContextMasterRepository;
import com.trigyn.jws.dashboard.repository.interfaces.IDashboardDashletAssociationRepository;
import com.trigyn.jws.dashboard.repository.interfaces.IDashboardRepository;
import com.trigyn.jws.dashboard.repository.interfaces.IDashboardRoleAssociationRepository;
import com.trigyn.jws.dashboard.repository.interfaces.IDashletPropertiesRepository;
import com.trigyn.jws.dashboard.repository.interfaces.IDashletRepository;
import com.trigyn.jws.dashboard.repository.interfaces.IDashletRoleAssociationRepository;
import com.trigyn.jws.dashboard.utility.Constants;
import com.trigyn.jws.dashboard.vo.ContextMasterVO;
import com.trigyn.jws.dashboard.vo.DashboardVO;
import com.trigyn.jws.dashboard.vo.DashletPropertyVO;
import com.trigyn.jws.dashboard.vo.DashletVO;
import com.trigyn.jws.webstarter.dao.DashboardCrudDAO;
import com.trigyn.jws.webstarter.utils.DownloadUploadModule;
import com.trigyn.jws.webstarter.utils.DownloadUploadModuleFactory;

@Service
@Transactional(readOnly = false)
public class DashboardCrudService {
	
	private final static Logger logger = LogManager.getLogger(DashboardCrudService.class);

    @Autowired
    private DashboardCrudDAO dashboardCrudDAO                                       = null;

    @Autowired
	private IDashboardRepository iDashboardRepository								= null;
	
	@Autowired
	private IDashboardRoleAssociationRepository iDashboardRoleRepository			= null;
	
	@Autowired
    private IDashboardDashletAssociationRepository iDashboardDashletRepository 		= null;

    @Autowired
    private IContextMasterRepository iContextMasterRepository						= null; 
    
    @Autowired
	private IDashletPropertiesRepository iDashletPropertiesRepository 				= null;
	
	@Autowired
    private IDashletRepository iDashletRepository 									= null;
    
    @Autowired
	private IDashletRoleAssociationRepository iDashletRoleAssociationRepository		= null;
    
	@Autowired
	private DownloadUploadModuleFactory moduleFactory = null;
	
    /**
	 * @param dashboardId
	 * @return {@link Dashboard}
	 * @throws Exception
	 */
	public Dashboard findDashboardByDashboardId(String dashboardId) throws Exception {
		return dashboardCrudDAO.findDashboardByDashboardId(dashboardId);
	}

	/**
	 * @param dashboardId
	 * @return {@link List}
	 * @throws Exception
	 */
	public List<DashboardRoleAssociation> findDashboardRoleByDashboardId(String dashboardId) throws Exception {
		return dashboardCrudDAO.findDashboardRoleByDashboardId(dashboardId);
	}

	/**
	 * @param userId
	 * @param dashboard
	 * @throws Exception
	 */
	public void saveDashboardDetails(DashboardVO dashboardVO, String userId) throws Exception {
		Dashboard dashboardEntity = convertDashboarVOToEntity(dashboardVO, userId);
		iDashboardRepository.saveAndFlush(dashboardEntity);
		if (!CollectionUtils.isEmpty(dashboardVO.getRoleIdList())) {
			for (String roleId : dashboardVO.getRoleIdList()) {
				DashboardRoleAssociation dashboardRoleAssociation 		= new DashboardRoleAssociation();
				DashboardRoleAssociationPK dashboardRoleAssociationPK 	= new DashboardRoleAssociationPK();
				dashboardRoleAssociationPK.setDashboardId(dashboardEntity.getDashboardId());
				dashboardRoleAssociationPK.setRoleId(roleId);
				dashboardRoleAssociation.setId(dashboardRoleAssociationPK);
				iDashboardRoleRepository.saveAndFlush(dashboardRoleAssociation);
			}
		}else if(userId != null && !userId.isBlank() && !userId.isEmpty()) {
			DashboardRoleAssociation dashboardRoleAssociation 		= new DashboardRoleAssociation();
			DashboardRoleAssociationPK dashboardRoleAssociationPK 	= new DashboardRoleAssociationPK();
			dashboardRoleAssociationPK.setDashboardId(dashboardEntity.getDashboardId());
			dashboardRoleAssociationPK.setRoleId(userId);
			dashboardRoleAssociation.setId(dashboardRoleAssociationPK);
			iDashboardRoleRepository.saveAndFlush(dashboardRoleAssociation);
		}
		if (!CollectionUtils.isEmpty(dashboardVO.getDashletIdList())) {
			dashboardCrudDAO.deleteAllDashletFromDashboard(dashboardEntity.getDashboardId());
			for (String dahsletId : dashboardVO.getDashletIdList()) {
				DashboardDashletAssociation dashboardDashletAssociation 	= new DashboardDashletAssociation();
				DashboardDashletAssociationPK dashboardDashletAssociationPK = new DashboardDashletAssociationPK();
				dashboardDashletAssociationPK.setDashboardId(dashboardEntity.getDashboardId());
				dashboardDashletAssociationPK.setDashletId(dahsletId);
				dashboardDashletAssociation.setId(dashboardDashletAssociationPK);
				iDashboardDashletRepository.save(dashboardDashletAssociation);
			}
		}
    }
    
    /**
	 * @param dashboardVO
	 * @param userId
	 * @return {@link Dashboard}
	 */
	private Dashboard convertDashboarVOToEntity(DashboardVO dashboardVO, String userId) {
		Dashboard dashboardEntity = new Dashboard();
		Date date = new Date();
		
		if(dashboardVO.getDashboardId() != null && !dashboardVO.getDashboardId().isBlank() && !dashboardVO.getDashboardId().isEmpty()) {
			dashboardEntity.setDashboardId(dashboardVO.getDashboardId());
		}
		dashboardEntity.setDashboardName(dashboardVO.getDashboardName());
		dashboardEntity.setDashboardType(dashboardVO.getDashboardType());
		dashboardEntity.setContextId(dashboardVO.getContextId());
		dashboardEntity.setIsExportable(dashboardVO.getIsExportable());
		dashboardEntity.setIsDraggable(dashboardVO.getIsDraggable());
		dashboardEntity.setLastUpdatedDate(date);
		dashboardEntity.setCreatedBy(userId);
		dashboardEntity.setCreatedDate(date);
		dashboardEntity.setLastUpdatedDate(date);
		return dashboardEntity;
    }
    
    /**
	 * @param dashboardDashletAssociation
	 * @throws Exception
	 */
	public void saveDashboardDashletAssociation(DashboardDashletAssociation dashboardDashletAssociation) throws Exception {
		dashboardCrudDAO.saveDashboardDashletAssociation(dashboardDashletAssociation);
    }
    
    /**
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> findContextDetails() throws Exception {
		Map<String, String> contextDetails = new HashMap<>();
		List<ContextMasterVO> contextMasterVOList = iContextMasterRepository.findAllContext();
		for (ContextMasterVO contextMasterVO : contextMasterVOList) {
			contextDetails.put(contextMasterVO.getContextId(), contextMasterVO.getContextDescription());
		}
		return contextDetails;
    }
    
     /**
     * @param userId
     * @param dashlet
     * @return {@link Boolean}
     * @throws Exception
     */
    public Boolean saveDashlet(String userId, DashletVO dashletVO) throws Exception {
    	Dashlet dashlet = convertDashletVOToEntity(userId, dashletVO);
		dashlet 		= iDashletRepository.saveAndFlush(dashlet);
		
		try {
			if(!CollectionUtils.isEmpty(dashletVO.getDashletPropertVOList())) {
				for (DashletPropertyVO dashletPropertyVO : dashletVO.getDashletPropertVOList()) {
					DashletProperties dashletProperties = convertDashletPropertyVOtoEntity(dashlet.getDashletId(),dashletPropertyVO);
					iDashletPropertiesRepository.save(dashletProperties);
				}
			}
			if(!CollectionUtils.isEmpty(dashletVO.getRoleIdList())) {
				for(String roleId : dashletVO.getRoleIdList()) {
					DashletRoleAssociation dashletRoleAssociation = new DashletRoleAssociation();
					DashletRoleAssociationPK dashletRoleAssociationPK = new DashletRoleAssociationPK();
					dashletRoleAssociationPK.setDashletId(dashlet.getDashletId());
					dashletRoleAssociationPK.setRoleId(roleId);
					dashletRoleAssociation.setId(dashletRoleAssociationPK);
					iDashletRoleAssociationRepository.saveAndFlush(dashletRoleAssociation);
				}
			}else if(userId != null && !userId.isBlank() && !userId.isEmpty()) {
				DashletRoleAssociation dashletRoleAssociation = new DashletRoleAssociation();
				DashletRoleAssociationPK dashletRoleAssociationPK = new DashletRoleAssociationPK();
				dashletRoleAssociationPK.setDashletId(dashlet.getDashletId());
				dashletRoleAssociationPK.setRoleId(userId);
				dashletRoleAssociation.setId(dashletRoleAssociationPK);
				iDashletRoleAssociationRepository.saveAndFlush(dashletRoleAssociation);
			}
			return true;
		} catch (Exception e) {
			throw new RuntimeException("Error ocurred while saving dashlet details.");
		}

    }
    
    
    /**
     * @param userId
     * @param dashletVO
     * @return {@link Dashlet}
     * @throws Exception
     */
    public Dashlet convertDashletVOToEntity(String userId, DashletVO dashletVO) throws Exception{
    	Dashlet dashlet = new Dashlet();
		Date date 		= new Date();
		
    	try {
    		if(dashletVO.getDashletId() != null && !dashletVO.getDashletId().isBlank() && !dashletVO.getDashletId().isEmpty()) {
    			dashlet.setDashletId(dashletVO.getDashletId());
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
			dashlet.setContextId(dashletVO.getContextId());
			dashlet.setCreatedBy(userId);
			dashlet.setCreatedDate(date);
			dashlet.setUpdatedBy(userId);
			dashlet.setUpdatedDate(date);
			dashlet.setIsActive(dashletVO.getIsActive());
	    	return dashlet;
		} catch (Exception e) {
			throw new RuntimeException("Error occurred while converting instance of DashletVO to Dashlet entity.");
		}

    }
    
    /**
     * @param userId
     * @param dashletVO
     * @return
     * @throws Exception
     */
    public DashletProperties convertDashletPropertyVOtoEntity(String dashletId, DashletPropertyVO dashletPropertyVO) throws Exception{
    	DashletProperties dashletProperties 	= new DashletProperties();
    	try {
    		if(dashletPropertyVO.getPropertyId() != null && !dashletPropertyVO.getPropertyId().isBlank() 
    				&& !dashletPropertyVO.getPropertyId().isEmpty()) {
    			dashletProperties.setPropertyId(dashletPropertyVO.getPropertyId());
    		}
    		dashletProperties.setSequence(dashletPropertyVO.getSequence());	
    		dashletProperties.setDashletId(dashletId);
    		dashletProperties.setPlaceholderName(dashletPropertyVO.getPlaceholderName());
    		dashletProperties.setDisplayName(dashletPropertyVO.getDisplayName());
    		dashletProperties.setType(dashletPropertyVO.getType());
    		dashletProperties.setValue(dashletPropertyVO.getValue());
    		dashletProperties.setDefaultValue(dashletPropertyVO.getDefaultValue());
    		dashletProperties.setConfigurationScript(dashletPropertyVO.getConfigurationScript());
    		dashletProperties.setToDisplay(dashletPropertyVO.getToDisplay());
    		dashletProperties.setIsDeleted(Constants.RecordStatus.INSERTED.getStatus());
    		return dashletProperties;
    	}catch(Exception exception) {
    		throw new RuntimeException("Error occurred while converting instance of DashletPropertyVO to DashletProperties entity.");
    	}
    }

	public void downloadDashlets() throws Exception {
		
		DownloadUploadModule downloadUploadModule = moduleFactory.getModule("dynarest");
		downloadUploadModule.downloadCodeToLocal();
	}

	public void uploadDashlets() throws Exception {
		DownloadUploadModule downloadUploadModule = moduleFactory.getModule("dynarest");
		downloadUploadModule.downloadCodeToLocal();
	}    
}