package app.trigyn.common.gridutils.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import app.trigyn.common.gridutils.entities.GridDetails;

public class GridUtility {
	
	private final static Logger logger = LogManager.getLogger(GridUtility.class);

	public static String generateQueryForCount(GridDetails gridDetails, GenericGridParams gridParams) {
		boolean       criteriaParamsPressent = gridParams.getCriteriaParams() != null && gridParams.getCriteriaParams().size() > 0 ? true : false;
		boolean       filterParamsPresent    = gridParams.getFilterParams() != null && (gridParams.getFilterParams().getRules() != null && gridParams.getFilterParams().getRules().size() > 0) ? true
				: false;
		StringBuilder query                  = new StringBuilder("select count(*) from " + gridDetails.getGridTableName() + " ");
		if (criteriaParamsPressent) {
			query.append("where ");
			for (Map.Entry<String, Object> criteriaParams : gridParams.getCriteriaParams().entrySet()) {
				query.append(criteriaParams.getKey() + " = ? and ");
			}
		}
		if (filterParamsPresent) {
			if (gridParams.getFilterParams().getGroupOp().equalsIgnoreCase("or")) {
				query.append(!criteriaParamsPressent ? "where " : "");
				for (SearchFields sf : gridParams.getFilterParams().getRules()) {
					query.append(sf.getField() + " like ? or ");
				}
			} else {
				query.append(!criteriaParamsPressent ? "where " : "");
				for (SearchFields sf : gridParams.getFilterParams().getRules()) {
					query.append(sf.getField() + " like ? and ");
				}
			}
		}
		if (query.lastIndexOf("and ") > -1) {
			query.replace(query.lastIndexOf("and "), query.lastIndexOf("and ") + 4, "");
		}
		if (query.lastIndexOf("or ") > -1) {
			query.replace(query.lastIndexOf("or "), query.lastIndexOf("or ") + 4, "");
		}
		return query.toString();
	}

	public static Object[] generateCriteriaForCount(GenericGridParams gridParams) {
		boolean           criteriaParamsPressent = gridParams.getCriteriaParams() != null && gridParams.getCriteriaParams().size() > 0 ? true : false;
		boolean           filterParamsPresent    = gridParams.getFilterParams() != null && (gridParams.getFilterParams().getRules() != null && gridParams.getFilterParams().getRules().size() > 0)
				? true
				: false;
		ArrayList<Object> params                 = new ArrayList<Object>();
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
				params.add("%" + escapeSql(sf.getData()) + "%");
			}
		}
		return params.toArray();
	}

	public static String generateQueryForList(GridDetails gridDetails, GenericGridParams gridParams) {
		boolean       criteriaParamsPressent = gridParams.getCriteriaParams() != null && gridParams.getCriteriaParams().size() > 0 ? true : false;
		boolean       filterParamsPresent    = gridParams.getFilterParams() != null && (gridParams.getFilterParams().getRules() != null && gridParams.getFilterParams().getRules().size() > 0) ? true
				: false;
		StringBuilder query                  = new StringBuilder("select ");
		query.append(gridDetails.getGridColumnNames() + " from " + gridDetails.getGridTableName() + " ");
		if (criteriaParamsPressent) {
			query.append("where ");
			for (Map.Entry<String, Object> criteriaParams : gridParams.getCriteriaParams().entrySet()) {
				query.append(criteriaParams.getKey() + " = ? and ");
			}
		}
		if (filterParamsPresent) {
			if (gridParams.getFilterParams().getGroupOp().equalsIgnoreCase("or")) {
				query.append(!criteriaParamsPressent ? "where " : "");
				for (SearchFields sf : gridParams.getFilterParams().getRules()) {
					query.append(sf.getField() + " like ? or ");
				}
			} else {
				query.append(!criteriaParamsPressent ? "where " : "");
				for (SearchFields sf : gridParams.getFilterParams().getRules()) {
					query.append(sf.getField() + " like ? and ");
				}
			}
		}
		if (query.lastIndexOf("and ") > -1) {
			query.replace(query.lastIndexOf("and "), query.lastIndexOf("and ") + 4, "");
		}
		if (query.lastIndexOf("or ") > -1) {
			query.replace(query.lastIndexOf("or "), query.lastIndexOf("or ") + 4, "");
		}
		if ((gridParams.getSortIndex() != null && !gridParams.getSortIndex().isEmpty()) && (gridParams.getSortOrder() != null && !gridParams.getSortOrder().isEmpty())) {
			query.append("order by " + gridParams.getSortIndex() + " " + gridParams.getSortOrder());
		}
		query.append(" limit ?,?");
		return query.toString();
	}

	public static Object[] generateCriteriaForList(GenericGridParams gridParams) {
		boolean           criteriaParamsPressent = gridParams.getCriteriaParams() != null && gridParams.getCriteriaParams().size() > 0 ? true : false;
		boolean           filterParamsPresent    = gridParams.getFilterParams() != null && (gridParams.getFilterParams().getRules() != null && gridParams.getFilterParams().getRules().size() > 0)
				? true
				: false;
		ArrayList<Object> params                 = new ArrayList<Object>();
		if (criteriaParamsPressent) {
			for (Map.Entry<String, Object> criteriaParams : gridParams.getCriteriaParams().entrySet()) {
				params.add(criteriaParams.getValue());
			}
		}
		if (filterParamsPresent) {
			for (SearchFields sf : gridParams.getFilterParams().getRules()) {
				String data = escapeSql(sf.getData());
				params.add("%" + data + "%");
			}
		}
		params.add(gridParams.getStartIndex());
		params.add(gridParams.getRowsPerPage());
		return params.toArray();
	}

	public static Map<String, Object> generateParamMap(GridDetails gridDetails, GenericGridParams gridParams, boolean forCnt) {
		boolean             criteriaParamsPressent = gridParams.getCriteriaParams() != null && gridParams.getCriteriaParams().size() > 0 ? true : false;
		boolean             filterParamsPresent    = gridParams.getFilterParams() != null && (gridParams.getFilterParams().getRules() != null && gridParams.getFilterParams().getRules().size() > 0)
				? true
				: false;
		Map<String, Object> inParamMap             = new HashMap<String, Object>();
		String[]            columnNames            = gridDetails.getGridColumnNames().split(",");
		List<String>        columns                = new ArrayList<String>(Arrays.asList(columnNames));
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
				inParamMap.put(column, null);
			}
		}

		if (forCnt) {
			inParamMap.put("sortIndex", null);
			inParamMap.put("sortOrder", null);
			inParamMap.put("limitFrom", null);
			inParamMap.put("limitTo", null);
			inParamMap.put("forCount", 1);
		} else {
			inParamMap.put("sortIndex", gridParams.getSortIndex() != null && !gridParams.getSortIndex().isEmpty() ? escapeSql(gridParams.getSortIndex()) : null);
			inParamMap.put("sortOrder", gridParams.getSortOrder() != null && !gridParams.getSortOrder().isEmpty() ? escapeSql(gridParams.getSortOrder()) : null);
			inParamMap.put("limitFrom", gridParams.getStartIndex());
			inParamMap.put("limitTo", gridParams.getRowsPerPage());
			inParamMap.put("forCount", 0);
		}
		return inParamMap;
	}

	public static String escapeSql(String data) {
		data = data.replace("\\", "\\\\\\\\");
		data = data.replace("%", "\\%");
		data = data.replace("'", "''");
		return data;
	}

}
