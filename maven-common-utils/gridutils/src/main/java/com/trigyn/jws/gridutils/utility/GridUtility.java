package com.trigyn.jws.gridutils.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.trigyn.jws.dbutils.utils.ApplicationContextUtils;
import com.trigyn.jws.gridutils.entities.GridDetails;
import com.trigyn.jws.templating.utils.TemplatingUtils;

@Component
public class GridUtility {

	private final static Logger logger = LogManager.getLogger(GridUtility.class);

	public static String generateQueryForCount(String dbProductName, GridDetails gridDetails, GenericGridParams gridParams, Map<String, Object> requestParam)
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

			if (gridParams.getCriteriaParams().entrySet().size() == 1) {
				query.append(joiner.toString() + " = ? ");
			} else {
				query.append(joiner.toString());
			}
		}
		if (filterParamsPresent) {
			String			groupOn			= gridParams.getFilterParams().getGroupOp();
			StringBuilder	conditionType	= new StringBuilder(groupOn).append(" ");
			if(query.toString().contains(" WHERE ")) {
				query.append("AND ");
			}else {
				query.append(" WHERE ");
			}
			int counter = 0;
			for (SearchFields sf : gridParams.getFilterParams().getRules()) {	
				if (StringUtils.isBlank(dbProductName) == false && dbProductName.equals("postgresql") == true) {
					query.append("CAST(" + sf.getField() + " AS VARCHAR) LIKE ? ");
				} else {					
					query.append("("+sf.getField() + " LIKE ? ");
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
		generateCustomCriteria(gridDetails, criteriaParamsPressent, filterParamsPresent, query, requestParam);

		return query.toString();
	}

	public static Object[] generateCriteriaForCount(GenericGridParams gridParams) {
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
		if (filterParamsPresent) {
			for (SearchFields sf : gridParams.getFilterParams().getRules()) {				
				if(sf.getData() != null) {
					String originalData = sf.getData();
					String data;
					for(String oData : originalData.split(",")) {
						data = escapeSql(oData);
						params.add("%" + data + "%");
					}
				}
			}
		}
		return params.toArray();
	}

	public static String generateQueryForList(String dbProductName, GridDetails gridDetails, GenericGridParams gridParams, Map<String, Object> requestParam)
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

			if (gridParams.getCriteriaParams().entrySet().size() == 1) {
				query.append(joiner.toString() + " = ? ");
			} else {
				query.append(joiner.toString());
			}
		}
		if (filterParamsPresent) {
			String			groupOn			= gridParams.getFilterParams().getGroupOp();
			StringBuilder	conditionType	= new StringBuilder(groupOn).append(" ");
			if(query.toString().contains(" WHERE ")) {
				query.append("AND ");
			}else {
			query.append(" WHERE ");
			}
			int counter = 0;
			for (SearchFields sf : gridParams.getFilterParams().getRules()) {	
				if (StringUtils.isBlank(dbProductName) == false && dbProductName.equals("postgresql") == true) {
					query.append("CAST(" + sf.getField() + " AS VARCHAR) LIKE ? ");
				} else {					
					query.append("("+sf.getField() + " LIKE ? ");
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
		generateCustomCriteria(gridDetails, criteriaParamsPressent, filterParamsPresent, query, requestParam);

		if ((gridParams.getSortIndex() != null && !gridParams.getSortIndex().isEmpty())
				&& (gridParams.getSortOrder() != null && !gridParams.getSortOrder().isEmpty())) {
			query.append("ORDER BY " + gridParams.getSortIndex() + " " + gridParams.getSortOrder());
		} else {
			if (StringUtils.isBlank(dbProductName) == false && dbProductName.equals("sqlserver") == true) {
				query.append("ORDER BY (SELECT NULL)");
			}
		}

		//		if(dbProductName.contains("oracle:thin")) {
		//			if(query.toString().contains("where")) {
		//				query.append(" AND ");
		//			} else {
		//				query.append(" WHERE ");
		//			}
		//			query.append(" rownum >= ? and rownum < ?");
		//		} else
		if (StringUtils.isBlank(dbProductName)) {
			query.append(" LIMIT ?,?");
		} else {
			Map<String, String> QUERY_NAME_MAP = Constants.getLimitClause();
			query.append(QUERY_NAME_MAP.get(dbProductName));
		}
		return query.toString();
	}

	private static void generateCustomCriteria(GridDetails gridDetails, boolean criteriaParamsPressent, boolean filterParamsPresent,
		StringBuilder query, Map<String, Object> requestParam) throws Exception {
		if (StringUtils.isBlank(gridDetails.getCustomFilterCriteria()) == false) {
			
			String			customCriteria	= gridDetails.getCustomFilterCriteria();
			TemplatingUtils	templatingUtils	= ApplicationContextUtils.getApplicationContext().getBean("templatingUtils",
					TemplatingUtils.class);
			customCriteria	= customCriteria.replace("<#noparse>", "");
			customCriteria	= customCriteria.replace("</#noparse>", "");
			String customFilterCriteria = templatingUtils.processTemplateContents(customCriteria, "gridCustomCriteria", requestParam);
			if (criteriaParamsPressent || filterParamsPresent) {
				query.append(" AND (");
			} else {
				query.append(" WHERE (");
			}
			query.append(customFilterCriteria).append(") ");
		}
	}

	public static Object[] generateCriteriaForList(String datasourceProductName, GenericGridParams gridParams) {
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
		if (filterParamsPresent) {
			for (SearchFields sf : gridParams.getFilterParams().getRules()) {
				String originalData = sf.getData();
				String data;
				for(String oData : originalData.split(",")) {
					data = escapeSql(oData);
					params.add("%" + data + "%");
				}
				
			}
		}
		params.add(gridParams.getStartIndex());
		params.add(gridParams.getRowsPerPage());
		return params.toArray();
	}

	public static Map<String, Object> generateParamMap(GridDetails gridDetails, GenericGridParams gridParams, boolean forCnt, Map<String, Object> requestParam)
			throws Exception {
		logger.debug("Inside GridUtility.generateParamMap(gridDetails: {}, gridParams: {}, forCnt: {})", gridDetails, gridParams, forCnt);

		boolean				criteriaParamsPressent	= gridParams.getCriteriaParams() != null && gridParams.getCriteriaParams().size() > 0
				? true
				: false;
		boolean				filterParamsPresent		= gridParams.getFilterParams() != null
				&& (gridParams.getFilterParams().getRules() != null && gridParams.getFilterParams().getRules().size() > 0) ? true : false;
		Map<String, Object>	inParamMap				= new HashMap<String, Object>();
		String[]			columnNames				= gridDetails.getGridColumnName().split(",");
		List<String>		columns					= new ArrayList<String>(Arrays.asList(columnNames));
		if (criteriaParamsPressent) {
			for (Map.Entry<String, Object> criteriaParams : gridParams.getCriteriaParams().entrySet()) {
				if (criteriaParams.getValue() instanceof String) {
					String criteriaParam = criteriaParams.getValue() == null ? null : escapeSql(criteriaParams.getValue().toString());
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
			String			customCriteria	= gridDetails.getCustomFilterCriteria();
			TemplatingUtils	templatingUtils	= ApplicationContextUtils.getApplicationContext().getBean("templatingUtils",
					TemplatingUtils.class);
			customCriteria	= customCriteria.replace("<#noparse>", "");
			customCriteria	= customCriteria.replace("</#noparse>", "");
			customCriteria	= templatingUtils.processTemplateContents(customCriteria, "gridCustomCriteria", requestParam);

			List<String>		customCriteriaList	= Stream.of(customCriteria.split("AND")).map(String::trim).collect(Collectors.toList());

			Map<String, Object>	customCriteriaMap	= new HashMap<>();
			customCriteriaList.stream().map(elem -> elem.split("=")).forEach(elem -> customCriteriaMap.put(elem[0].trim(), (elem[1].trim())));
			inParamMap.putAll(customCriteriaMap);
		}

		if (forCnt) {
			inParamMap.put("sortIndex", null);
			inParamMap.put("sortOrder", null);
			inParamMap.put("limitFrom", null);
			inParamMap.put("limitTo", null);
			inParamMap.put("forCount", 1);
		} else {
			inParamMap.put("sortIndex",
					gridParams.getSortIndex() != null && !gridParams.getSortIndex().isEmpty() ? escapeSql(gridParams.getSortIndex())
							: null);
			inParamMap.put("sortOrder",
					gridParams.getSortOrder() != null && !gridParams.getSortOrder().isEmpty() ? escapeSql(gridParams.getSortOrder())
							: null);
			inParamMap.put("limitFrom", gridParams.getStartIndex());
			inParamMap.put("limitTo", gridParams.getRowsPerPage());
			inParamMap.put("forCount", 0);
		}
		return inParamMap;
	}

	public static String escapeSql(String data) {
		data	= data.replace("\\", "\\\\\\\\");
		data	= data.replace("%", "\\%");
		data	= data.replace("'", "''");
		return data;
	}
}
