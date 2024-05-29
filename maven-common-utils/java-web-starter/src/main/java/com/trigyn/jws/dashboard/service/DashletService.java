package com.trigyn.jws.dashboard.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dashboard.dao.DashletDAO;
import com.trigyn.jws.dashboard.entities.Dashboard;
import com.trigyn.jws.dashboard.entities.Dashlet;
import com.trigyn.jws.dashboard.entities.DashletConfiguration;
import com.trigyn.jws.dashboard.entities.DashletConfigurationPK;
import com.trigyn.jws.dashboard.entities.DashletProperties;
import com.trigyn.jws.dashboard.entities.DashletPropertyConfiguration;
import com.trigyn.jws.dashboard.entities.DashletPropertyConfigurationPK;
import com.trigyn.jws.dashboard.repository.interfaces.IDashboardLookupCategoryRepository;
import com.trigyn.jws.dashboard.repository.interfaces.IDashboardRepository;
import com.trigyn.jws.dashboard.repository.interfaces.IDashletPropertiesRepository;
import com.trigyn.jws.dashboard.repository.interfaces.IDashletRepository;
import com.trigyn.jws.dashboard.utility.Constants;
import com.trigyn.jws.dashboard.vo.DashboardLookupCategoryVO;
import com.trigyn.jws.dashboard.vo.DashletPropertyVO;
import com.trigyn.jws.dashboard.vo.DashletVO;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.repository.AuthorizedValidatorDAO;
import com.trigyn.jws.usermanagement.security.config.UserInformation;

@Service
@Transactional
public class DashletService {

	private final static Logger logger = LogManager.getLogger(DashletService.class);

	@Autowired
	private DashletDAO dashletDAO = null;

	@Autowired
	private IDashletPropertiesRepository iDashletPropertiesRepository = null;

	@Autowired
	private IDashletRepository iDashletRepository = null;

	@Autowired
	private IDashboardLookupCategoryRepository iDashboardLookupCategoryRepository = null;

	@Autowired
	private TemplatingUtils templateEngine = null;

	@Autowired
	private DBTemplatingService templatingService = null;

	@Autowired
	private PropertyMasterDAO propertyMasterDAO = null;

	@Autowired
	private FileUtilities fileUtilities = null;

	@Autowired
	private IDashboardRepository iDashboardRepository = null;
	
	@Autowired
	private AuthorizedValidatorDAO				authorizedValidatorDAO			= null;
	
	public Dashlet getDashletById(String id) throws Exception {
		return dashletDAO.findById(id);
	}

	public List<DashletProperties> findDashletPropertyByDashletId(String dashletId, boolean includeHidden)
			throws Exception {
		return dashletDAO.findDashletPropertyByDashletId(dashletId, includeHidden);
	}

	public List<Dashlet> getDashlets(List<String> userRoleList, String dashboardId, 
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
		List<Object[]> dashlets = dashletDAO.getDashlets(userRoleList, dashboardId);
		List<Dashlet> listOfDashlets = new ArrayList<>();

		for (Object[] objects : dashlets) {
			listOfDashlets.add((Dashlet) objects[0]);
		}
		return listOfDashlets;
	}

	public List<Map<String, String>> getDashletData(String selectionQuery) throws Exception {
		return dashletDAO.getDashletData(null, selectionQuery);
	}

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

	public List<Map<String, Object>> getDashletsByContextId(String contextId) {
		return dashletDAO.getDashletsByContextId(contextId);
	}

	public String getDashletUIString(Dashlet a_dashlet, String userId, boolean isContentOnly, String dashboardId,
			String[] params, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String properties)
			throws Exception, CustomStopException {

		String selectCriteria = null;
		String htmlBody = null;
		String selectQueryFile = "selectQuery";
		String htmlBodyFile = "htmlContent";
		String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
		if (environment.equalsIgnoreCase("dev")) {
			selectCriteria = getContentForDevEnvironment(a_dashlet.getDashletName(), a_dashlet.getDashletQuery(),
					selectQueryFile);
			htmlBody = getContentForDevEnvironment(a_dashlet.getDashletName(), a_dashlet.getDashletBody(),
					htmlBodyFile);
		} else {
			selectCriteria = a_dashlet.getDashletQuery();
			htmlBody = a_dashlet.getDashletBody();
		}
		List<Map<String, String>> resultSet = null;
		Map<String, Object> templateMap = new HashMap<>();
		String templateQuery = null;
		String templateHtml = null;
		String finalTemplate = null;
		Map<String, Object> mapOfVariables = new HashMap<String, Object>();
		boolean hasAccess = hasAccessToEntity(a_dashlet);
		Map<Object, Object> usersProp = getUserPreferences(userId, a_dashlet.getDashletId(), dashboardId);
		List<Object[]> dashletCoordinates = getUserDashletCoordinates(userId, a_dashlet.getDashletId(), dashboardId);

		for (DashletProperties property : a_dashlet.getProperties()) {
			String userPreferredValue = (String) usersProp.get(property.getPropertyId());
			if (userPreferredValue != null) {
				templateMap.put(property.getPlaceholderName(), userPreferredValue);
			} else {
				templateMap.put(property.getPlaceholderName(), property.getDefaultValue());
			}
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
			if (Constants.QueryType.SELECT.getQueryType() == a_dashlet.getDaoQueryType()) {
				try {
					templateQuery = templateEngine.processTemplateContents(selectCriteria, a_dashlet.getDashletName(),
							templateMap);
					if (null != templateQuery && !templateQuery.equals("")) {

						resultSet = dashletDAO.getDashletData(a_dashlet.getDatasourceId(), templateQuery.toString());

						templateMap.put(a_dashlet.getResultVariableName(), resultSet);
					}
					if (resultSet == null) {
						isErrorOccured = Boolean.TRUE;
					}
				} catch (CustomStopException custStopException) {
					logger.error("Error occured in getDashletUIString.", custStopException);
					throw custStopException;

				} catch (Throwable a_thr) {
					logger.error(ExceptionUtils.getStackTrace(a_thr));
					httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value(), ExceptionUtils.getStackTrace(a_thr));
				}
			} else {
				try {
					TemplateVO templateVO = templatingService.getTemplateByName("script-util");
					StringBuilder resultStringBuilder = new StringBuilder();
					resultStringBuilder.append(templateVO.getTemplate()).append("\n");

					ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

					ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("nashorn");
					scriptEngine.put("requestDetails", templateMap);

					resultStringBuilder.append(selectCriteria.toString());

					Object resultMap =  scriptEngine
							.eval(resultStringBuilder.toString());
					if (resultMap != null) {
                        templateMap.put(a_dashlet.getResultVariableName(), resultMap);
                    } else {
                        templateMap.put(a_dashlet.getResultVariableName(), new HashMap<>());
                    }
					if (resultMap == null) {
						isErrorOccured = Boolean.TRUE;
					}
				} catch (CustomStopException custStopException) {
					logger.error("Error occured in getDashletUIString.", custStopException);
					throw custStopException;
				} catch (Throwable a_thr) {
					logger.error(ExceptionUtils.getStackTrace(a_thr));
					httpServletResponse.sendError(HttpStatus.BAD_REQUEST.value(), ExceptionUtils.getStackTrace(a_thr));
				}
			}
		
		} catch (Exception a_ex) {
			isErrorOccured = Boolean.TRUE;
			logger.error(a_ex);
		}
		try {
			templateHtml = templateEngine.processTemplateContents(htmlBody, a_dashlet.getDashletName(), templateMap);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in getDashletUIString.", custStopException);
			throw custStopException;
		} catch (Exception a_ex) {
			isErrorOccured = Boolean.TRUE;
			logger.error(a_ex);
		}

		if (isErrorOccured) {
			templateHtml = "label.errorInDashlet";
		}
		Dashboard dashboard = new Dashboard();
		List<DashletVO> listDashletVO = new ArrayList();
		if (!StringUtils.isBlank(dashboardId)) {
			dashboard = iDashboardRepository.findById(dashboardId).orElse(null);
			DashletVO dashletVO = new DashletVO();
			dashletVO.setDashletId(a_dashlet.getDashletId());
			dashletVO.setDashletName(a_dashlet.getDashletName());
			dashletVO.setDashletTitle(a_dashlet.getDashletTitle());
			listDashletVO.add(dashletVO);
			if (userId == "anonymous-user") {
				mapOfVariables.put("isDraggable", 0);
			} else {
				mapOfVariables.put("isDraggable", dashboard.getIsDraggable());
			}
			
			if (CollectionUtils.isEmpty(dashletCoordinates) == false && dashboard.getIsDraggable() == 1) {
				mapOfVariables.put("xCord", dashletCoordinates.get(0)[0]);
				mapOfVariables.put("yCord", dashletCoordinates.get(0)[1]);
			} else {
				mapOfVariables.put("xCord", a_dashlet.getXCoordinate());
				mapOfVariables.put("yCord", a_dashlet.getYCoordinate());
			}
			mapOfVariables.put("dashboardName", dashboard.getDashboardName());
			mapOfVariables.put("width", a_dashlet.getWidth());
			mapOfVariables.put("height", a_dashlet.getHeight());
			
			mapOfVariables.put("showHeader", a_dashlet.getShowHeader());
			mapOfVariables.put("dashboardBody", dashboard.getDashboardBody());
			for (DashletProperties property : a_dashlet.getProperties()) {
				String userPreferredValue = (String) usersProp.get(property.getPropertyId());
				if (userPreferredValue != null) {
					mapOfVariables.put(property.getPlaceholderName(), userPreferredValue);
				} else {
					mapOfVariables.put(property.getPlaceholderName(), property.getDefaultValue());
				}
			}
			mapOfVariables.put("DashletDetails", listDashletVO);
		}
		if (hasAccess == Boolean.FALSE && isContentOnly == Boolean.TRUE) {
			htmlBody = "Access denied";
			Map<String, Object> errorMap = new HashMap<>();
			String errorDiv = templatingService.getTemplateByName("error-page").getTemplate();
			errorMap.put("template-body", errorDiv);
			errorMap.put("statusCode", 403);
			errorMap.put("errorMessage", "You do not have enough privilege to access this module.");
			templateHtml = templateEngine.processTemplateContents(errorDiv, a_dashlet.getDashletName(), errorMap);
			if (isContentOnly) {
				return templateHtml;
			}
			mapOfVariables.put("content", templateHtml);
		} else {
			if (isContentOnly) {
				return templateHtml;
			}
			mapOfVariables.put("content", templateHtml);
		}
		String commonDashletDiv = null;

		TemplateVO template = templatingService.getTemplateByName("dashlet-common-div");

		if (template != null) {
			Map<String, Object> templateDetails = getDashletConfigDetails(userId, dashboardId,
					a_dashlet.getDashletId());
			TemplateVO filterTemplate = templatingService.getTemplateByName("dashlet-configuration");
			if(filterTemplate != null) {
				templateEngine.processTemplateContents(filterTemplate.getTemplate(), filterTemplate.getTemplateName(),
						templateDetails);
			}else {
				return null;
			}
		    commonDashletDiv = template.getTemplate();
		    finalTemplate = templateEngine.processTemplateContents(commonDashletDiv, a_dashlet.getDashletName(),
					mapOfVariables);
		} 
		return finalTemplate;
	}


	private HttpServletRequest getRequest() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return sra.getRequest();
	}

	boolean hasAccessToEntity(Dashlet dashlet) {

		List<String> roleNames = new ArrayList<>();
		Authentication authentication = null;
		HttpServletRequest requestObject = getRequest();
		if (requestObject.getSession().getAttribute("SPRING_SECURITY_CONTEXT") != null) {
			authentication = ((SecurityContextImpl) requestObject.getSession().getAttribute("SPRING_SECURITY_CONTEXT"))
					.getAuthentication();
		}
		if (authentication == null) {
			authentication = SecurityContextHolder.getContext().getAuthentication();
		}
		if (authentication == null || (authentication instanceof AnonymousAuthenticationToken)) {

			roleNames.add(com.trigyn.jws.usermanagement.utils.Constants.ANONYMOUS_ROLE_NAME);

		} else {
			UserInformation userInformation = (UserInformation) authentication.getPrincipal();
			roleNames.addAll(userInformation.getRoles());
		}

		boolean hasAccess = false;
		String dashletId = dashlet.getDashletId();
		Long count = authorizedValidatorDAO.hasAccessToDashlet(dashletId, roleNames);
		if (count > 0) {
			hasAccess = true;
		}
		return hasAccess;
	}

	public Map<Object, Object> getUserPreferences(String userId, String dashletId, String dashboardId)
			throws Exception {
		List<Object[]> preferences = dashletDAO.getUserPreferences(userId, dashletId, dashboardId);
		Map<Object, Object> userPreferences = new HashMap<Object, Object>();
		for (Object[] preference : preferences) {
			userPreferences.put(preference[0], preference[1]);
		}
		return userPreferences;
	}
	public List<Object[]> getUserDashletCoordinates(String userId, String dashletId, String dashboardId)
			throws Exception {
		return dashletDAO.getUserDashletCoordinates(userId, dashletId, dashboardId);
	}
	
	public String refreshDashletContent(String userId, String dashletId, String[] paramArray, String dashboardId,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String properties)
			throws Exception {
		Dashlet dashlet = dashletDAO.findById(dashletId);
		if (null != properties) {
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> map = objectMapper.readValue(properties, Map.class);
			MultiValueMap<String, String> propertyMap = new LinkedMultiValueMap<String, String>();

			for (DashletProperties property : dashlet.getProperties()) {
				for (Entry<String, Object> entry : map.entrySet()) {
					String type = property.getType().toString();
					switch (type) {
					case Constants.NUMBER:
						if (property.getPlaceholderName().equalsIgnoreCase(entry.getKey().toString())) {
							validateNumberValues(property, map, propertyMap,httpServletResponse);
							break;
						}
					case Constants.DECIMAL:
						if (property.getPlaceholderName().equalsIgnoreCase(entry.getKey().toString())) {
							validateDecimalValues(property, map, propertyMap,httpServletResponse);
							break;
						}
					case Constants.RANGESLIDER:
						if (property.getPlaceholderName().equalsIgnoreCase(entry.getKey().toString())) {
							validateRangeSliderValues(property, map, propertyMap,httpServletResponse);
							break;
						}
					case Constants.TEXT:
						if (property.getPlaceholderName().equalsIgnoreCase(entry.getKey().toString())) {
							validateTextValues(property, map, propertyMap,httpServletResponse);
							break;
						}
					default:
						break;
					}
				}
			}
									
			saveConfiguration(propertyMap, userId, dashboardId);
		}
		return getDashletUIString(dashlet, userId, Boolean.TRUE, dashboardId, paramArray, httpServletRequest,
				httpServletResponse, properties);
	}
	
	private void validateNumberValues(DashletProperties property, Map<String, Object> map,
	        MultiValueMap<String, String> propertyMap,HttpServletResponse httpServletResponse) throws IOException {
	    Pattern numberPattern = Pattern.compile("^[0-9]+$");

	    for (Entry<String, Object> entry : map.entrySet()) {
	        if (numberPattern.matcher(entry.getValue().toString()).matches()) {
	            propertyMap.add(property.getPropertyId(), entry.getValue().toString());
	        } else {
	        	httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(),
						"Invalid input for  " + " Number " + " : " + entry.getValue());
	            logger.error("Invalid value for property type" + "Number" + " : " + entry.getValue());
	        }
	    }
	}

	private void validateDecimalValues(DashletProperties property, Map<String, Object> map,
	        MultiValueMap<String, String> propertyMap,HttpServletResponse httpServletResponse) throws IOException {
	    Pattern decimalPattern = Pattern.compile("^(0|\\d+)\\.?\\d{0,2}$");

	    for (Entry<String, Object> entry : map.entrySet()) {
	        if (decimalPattern.matcher(entry.getValue().toString()).matches()) {
	            propertyMap.add(property.getPropertyId(), entry.getValue().toString());
	        } else {
	        	httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(),
						"Invalid input for  " + " Decimal " + " : " + entry.getValue());
	            logger.error("Invalid value for property type" + "Decimal" + " : " + entry.getValue());
	        }
	    }
	}
	
	private void validateRangeSliderValues(DashletProperties property, Map<String, Object> map,
	        MultiValueMap<String, String> propertyMap,HttpServletResponse httpServletResponse) throws IOException {
	    Pattern numberPattern = Pattern.compile("^\\d+$");
	    for (Entry<String, Object> entry : map.entrySet()) {
	        if (numberPattern.matcher(entry.getValue().toString()).matches()) {
	            propertyMap.add(property.getPropertyId(), entry.getValue().toString());
	        } else {
	        	httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(),
						"Invalid input for  " + " Rangeslider " + " : " + entry.getValue());
	            logger.error("Invalid value for property type" + "Rangeslider" + " : " + entry.getValue());
	        }
	    }
	}
	
	private void validateTextValues(DashletProperties property, Map<String, Object> map,
	        MultiValueMap<String, String> propertyMap,HttpServletResponse httpServletResponse) throws IOException {
	    String regexPattern = "^[a-zA-Z\\s]*$"; 
	    for (Entry<String, Object> entry : map.entrySet()) {
	        String textValue = entry.getValue().toString();
	        if (textValue.matches(regexPattern)) {
	            propertyMap.add(property.getPropertyId(), textValue);
	        } else {
	        	httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(),
						"Invalid input for " + " Text " + " : " + entry.getValue());
	            logger.error("Invalid text format for property type" + "Text" + " : " + textValue);
	        }
	    }
	}

	@Transactional(readOnly = true)
	public DashletVO getDashletDetailsById(String dashletId) throws Exception {
		DashletVO dashletVO = new DashletVO();
		try {
			if (dashletId != null && !dashletId.equals("") && !dashletId.isEmpty()) {
				dashletVO = findDashletByDashletId(dashletId);
				StringBuilder dashletBody = new StringBuilder(dashletVO.getDashletBody());
				StringBuilder dashletQuery = new StringBuilder(dashletVO.getDashletQuery());
				dashletVO.setDashletBody(dashletBody.toString());
				dashletVO.setDashletQuery(dashletQuery.toString());
				List<DashletPropertyVO> dashletPropertyVOList = getDashletPropertyByDashletId(dashletId);
				List<String> dashletRoleIdList = findDashletRolesByDashletId(dashletId);
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
		} catch (Exception a_exception) {
			logger.error("Error occurred while fetching dashlet details" + a_exception);
			throw new RuntimeException("Error occurred while fetching dashlet details");
		}
	}

	public DashletVO findDashletByDashletId(String dashletId) throws Exception {
		return iDashletRepository.findDashletByDashletId(dashletId);
	}

	public List<DashletPropertyVO> getDashletPropertyByDashletId(String dashletId) throws Exception {
		return iDashletPropertiesRepository.findDashletPropertyByDashletId(dashletId,
				Constants.RecordStatus.INSERTED.getStatus());
	}

	public List<String> findDashletRolesByDashletId(String dashletId) throws Exception {
		return dashletDAO.findDashletRoleByDashletId(dashletId);
	}

	public Map<String, String> findComponentTypes(String componentName) throws Exception {
		Map<String, String> componentTypes = new HashMap<String, String>();
		List<DashboardLookupCategoryVO> dashboardLookupCategoryVOList = iDashboardLookupCategoryRepository
				.findDashboardLookupCategoryByName(componentName);
		for (DashboardLookupCategoryVO dashboardLookupCategoryVO : dashboardLookupCategoryVOList) {
			componentTypes.put(dashboardLookupCategoryVO.getLookupCategoryId(),
					dashboardLookupCategoryVO.getLookupDescription());
		}
		return componentTypes;

	}
	
	public Map<String, String> findComponentValidation(String type) throws Exception {
		Map<String, String> validation = new HashMap<String, String>();
		List<DashboardLookupCategoryVO> dashboardLookupCategoryVOList = iDashboardLookupCategoryRepository
				.findDashboardLookupCategoryByType(type);
		for (DashboardLookupCategoryVO dashboardLookupCategoryVO : dashboardLookupCategoryVOList) {
			validation.put(dashboardLookupCategoryVO.getLookupCategoryId(),
					dashboardLookupCategoryVO.getValidation());
		}
		return validation;

	}

	public Map<String, Object> getDashletConfigDetails(String userId, String dashboardId, String dashletId)
			throws Exception {
		List<DashletProperties> properties = findDashletPropertyByDashletId(dashletId, false);
		Map<String, Object> templateDetails = new HashMap<>();
		templateDetails.put("properties", properties);
		templateDetails.put("dashboardId", dashboardId);

		if (Boolean.FALSE.equals(properties.isEmpty())) {
			List<DashletPropertyVO> dashletPropertyVOs = iDashletPropertiesRepository
					.getDashletPropertyDetailsById(dashletId, userId, Constants.RecordStatus.INSERTED.getStatus());

			templateDetails.put("dashletPropertyVOs", dashletPropertyVOs);
		}
		return templateDetails;
	}

	public String getContentForDevEnvironment(String dashletName, String dbContent, String fileName) throws Exception {

		String ftlCustomExtension = ".tgn";
		String templateDirectory = "Dashlets";
		String folderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		folderLocation = folderLocation + File.separator + templateDirectory + File.separator + dashletName;
		File directory = new File(folderLocation);
		if (!directory.exists()) {
			return dbContent;
		}

		File selectFile = new File(folderLocation + File.separator + fileName + ftlCustomExtension);
		if (selectFile.exists()) {
			return fileUtilities.readContentsOfFile(selectFile.getAbsolutePath());
		} else {
			return dbContent;
		}
	}
}
