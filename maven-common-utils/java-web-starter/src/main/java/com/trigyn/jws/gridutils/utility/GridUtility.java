package com.trigyn.jws.gridutils.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.dbutils.utils.ApplicationContextUtils;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.gridutils.entities.GridDetails;
import com.trigyn.jws.gridutils.utility.Constants.Comparator;
import com.trigyn.jws.templating.utils.TemplatingUtils;

@Component
public class GridUtility extends DBConnection {

	public GridUtility(DataSource dataSource) {
		super(dataSource);
	}

	private final static Logger logger = LoggerFactory.getLogger(GridUtility.class);

	public String generateQueryForCount(String dbProductName, GridDetails gridDetails, GenericGridParams gridParams, Map<String, Object> requestParam)
			throws Exception {
		logger.debug("Inside GridUtility.generateQueryForCount(gridDetails: {}, gridParams: {})", gridDetails, gridParams);
		boolean			criteriaParamsPressent	= gridParams.getCriteriaParams() != null && gridParams.getCriteriaParams().size() > 0 ? true
				: false;
		boolean			filterParamsPresent		= gridParams.getFilterParams() != null
				&& (gridParams.getFilterParams().getRules() != null && gridParams.getFilterParams().getRules().size() > 0) ? true : false;
		StringBuilder	query					= new StringBuilder("SELECT COUNT(*) FROM " + gridDetails.getGridTableName() + " ");
		
		
		if (criteriaParamsPressent) {
			StringJoiner joiner = new StringJoiner(" = ? AND ", " WHERE ", " ");
			for (Map.Entry<String, Object> criteriaParams : gridParams.getCriteriaParams().entrySet()) {
				String criteriaParam = criteriaParams.getKey() == null ? null : escapeSql(criteriaParams.getKey().toString());
				joiner.add(criteriaParam);
			}

			if (gridParams.getCriteriaParams().entrySet().size() >=1) {
				query.append(joiner.toString() + " = ? ");
			
			} else {
				query.append(joiner.toString());
			}
		}
		if (filterParamsPresent) {
			if(query.toString().contains(" WHERE ")) {
				query.append("AND ");
			}else {
				query.append(" WHERE ");
			}
			
			if(gridParams.isMobile() == true) {
				String searchVal = "";
				
				for (SearchFields sf : gridParams.getFilterParams().getRules()) {	
					if(sf.getData() != null && sf.getData() != "") {
						searchVal = sf.getData();
					}
				}
				JdbcTemplate	jdbcTemplate	= updateJdbcTemplateDataSource(gridDetails.getDatasourceId());
				StringBuilder colQuery = new StringBuilder("SELECT GROUP_CONCAT(CONCAT('`', COLUMN_NAME, '` "
						+ " LIKE \"%" + searchVal  + "%\"') SEPARATOR ' OR ')"
						+ " FROM INFORMATION_SCHEMA.COLUMNS "
						+ " WHERE TABLE_NAME = '" + gridDetails.getGridTableName() + "' AND TABLE_SCHEMA = '"
						+ jdbcTemplate.getDataSource().getConnection().getCatalog() + "'");
				List<String> colList = jdbcTemplate.queryForList(colQuery.toString(), String.class);
				query.append(" (" + colList.get(0) + ")");
				
			} else {
			
				String			groupOn			= gridParams.getFilterParams().getGroupOp();
				StringBuilder	conditionType	= new StringBuilder(groupOn).append(" ");
				int counter = 0;
				for (SearchFields sf : gridParams.getFilterParams().getRules()) {	
					if (StringUtils.isBlank(dbProductName) == false && dbProductName.equals("postgresql") == true) {
						query.append("(CAST(" + sf.getField() + " AS VARCHAR) LIKE ? ");
					} else if (sf.getOp() != null && EnumUtils.isValidEnum(Comparator.class, sf.getOp())) {
						query.append("(" + sf.getField() + " " + Comparator.valueOf(sf.getOp()).getoperation() + " ? ");
					} else {
						query.append("(" + sf.getField() +" = ?");
					}
	
					if(sf.getData() != null) {
						String values[] = sf.getData().split(",");
						for(int valueCounter = 1; valueCounter < values.length; valueCounter++) {
							if (StringUtils.isBlank(dbProductName) == false && dbProductName.equals("postgresql") == true) {
								query.append("OR CAST(" + sf.getField() + " AS VARCHAR) LIKE ? ");
							} else {
							query.append( "OR " + sf.getField() + " LIKE ? ");
							}
						}
					}
					query.append(") ");
					if(counter < (gridParams.getFilterParams().getRules().size()-1)) {
						query.append(conditionType);
						counter++;
					}
				}
			}
		}
		generateCustomCriteria(gridDetails, criteriaParamsPressent, filterParamsPresent, query, requestParam);

		return query.toString();
	}

	public Object[] generateCriteriaForCount(GenericGridParams gridParams) {
		logger.debug("Inside GridUtility.generateCriteriaForCount(gridDetails: {})", gridParams);

		boolean				criteriaParamsPressent	= gridParams.getCriteriaParams() != null && gridParams.getCriteriaParams().size() > 0
				? true
				: false;
		boolean				filterParamsPresent		= gridParams.getFilterParams() != null
				&& (gridParams.getFilterParams().getRules() != null && gridParams.getFilterParams().getRules().size() > 0) ? true : false;
		ArrayList<Object>	params					= new ArrayList<Object>();
		if (criteriaParamsPressent) {
			for (Map.Entry<String, Object> criteriaParams : gridParams.getCriteriaParams().entrySet()) {
				if (criteriaParams.getValue() instanceof String) {
					String criteriaParam = criteriaParams.getValue() == null ? null : escapeSql(criteriaParams.getValue().toString());
					params.add(criteriaParam);
				} else {
					params.add(criteriaParams.getValue());
				}
			}
		}
		if (filterParamsPresent && gridParams.isMobile() == false) {
			for (SearchFields sf : gridParams.getFilterParams().getRules()) {				
				if(sf.getData() != null) {
					String originalData = sf.getData();
					String data;
					for (String oData : originalData.split(",")) {
						data = escapeSql(oData);
						if (Comparator.contain.toString().equals(sf.getOp())
								|| Comparator.dnc.toString().equals(sf.getOp())) {
							params.add("%" + data + "%");
						} else if (Comparator.sw.toString().equals(sf.getOp())) {
							params.add(data + "%");
						} else if (Comparator.ew.toString().equals(sf.getOp())) {
							params.add("%" + data);
						} else {
							params.add(data);
						}
					}
				}
			}
		}
		return params.toArray();
	}

	public String generateQueryForList(String dbProductName, GridDetails gridDetails, GenericGridParams gridParams, Map<String, Object> requestParam)
			throws Exception {
		logger.debug("Inside GridUtility.generateQueryForList(datasourceProductName: {}, gridDetails: {}, gridParams: {})", dbProductName,
				gridDetails, gridParams);

		boolean			criteriaParamsPressent	= gridParams.getCriteriaParams() != null && gridParams.getCriteriaParams().size() > 0 ? true
				: false;
		boolean			filterParamsPresent		= gridParams.getFilterParams() != null
				&& (gridParams.getFilterParams().getRules() != null && gridParams.getFilterParams().getRules().size() > 0) ? true : false;
		StringBuilder	query					= new StringBuilder("SELECT ");
		query.append(gridDetails.getGridColumnName() + " FROM " + gridDetails.getGridTableName() + " ");
		if (criteriaParamsPressent) {
			StringJoiner joiner = new StringJoiner(" = ? AND ", " WHERE ", " ");
			for (Map.Entry<String, Object> criteriaParams : gridParams.getCriteriaParams().entrySet()) {
				String criteriaParam = criteriaParams.getKey() == null ? null : escapeSql(criteriaParams.getKey().toString());
				joiner.add(criteriaParam);
			}

			if (gridParams.getCriteriaParams().entrySet().size() >=1) {
				query.append(joiner.toString() + " = ? ");
			} else {
				query.append(joiner.toString());
			}
		}
		if (filterParamsPresent) {
			if(query.toString().contains(" WHERE ")) {
				query.append("AND ");
			}else {
				query.append(" WHERE ");
			}
			
			if(gridParams.isMobile() == true) {
				String searchVal = "";
				
				for (SearchFields sf : gridParams.getFilterParams().getRules()) {	
					if(sf.getData() != null && sf.getData() != "") {
						searchVal = sf.getData();
					}
				}
				JdbcTemplate	jdbcTemplate	= updateJdbcTemplateDataSource(gridDetails.getDatasourceId());
				StringBuilder colQuery = new StringBuilder("SELECT GROUP_CONCAT(CONCAT('`', COLUMN_NAME, '` "
						+ " LIKE \"%" + searchVal  + "%\"') SEPARATOR ' OR ')"
						+ " FROM INFORMATION_SCHEMA.COLUMNS "
						+ " WHERE TABLE_NAME = '" + gridDetails.getGridTableName() + "' AND TABLE_SCHEMA = '"
						+ jdbcTemplate.getDataSource().getConnection().getCatalog() + "'");
				List<String> colList = jdbcTemplate.queryForList(colQuery.toString(), String.class);
				query.append(" (" + colList.get(0) + ")");
				
			} else {
				String			groupOn			= gridParams.getFilterParams().getGroupOp();
				StringBuilder	conditionType	= new StringBuilder(groupOn).append(" ");
				int counter = 0;
				for (SearchFields sf : gridParams.getFilterParams().getRules()) {	
					if (StringUtils.isBlank(dbProductName) == false && dbProductName.equals("postgresql") == true) {
						query.append("(CAST(" + sf.getField() + " AS VARCHAR) LIKE ? ");
					} else if (sf.getOp() != null && EnumUtils.isValidEnum(Comparator.class, sf.getOp())) {
						query.append("(" + sf.getField() + " " + Comparator.valueOf(sf.getOp()).getoperation() + " ? ");
					} else {
						query.append("(" + sf.getField() +" = ?");
					}
	
					if(sf.getData() != null) {
						String values[] = sf.getData().split(",");
						for(int valueCounter = 1; valueCounter < values.length; valueCounter++) {
							if (StringUtils.isBlank(dbProductName) == false && dbProductName.equals("postgresql") == true) {
								query.append("OR CAST(" + sf.getField() + " AS VARCHAR) LIKE ? ");
							}  
							else {
							query.append( "OR " + sf.getField() + " LIKE ? ");
							}
						}
					}
					query.append(") ");
					if(counter < (gridParams.getFilterParams().getRules().size()-1)) {
						query.append(conditionType);
						counter++;
					}
				}
			}
		}
		generateCustomCriteria(gridDetails, criteriaParamsPressent, filterParamsPresent, query, requestParam);

		if ((gridParams.getSortIndex() != null && !gridParams.getSortIndex().isEmpty())
				&& (gridParams.getSortOrder() != null && !gridParams.getSortOrder().isEmpty())) {
			query.append("ORDER BY " + gridParams.getSortIndex() + " " + gridParams.getSortOrder());
		} else {
			if (StringUtils.isBlank(dbProductName) == false && dbProductName.equals("sqlserver") == true) {
				query.append("ORDER BY (SELECT NULL)");
			}
		}

		if (StringUtils.isBlank(dbProductName)) {
			query.append(" LIMIT ?,?");
		} else {
			Map<String, String> QUERY_NAME_MAP = Constants.getLimitClause();
			query.append(QUERY_NAME_MAP.get(dbProductName));
		}
		return query.toString();
	}

	private void generateCustomCriteria(GridDetails gridDetails, boolean criteriaParamsPressent,
			boolean filterParamsPresent, StringBuilder query, Map<String, Object> requestParam)
			throws Exception, CustomStopException {
		try {
			if (StringUtils.isBlank(gridDetails.getCustomFilterCriteria()) == false) {

				String customCriteria = gridDetails.getCustomFilterCriteria();
				TemplatingUtils templatingUtils = ApplicationContextUtils.getApplicationContext()
						.getBean("templatingUtils", TemplatingUtils.class);
				customCriteria = customCriteria.replace("<#noparse>", "");
				customCriteria = customCriteria.replace("</#noparse>", "");
				String customFilterCriteria = templatingUtils.processTemplateContents(customCriteria,
						"gridCustomCriteria", requestParam);
				if (criteriaParamsPressent || filterParamsPresent) {
					query.append(" AND (");
				} else {
					query.append(" WHERE (");
				}
				query.append(customFilterCriteria).append(") ");
			}
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in generateCustomCriteria.", custStopException);
			throw custStopException;
		}
	}

	public Object[] generateCriteriaForList(String datasourceProductName, GenericGridParams gridParams) {
		logger.debug("Inside GridUtility.generateCriteriaForList(datasourceProductName: {}, gridParams: {})", datasourceProductName,
				gridParams);

		boolean				criteriaParamsPressent	= gridParams.getCriteriaParams() != null && gridParams.getCriteriaParams().size() > 0
				? true
				: false;
		boolean				filterParamsPresent		= gridParams.getFilterParams() != null
				&& (gridParams.getFilterParams().getRules() != null && gridParams.getFilterParams().getRules().size() > 0) ? true : false;
		ArrayList<Object>	params					= new ArrayList<Object>();
		if (criteriaParamsPressent) {
			for (Map.Entry<String, Object> criteriaParams : gridParams.getCriteriaParams().entrySet()) {
				params.add(criteriaParams.getValue());
			}
		}
		if (filterParamsPresent && gridParams.isMobile() == false) {
			for (SearchFields sf : gridParams.getFilterParams().getRules()) {
				String originalData = sf.getData();
				String data;
				for (String oData : originalData.split(",")) {
					data = escapeSql(oData);
					if (Comparator.contain.toString().equals(sf.getOp())
							|| Comparator.dnc.toString().equals(sf.getOp())) {
						params.add("%" + data + "%");
					} else if (Comparator.sw.toString().equals(sf.getOp())) {
						params.add(data + "%");
					} else if (Comparator.ew.toString().equals(sf.getOp())) {
						params.add("%" + data);
					} else {
						params.add(data);
					}
				}

			}
		}
		params.add(gridParams.getStartIndex());
		params.add(gridParams.getRowsPerPage());
		return params.toArray();
	}

	public Map<String, Object> generateParamMap(GridDetails gridDetails, GenericGridParams gridParams,
			boolean forCnt, Map<String, Object> requestParam) throws Exception, CustomStopException {
		logger.debug("Inside GridUtility.generateParamMap(gridDetails: {}, gridParams: {}, forCnt: {})", gridDetails,
				gridParams, forCnt);

		boolean criteriaParamsPressent = gridParams.getCriteriaParams() != null
				&& gridParams.getCriteriaParams().size() > 0 ? true : false;
		boolean filterParamsPresent = gridParams.getFilterParams() != null
				&& (gridParams.getFilterParams().getRules() != null
						&& gridParams.getFilterParams().getRules().size() > 0) ? true : false;
		Map<String, Object> inParamMap = new HashMap<String, Object>();
		String[] columnNames = gridDetails.getGridColumnName().split(",");
		List<String> columns = new ArrayList<String>(Arrays.asList(columnNames));
		try {
			if (criteriaParamsPressent) {
				for (Map.Entry<String, Object> criteriaParams : gridParams.getCriteriaParams().entrySet()) {
					if (criteriaParams.getValue() instanceof String) {
						String criteriaParam = criteriaParams.getValue() == null ? null
								: escapeSql(criteriaParams.getValue().toString());
						inParamMap.put(criteriaParams.getKey(), criteriaParam);
					} else {
						inParamMap.put(criteriaParams.getKey(), criteriaParams.getValue());
					}
				}
			}
			if (filterParamsPresent) {
				for (SearchFields sf : gridParams.getFilterParams().getRules()) {
					inParamMap.put(sf.getField(), escapeSql(sf.getData()));
					columns.remove(sf.getField());
				}
			}

			for (String column : columns) {
				if (column.length() > 0) {
					if (inParamMap.containsKey(column) == false) {
						inParamMap.put(column, null);
					}
				}
			}

			if (StringUtils.isBlank(gridDetails.getCustomFilterCriteria()) == false) {
				String customCriteria = gridDetails.getCustomFilterCriteria();
				TemplatingUtils templatingUtils = ApplicationContextUtils.getApplicationContext()
						.getBean("templatingUtils", TemplatingUtils.class);
				customCriteria = customCriteria.replace("<#noparse>", "");
				customCriteria = customCriteria.replace("</#noparse>", "");
				customCriteria = templatingUtils.processTemplateContents(customCriteria, "gridCustomCriteria",
						requestParam);

				List<String> customCriteriaList = Stream.of(customCriteria.split("AND")).map(String::trim)
						.collect(Collectors.toList());

				Map<String, Object> customCriteriaMap = new HashMap<>();
				customCriteriaList.stream().map(elem -> elem.split("="))
						.forEach(elem -> customCriteriaMap.put(elem[0].trim(), (elem[1].trim())));
				inParamMap.putAll(customCriteriaMap);
			}
			
			if(gridParams.isMobile() == true) {
				inParamMap.put("isMobile", 1);
			} else {
				inParamMap.put("isMobile", 0);
			}

			if (forCnt) {
				inParamMap.put("sortIndex", null);
				inParamMap.put("sortOrder", null);
				inParamMap.put("limitFrom", null);
				inParamMap.put("limitTo", null);
				inParamMap.put("forCount", 1);
			} else {
				inParamMap.put("sortIndex",
						gridParams.getSortIndex() != null && !gridParams.getSortIndex().isEmpty()
								? escapeSql(gridParams.getSortIndex())
								: null);
				inParamMap.put("sortOrder",
						gridParams.getSortOrder() != null && !gridParams.getSortOrder().isEmpty()
								? escapeSql(gridParams.getSortOrder())
								: null);
				inParamMap.put("limitFrom", gridParams.getStartIndex());
				inParamMap.put("limitTo", gridParams.getRowsPerPage());
				inParamMap.put("forCount", 0);
			}
			return inParamMap;
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in generateParamMap.", custStopException);
			throw custStopException;
		}
	}

	public String escapeSql(String data) {
		data	= data.replace("\\", "\\\\\\\\");
		data	= data.replace("%", "\\%");
		data	= data.replace("'", "''");
		return data;
	}
}
