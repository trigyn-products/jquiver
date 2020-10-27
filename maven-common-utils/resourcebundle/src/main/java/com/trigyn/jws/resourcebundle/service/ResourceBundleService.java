package com.trigyn.jws.resourcebundle.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.resourcebundle.dao.ResourceBundleDAO;
import com.trigyn.jws.resourcebundle.entities.ResourceBundle;
import com.trigyn.jws.resourcebundle.entities.ResourceBundlePK;
import com.trigyn.jws.resourcebundle.repository.interfaces.ILanguageRepository;
import com.trigyn.jws.resourcebundle.repository.interfaces.IResourceBundleRepository;
import com.trigyn.jws.resourcebundle.utils.Constant;
import com.trigyn.jws.resourcebundle.utils.ResourceBundleUtils;
import com.trigyn.jws.resourcebundle.vo.LanguageVO;
import com.trigyn.jws.resourcebundle.vo.ResourceBundleVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
public class ResourceBundleService {
    
    @Autowired
	private ResourceBundleDAO dbResourceDAO								= null;
    
    @Autowired
    private IResourceBundleRepository iResourceBundleRepository			= null;
    
    @Autowired
    private ILanguageRepository iLanguageRepository						= null;
    
	@Autowired
	private ModuleVersionService moduleVersionService					= null;
	
	
	public Map<Integer, ResourceBundleVO> getResourceBundleVOMap(String resourceBundleKey) throws Exception {
		try{
			Map<Integer, ResourceBundleVO> resourceBundleVOMap = new HashMap<Integer, ResourceBundleVO>();
			List<ResourceBundleVO> resourceBundleVOList = iResourceBundleRepository.findResourceBundleByKey(resourceBundleKey);
			if (!CollectionUtils.isEmpty(resourceBundleVOList)) {
				for (ResourceBundleVO dbResource : resourceBundleVOList) {
					resourceBundleVOMap.put(dbResource.getLanguageId(), dbResource);
				}
			}
			return resourceBundleVOMap;
		}catch(Exception exception){
			throw new RuntimeException("Error ocurred while fetching resource bundle data");
		}
    }
	
	
	
	
	public List<LanguageVO> getLanguagesList()throws Exception {
		try{
			return iLanguageRepository.getAllLanguages(Constant.RecordStatus.INSERTED.getStatus());
		}catch(Exception exception){
			throw new RuntimeException("Error ocurred while fetching list of languages.");
		}
	}
	
	
	
	public Boolean checkResourceKeyExist(String resourceBundleKey)throws Exception {
		Boolean keyAlreadyExist = true;
		try{
			String savedResourceBundleKey = iResourceBundleRepository.checkResourceKeyExist(resourceBundleKey);
			if (resourceBundleKey == null || 
					(resourceBundleKey != null && !resourceBundleKey.equals(savedResourceBundleKey))) {
				keyAlreadyExist = false;
			}
			return keyAlreadyExist;
		}catch(Exception exception){
			throw new RuntimeException("Error ocurred while fetching list of languages.");
		}
	}
	
	
	
	@Transactional(readOnly = false)
	public void saveResourceBundleDetails(String resourceBundleKey, List<ResourceBundleVO> resourceBundleVOList)throws Exception {
		try{
			List<ResourceBundle> resourceBundleList = new ArrayList<>();
			
			if (!CollectionUtils.isEmpty(resourceBundleVOList)) {
				for (ResourceBundleVO resourceBundleVO : resourceBundleVOList) {
					if (resourceBundleVO.getText() != null && !resourceBundleVO.getText().equals("")) {
						
						ResourceBundle resourceBundle = new ResourceBundle();
						if (resourceBundleVO.getLanguageId().equals(Constant.DEFAULT_LANGUAGE_ID)) {
							resourceBundleVO.setResourceKey(resourceBundleKey);
						} else {
							resourceBundleVO.setResourceKey(resourceBundleKey);
							resourceBundleVO.setText(ResourceBundleUtils.getUnicode(resourceBundleVO.getText()));
						}
						resourceBundle = convertResourceBundleEntityToVO(resourceBundleKey, resourceBundleVO);
						resourceBundleList.add(resourceBundle);
					}

				}
				iResourceBundleRepository.saveAll(resourceBundleList);
				moduleVersionService.saveModuleVersion(resourceBundleList,null, resourceBundleKey, "resource_bundle");
			}
		}catch(Exception exception){
			throw new RuntimeException("Error ocurred while saving resource bundle data");
		}
	}
	
	
	
	private ResourceBundle convertResourceBundleEntityToVO(String resourceBundleKey, ResourceBundleVO resourceBundleVO) throws Exception{
		ResourceBundle resourceBundle 		= new ResourceBundle();
		ResourceBundlePK resourceBundlePK 	= new ResourceBundlePK();
		resourceBundlePK.setResourceKey(resourceBundleKey);
		resourceBundlePK.setLanguageId(resourceBundleVO.getLanguageId());
		resourceBundle.setId(resourceBundlePK);
		resourceBundle.setText(resourceBundleVO.getText());
		return resourceBundle;
	}
	
	
	
	@Transactional(readOnly = false)
	public void deleteDbResourceEntry(ResourceBundleVO dbresource) throws Exception{
		try{
			dbResourceDAO.deleteResourceEntry(dbresource, null);
		}catch(Exception exception){
			throw new RuntimeException("Error ocurred while deleting an entry from resource bundle");
		}
	}

	
	
	public ResourceBundleVO checkResourceData(String resKey,String langId) throws Exception {
		try{
			List<ResourceBundleVO> resData 	= dbResourceDAO.checkResourceData(resKey,langId);
			ResourceBundleVO editData 		= null;
			for(ResourceBundleVO resMap : resData){
				editData = new ResourceBundleVO();
				editData.setResourceKey(resMap.getResourceKey());
				editData.setText(resMap.getText());
				editData.setLanguageId(resMap.getLanguageId());
			}
			return editData;
		}catch(Exception exception){
			throw new RuntimeException("Error ocurred while fetching resource bundle data");
		}
	}


	
    
}