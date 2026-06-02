package com.trigyn.jws.gridutils.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.entities.DatasourceLookUpRepository;
import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.gridutils.entities.GridDetails;
import com.trigyn.jws.gridutils.utility.Constants;
import com.trigyn.jws.gridutils.utility.GenericGridParams;
import com.trigyn.jws.gridutils.utility.GridUtility;

@Repository
public class GridUtilsDAO extends DBConnection {

	public GridUtilsDAO(DataSource dataSource) {
		super(dataSource);
	}

	@Value("${jquiver.redis.cache.enabled:false}")
	private boolean						isCacheEnabled;

	private final static Logger			logger						= LoggerFactory.getLogger(GridUtilsDAO.class);

	@Autowired
	private DatasourceLookUpRepository	datasourceLookUpRepository	= null;

	@Autowired
	private GridUtility					gridUtility					= null;

	@SuppressWarnings("unchecked")
	public Integer findCount(GridDetails gridDetails, GenericGridParams gridParams, Map<String, Object> requestParam)
			throws Exception {
		Integer			rowCount		= null;
		JdbcTemplate	jdbcTemplate	= updateJdbcTemplateDataSource(gridDetails.getDatasourceId());
		String			dataSourceId	= gridDetails.getDatasourceId();
		String			dbProductName	= datasourceLookUpRepository.getDataSourceProductNameById(dataSourceId);

		if (gridDetails.getQueryType().intValue() == Constants.queryImplementationType.VIEW.getType()) {
			String	query				= gridUtility.generateQueryForCount(dbProductName, gridDetails, gridParams,
					requestParam);
			Object	criteriaParams[]	= gridUtility.generateCriteriaForCount(gridParams);
			rowCount = jdbcTemplate.queryForObject(query, criteriaParams, Integer.class);
		} else {
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
					.withProcedureName(gridDetails.getGridTableName());
			try (Connection connection = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());) {
				simpleJdbcCall.setCatalogName(connection.getCatalog());
			} catch (SQLException a_sqlException) {
				logger.error("Didn't find the schema name in datasource ", a_sqlException);
			}
			Map<String, Object>		inParamMap				= gridUtility.generateParamMap(gridDetails, gridParams,
					true, requestParam);
			SqlParameterSource		in						= new MapSqlParameterSource(inParamMap);
			Map<String, Object>		simpleJdbcCallResult	= simpleJdbcCall.execute(in);
			List<Map<String, Long>>	list					= (List<Map<String, Long>>) (Object) simpleJdbcCallResult
					.get("#result-set-1");
			rowCount = list.get(0).get("COUNT(*)").intValue();
		}
		return rowCount;
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findAllRecords(GridDetails gridDetails, GenericGridParams gridParams,
			Map<String, Object> requestParam) throws Exception {
		List<Map<String, Object>>	list			= null;
		List<Map<String, Object>>	formatedList	= null;
		String						dataSourceId	= gridDetails.getDatasourceId();
		String						dbProductName	= datasourceLookUpRepository
				.getDataSourceProductNameById(dataSourceId);
		JdbcTemplate				jdbcTemplate	= updateJdbcTemplateDataSource(dataSourceId);

		if (gridDetails.getQueryType().intValue() == Constants.queryImplementationType.VIEW.getType()) {
			String	query				= gridUtility.generateQueryForList(dbProductName, gridDetails, gridParams,
					requestParam);
			Object	criteriaParams[]	= gridUtility.generateCriteriaForList(dbProductName, gridParams);
			list = (List<Map<String, Object>>) (Object) jdbcTemplate.queryForList(query, criteriaParams);
			for (Map<String, Object> row : list) {
				for (Map.Entry<String, Object> entry : row.entrySet()) {
					Object value = entry.getValue();
					if ("sqlserver".equalsIgnoreCase(dbProductName) && value != null) {
						if (value.toString().contains("<")) {
							String escapedXml = StringEscapeUtils.escapeHtml4(value.toString());
							entry.setValue(escapedXml);
						}
					}
					if (null != dbProductName && dbProductName.toLowerCase().contains("postgresql")) {
						String stringValue = tryConvertToString(value);
						entry.setValue(stringValue);
					}

				}
			}
		} else {
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
					.withProcedureName(gridDetails.getGridTableName());
			try (Connection connection = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());) {
				simpleJdbcCall.setCatalogName(connection.getCatalog());
			} catch (SQLException a_sqlException) {
				logger.error("Didn't find the schema name in datasource ", a_sqlException);
			}
			Map<String, Object>	inParamMap				= gridUtility.generateParamMap(gridDetails, gridParams, false,
					requestParam);
			SqlParameterSource	in						= new MapSqlParameterSource(inParamMap);
			Map<String, Object>	simpleJdbcCallResult	= simpleJdbcCall.execute(in);
			list = (List<Map<String, Object>>) simpleJdbcCallResult.get("#result-set-1");
		}
		return list;
	}

	private String tryConvertToString(Object value) {
		if (value == null) {
			return null;
		}

		try {
			if (value instanceof java.sql.Clob clob) {
				return clob.getSubString(1, (int) clob.length());
			} else if (value instanceof java.sql.SQLXML xml) {
				String	xmlContent	= ((SQLXML) xml).getString();
			   String	escapedXml	= StringEscapeUtils.escapeHtml4(xmlContent);
				return escapedXml;
			} else if ("org.postgresql.util.PGobject".equals(value.getClass().getName())) {
				// PostgreSQL JSON/JSONB type
				return value.toString();
			} else {
				return value.toString();
			}
		} catch (Exception e) {
			return null;
		}
	}

	// public GridDetails getGridDetails(String gridId) {
	//
	// String sql = "SELECT * FROM jq_grid_details WHERE grid_id = ?";
	// return jdbcTemplate.queryForObject(sql, new Object[] { gridId },
	// (rs, rowNum) -> new GridDetails(rs.getString("grid_id"),
	// rs.getString("grid_name"), rs.getString("grid_description"),
	// rs.getString("grid_table_name"), rs.getString("grid_column_names"),
	// Integer.parseInt(rs.getString("query_type")),
	// Integer.parseInt(rs.getString("grid_type_id")), rs.getString("created_by"),
	// rs.getDate("created_date"),
	// rs.getString("datasource_id"), rs.getString("custom_filter_criteria"),
	// rs.getString("last_updated_by"),
	// rs.getDate("last_updated_ts")));
	// }

	public GridDetails getGridDetails(String gridId) {
		GridDetails details = getCurrentSession().get(GridDetails.class, gridId);
		logger.debug("Inside GridUtilsDAO::getGridDetials");
		return details;
	}

}