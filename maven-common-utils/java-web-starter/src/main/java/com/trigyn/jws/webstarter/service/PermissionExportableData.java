package com.trigyn.jws.webstarter.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;
import com.trigyn.jws.webstarter.dao.CrudQueryStore;
import com.trigyn.jws.webstarter.dao.GenerateModuleMasterQueries;
import com.trigyn.jws.webstarter.dao.ImportExportCrudDAO;

@Component
public class PermissionExportableData implements GenerateModuleMasterQueries {

	private final static Logger logger = LoggerFactory.getLogger(PermissionExportableData.class);

	@Autowired
	private ImportExportCrudDAO importExportCrudDAO = null;

	@Override
	public List<Object> generateDynamicModuleQuery(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String moduleType, List<String> exportedList, XMLVO xmlVO,
			Date modifiedAfter, String entityType, String name, boolean autoExport) throws Exception {
		List<Object> exportableList = new ArrayList<>();

		if (autoExport) {
			StringBuilder querySQL = new StringBuilder(CrudQueryStore.HQL_QUERY_TO_FETCH_PERMISSION_FOR_AUTO_EXPORT);
			if (modifiedAfter != null) {
				if (querySQL.toString().contains(" WHERE ")) {
					querySQL.append("AND ");
				} else {
					querySQL.append(" WHERE ");
				}
				querySQL.append("jr.last_updated_date   >=:modifiedAfter ");
				// params.addValue("modifiedAfter", modifiedAfter);
			}

			if (name != null) {
				if (querySQL.toString().contains(" WHERE ")) {
					querySQL.append("AND ");
				} else {
					querySQL.append(" WHERE ");
				}
				querySQL.append(" jr.entity_name REGEXP :name ");
			}
			BeanPropertyRowMapper<JwsEntityRoleAssociation> mapper = new BeanPropertyRowMapper<JwsEntityRoleAssociation>(JwsEntityRoleAssociation.class);
			exportableList = importExportCrudDAO.getAllAutoExportableData(querySQL.toString(), modifiedAfter, null,
					name,mapper);

		} else {
			exportableList = importExportCrudDAO.getAllExportableData(
					CrudQueryStore.HQL_QUERY_TO_FETCH_PERMISSION_FOR_EXPORT, systemConfigIncludeList, null, null, null);

		}
		return exportableList;

	}

}
