package app.trigyn.common.resourcebundle.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import app.trigyn.common.resourcebundle.dao.ResourceBundleDAO;
import app.trigyn.common.resourcebundle.entities.ResourceBundle;
import app.trigyn.common.resourcebundle.entities.ResourceBundlePK;
import app.trigyn.common.resourcebundle.repository.interfaces.ILanguageRepository;
import app.trigyn.common.resourcebundle.repository.interfaces.IResourceBundleRepository;
import app.trigyn.common.resourcebundle.utils.Constant;
import app.trigyn.common.resourcebundle.utils.ResourceBundleUtils;
import app.trigyn.common.resourcebundle.vo.ResourceBundleVO;
import app.trigyn.common.resourcebundle.vo.LanguageVO;

@Service
@Transactional(readOnly = true)
public class ResourceBundleService {
    
    @Autowired
	private ResourceBundleDAO dbResourceDAO								= null;
    
    @Autowired
    private IResourceBundleRepository iResourceBundleRepository			= null;
    
    @Autowired
    private ILanguageRepository iLanguageRepository						= null;

	
	/** 
	 * @param resourceBundleKey
	 * @return List<DBResource>
	 * @throws Exception
	 */
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
	
	
	
	/** 
	 * @return List<DBResource>
	 * @throws Exception
	 */
	public List<LanguageVO> getLanguagesList()throws Exception {
		try{
			return iLanguageRepository.getAllLanguages(Constant.RecordStatus.INSERTED.getStatus());
		}catch(Exception exception){
			throw new RuntimeException("Error ocurred while fetching list of languages.");
		}
	}
	
	
	/**
	 * @return
	 * @throws Exception
	 */
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
	
	
	/** 
	 * @param resourceBundleKey 
	 * @param resourceBundleVOList
	 * @throws Exception
	 */
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
			}
			iResourceBundleRepository.saveAll(resourceBundleList);
		}catch(Exception exception){
			throw new RuntimeException("Error ocurred while saving resource bundle data");
		}
	}
	
	
	/**
	 * @param resourceBundleKey
	 * @param resourceBundleVO
	 * @return {@link ResourceBundle}
	 * @throws Exception
	 */
	private ResourceBundle convertResourceBundleEntityToVO(String resourceBundleKey, ResourceBundleVO resourceBundleVO) throws Exception{
		ResourceBundle resourceBundle 		= new ResourceBundle();
		ResourceBundlePK resourceBundlePK 	= new ResourceBundlePK();
		resourceBundlePK.setResourceKey(resourceBundleKey);
		resourceBundlePK.setLanguageId(resourceBundleVO.getLanguageId());
		resourceBundle.setId(resourceBundlePK);
		resourceBundle.setText(resourceBundleVO.getText());
		return resourceBundle;
	}
	
	
	/** 
	 * @param dbresource
	 * @throws Exception
	 */
	@Transactional(readOnly = false)
	public void deleteDbResourceEntry(ResourceBundleVO dbresource) throws Exception{
		try{
			dbResourceDAO.deleteResourceEntry(dbresource, null);
		}catch(Exception exception){
			throw new RuntimeException("Error ocurred while deleting an entry from resource bundle");
		}
	}

	
	/** 
	 * @param resKey
	 * @param langId
	 * @return DBResource
	 * @throws Exception
	 */
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