package com.trigyn.jws.menu.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.trigyn.jws.menu.dao.ModuleDAO;
import com.trigyn.jws.menu.entities.ModuleListing;
import com.trigyn.jws.menu.entities.ModuleListingI18n;
import com.trigyn.jws.menu.entities.ModuleListingI18nPK;
import com.trigyn.jws.menu.reposirtory.interfaces.IModuleListingI18nRepository;
import com.trigyn.jws.menu.reposirtory.interfaces.IModuleListingRepository;
import com.trigyn.jws.menu.reposirtory.interfaces.IModuleTargetLookupRepository;
import com.trigyn.jws.menu.utils.Constants;
import com.trigyn.jws.menu.vo.ModuleDetailsVO;
import com.trigyn.jws.menu.vo.ModuleTargetLookupVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
	
	
	
	public ModuleDetailsVO getModuleDetails(String moduleId) throws Exception{
		if(moduleId != null && !moduleId.isBlank() && !moduleId.isEmpty()) {
			return iModuleListingRepository.getModuleDetails(moduleId, Constants.DEFAULT_LANGUAGE_ID ,Constants.DEFAULT_LANGUAGE_ID);
		}		
		return null;
	}
	
	
	public List<ModuleDetailsVO> getAllMenuModules() throws Exception{
		return iModuleListingRepository.getAllModulesDetails(Constants.DEFAULT_LANGUAGE_ID ,Constants.DEFAULT_LANGUAGE_ID);
	}
	
	
	public List<ModuleDetailsVO> getAllModules(String moduleId) throws Exception{
		List<ModuleDetailsVO> moduleDetailsVOList  = new ArrayList<>();
		List<ModuleDetailsVO> moduleDetailsVOs  = new ArrayList<>();
		moduleDetailsVOList = iModuleListingRepository.getAllModules(Constants.DEFAULT_LANGUAGE_ID ,Constants.DEFAULT_LANGUAGE_ID);
		if(moduleId != null && !moduleId.isBlank() && !moduleId.isEmpty()) {
			for (ModuleDetailsVO moduleDetailsVO : moduleDetailsVOList) {
				if(!moduleDetailsVO.getModuleId().equals(moduleId)) {
					moduleDetailsVOs.add(moduleDetailsVO);
				}
			}
			return moduleDetailsVOs;
		}
		return moduleDetailsVOList;
	}
	
	
	public List<ModuleTargetLookupVO> getAllModuleLookUp() throws Exception{
		return iModuleTargetLookupRepository.getAllModuleTargetLookup();
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
	
	
	public String getModuleIdBySequence(String parentModuleId, Integer sequence) throws Exception {
		if(!StringUtils.isEmpty(parentModuleId)) {
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
		
		if(!StringUtils.isEmpty(moduleDetailsVO.getModuleId())) {
			moduleListing.setModuleId(moduleDetailsVO.getModuleId());
		}
		if(!StringUtils.isEmpty(moduleDetailsVO.getParentModuleId())) {
			moduleListing.setParentId(moduleDetailsVO.getParentModuleId());
		}
		moduleListing.setModuleUrl(moduleDetailsVO.getModuleURL());
		moduleListing.setSequence(moduleDetailsVO.getSequence());
		moduleListing.setTargetLookupId(moduleDetailsVO.getTargetLookupId());
		moduleListing.setTargetTypeId(moduleDetailsVO.getTargetTypeId());
		return moduleListing;
    }
	
	
	private ModuleListingI18n convertModuleVOToI18nEntitity(ModuleDetailsVO moduleDetailsVO) {
		ModuleListingI18n moduleListingI18n = new ModuleListingI18n();
		ModuleListingI18nPK moduleListingI18nPK = new ModuleListingI18nPK();
		if(moduleDetailsVO.getModuleId() != null && !moduleDetailsVO.getModuleId().isBlank() && !moduleDetailsVO.getModuleId().isEmpty()) {
			moduleListingI18nPK.setModuleId(moduleDetailsVO.getModuleId());
		}
		moduleListingI18nPK.setLanguageId(Constants.DEFAULT_LANGUAGE_ID);
		moduleListingI18n.setId(moduleListingI18nPK);
		moduleListingI18n.setModuleName(moduleDetailsVO.getModuleName());
		return moduleListingI18n;
    }


	
	public List<Map<String, Object>> getTargetTypes(Integer targetTypeId) throws Exception {
		List<Map<String, Object>> targetTypeMapList = new ArrayList<>();
		if(targetTypeId != null) {
			targetTypeMapList = moduleDAO.findTargetTypeDetails(targetTypeId);
		}
		return targetTypeMapList;
	}
	
    
}
