package app.trigyn.core.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import app.trigyn.core.menu.dao.ModuleDAO;
import app.trigyn.core.menu.entities.ModuleListing;
import app.trigyn.core.menu.entities.ModuleListingI18n;
import app.trigyn.core.menu.entities.ModuleListingI18nPK;
import app.trigyn.core.menu.reposirtory.interfaces.IModuleListingI18nRepository;
import app.trigyn.core.menu.reposirtory.interfaces.IModuleListingRepository;
import app.trigyn.core.menu.reposirtory.interfaces.IModuleTargetLookupRepository;
import app.trigyn.core.menu.utils.Constants;
import app.trigyn.core.menu.vo.ModuleDetailsVO;
import app.trigyn.core.menu.vo.ModuleTargetLookupVO;

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
	
	
	/**
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
	public ModuleDetailsVO getModuleDetails(String moduleId) throws Exception{
		if(moduleId != null && !moduleId.isBlank() && !moduleId.isEmpty()) {
			return iModuleListingRepository.getModuleDetails(moduleId, Constants.DEFAULT_LANGUAGE_ID ,Constants.DEFAULT_LANGUAGE_ID);
		}		
		return null;
	}
	
	
	public List<ModuleDetailsVO> getAllMenuModules() throws Exception{
		return iModuleListingRepository.getAllModulesDetails(Constants.DEFAULT_LANGUAGE_ID ,Constants.DEFAULT_LANGUAGE_ID);
	}
	
	/**
	 * @param moduleId
	 * @return
	 * @throws Exception
	 */
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
	
	
	
	/**
	 * @param moduleDetailsVO
	 * @throws Exception
	 */
	public void saveModuleDetails(ModuleDetailsVO moduleDetailsVO) throws Exception {
		ModuleListing moduleListing = convertModuleVOToEntitity(moduleDetailsVO);
		ModuleListingI18n moduleListingI18n = convertModuleVOToI18nEntitity(moduleDetailsVO);
		iModuleListingRepository.saveAndFlush(moduleListing);
		ModuleListingI18nPK moduleListingI18nPK = moduleListingI18n.getId();
		moduleListingI18nPK.setModuleId(moduleListing.getModuleId());
		iModuleListingI18nRepository.save(moduleListingI18n);
	}
	
	public String getModuleIdBySequence(String parentModuleId, Integer sequence) throws Exception {
		if(!StringUtils.isEmpty(parentModuleId)) {
			return iModuleListingRepository.getParentModuleIdBySequence(parentModuleId, sequence);
		}else {
			return iModuleListingRepository.getModuleIdBySequence(sequence);
		}
		
	}
	
	/**
	 * @param moduleDetailsVO
	 * @return
	 */
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
	
	/**
	 * @param moduleDetailsVO
	 * @return
	 */
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


	/**
	 * @param targetTypeId
	 * @return {@link List}
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getTargetTypes(Integer targetTypeId) throws Exception {
		List<Map<String, Object>> targetTypeMapList = new ArrayList<>();
		if(targetTypeId != null) {
			targetTypeMapList = moduleDAO.findTargetTypeDetails(targetTypeId);
		}
		return targetTypeMapList;
	}
	
    
}
