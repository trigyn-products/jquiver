package com.trigyn.jws.webstarter.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dbutils.entities.ModuleListing;
import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.webstarter.dao.CrudQueryStore;
import com.trigyn.jws.webstarter.dao.GenerateModuleMasterQueries;
import com.trigyn.jws.webstarter.dao.ImportExportCrudDAO;

@Component
public class SiteLayoutExportableData implements GenerateModuleMasterQueries {

	private final static Logger logger = LoggerFactory.getLogger(SiteLayoutExportableData.class);

	@Autowired
	private ImportExportCrudDAO importExportCrudDAO = null;

	@Override
	public List<Object> generateDynamicModuleQuery(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String moduleType, List<String> exportedList, XMLVO xmlVO,
			Date modifiedAfter, String entityType, String name, boolean autoExport) throws Exception {
		List<Object> exportableList = new ArrayList<>();

		if (autoExport) {
			StringBuilder queryHQL = new StringBuilder(
					CrudQueryStore.HQL_QUERY_TO_FETCH_SITE_LAYOUT_DATA_FOR_AUTO_EXPORT);
			if (modifiedAfter != null) {
				if (queryHQL.toString().contains(" WHERE ")) {
					queryHQL.append("AND ");
				} else {
					queryHQL.append(" WHERE ");
				}
				queryHQL.append("jml.last_modified_date >=:modifiedAfter ");
			}
			if (name != null) {
				 name = "%" + name + "%";
				if (queryHQL.toString().contains(" WHERE ")) {
					queryHQL.append("AND ");
				} else {
					queryHQL.append(" WHERE ");
				}
				//queryHQL.append(" jml.module_url = :name ");
				queryHQL.append(" LOWER(jml.module_url) LIKE LOWER(:name) ");
			}

			if (entityType != null) {
				if (queryHQL.toString().contains(" WHERE ")) {
					queryHQL.append("AND ");
				} else {
					queryHQL.append(" WHERE ");
				}
				queryHQL.append(" jml.module_type_id = :entityType ");
			}
			BeanPropertyRowMapper<ModuleListing> mapper = new BeanPropertyRowMapper<ModuleListing>(ModuleListing.class);
//			exportableList = importExportCrudDAO.getAllAutoExportable(querySQL.toString(), modifiedAfter,
//					entityType, name,mapper);
			exportableList = importExportCrudDAO.getAllAutoExportableEntityData(queryHQL.toString(), modifiedAfter,
					entityType, name);

		} else {
			exportableList = importExportCrudDAO.getAllExportableData(
					CrudQueryStore.HQL_QUERY_TO_FETCH_SITE_LAYOUT_DATA_FOR_EXPORT, systemConfigIncludeList, 2,
					customConfigExcludeList, 1);

		}
		return exportableList;

	}

}
