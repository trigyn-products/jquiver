package app.trigyn.common.dashboard.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import app.trigyn.common.dashboard.dao.DashboardDaoImpl;
import app.trigyn.common.dashboard.dao.DashletDAO;
import app.trigyn.common.dashboard.entities.Dashboard;
import app.trigyn.common.dashboard.entities.Dashlet;
import app.trigyn.common.dashboard.entities.DashletConfiguration;
import app.trigyn.common.dashboard.entities.DashletConfigurationPK;
import app.trigyn.common.dashboard.entities.DashletProperties;
import app.trigyn.common.dashboard.entities.DashletPropertyConfiguration;
import app.trigyn.common.dashboard.entities.DashletPropertyConfigurationPK;
import app.trigyn.common.dashboard.repository.interfaces.IDashboardLookupCategoryRepository;
import app.trigyn.common.dashboard.repository.interfaces.IDashletPropertiesRepository;
import app.trigyn.common.dashboard.repository.interfaces.IDashletRepository;
import app.trigyn.common.dashboard.repository.interfaces.IDashletRoleAssociationRepository;
import app.trigyn.common.dashboard.utility.Constants;
import app.trigyn.common.dashboard.vo.DashboardLookupCategoryVO;
import app.trigyn.common.dashboard.vo.DashletPropertyVO;
import app.trigyn.common.dashboard.vo.DashletVO;
import app.trigyn.common.dbutils.repository.PropertyMasterDAO;
import app.trigyn.common.dbutils.utils.FileUtilities;
import app.trigyn.core.templating.service.DBTemplatingService;
import app.trigyn.core.templating.utils.TemplatingUtils;
import app.trigyn.core.templating.vo.TemplateVO;

@Service
@Transactional
public class DashletService {

	@Autowired
	private DashletDAO dashletDAO 													= null;

	@Autowired
	private DashboardDaoImpl dashboardDAO 											= null;
	
	@Autowired
	private IDashletPropertiesRepository iDashletPropertiesRepository 				= null;
	
	@Autowired
	private IDashletRepository iDashletRepository 									= null;
	
	@Autowired
	private IDashboardLookupCategoryRepository iDashboardLookupCategoryRepository	= null;
	
	@Autowired
	private TemplatingUtils templateEngine 											= null;

	@Autowired
	private DBTemplatingService templatingService 									= null;
	
	@Autowired
	private PropertyMasterDAO propertyMasterDAO										= null;
	
	@Autowired
	private FileUtilities fileUtilities  = null;
	
	public Dashlet getDashletById(String id) throws Exception {
		return dashletDAO.findById(id);
	}

	/**
	 * @param dashletId
	 * @param includeHidden
	 * @return
	 * @throws Exception
	 */
	public List<DashletProperties> findDashletPropertyByDashletId(String dashletId, boolean includeHidden)
			throws Exception {
		return dashletDAO.findDashletPropertyByDashletId(dashletId, includeHidden);
	}


	/**
	 * @param userRoleList
	 * @return
	 * @throws Exception
	 */
	public List<Dashlet> getDashlets(List<String> userRoleList, String dashboardId) throws Exception {
		List<Object[]> dashlets = dashletDAO.getDashlets(userRoleList, dashboardId);
		List<Dashlet> listOfDashlets = new ArrayList<>();

		for (Object[] objects : dashlets) {
			listOfDashlets.add((Dashlet) objects[0]);
		}
		return listOfDashlets;
	}

	/**
	 * @param selectionQuery
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getDashletData(String selectionQuery) throws Exception {
		return dashletDAO.getDashletData(selectionQuery);
	}

	/**
	 * @param userId
	 * @param string
	 * @param dashboardId
	 * @return
	 * @throws Exception
	 */
	public List<Object[]> getUserDashletCoordinates(String userId, String dashletId, String dashboardId)
			throws Exception {
		return dashletDAO.getUserDashletCoordinates(userId, dashletId);
	}

	/**
	 * @param formData
	 * @param userId
	 * @param dashboardId
	 * @return
	 * @throws Exception
	 */
	public Boolean saveConfiguration(MultiValueMap<String, String> formData, String userId, String dashboardId)
			throws Exception {
		Iterator<String> it = formData.keySet().iterator();
		while (it.hasNext()) {
			String propertyId = it.next();
			DashletPropertyConfiguration configuration = new DashletPropertyConfiguration();
			DashletPropertyConfigurationPK dashletPropertyConfigurationPK = new DashletPropertyConfigurationPK();
			dashletPropertyConfigurationPK.setUserId(userId);
			dashletPropertyConfigurationPK.setPropertyId(propertyId);
			dashletPropertyConfigurationPK.setDashboardId(dashboardId);

			configuration.setId(dashletPropertyConfigurationPK);
			configuration.setPropertyValue(formData.getFirst(propertyId));

			dashletDAO.saveConfiguration(configuration);
		}
		return true;
	}

	/**
	 * @param userId
	 * @param dashlets
	 * @param dashboardId
	 * @return
	 * @throws Exception
	 */
	public Boolean saveDashletConfiguration(String userId, String[] dashlets, String dashboardId) throws Exception {
		for (int dashletCnt = 0; dashletCnt < dashlets.length; dashletCnt++) {
			JSONObject jsonObj = new JSONObject(dashlets[dashletCnt]);
			if (!jsonObj.getString("id").equals("")) {
				DashletConfiguration dashletConfiguration = new DashletConfiguration();
				DashletConfigurationPK dashletConfigurationPK = new DashletConfigurationPK();
				dashletConfigurationPK.setUserId(userId);
				dashletConfigurationPK.setDashletId(jsonObj.getString("id"));
				dashletConfigurationPK.setDashboardId(dashboardId);
				dashletConfiguration.setxCoordinate(jsonObj.getInt("x"));
				dashletConfiguration.setyCoordinate(jsonObj.getInt("y"));
				dashletConfiguration.setId(dashletConfigurationPK);
				dashletDAO.saveDashletConfiguration(dashletConfiguration);
			}
		}
		return true;
	}


	/**
	 * @param contextId
	 * @return
	 */
	public List<Map<String, Object>> getDashletsByContextId(String contextId) {
		return dashletDAO.getDashletsByContextId(contextId);
	}

	public String getDashletUI(String a_userId, boolean a_isContentOnly, String dashboardId) throws Exception {
		List<String> dashletUIs = new ArrayList<String>();
		Map<String, Object> mapOfVariables = new HashMap<String, Object>();
		String listOfDashlets = null;

		// String userType=userService.findUserType(a_userId);
		// List<String> userRoles = Arrays.asList(userType.split(","));
		Dashboard dashboard = dashboardDAO.findDashboardByDashboardId(dashboardId);
		List<String> userRoles = new ArrayList<>();
		userRoles.add("ADMIN");
		List<Dashlet> dashlets = getDashlets(userRoles, dashboardId);

		for (Dashlet l_dashlet : dashlets) {
			String dashletUI = getDashletUIString(l_dashlet, a_userId, a_isContentOnly, null);
			dashletUIs.add(dashletUI);
		}

		mapOfVariables.put("dashletUIs", dashletUIs);
		mapOfVariables.put("dashboardId", dashboardId);
		mapOfVariables.put("dashboard", dashboard);
		TemplateVO templateVO = templatingService.getTemplateByName("dashlets");
		listOfDashlets = templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
				mapOfVariables);

		return listOfDashlets;
	}

	public String getDashletUIString(Dashlet a_dashlet, String userId, boolean isContentOnly, String[] params)
			throws Exception {

		
		
		String selectCriteria = null; a_dashlet.getDashletQuery();
		String htmlBody = null; 
		String selectQueryFile = "selectQuery";
		String htmlBodyFile = "hmtlQuery";
		
		 String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
	        if(environment.equalsIgnoreCase("dev")) {
	        	selectCriteria =  getContentForDevEnvironment(a_dashlet.getDashletName(), selectQueryFile);
	        	htmlBody = getContentForDevEnvironment(a_dashlet.getDashletName(), htmlBodyFile);
	        }else {
	        	selectCriteria =  a_dashlet.getDashletQuery();
	        	htmlBody = a_dashlet.getDashletBody();
	        }	 
		List<Map<String, String>> resultSet = null;
		Map<String, Object> templateMap = new HashMap<>();
		String templateQuery = null;
		String templateHtml = null;
		String finalTemplate = null;

		Map<Object, Object> usersProp = getUserPreferences(userId, a_dashlet.getDashletId());
		List<Object[]> dashletCoordinates = getUserDashletCoordinates(userId, a_dashlet.getDashletId());

		for (DashletProperties property : a_dashlet.getProperties()) {
			templateMap.put(property.getPlaceholderName(), usersProp.get(property.getPropertyId()));
		}

		if (params != null) {
			for (int paramCnt = 0; paramCnt < params.length; paramCnt++) {
				templateMap.put(params[paramCnt].split(":")[0], params[paramCnt].split(":")[1]);
			}
		}

		templateMap.put("userId", userId);
		templateMap.put("dashletTitle", a_dashlet.getDashletTitle());
		templateMap.put("dashletId", a_dashlet.getDashletId());
		boolean isErrorOccured = Boolean.FALSE;
		try {
			templateQuery = templateEngine.processTemplateContents(selectCriteria, a_dashlet.getDashletName(),
					templateMap);
		} catch (Exception ex) {
			isErrorOccured = Boolean.TRUE;
			ex.printStackTrace();
		}

		if (null != templateQuery && !templateQuery.equals("")) {
			resultSet = dashletDAO.getDashletData(templateQuery.toString());
		}
		templateMap.put("resultSet", resultSet);

		if (resultSet == null) {
			isErrorOccured = Boolean.TRUE;
		}

		try {
			templateHtml = templateEngine.processTemplateContents(htmlBody, a_dashlet.getDashletName(), templateMap);
		} catch (Exception e) {
			isErrorOccured = Boolean.TRUE;
			e.printStackTrace();
		}

		if (isErrorOccured) {
			templateHtml = "label.errorInDashlet";
		}

		if (isContentOnly) {
			return templateHtml;
		}

		Map<String, Object> mapOfVariables = new HashMap<String, Object>();
		mapOfVariables.put("dashletId", a_dashlet.getDashletId());
		mapOfVariables.put("xCord", dashletCoordinates.get(0)[0]);
		mapOfVariables.put("yCord", dashletCoordinates.get(0)[1]);
		mapOfVariables.put("width", a_dashlet.getWidth());
		mapOfVariables.put("height", a_dashlet.getHeight());
		mapOfVariables.put("content", templateHtml);
		mapOfVariables.put("dashletId", a_dashlet.getDashletId());
		mapOfVariables.put("dashletTitle", a_dashlet.getDashletTitle());
		for (DashletProperties property : a_dashlet.getProperties()) {
			mapOfVariables.put(property.getPlaceholderName(), usersProp.get(property.getPropertyId()));
		}

		String commonDashletDiv = templatingService.getTemplateByName("dashlet-common-div").getTemplate();
		finalTemplate = templateEngine.processTemplateContents(commonDashletDiv, a_dashlet.getDashletName(),
				mapOfVariables);

		return finalTemplate;
	}

	public Map<Object, Object> getUserPreferences(String userId, String dashletId) throws Exception {
		List<Object[]> preferences = dashletDAO.getUserPreferences(userId, dashletId);
		Map<Object, Object> userPreferences = new HashMap<Object, Object>();
		for (Object[] preference : preferences) {
			userPreferences.put(preference[0], preference[1]);
		}
		return userPreferences;
	}

	public List<Object[]> getUserDashletCoordinates(String userId, String dashletId) throws Exception {
		return dashletDAO.getUserDashletCoordinates(userId, dashletId);
	}

	public String refreshDashletContent(String userId, String dashletId, String[] paramArray) throws Exception {
		Dashlet dashlet = dashletDAO.findById(dashletId);
		return getDashletUIString(dashlet, userId, Boolean.TRUE, paramArray);
	}

	
	/**
	 * @param dashletId
	 * @return {@link DashletVO}
	 * @throws Exception
	 */
	public DashletVO getDashletDetailsById(String dashletId) throws Exception{
		DashletVO dashletVO					= new DashletVO();
		try {
			if (dashletId != null && !dashletId.equals("") && !dashletId.isEmpty()) {
				dashletVO 										= findDashletByDashletId(dashletId);
				List<DashletPropertyVO> dashletPropertyVOList 	= getDashletPropertyByDashletId(dashletId);
				List<String> dashletRoleIdList 					= findDashletRolesByDashletId(dashletId);
				if (dashletPropertyVOList != null) {
					dashletVO.setDashletPropertVOList(dashletPropertyVOList);
				} else {
					dashletVO.setDashletPropertVOList(new ArrayList<DashletPropertyVO>());
				}
				if (dashletRoleIdList != null) {
					dashletVO.setRoleIdList(dashletRoleIdList);
				} else {
					dashletVO.setRoleIdList(new ArrayList<String>());
				}
			}
			return dashletVO;
		} catch (Exception exception) {
			throw new RuntimeException("Error occurred while fetching dashlet details");
		} 
		
	}
	
	
    /**
     * @param dashletId
     * @return {@link DashletVO}
     * @throws Exception
     */
    public DashletVO findDashletByDashletId(String dashletId) throws Exception {
		return iDashletRepository.findDashletByDashletId(dashletId);
    }
    
    
    /**
     * @param dashletId
     * @return {@link List}
     * @throws Exception
     */
    public List<DashletPropertyVO> getDashletPropertyByDashletId(String dashletId) throws Exception {
		return iDashletPropertiesRepository.findDashletPropertyByDashletId(dashletId, Constants.RecordStatus.INSERTED.getStatus());
    }
    
    /**
     * @param dashletId
     * @return
     */
    public List<String> findDashletRolesByDashletId(String dashletId) throws Exception {
		return dashletDAO.findDashletRoleByDashletId(dashletId);
	}

	/**
	 * @param componentName
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> findComponentTypes(String componentName)throws Exception {
		Map<String, String> componentTypes = new HashMap<String, String>();
		List<DashboardLookupCategoryVO> dashboardLookupCategoryVOList = iDashboardLookupCategoryRepository.findDashboardLookupCategoryByName(componentName);
		for (DashboardLookupCategoryVO dashboardLookupCategoryVO : dashboardLookupCategoryVOList) {
			componentTypes.put(dashboardLookupCategoryVO.getLookupCategoryId(),dashboardLookupCategoryVO.getLookupDescription());
		}
		return componentTypes;
		
	}
	
public  String getContentForDevEnvironment(String dashletName, String fileName) throws Exception {
		
		String ftlCustomExtension = ".tgn";
		String templateDirectory = "Dashlets";
		String folderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		folderLocation = folderLocation +File.separator+templateDirectory+File.separator+dashletName;
		File directory = new File(folderLocation);
		if(!directory.exists()) {
			throw new Exception("No such directory present");
		}
		
		File selectFile = new File(folderLocation+File.separator+fileName+ftlCustomExtension);
		if(selectFile.exists()) {
			return fileUtilities.readContentsOfFile(selectFile.getAbsolutePath());
		}else {
			throw new Exception("Please download the dashlets from dashlet  listing  " + dashletName);
		}
	}
}