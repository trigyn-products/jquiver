package com.trigyn.jws.templating.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.trigyn.jws.dbutils.entities.ModuleListing;
import com.trigyn.jws.dbutils.entities.ModuleListingI18n;
import com.trigyn.jws.dbutils.entities.ModuleListingI18nPK;
import com.trigyn.jws.dbutils.entities.ModuleRoleAssociation;
import com.trigyn.jws.dbutils.repository.IModuleListingI18nRepository;
import com.trigyn.jws.dbutils.repository.IModuleListingRepository;
import com.trigyn.jws.dbutils.repository.IModuleRoleAssociationRepository;
import com.trigyn.jws.dbutils.repository.IModuleTargetLookupRepository;
import com.trigyn.jws.dbutils.repository.ModuleDAO;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dbutils.vo.ModuleDetailsVO;
import com.trigyn.jws.dbutils.vo.ModuleTargetLookupVO;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.usermanagement.entities.JwsRole;
import com.trigyn.jws.usermanagement.repository.JwsRoleRepository;

@Service
@Transactional(readOnly = false)
public class ModuleService {

	@Autowired
	private IModuleListingRepository iModuleListingRepository = null;

	@Autowired
	private IModuleListingI18nRepository iModuleListingI18nRepository = null;

	@Autowired
	private IModuleTargetLookupRepository iModuleTargetLookupRepository = null;

	@Autowired
	private ModuleDAO moduleDAO = null;

	@Autowired
	private JwsRoleRepository userRoleRepository = null;

	@Autowired
	private PropertyMasterService propertyMasterService = null;

	@Autowired
	private IUserDetailsService detailsService = null;

	@Autowired
	private ActivityLog activitylog = null;

	@Autowired
	private IModuleRoleAssociationRepository roleAssociationRepository = null;

	public ModuleDetailsVO getModuleDetails(String moduleId) throws Exception {
		if (!StringUtils.isBlank(moduleId)) {
			ModuleDetailsVO moduleDetailsVO = iModuleListingRepository.getModuleDetails(moduleId,
					Constant.DEFAULT_LANGUAGE_ID, Constant.DEFAULT_LANGUAGE_ID);

			if (moduleDetailsVO != null) {
				Integer targetLookupId = moduleDetailsVO.getTargetLookupId();
				if (targetLookupId != null && !targetLookupId.equals(Constant.MODULE_GROUP_ID)) {
					Map<String, Object> moduleDetailsMap = getContextNameDetailsByType(moduleDetailsVO);
					moduleDetailsVO.setTargetLookupName(moduleDetailsMap.get("targetTypeName").toString());
				}
			}
			return moduleDetailsVO;
		}
		return null;
	}

	public List<ModuleDetailsVO> getAllMenuModules() throws Exception {
		UserDetailsVO detailsVO = detailsService.getUserDetails();
		List<String> roleIdList = detailsVO.getRoleIdList();

		if (roleIdList.contains("ADMIN")) {
			return iModuleListingRepository.getAllModulesDetails(Constant.DEFAULT_LANGUAGE_ID,
					Constant.DEFAULT_LANGUAGE_ID);
		} else {
			return iModuleListingRepository.getRoleSpecificModulesDetails(Constant.DEFAULT_LANGUAGE_ID,
					Constant.DEFAULT_LANGUAGE_ID, roleIdList, "c6cc466a-0ed3-11eb-94b2-f48e38ab9348");
		}
	}

	public List<ModuleDetailsVO> getAllParentModules(String moduleId) throws Exception {
		List<ModuleDetailsVO> parentModulesList = new ArrayList<>();
		List<ModuleDetailsVO> parentModuleVOs = new ArrayList<>();
		parentModulesList = iModuleListingRepository.getAllParentModules(Constant.IS_NOT_HOME_PAGE,
				Constant.DEFAULT_LANGUAGE_ID, Constant.DEFAULT_LANGUAGE_ID, Constant.IS_INSIDE_MENU);
		if (!StringUtils.isBlank(moduleId)) {
			for (ModuleDetailsVO moduleDetailsVO : parentModulesList) {
				if (!moduleDetailsVO.getModuleId().equals(moduleId)) {
					parentModuleVOs.add(moduleDetailsVO);
				}
			}
			return parentModuleVOs;
		}
		return parentModulesList;
	}

	public List<ModuleTargetLookupVO> getAllModuleLookUp() throws Exception {
		return iModuleTargetLookupRepository.getAllModuleTargetLookup();
	}

	public List<JwsRole> getAllUserRoles() throws Exception {
		return userRoleRepository.findAllRoles();
	}

	public String saveModuleDetails(ModuleDetailsVO moduleDetailsVO) throws Exception {
		ModuleListing moduleListing = convertModuleVOToEntitity(moduleDetailsVO);
		ModuleListingI18n moduleListingI18n = convertModuleVOToI18nEntitity(moduleDetailsVO);
		moduleListing.setLastUpdatedBy(detailsService.getUserDetails().getFullName());
		moduleListing = iModuleListingRepository.save(moduleListing);
		ModuleListingI18nPK moduleListingI18nPK = moduleListingI18n.getId();
		moduleListingI18nPK.setModuleId(moduleListing.getModuleId());
		iModuleListingI18nRepository.save(moduleListingI18n);
		/* Method called for implementing Activity Log */
		logActivity(moduleListing.getModuleTypeId(), moduleDetailsVO.getModuleName(), moduleDetailsVO.getModuleId());
		return moduleListing.getModuleId();
	}

	/**
	 * Purpose of this method is to log activities</br>
	 * in Site Layout Module.
	 * 
	 * @author Bibhusrita.Nayak
	 * @param entityName
	 * @param typeSelect
	 * @param moduleId
	 * @throws Exception
	 */
	private void logActivity(Integer typeSelect, String entityName, String moduleId) throws Exception {
		Date activityTimestamp = new Date();
		Map<String, String> requestParams = new HashMap<>();
		UserDetailsVO detailsVO = detailsService.getUserDetails();
		String action = "";
		if (!StringUtils.isBlank(moduleId)) {
			action = Constant.Action.EDIT.getAction();
			if (typeSelect == Constant.Changetype.CUSTOM.getChangeTypeInt()) {
				requestParams.put("typeSelect", Constant.Changetype.CUSTOM.getChangetype());
			} else {
				requestParams.put("typeSelect", Constant.Changetype.SYSTEM.getChangetype());
			}
		} else {
			action = Constant.Action.ADD.getAction();
			requestParams.put("typeSelect", Constant.Changetype.CUSTOM.getChangetype());
		}
		requestParams.put("entityName", entityName);
		requestParams.put("masterModuleType", Constant.MasterModuleType.SITELAYOUT.getModuleType());
		requestParams.put("userName", detailsVO.getUserName());
		requestParams.put("message", "");
		requestParams.put("date", activityTimestamp.toString());
		requestParams.put("action", action);
		activitylog.activitylog(requestParams);
	}

	public Map<String, Object> getExistingModuleData(String moduleId, String moduleName, String parentModuleId,
			Integer sequence, String moduleURL) throws Exception {
		Map<String, Object> moduleDetailsMap = new HashMap<>();
		String moduleIdDB = getModuleIdByName(moduleName, Constant.DEFAULT_LANGUAGE_ID, Constant.DEFAULT_LANGUAGE_ID);
		if (!StringUtils.isBlank(moduleIdDB)) {
			moduleDetailsMap.put("moduleIdName", moduleIdDB);
		}
		moduleIdDB = getModuleIdBySequence(parentModuleId, sequence);
		if (!StringUtils.isBlank(moduleIdDB)) {
			moduleDetailsMap.put("moduleIdSequence", moduleIdDB);
		}

		if (!StringUtils.isBlank(moduleURL) && !moduleURL.equals(Constant.GROUP_MODULE_URL)) {
			StringBuilder newURL = new StringBuilder();
			List<String> pathVariableList = new ArrayList<>();
			if (moduleURL.indexOf("/**") != -1) {
				moduleURL = moduleURL.substring(0, moduleURL.lastIndexOf("/**"));
			}

			if (moduleURL.indexOf("/") != -1) {
				pathVariableList = Stream.of(moduleURL.split("/")).map(urlElement -> new String(urlElement))
						.collect(Collectors.toList());
			}

			List<ModuleDetailsVO> moduleDetailsVOList = getAllModuleId(moduleId);
			if (CollectionUtils.isEmpty(pathVariableList) == false) {
				for (String pathVariable : pathVariableList) {
					newURL = new StringBuilder(pathVariable);
					for (ModuleDetailsVO moduleDetailsVO : moduleDetailsVOList) {
						if ((moduleDetailsVO.getModuleId() == moduleId 
								&& moduleDetailsVO.getModuleUrl().matches(".*\\b" + newURL + "\\b.*"))
								|| moduleDetailsVO.getModuleUrl().equals(moduleURL)) {
							moduleIdDB = moduleDetailsVO.getModuleId();
						}
					}
				}
			} else {
				List<ModuleDetailsVO> moduleDetailsVOs = getAllModuleId(moduleId);
				for (ModuleDetailsVO moduleDetailsVO : moduleDetailsVOs) {
					if ((moduleDetailsVO.getModuleId() == moduleId 
							&& moduleDetailsVO.getModuleUrl().matches(".*\\b" + moduleURL + "\\b.*"))
							|| moduleDetailsVO.getModuleUrl().equals(moduleURL)) {
						moduleIdDB = moduleDetailsVO.getModuleId();
					}
				}
			}

			// moduleId = getModuleIdByURL(moduleURL);
			if (!StringUtils.isBlank(moduleIdDB)) {
				moduleDetailsMap.put("moduleIdURL", moduleIdDB);
			}
		}else {
			List<ModuleDetailsVO> moduleDetailsVOList = iModuleListingRepository.getTargetTypeURL(moduleURL);
			String maxModuleUrl = "#";
			boolean moduleUrlExist = false;
			if(moduleDetailsVOList.size()>0) {
				for (ModuleDetailsVO moduleDetailsVO : moduleDetailsVOList) {
					if(moduleDetailsVO.getModuleUrl().length() >= maxModuleUrl.length() && moduleDetailsVO.getModuleUrl().matches("#+")) {
						maxModuleUrl = moduleDetailsVO.getModuleUrl();
						moduleUrlExist = true;
					}
				}
			}
			// Below code is used append # for root module for first time.
			if(maxModuleUrl.length() == 1 && moduleUrlExist == false) {
				maxModuleUrl = "";
			}
			moduleDetailsMap.put("parentModuleURL", maxModuleUrl);	
		}
		return moduleDetailsMap;
	}

	public String getModuleIdByName(String moduleName, Integer languageId, Integer defaultLanguageId) throws Exception {
		return iModuleListingRepository.getModuleIdByName(moduleName, languageId, defaultLanguageId);
	}

	public String getModuleIdBySequence(String parentModuleId, Integer sequence) throws Exception {
		if (!StringUtils.isBlank(parentModuleId)) {
			return iModuleListingRepository.getParentModuleIdBySequence(parentModuleId, sequence);
		} else {
			return iModuleListingRepository.getModuleIdBySequence(sequence);
		}

	}

	public List<ModuleDetailsVO> getAllModuleId(String moduleId) throws Exception {
		return iModuleListingRepository.getAllModuleId(moduleId);
	}

	public List<ModuleDetailsVO> getModuleIdByURL(String moduleUrl, String moduleId) throws Exception {
		return iModuleListingRepository.getModuleIdByURL(moduleUrl, moduleId);
	}

	private ModuleListing convertModuleVOToEntitity(ModuleDetailsVO moduleDetailsVO) {
		ModuleListing moduleListing = new ModuleListing();

		if (!StringUtils.isBlank(moduleDetailsVO.getModuleId())) {
			moduleListing = iModuleListingRepository.getModuleListing(moduleDetailsVO.getModuleId());
			// moduleListing.setModuleId(moduleDetailsVO.getModuleId());

		}

		if (!StringUtils.isBlank(moduleDetailsVO.getParentModuleId())) {
			moduleListing.setParentId(moduleDetailsVO.getParentModuleId());
		} else {
			moduleListing.setParentId(null);
		}

		if (moduleDetailsVO.getIsInsideMenu().equals(Constant.IS_INSIDE_MENU)) {
			moduleListing.setIsInsideMenu(moduleDetailsVO.getIsInsideMenu());
		} else {
			moduleListing.setIsInsideMenu(Constant.IS_NOT_INSIDE_MENU);
		}

		if (moduleDetailsVO.getIncludeLayout().equals(Constant.EXCLUDE_LAYOUT)) {
			moduleListing.setIncludeLayout(moduleDetailsVO.getIncludeLayout());
		} else {
			moduleListing.setIncludeLayout(Constant.INCLUDE_LAYOUT);
		}

		if (!StringUtils.isBlank(moduleDetailsVO.getHeaderJson())) {
			moduleListing.setHeaderJson(moduleDetailsVO.getHeaderJson());
		}

		if (!StringUtils.isBlank(moduleDetailsVO.getRequestParamJson())) {
			moduleListing.setRequestParamJson(moduleDetailsVO.getRequestParamJson());
		}
		moduleListing.setSequence(moduleDetailsVO.getSequence());
		moduleListing.setModuleUrl(moduleDetailsVO.getModuleUrl());
		moduleListing.setOpenInNewTab(moduleDetailsVO.getOpenInNewTab());
		moduleListing.setMenuStyle(moduleDetailsVO.getMenuStyle());
		moduleListing.setIsCustomUpdated(1);

		if (!StringUtils.isBlank(moduleDetailsVO.getModuleId())) {
			Optional<ModuleListing> savedModuleOptional = iModuleListingRepository
					.findById(moduleDetailsVO.getModuleId());
			if (savedModuleOptional.isPresent() == true) {
				ModuleListing savedModuleDetails = savedModuleOptional.get();
				moduleListing.setIsHomePage(savedModuleDetails.getIsHomePage());
			} else {
				moduleListing.setIsHomePage(Constant.IS_NOT_HOME_PAGE);
			}
		} else {
			moduleListing.setIsHomePage(Constant.IS_NOT_HOME_PAGE);
		}

		moduleListing.setTargetLookupId(moduleDetailsVO.getTargetLookupId());
		moduleListing.setTargetTypeId(moduleDetailsVO.getTargetTypeId());
		moduleListing.setUpdatedDate(new Date());
		return moduleListing;
	}

	private ModuleListingI18n convertModuleVOToI18nEntitity(ModuleDetailsVO moduleDetailsVO) {
		ModuleListingI18n moduleListingI18n = new ModuleListingI18n();
		ModuleListingI18nPK moduleListingI18nPK = new ModuleListingI18nPK();
		if (moduleDetailsVO.getModuleId() != null && !moduleDetailsVO.getModuleId().isBlank()
				&& !moduleDetailsVO.getModuleId().isEmpty()) {
			moduleListingI18nPK.setModuleId(moduleDetailsVO.getModuleId());
		}
		moduleListingI18nPK.setLanguageId(Constant.DEFAULT_LANGUAGE_ID);
		moduleListingI18n.setId(moduleListingI18nPK);
		moduleListingI18n.setModuleName(moduleDetailsVO.getModuleName());
		return moduleListingI18n;
	}

	public ModuleDetailsVO convertModuleEntityToVO(ModuleListing moduleListing) {
		ModuleDetailsVO vo = new ModuleDetailsVO();
		vo.setIsInsideMenu(moduleListing.getIsInsideMenu());
		vo.setModuleId(moduleListing.getModuleId());
		vo.setModuleUrl(moduleListing.getModuleUrl());
		vo.setParentId(moduleListing.getParentId());

		List<String> roleIdList = new ArrayList<>();
		if (moduleListing.getModuleRoleAssociations() != null) {
			for (ModuleRoleAssociation mra : moduleListing.getModuleRoleAssociations()) {
				roleIdList.add(mra.getRoleId());
			}
		}
		vo.setSequence(moduleListing.getSequence());
		vo.setTargetLookupId(moduleListing.getTargetLookupId());
		vo.setTargetTypeId(moduleListing.getTargetTypeId());
		vo.setHeaderJson(moduleListing.getHeaderJson());
		vo.setIncludeLayout(moduleListing.getIncludeLayout());
		vo.setIsHomePage(moduleListing.getIsHomePage());
		vo.setMenuStyle(moduleListing.getMenuStyle());
		vo.setOpenInNewTab(moduleListing.getOpenInNewTab());
		return vo;
	}

	public List<Map<String, Object>> getTargetTypes(Integer targetLookupId, String targetTypeId) throws Exception {
		List<Map<String, Object>> targetTypeMapList = new ArrayList<>();
		if (targetLookupId != null) {
			targetTypeMapList = moduleDAO.findTargetTypeDetails(targetLookupId, targetTypeId);
		}
		return targetTypeMapList;
	}

	public Map<String, Object> getModuleTargetByURL(String moduleURL) throws Exception {
		ModuleDetailsVO moduleDetailsVO = iModuleListingRepository.getTargetTypeByURL(moduleURL);
		Map<String, Object> moduleDetailsMap = getContextNameDetailsByType(moduleDetailsVO);
		return moduleDetailsMap;
	}

	private Map<String, Object> getContextNameDetailsByType(ModuleDetailsVO moduleDetailsVO) throws Exception {
		Map<String, Object> moduleDetailsMap = new HashMap<>();
		if (moduleDetailsVO != null) {
			List<Map<String, Object>> targetTypeList = moduleDAO
					.findTargetTypeDetails(moduleDetailsVO.getTargetLookupId(), moduleDetailsVO.getTargetTypeId());
			moduleDetailsMap.put("targetLookupId", moduleDetailsVO.getTargetLookupId());
			if (!CollectionUtils.isEmpty(targetTypeList)) {
				moduleDetailsMap.put("includeLayout", moduleDetailsVO.getIncludeLayout());
				moduleDetailsMap.put("headerJson", moduleDetailsVO.getHeaderJson());
				moduleDetailsMap.put("requestParamJson", moduleDetailsVO.getRequestParamJson());
				moduleDetailsMap.put("targetTypeId", targetTypeList.get(0).get("targetTypeId"));
				moduleDetailsMap.put("targetTypeName", targetTypeList.get(0).get("targetTypeName"));
			}
		}
		return moduleDetailsMap;
	}

	public List<Map<String, Object>> getModuleTargetTypeURL(String moduleURL) throws Exception {
		List<ModuleDetailsVO> moduleDetailsVOList = iModuleListingRepository.getTargetTypeURL(moduleURL);
		List<Map<String, Object>> moduleDetailsList = getContextNameDetails(moduleDetailsVOList);
		return moduleDetailsList;
	}

	private List<Map<String, Object>> getContextNameDetails(List<ModuleDetailsVO> moduleDetailsVOList)
			throws Exception {
		List<Map<String, Object>> moduleDetailsList = new ArrayList<>();
		if (CollectionUtils.isEmpty(moduleDetailsVOList) == false) {

			for (ModuleDetailsVO moduleDetailsVO : moduleDetailsVOList) {
				Map<String, Object> moduleDetailsMap = new HashMap<>();
				List<Map<String, Object>> targetTypeList = moduleDAO
						.findTargetTypeDetails(moduleDetailsVO.getTargetLookupId(), moduleDetailsVO.getTargetTypeId());
				moduleDetailsMap.put("targetLookupId", moduleDetailsVO.getTargetLookupId());
				if (!CollectionUtils.isEmpty(targetTypeList)) {
					moduleDetailsMap.put("moduleUrl", moduleDetailsVO.getModuleUrl());
					moduleDetailsMap.put("headerJson", moduleDetailsVO.getHeaderJson());
					moduleDetailsMap.put("requestParamJson", moduleDetailsVO.getRequestParamJson());
					moduleDetailsMap.put("includeLayout", moduleDetailsVO.getIncludeLayout());
					moduleDetailsMap.put("targetTypeId", targetTypeList.get(0).get("targetTypeId"));
					moduleDetailsMap.put("targetTypeName", targetTypeList.get(0).get("targetTypeName"));
				}
				moduleDetailsList.add(moduleDetailsMap);

			} // End of forLoop
		}
		return moduleDetailsList;
	}

	public Integer getModuleMaxSequence() throws Exception {
		Integer defaultSequence = moduleDAO.getModuleMaxSequence();
		if (defaultSequence != null) {
			defaultSequence = defaultSequence + 1;
		} else {
			defaultSequence = Constant.DEFAULT_SEQUENCE_NUMBER;
		}
		return defaultSequence;
	}

	public Integer getMaxSequenceByParent(String parentModuleId) throws Exception {
		Integer defaultSequence = moduleDAO.getMaxSequenceByParent(parentModuleId);
		if (defaultSequence != null) {
			defaultSequence = defaultSequence + 1;
		} else {
			defaultSequence = Constant.DEFAULT_SEQUENCE_NUMBER;
		}
		return defaultSequence;
	}

	public String saveConfigHomePage(HttpServletRequest a_httHttpServletRequest) {
		String moduleId = a_httHttpServletRequest.getParameter("moduleId");
		String oldModuleId = a_httHttpServletRequest.getParameter("oldModuleId");

		ModuleListing moduleListing = new ModuleListing();
		ModuleListing oldModuleListing = new ModuleListing();

		if (StringUtils.isBlank(moduleId) == false) {
			moduleListing = iModuleListingRepository.getModuleListing(moduleId);
			moduleListing.setIsHomePage(Constant.IS_HOME_PAGE);
			moduleListing.setIsCustomUpdated(1);
			iModuleListingRepository.saveAndFlush(moduleListing);
		}
		if (StringUtils.isBlank(oldModuleId) == false && oldModuleId.equals(moduleId) == false) {
			oldModuleListing = iModuleListingRepository.getModuleListingByRole(oldModuleId);
			if (oldModuleListing != null) {
				oldModuleListing.setIsCustomUpdated(1);
				oldModuleListing.setIsHomePage(Constant.IS_NOT_HOME_PAGE);
				iModuleListingRepository.saveAndFlush(oldModuleListing);
			}
		}

		return moduleListing.getModuleId();
	}

	public void saveModuleRoleAssociation(HttpServletRequest a_httHttpServletRequest) {
		String moduleId = a_httHttpServletRequest.getParameter("moduleId");
		String oldModuleId = a_httHttpServletRequest.getParameter("oldModuleId");
		UserDetailsVO detailsVO = detailsService.getUserDetails();
		String loggedInUserId = detailsVO.getUserName();
		Date date = new Date();
		String roleId = a_httHttpServletRequest.getParameter("roleId");
		ModuleRoleAssociation moduleRoleAssociation = new ModuleRoleAssociation();
		if (StringUtils.isBlank(roleId) == false
				&& (StringUtils.isBlank(moduleId) == false || StringUtils.isBlank(oldModuleId) == false)) {
			moduleRoleAssociation.setRoleId(roleId);
			moduleRoleAssociation.setUpdatedBy(loggedInUserId);
			moduleRoleAssociation.setUpdatedDate(date);
			if (StringUtils.isBlank(moduleId) == false) {
				moduleRoleAssociation.setModuleId(moduleId);
				moduleRoleAssociation.setIsDeleted(Constant.RecordStatus.INSERTED.getStatus());
			} else {
				moduleRoleAssociation.setModuleId(oldModuleId);
				moduleRoleAssociation.setIsDeleted(Constant.RecordStatus.DELETED.getStatus());
			}
			roleAssociationRepository.save(moduleRoleAssociation);
		}
	}

	public String findModuleIdByRoleId(String roleId, String moduleId) throws Exception {
		String mouduleIdDB = roleAssociationRepository.findModuleIdByRoleId(roleId, moduleId);
		return mouduleIdDB;
	}

	public ModuleListing getModuleListing(String moduleId) throws Exception {
		return iModuleListingRepository.getModuleListing(moduleId);
	}

	public void saveModuleListing(ModuleListing moduleListing) throws Exception {
		
		moduleListing.setIsCustomUpdated(1);
		moduleListing.getModuleUrl();
		iModuleListingRepository.save(moduleListing);
	}

	public String getModuleListingJson(String entityId) throws Exception {
		ModuleListing module = getModuleListing(entityId);
		String jsonString = "";
		if (module != null) {
			module = module.getObject();

			ModuleDetailsVO vo = convertModuleEntityToVO(module);
			
			Gson gson = new Gson();
			ObjectMapper objectMapper = new ObjectMapper();
			String dbDateFormat = propertyMasterService.getDateFormatByName(Constant.PROPERTY_MASTER_OWNER_TYPE,
					Constant.PROPERTY_MASTER_OWNER_ID, Constant.JWS_DATE_FORMAT_PROPERTY_NAME,
					com.trigyn.jws.dbutils.utils.Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);
			DateFormat dateFormat = new SimpleDateFormat(dbDateFormat);
			objectMapper.setDateFormat(dateFormat);
			Map<String, Object> objectMap = objectMapper.convertValue(vo, TreeMap.class);
			jsonString = gson.toJson(objectMap);

		}
		return jsonString;
	}
	
	public String getServerBaseURL(HttpServletRequest a_httpServletRequest) throws Exception {
		String	baseURL	= propertyMasterService.getDateFormatByName(Constant.PROPERTY_MASTER_OWNER_TYPE,
				Constant.PROPERTY_MASTER_OWNER_ID, Constant.JWS_DATE_FORMAT_PROPERTY_NAME,
				"base-url");
		if (StringUtils.isBlank(baseURL) == false && a_httpServletRequest.getContextPath().isBlank() == false) {
			baseURL = baseURL + a_httpServletRequest.getContextPath();
		}
		return baseURL;
	}
}
