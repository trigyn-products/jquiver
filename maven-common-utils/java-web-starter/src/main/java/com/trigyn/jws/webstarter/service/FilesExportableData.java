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
import com.trigyn.jws.dynarest.entities.FileUpload;
import com.trigyn.jws.webstarter.dao.CrudQueryStore;
import com.trigyn.jws.webstarter.dao.GenerateModuleMasterQueries;
import com.trigyn.jws.webstarter.dao.ImportExportCrudDAO;
import com.trigyn.jws.webstarter.utils.Constant;

@Component
public class FilesExportableData implements GenerateModuleMasterQueries {

	private final static Logger logger = LoggerFactory.getLogger(FilesExportableData.class);

	@Autowired
	private ImportExportCrudDAO importExportCrudDAO = null;

	@Override
	public List<Object> generateDynamicModuleQuery(List<String> systemConfigIncludeList,
			List<String> customConfigExcludeList, String moduleType, List<String> exportedList, XMLVO xmlVO,
			Date modifiedAfter, String entityType, String name, boolean autoExport) throws Exception {
		List<Object> exportableList = new ArrayList<>();

		if (autoExport) {
			StringBuilder querySQL = new StringBuilder(CrudQueryStore.HQL_QUERY_TO_FETCH_FILES_DATA_FOR_AUTO_EXPORT);

			if (name != null) {
				if (querySQL.toString().contains(" WHERE ")) {
					querySQL.append("AND ");
				} else {
					querySQL.append(" WHERE ");
				}
				querySQL.append(" fu.physical_file_name REGEXP :name ");
			}
			BeanPropertyRowMapper<FileUpload> mapper = new BeanPropertyRowMapper<FileUpload>(FileUpload.class);
			exportableList = importExportCrudDAO.getAllAutoExportableData(querySQL.toString(), null, null, name,mapper);

		} else {
			if (exportedList != null && exportedList.isEmpty() == false) {

				exportableList = importExportCrudDAO.getAllExportableData(
						CrudQueryStore.HQL_QUERY_TO_FETCH_FILES_DATA_FOR_EXPORT, exportedList, null, null, null);
			}
		}
		return exportableList;

	}

}
