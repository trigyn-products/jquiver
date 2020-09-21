package app.trigyn.core.webstarter.service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
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

import app.trigyn.common.dashboard.dao.DashletDAO;
import app.trigyn.common.dashboard.entities.Dashboard;
import app.trigyn.common.dashboard.entities.DashboardDashletAssociation;
import app.trigyn.common.dashboard.entities.DashboardDashletAssociationPK;
import app.trigyn.common.dashboard.entities.DashboardRoleAssociation;
import app.trigyn.common.dashboard.entities.DashboardRoleAssociationPK;
import app.trigyn.common.dashboard.entities.Dashlet;
import app.trigyn.common.dashboard.entities.DashletProperties;
import app.trigyn.common.dashboard.entities.DashletRoleAssociation;
import app.trigyn.common.dashboard.entities.DashletRoleAssociationPK;
import app.trigyn.common.dashboard.repository.interfaces.IContextMasterRepository;
import app.trigyn.common.dashboard.repository.interfaces.IDashboardDashletAssociationRepository;
import app.trigyn.common.dashboard.repository.interfaces.IDashboardRepository;
import app.trigyn.common.dashboard.repository.interfaces.IDashboardRoleAssociationRepository;
import app.trigyn.common.dashboard.repository.interfaces.IDashletPropertiesRepository;
import app.trigyn.common.dashboard.repository.interfaces.IDashletRepository;
import app.trigyn.common.dashboard.repository.interfaces.IDashletRoleAssociationRepository;
import app.trigyn.common.dashboard.utility.Constants;
import app.trigyn.common.dashboard.vo.ContextMasterVO;
import app.trigyn.common.dashboard.vo.DashboardVO;
import app.trigyn.common.dashboard.vo.DashletPropertyVO;
import app.trigyn.common.dashboard.vo.DashletVO;
import app.trigyn.common.dbutils.spi.IUserDetailsService;
import app.trigyn.common.dbutils.repository.PropertyMasterDAO;
import app.trigyn.common.dbutils.utils.FileUtilities;
import app.trigyn.core.webstarter.dao.DashboardCrudDAO;

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
	private IUserDetailsService userDetailsService 									= null;
	
	@Autowired
    private DashletDAO dashletDAO 													= null;
    
	@Autowired
	private PropertyMasterDAO propertyMasterDAO										= null;
    
	@Autowired
	private FileUtilities fileUtilities  = null;
	
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
		List<Dashlet> dashlets =  dashletDAO.getAllDashlets();
		String ftlCustomExtension = ".tgn";
		String templateDirectory = "Dashlets";
		String selectQuery = "selectQuery";
		String htmlBody = "hmtlQuery";
		String folderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		folderLocation = folderLocation +File.separator+templateDirectory;
		
		for (Dashlet dashlet : dashlets) {
			boolean isCheckSumChanged = false;
			String dashletName = dashlet.getDashletName();
			String formFolder =  folderLocation + File.separator + dashletName;
			if(!new File(formFolder).exists()) {
				File fileDirectory = new File(formFolder);
				fileDirectory.mkdirs();
			}
			// html
			String dashletBodySum = fileUtilities.checkFileContents(htmlBody, formFolder,dashlet.getDashletBody() ,dashlet.getDashletBodyChecksum(),ftlCustomExtension);
			if(dashletBodySum!= null) {
				isCheckSumChanged = true;
				dashlet.setDashletBodyChecksum(dashletBodySum);
			}
			
			//query
			String dashletQueryCheckSum  = fileUtilities.checkFileContents(selectQuery, formFolder, dashlet.getDashletQuery() ,dashlet.getDashletQueryChecksum(),ftlCustomExtension);
			if(dashletQueryCheckSum!= null) {
				isCheckSumChanged = true;
				dashlet.setDashletQueryChecksum(dashletQueryCheckSum);
			}
			
			// save checksum
			if(isCheckSumChanged) {
				iDashletRepository.save(dashlet);
			}	
		}
		
	}

	public void uploadDashlets() throws Exception {
		String user ="admin";
		String ftlCustomExtension = ".tgn";
		String templateDirectory = "Dashlets";
		String selectQuery = "selectQuery";
		String htmlBody = "hmtlQuery";
		String folderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		folderLocation = folderLocation +File.separator+templateDirectory;
		File directory = new File(folderLocation);
		if(!directory.exists()) {
			throw new Exception("No such directory present");
		}
		FilenameFilter textFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(ftlCustomExtension);
            }
        };
        
        File[] directories = directory.listFiles((new FilenameFilter() {
        	  @Override
        	  public boolean accept(File current, String name) {
        	    return new File(current, name).isDirectory();
        	  }
        	}));
        
        for (File currentDirectory : directories) {
        	String selectCheckSum = null;
        	String htmlCheckSum = null;
        	String currentDirectoryName = currentDirectory.getName();
        	Dashlet dashlet = dashletDAO.getDashletByName(currentDirectoryName);
        	
        	if(dashlet ==  null) {
        		dashlet = new Dashlet();
        		dashlet.setCreatedBy(user);
        		dashlet.setCreatedDate(new Date());
        		dashlet.setDashletName(currentDirectoryName);
        		dashlet.setDashletTitle("Uploaded from Local Directory");
        	}
				File[] directoryFiles = currentDirectory.listFiles(textFilter);
				Integer filesPresent = directoryFiles.length;
				if(filesPresent == 2) {
					File selectFile = new File(currentDirectory.getAbsolutePath()+File.separator+selectQuery+ftlCustomExtension);
					File hmtlBodyFile = new File(currentDirectory.getAbsolutePath()+File.separator+htmlBody+ftlCustomExtension);
					if(!selectFile.exists() || !hmtlBodyFile.exists()) {
						throw new Exception("selectQuery  file not and hmtlQueryfile are mandatory  for saving dashlet"+currentDirectoryName);
					}else {
						// set select
						selectCheckSum = fileUtilities.generateFileChecksum(selectFile);
						if(!selectCheckSum.equalsIgnoreCase(dashlet.getDashletQueryChecksum())) {
							
							dashlet.setDashletQuery(fileUtilities.readContentsOfFile(selectFile.getAbsolutePath()));
							dashlet.setDashletQueryChecksum(selectCheckSum);
						}
						
						// set html
						htmlCheckSum = fileUtilities.generateFileChecksum(hmtlBodyFile);
						if(!htmlCheckSum.equalsIgnoreCase(dashlet.getDashletBodyChecksum())) {
							
							dashlet.setDashletBody(fileUtilities.readContentsOfFile(hmtlBodyFile.getAbsolutePath()));
							dashlet.setDashletBodyChecksum(htmlCheckSum);
						}
						iDashletRepository.save(dashlet);
					}	// saveQuery
				}else {
					throw new Exception("Invalid count of files for saving dashlet"+currentDirectoryName);
				}
		
        }
	}    
}
