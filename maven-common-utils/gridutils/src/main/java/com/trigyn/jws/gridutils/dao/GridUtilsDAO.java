package com.trigyn.jws.gridutils.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.gridutils.entities.GridDetails;
import com.trigyn.jws.gridutils.utility.Constants;
import com.trigyn.jws.gridutils.utility.GenericGridParams;
import com.trigyn.jws.gridutils.utility.GridUtility;

@Repository
public class GridUtilsDAO extends DBConnection {
	
	private final static Logger logger = LogManager.getLogger(GridUtilsDAO.class);

	@Autowired
	public GridUtilsDAO(DataSource dataSource) {
		super(dataSource);
	}

	/**
	 * @throws Exception
	 */
	public void getGridDetails() throws Exception {
		System.out.println();
	}

	@SuppressWarnings("unchecked")
	public Integer findCount(GridDetails gridDetails, GenericGridParams gridParams) {
		Integer rowCount = null;
		if (gridDetails.getQueryType().intValue() == Constants.queryImplementationType.VIEW.getType()) {
			String query = GridUtility.generateQueryForCount(gridDetails, gridParams);
			Object criteriaParams[] = GridUtility.generateCriteriaForCount(gridParams);
			rowCount = jdbcTemplate.queryForObject(query, criteriaParams, Integer.class);
		} else {
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(gridDetails.getGridTableName());
			Map<String, Object> inParamMap = GridUtility.generateParamMap(gridDetails, gridParams, true);
			SqlParameterSource in = new MapSqlParameterSource(inParamMap);
			Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);
			List<Map<String, Long>> list = (List<Map<String, Long>>) (Object) simpleJdbcCallResult.get("#result-set-1");
			rowCount = list.get(0).get("COUNT(*)").intValue();
		}
		return rowCount;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findAllRecords(GridDetails gridDetails, GenericGridParams gridParams) {
		List<Map<String, Object>> list = null;
		if (gridDetails.getQueryType().intValue() == Constants.queryImplementationType.VIEW.getType()) {
			String query = GridUtility.generateQueryForList(gridDetails, gridParams);
			Object criteriaParams[] = GridUtility.generateCriteriaForList(gridParams);
			list = (List<Map<String, Object>>) (Object) jdbcTemplate.queryForList(query, criteriaParams);
		} else {
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(gridDetails.getGridTableName());
			Map<String, Object> inParamMap = GridUtility.generateParamMap(gridDetails, gridParams, false);
			SqlParameterSource in = new MapSqlParameterSource(inParamMap);
			Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(in);
			list = (List<Map<String, Object>>) simpleJdbcCallResult.get("#result-set-1");
		}
		return list;
	}

	public GridDetails getGridDetails(String gridId) {

		String sql = "SELECT * FROM grid_details WHERE grid_id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { gridId }, (rs, rowNum) -> new GridDetails(rs.getString("grid_id"), rs.getString("grid_name"), rs.getString("grid_description"),
				rs.getString("grid_table_name"), rs.getString("grid_column_names"), Integer.parseInt(rs.getString("query_type"))));
	}

}