package com.trigyn.jws.gridutils.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.entities.DatasourceLookUpRepository;
import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.gridutils.entities.GridDetails;
import com.trigyn.jws.gridutils.utility.Constants;
import com.trigyn.jws.gridutils.utility.GenericGridParams;
import com.trigyn.jws.gridutils.utility.GridUtility;

@Repository
public class GridUtilsDAO extends DBConnection {

	private final static Logger			logger						= LogManager.getLogger(GridUtilsDAO.class);

	@Autowired
	private DatasourceLookUpRepository	datasourceLookUpRepository	= null;

	@Autowired
	public GridUtilsDAO(DataSource dataSource) {
		super(dataSource);
	}

	public void getGridDetails() throws Exception {
		System.out.println();
	}

	@SuppressWarnings("unchecked")
	public Integer findCount(GridDetails gridDetails, GenericGridParams gridParams, Map<String, Object> requestParam) throws Exception {
		Integer			rowCount		= null;
		JdbcTemplate	jdbcTemplate	= updateJdbcTemplateDataSource(gridDetails.getDatasourceId());
		String			dataSourceId	= gridDetails.getDatasourceId();
		String			dbProductName	= datasourceLookUpRepository.getDataSourceProductNameById(dataSourceId);

		if (gridDetails.getQueryType().intValue() == Constants.queryImplementationType.VIEW.getType()) {
			String	query				= GridUtility.generateQueryForCount(dbProductName, gridDetails, gridParams, requestParam);
			Object	criteriaParams[]	= GridUtility.generateCriteriaForCount(gridParams);
			rowCount = jdbcTemplate.queryForObject(query, criteriaParams, Integer.class);
		} else {
			SimpleJdbcCall	simpleJdbcCall	= new SimpleJdbcCall(jdbcTemplate).withProcedureName(gridDetails.getGridTableName());
			DataSource		dataSource		= jdbcTemplate.getDataSource();
			try (Connection connection = dataSource.getConnection();) {
				simpleJdbcCall.setCatalogName(connection.getCatalog());
			} catch (SQLException a_sqlException) {
				logger.error("Didn't find the schema name in datasource ", a_sqlException);
			}
			Map<String, Object>		inParamMap				= GridUtility.generateParamMap(gridDetails, gridParams, true, requestParam);
			SqlParameterSource		in						= new MapSqlParameterSource(inParamMap);
			Map<String, Object>		simpleJdbcCallResult	= simpleJdbcCall.execute(in);
			List<Map<String, Long>>	list					= (List<Map<String, Long>>) (Object) simpleJdbcCallResult.get("#result-set-1");
			rowCount = list.get(0).get("COUNT(*)").intValue();
		}
		return rowCount;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findAllRecords(GridDetails gridDetails, GenericGridParams gridParams, Map<String, Object> requestParam) throws Exception {
		List<Map<String, Object>>	list			= null;
		String						dataSourceId	= gridDetails.getDatasourceId();
		String						dbProductName	= datasourceLookUpRepository.getDataSourceProductNameById(dataSourceId);
		JdbcTemplate				jdbcTemplate	= updateJdbcTemplateDataSource(dataSourceId);

		if (gridDetails.getQueryType().intValue() == Constants.queryImplementationType.VIEW.getType()) {
			String	query				= GridUtility.generateQueryForList(dbProductName, gridDetails, gridParams, requestParam);
			Object	criteriaParams[]	= GridUtility.generateCriteriaForList(dbProductName, gridParams);
			list = (List<Map<String, Object>>) (Object) jdbcTemplate.queryForList(query, criteriaParams);
		} else {
			SimpleJdbcCall	simpleJdbcCall	= new SimpleJdbcCall(jdbcTemplate).withProcedureName(gridDetails.getGridTableName());
			DataSource		dataSource		= jdbcTemplate.getDataSource();
			try (Connection connection = dataSource.getConnection();) {
				simpleJdbcCall.setCatalogName(connection.getCatalog());
			} catch (SQLException a_sqlException) {
				logger.error("Didn't find the schema name in datasource ", a_sqlException);
			}
			Map<String, Object>	inParamMap				= GridUtility.generateParamMap(gridDetails, gridParams, false, requestParam);
			SqlParameterSource	in						= new MapSqlParameterSource(inParamMap);
			Map<String, Object>	simpleJdbcCallResult	= simpleJdbcCall.execute(in);
			list = (List<Map<String, Object>>) simpleJdbcCallResult.get("#result-set-1");
		}
		return list;
	}

	public GridDetails getGridDetails(String gridId) {

		String sql = "SELECT * FROM jq_grid_details WHERE grid_id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { gridId },
				(rs, rowNum) -> new GridDetails(rs.getString("grid_id"), rs.getString("grid_name"), rs.getString("grid_description"),
						rs.getString("grid_table_name"), rs.getString("grid_column_names"), Integer.parseInt(rs.getString("query_type")),
						Integer.parseInt(rs.getString("grid_type_id")), rs.getString("created_by"), rs.getDate("created_date"),
						rs.getString("datasource_id"), rs.getString("custom_filter_criteria"), rs.getString("last_updated_by"),
						rs.getDate("last_updated_ts")));
	}

	public GridDetails saveGridDetails(GridDetails gridDetails) {
		getCurrentSession().saveOrUpdate(gridDetails);
		return gridDetails;
	}

}