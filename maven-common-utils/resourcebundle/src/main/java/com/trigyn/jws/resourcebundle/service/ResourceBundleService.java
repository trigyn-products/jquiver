package com.trigyn.jws.resourcebundle.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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

@Service
@Transactional(readOnly = true)
public class ResourceBundleService {

	@Autowired
	private ResourceBundleDAO			dbResourceDAO				= null;

	@Autowired
	private IResourceBundleRepository	iResourceBundleRepository	= null;

	@Autowired
	private ILanguageRepository			iLanguageRepository			= null;

	@Autowired
	private ModuleVersionService		moduleVersionService		= null;

	private final static Logger			logger						= LogManager.getLogger(ResourceBundleService.class);

	public Map<Integer, ResourceBundleVO> getResourceBundleVOMap(String resourceBundleKey) throws Exception {
		try {
			Map<Integer, ResourceBundleVO>	resourceBundleVOMap		= new HashMap<Integer, ResourceBundleVO>();
			List<ResourceBundleVO>			resourceBundleVOList	= iResourceBundleRepository
					.findResourceBundleByKey(resourceBundleKey);
			if (!CollectionUtils.isEmpty(resourceBundleVOList)) {
				for (ResourceBundleVO dbResource : resourceBundleVOList) {
					resourceBundleVOMap.put(dbResource.getLanguageId(), dbResource);
				}
			}
			return resourceBundleVOMap;
		} catch (Exception a_excep) {
			logger.error("Error ocurred while fetching resource bundle data", a_excep);
			throw new RuntimeException("Error ocurred while fetching resource bundle data");
		}
	}

	public List<LanguageVO> getLanguagesList() throws Exception {
		try {
			return iLanguageRepository.getAllLanguages(Constant.RecordStatus.INSERTED.getStatus());
		} catch (Exception a_excep) {
			logger.error("Error ocurred while fetching list of languages.", a_excep);
			throw new RuntimeException("Error ocurred while fetching list of languages.");
		}
	}

	public Boolean checkResourceKeyExist(String resourceBundleKey) throws Exception {
		Boolean keyAlreadyExist = true;
		try {
			String savedResourceBundleKey = iResourceBundleRepository.checkResourceKeyExist(resourceBundleKey);
			if (resourceBundleKey == null
					|| (resourceBundleKey != null && !resourceBundleKey.equals(savedResourceBundleKey))) {
				keyAlreadyExist = false;
			}
			return keyAlreadyExist;
		} catch (Exception a_excep) {
			logger.error("Error ocurred while fetching list of languages.", a_excep);
			throw new RuntimeException("Error ocurred while fetching list of languages.");
		}
	}

	@Transactional(readOnly = false)
	public void saveResourceBundleDetails(List<ResourceBundleVO> resourceBundleVOList, Integer sourceTypeId)
			throws Exception {
		try {
			List<ResourceBundle> resourceBundleList = new ArrayList<>();

			if (!CollectionUtils.isEmpty(resourceBundleVOList)) {
				String resourceBundleKey = resourceBundleVOList.get(0).getResourceKey();
				for (ResourceBundleVO resourceBundleVO : resourceBundleVOList) {
					if (resourceBundleVO.getText() != null && !resourceBundleVO.getText().equals("")) {
						ResourceBundle resourceBundle = new ResourceBundle();
						resourceBundle = convertResourceBundleVOToEntity(resourceBundleKey, resourceBundleVO);
						resourceBundleList.add(resourceBundle);
					}

				}
				iResourceBundleRepository.saveAll(resourceBundleList);
				moduleVersionService.saveModuleVersion(resourceBundleVOList, null, resourceBundleKey,
						"jq_resource_bundle", sourceTypeId);
			}
		} catch (Exception a_exception) {
			logger.error("Error occurred while saving resource bundle data ", a_exception);
			throw new RuntimeException("Error ocurred while saving resource bundle data");
		}
	}

	private ResourceBundle convertResourceBundleVOToEntity(String resourceBundleKey, ResourceBundleVO resourceBundleVO)
			throws Exception {
		ResourceBundle		resourceBundle		= new ResourceBundle();
		ResourceBundlePK	resourceBundlePK	= new ResourceBundlePK();
		resourceBundlePK.setResourceKey(resourceBundleKey);
		resourceBundlePK.setLanguageId(resourceBundleVO.getLanguageId());
		resourceBundle.setId(resourceBundlePK);
		if (resourceBundleVO.getLanguageId().equals(Constant.DEFAULT_LANGUAGE_ID)) {
			resourceBundle.setText(resourceBundleVO.getText());
		} else {
			resourceBundle.setText(ResourceBundleUtils.getUnicode(resourceBundleVO.getText()));
		}
		return resourceBundle;
	}

	public ResourceBundleVO convertResourceBundleEntityToVO(ResourceBundle resourceBundle) throws Exception {
		ResourceBundleVO resourceBundleVO = new ResourceBundleVO();
		resourceBundleVO.setLanguageId(resourceBundle.getId().getLanguageId());
		resourceBundleVO.setResourceKey(resourceBundle.getId().getResourceKey());
		resourceBundleVO.setText(resourceBundle.getText());
		return resourceBundleVO;
	}

	public String findTextByKeyAndLanguageId(String resourceBundleKey, Integer languageId) throws Exception {
		if (languageId == null) {
			languageId = Constant.DEFAULT_LANGUAGE_ID;
		}
		return iResourceBundleRepository.findMessageByKeyAndLanguageId(resourceBundleKey, languageId,
				Constant.DEFAULT_LANGUAGE_ID, Constant.RecordStatus.INSERTED.getStatus());
	}

	public Map<String, String> getResourceBundleData(String localeId, List<String> keyList) throws Exception {
		return dbResourceDAO.getResourceBundleData(localeId, keyList);
	}

}