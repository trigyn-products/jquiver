package com.trigyn.jws.webstarter.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.webstarter.dao.CrudQueryStore;
import com.trigyn.jws.webstarter.dao.GenerateModuleMasterQueries;
import com.trigyn.jws.webstarter.dao.ImportExportCrudDAO;

@Component
public class RBExportableData implements GenerateModuleMasterQueries {

	private final static Logger logger = LoggerFactory.getLogger(RBExportableData.class);

	@Autowired
	private ImportExportCrudDAO importExportCrudDAO = null;

	@Override
	public List<Object> generateDynamicModuleQuery(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String moduleType, List<String> exportedList, XMLVO xmlVO,
			Date modifiedAfter, String entityType, String name, boolean autoExport) throws Exception {
		List<Object> exportableList = new ArrayList<>();

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
				if (querySQL.toString().contains(" WHERE ")) {
					querySQL.append("AND ");
				} else {
					querySQL.append(" WHERE ");
				}
				querySQL.append(" rb.resource_key REGEXP :name ");
			}

			exportableList = importExportCrudDAO.getAllAutoExportableData(querySQL.toString(), modifiedAfter, null,
					name,null);

		} else {
			exportableList = importExportCrudDAO.getRBExportableData(
					CrudQueryStore.HQL_QUERY_TO_FETCH_RESOURCE_BUNDLE_DATA_FOR_EXPORT, systemConfigIncludeList,
					customConfigExcludeList);

		}
		return exportableList;

	}

}
