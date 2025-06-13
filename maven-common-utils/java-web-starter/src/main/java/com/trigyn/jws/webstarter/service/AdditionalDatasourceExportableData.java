package com.trigyn.jws.webstarter.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dbutils.entities.AdditionalDatasource;
import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.gridutils.entities.GridDetails;
import com.trigyn.jws.webstarter.dao.CrudQueryStore;
import com.trigyn.jws.webstarter.dao.GenerateModuleMasterQueries;
import com.trigyn.jws.webstarter.dao.ImportExportCrudDAO;

@Component
public class AdditionalDatasourceExportableData implements GenerateModuleMasterQueries {

	private final static Logger logger = LoggerFactory.getLogger(AdditionalDatasourceExportableData.class);

	@Autowired
	private ImportExportCrudDAO importExportCrudDAO = null;

	@Override
	public List<Object> generateDynamicModuleQuery(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String moduleType, List<String> exportedList, XMLVO xmlVO,
			Date modifiedAfter, String entityType, String name, boolean autoExport) throws Exception {
		List<Object> exportableList = new ArrayList<>();

		if (autoExport) {
			StringBuilder querySQL = new StringBuilder(
					CrudQueryStore.HQL_QUERY_TO_FETCH_ADDITIONAL_DATASOURCE_FOR_AUTO_EXPORT);
			if (modifiedAfter != null) {
				if (querySQL.toString().contains(" WHERE ")) {
					querySQL.append("AND ");
				} else {
					querySQL.append(" WHERE ");
				}
				querySQL.append("COALESCE(ads.last_updated_ts , ads.created_date)  >=:modifiedAfter ");

			}

			if (name != null) {
				if (querySQL.toString().contains(" WHERE ")) {
					querySQL.append("AND ");
				} else {
					querySQL.append(" WHERE ");
				}
				querySQL.append(" ads.datasource_name REGEXP :name ");
			}
			BeanPropertyRowMapper<AdditionalDatasource> mapper = new BeanPropertyRowMapper<AdditionalDatasource>(AdditionalDatasource.class);
			exportableList = importExportCrudDAO.getAllAutoExportableData(querySQL.toString(), modifiedAfter, null,
					name,mapper);

		} else {
			exportableList = importExportCrudDAO.getAllExportableData(
					CrudQueryStore.HQL_QUERY_TO_FETCH_ADDITIONAL_DATASOURCE_FOR_EXPORT, null, null,
					customConfigExcludeList, null);

		}
		return exportableList;

	}

}
