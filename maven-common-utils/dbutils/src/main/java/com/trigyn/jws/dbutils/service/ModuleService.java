package com.trigyn.jws.dbutils.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.trigyn.jws.dbutils.entities.ModuleListing;
import com.trigyn.jws.dbutils.entities.ModuleListingI18n;
import com.trigyn.jws.dbutils.entities.ModuleListingI18nPK;
import com.trigyn.jws.dbutils.repository.IModuleListingI18nRepository;
import com.trigyn.jws.dbutils.repository.IModuleListingRepository;
import com.trigyn.jws.dbutils.repository.IModuleTargetLookupRepository;
import com.trigyn.jws.dbutils.repository.ModuleDAO;
import com.trigyn.jws.dbutils.repository.UserRoleRepository;
import com.trigyn.jws.dbutils.utils.Constant;
import com.trigyn.jws.dbutils.vo.ModuleDetailsVO;
import com.trigyn.jws.dbutils.vo.ModuleTargetLookupVO;
import com.trigyn.jws.dbutils.vo.UserRoleVO;

@Service
@Transactional(readOnly = false)
public class ModuleService {

	@Autowired
	private IModuleListingRepository iModuleListingRepository 			= null;
	
	@Autowired
	private IModuleListingI18nRepository iModuleListingI18nRepository 	= null;
	
	@Autowired
	private IModuleTargetLookupRepository iModuleTargetLookupRepository = null;
	
	@Autowired
	private ModuleDAO moduleDAO											= null;
	
    @Autowired
    private UserRoleRepository userRoleRepository						= null; 
    
	@Autowired
	private TemplateVersionService templateVersionService				= null;
    
	
	public ModuleDetailsVO getModuleDetails(String moduleId) throws Exception{
		if(!StringUtils.isBlank(moduleId)) {
			ModuleDetailsVO moduleDetailsVO = iModuleListingRepository.getModuleDetails(moduleId, Constant.DEFAULT_LANGUAGE_ID ,Constant.DEFAULT_LANGUAGE_ID);
			
			if(moduleDetailsVO != null) {
				Integer targetLookupId = moduleDetailsVO.getTargetLookupId();
				if( targetLookupId != null && !targetLookupId.equals(Constant.MODULE_GROUP_ID)) {
					Map<String, Object> moduleDetailsMap = getContextNameDetailsByType(moduleDetailsVO);
					moduleDetailsVO.setTargetLookupName(moduleDetailsMap.get("targetTypeName").toString());
				}
			}
			return moduleDetailsVO;
		}		
		return null;
	}
	
	
	public List<ModuleDetailsVO> getAllMenuModules() throws Exception{
		return iModuleListingRepository.getAllModulesDetails(
				Constant.HOME_PAGE_MODULE_URL, Constant.DEFAULT_LANGUAGE_ID ,Constant.DEFAULT_LANGUAGE_ID);
	}
	
	
	public List<ModuleDetailsVO> getAllParentModules(String moduleId) throws Exception{
		List<ModuleDetailsVO> parentModulesList  	= new ArrayList<>();
		List<ModuleDetailsVO> parentModuleVOs	  	= new ArrayList<>();
		parentModulesList = iModuleListingRepository.getAllParentModules(Constant.HOME_PAGE_MODULE_URL
				, Constant.DEFAULT_LANGUAGE_ID ,Constant.DEFAULT_LANGUAGE_ID, Constant.IS_NOT_INSIDE_MENU);
		if(!StringUtils.isBlank(moduleId)) {
			for (ModuleDetailsVO moduleDetailsVO : parentModulesList) {
				if(!moduleDetailsVO.getModuleId().equals(moduleId)) {
					parentModuleVOs.add(moduleDetailsVO);
				}
			}
			return parentModuleVOs;
		}
		return parentModulesList;
	}
	
	
	public List<ModuleTargetLookupVO> getAllModuleLookUp() throws Exception{
		return iModuleTargetLookupRepository.getAllModuleTargetLookup();
	}
	
	
	
	public List<UserRoleVO> getAllUserRoles() throws Exception{
		return userRoleRepository.getAllActiveRoles(Constant.RecordStatus.INSERTED.getStatus());
	}
	
	
	public String saveModuleDetails(ModuleDetailsVO moduleDetailsVO) throws Exception {
		ModuleListing moduleListing = convertModuleVOToEntitity(moduleDetailsVO);
		ModuleListingI18n moduleListingI18n = convertModuleVOToI18nEntitity(moduleDetailsVO);
		iModuleListingRepository.saveAndFlush(moduleListing);
		ModuleListingI18nPK moduleListingI18nPK = moduleListingI18n.getId();
		moduleListingI18nPK.setModuleId(moduleListing.getModuleId());
		iModuleListingI18nRepository.save(moduleListingI18n);
		return moduleListing.getModuleId();
	}
	
	public Map<String, Object> getExistingModuleData(String moduleName, String parentModuleId, Integer sequence, String moduleURL) throws Exception {
		Map<String, Object> moduleDetailsMap = new HashMap<>();
		String moduleId = getModuleIdByName(moduleName, Constant.DEFAULT_LANGUAGE_ID, Constant.DEFAULT_LANGUAGE_ID);
		if(!StringUtils.isBlank(moduleId)) {
			moduleDetailsMap.put("moduleIdName", moduleId);
		}
		moduleId = getModuleIdBySequence(parentModuleId, sequence);
		if(!StringUtils.isBlank(moduleId)) {
			moduleDetailsMap.put("moduleIdSequence", moduleId);
		}
		
		if(!StringUtils.isBlank(moduleURL) && !moduleURL.equals(Constant.GROUP_MODULE_URL)) {
			moduleId = getModuleIdByURL(moduleURL);
			if(!StringUtils.isBlank(moduleId)) {
				moduleDetailsMap.put("moduleIdURL", moduleId);
			}
		}
		return moduleDetailsMap;
	}
	
	public String getModuleIdByName(String moduleName, Integer languageId, Integer defaultLanguageId) throws Exception {
		return iModuleListingRepository.getModuleIdByName(moduleName, languageId, defaultLanguageId);
	}
	
	public String getModuleIdBySequence(String parentModuleId, Integer sequence) throws Exception {
		if(!StringUtils.isBlank(parentModuleId)) {
			return iModuleListingRepository.getParentModuleIdBySequence(parentModuleId, sequence);
		}else {
			return iModuleListingRepository.getModuleIdBySequence(sequence);
		}
		
	}
	
	
	public String getModuleIdByURL(String moduleURL) throws Exception {
		return iModuleListingRepository.getModuleIdByURL(moduleURL);
	}
	
	
	
	private ModuleListing convertModuleVOToEntitity(ModuleDetailsVO moduleDetailsVO) {
		ModuleListing moduleListing = new ModuleListing();
		
		if(!StringUtils.isBlank(moduleDetailsVO.getModuleId())) {
			moduleListing.setModuleId(moduleDetailsVO.getModuleId());
		}
		if(!StringUtils.isBlank(moduleDetailsVO.getParentModuleId())) {
			moduleListing.setParentId(moduleDetailsVO.getParentModuleId());
		}
		if(moduleDetailsVO.getIsInsideMenu().equals(Constant.IS_INSIDE_MENU)) {
			moduleListing.setIsInsideMenu(moduleDetailsVO.getIsInsideMenu());
		}else {
			moduleListing.setSequence(moduleDetailsVO.getSequence());
			moduleListing.setIsInsideMenu(Constant.IS_NOT_INSIDE_MENU);
		}
		
		moduleListing.setModuleUrl(moduleDetailsVO.getModuleURL());
		
		moduleListing.setTargetLookupId(moduleDetailsVO.getTargetLookupId());
		if(!StringUtils.isBlank(moduleDetailsVO.getTargetTypeId())) {
			moduleListing.setTargetTypeId(moduleDetailsVO.getTargetTypeId());
		}
		return moduleListing;
    }
	
	
	private ModuleListingI18n convertModuleVOToI18nEntitity(ModuleDetailsVO moduleDetailsVO) {
		ModuleListingI18n moduleListingI18n = new ModuleListingI18n();
		ModuleListingI18nPK moduleListingI18nPK = new ModuleListingI18nPK();
		if(moduleDetailsVO.getModuleId() != null && !moduleDetailsVO.getModuleId().isBlank() && !moduleDetailsVO.getModuleId().isEmpty()) {
			moduleListingI18nPK.setModuleId(moduleDetailsVO.getModuleId());
		}
		moduleListingI18nPK.setLanguageId(Constant.DEFAULT_LANGUAGE_ID);
		moduleListingI18n.setId(moduleListingI18nPK);
		moduleListingI18n.setModuleName(moduleDetailsVO.getModuleName());
		return moduleListingI18n;
    }


	
	public List<Map<String, Object>> getTargetTypes(Integer targetLookupId, String targetTypeId) throws Exception {
		List<Map<String, Object>> targetTypeMapList = new ArrayList<>();
		if(targetLookupId != null) {
			targetTypeMapList = moduleDAO.findTargetTypeDetails(targetLookupId, targetTypeId);
		}
		return targetTypeMapList;
	}
	
	public Map<String, Object> getModuleTargetTypeName(String moduleURL) throws Exception {
		ModuleDetailsVO moduleDetailsVO = iModuleListingRepository.getTargetTypeDetails(moduleURL);
		Map<String, Object> moduleDetailsMap = getContextNameDetailsByType(moduleDetailsVO);
		return moduleDetailsMap;
	}


	private Map<String, Object> getContextNameDetailsByType(ModuleDetailsVO moduleDetailsVO)
			throws Exception {
		Map<String, Object> moduleDetailsMap = new HashMap<>();
		if(moduleDetailsVO != null) {
			List<Map<String, Object>> targetTypeList = moduleDAO.findTargetTypeDetails(moduleDetailsVO.getTargetLookupId(), moduleDetailsVO.getTargetTypeId());
			moduleDetailsMap.put("targetLookupId", moduleDetailsVO.getTargetLookupId());
			if(!CollectionUtils.isEmpty(targetTypeList)) {
				moduleDetailsMap.put("targetTypeId", targetTypeList.get(0).get("targetTypeId"));
	        	moduleDetailsMap.put("targetTypeName", targetTypeList.get(0).get("targetTypeName"));
			}
		}
		return moduleDetailsMap;
	}
    
	public Integer getModuleMaxSequence() throws Exception {
		Integer defaultSequence = moduleDAO.getModuleMaxSequence();
		if(defaultSequence != null) {
			defaultSequence = defaultSequence + 1;
		}else {
			defaultSequence = Constant.DEFAULT_SEQUENCE_NUMBER;
		}
		return defaultSequence;
	}
	
	public Integer getMaxSequenceByParent(String parentModuleId) throws Exception {
		Integer defaultSequence = moduleDAO.getMaxSequenceByParent(parentModuleId);
		if(defaultSequence != null) {
			defaultSequence = defaultSequence + 1;
		}else {
			defaultSequence = Constant.DEFAULT_SEQUENCE_NUMBER;
		}
		return defaultSequence;
	}
	
	public String getHomePageModuleId() {
		return iModuleListingRepository.getHomeModuleId(Constant.HOME_PAGE_MODULE_URL);
	}
	
	public String saveConfigHomePage(String moduleId, Integer targetLookupTypeId, String targetTypeId) {
		ModuleListing moduleListing = new ModuleListing();
		
		if(!StringUtils.isBlank(moduleId)) {
			moduleListing.setModuleId(moduleId);
		}

		moduleListing.setModuleUrl(Constant.HOME_PAGE_MODULE_URL);
		moduleListing.setTargetLookupId(targetLookupTypeId);
		moduleListing.setTargetTypeId(targetTypeId);
		iModuleListingRepository.saveAndFlush(moduleListing);
		
		ModuleListingI18n moduleListingI18n = new ModuleListingI18n();
		ModuleListingI18nPK moduleListingI18nPK = new ModuleListingI18nPK();
		moduleListingI18nPK.setModuleId(moduleListing.getModuleId());
		moduleListingI18nPK.setLanguageId(Constant.DEFAULT_LANGUAGE_ID);
		moduleListingI18n.setId(moduleListingI18nPK);
		moduleListingI18n.setModuleName("home-page");
		moduleListingI18nPK.setModuleId(moduleListing.getModuleId());
		iModuleListingI18nRepository.save(moduleListingI18n);
		
		return moduleListing.getModuleId();
    }
	
}
