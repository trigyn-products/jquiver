package com.trigyn.jws.webstarter.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.resourcebundle.entities.Language;
import com.trigyn.jws.resourcebundle.entities.ResourceBundle;
import com.trigyn.jws.resourcebundle.repository.interfaces.ILanguageRepository;
import com.trigyn.jws.resourcebundle.service.ResourceBundleService;
import com.trigyn.jws.resourcebundle.vo.ResourceBundleVO;
import com.trigyn.jws.webstarter.dao.CrudQueryStore;
import com.trigyn.jws.webstarter.dao.GenerateModuleMasterQueries;
import com.trigyn.jws.webstarter.dao.ImportExportCrudDAO;

@Component
public class RBExportableData implements GenerateModuleMasterQueries {

	private final static Logger logger = LoggerFactory.getLogger(RBExportableData.class);

	@Autowired
	private ImportExportCrudDAO importExportCrudDAO = null;
	

	@Autowired
	private ResourceBundleService resourceBundleService = null;
	
	
	@Autowired
	private ILanguageRepository iLanguageRepository;

	@Override
	public List<Object> generateDynamicModuleQuery(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String moduleType, List<String> exportedList, XMLVO xmlVO,
			Date modifiedAfter, String entityType, String name, boolean autoExport) throws Exception {
		List<Object> exportableList = new ArrayList<>();
		List<Object> resourceBundleVOList = new ArrayList<>();

		if (autoExport) {
			StringBuilder querySQL = new StringBuilder(
					CrudQueryStore.HQL_QUERY_TO_FETCH_RESOURCE_BUNDLE_DATA_FOR_AUTO_EXPORT);
			if (modifiedAfter != null) {
				if (querySQL.toString().contains(" WHERE ")) {
					querySQL.append("AND ");
				} else {
					querySQL.append(" WHERE ");
				}
				querySQL.append("COALESCE(rb.updated_date , rb.created_date)  >=:modifiedAfter ");
				// params.addValue("modifiedAfter", modifiedAfter);
			}

			if (name != null) {
				 name = "%" + name + "%";
				if (querySQL.toString().contains(" WHERE ")) {
					querySQL.append("AND ");
				} else {
					querySQL.append(" WHERE ");
				}
				//querySQL.append(" rb.resource_key REGEXP :name ");
				querySQL.append(" LOWER(rb.resource_key) LIKE LOWER(:name) ");
			}
			
			// Resource Bundle export rules:
			// null = all
			// 1 = none
			// 2 = all

			if ("1".equals(entityType)) {

			    if (querySQL.toString().contains(" WHERE ")) {
			        querySQL.append("AND ");
			    } else {
			        querySQL.append(" WHERE ");
			    }

			    querySQL.append(" rb.resource_key NOT LIKE 'jws.%' ");

			} else if ("2".equals(entityType) || entityType == null) {

			    if (querySQL.toString().contains(" WHERE ")) {
			        querySQL.append("AND ");
			    } else {
			        querySQL.append(" WHERE ");
			    }

			    querySQL.append(" rb.resource_key LIKE 'jws.%' ");
			}
			BeanPropertyRowMapper<ResourceBundleVO> mapper = new BeanPropertyRowMapper<ResourceBundleVO>(ResourceBundleVO.class);
			resourceBundleVOList =  importExportCrudDAO.getAllAutoExportableData(querySQL.toString(), modifiedAfter, null,
					name,mapper);
			
			for (Object obj : resourceBundleVOList) {

	            ResourceBundleVO vo = (ResourceBundleVO) obj;

	            ResourceBundle resourceBundle = resourceBundleService.convertResourceBundleVOToEntity(vo.getResourceKey(), vo);
	            Language language =getLanguageById(
	                            vo.getLanguageId());

	            resourceBundle.setLanguage(language);

	            exportableList.add(resourceBundle);
	        }

		} else {
			exportableList = importExportCrudDAO.getRBExportableData(
					CrudQueryStore.HQL_QUERY_TO_FETCH_RESOURCE_BUNDLE_DATA_FOR_EXPORT, systemConfigIncludeList,
					customConfigExcludeList);

		}
		return exportableList;

	}
	
	public Language getLanguageById(Integer languageId) {

	    return iLanguageRepository.findById(languageId).orElse(null);
	}

}
