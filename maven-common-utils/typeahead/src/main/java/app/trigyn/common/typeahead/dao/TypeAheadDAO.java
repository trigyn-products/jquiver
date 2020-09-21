package app.trigyn.common.typeahead.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import app.trigyn.common.dbutils.repository.DBConnection;
import app.trigyn.common.typeahead.model.AutocompleteParams;

@Repository
public class TypeAheadDAO extends DBConnection {

    @Autowired
    public TypeAheadDAO(DataSource dataSource) {
        super(dataSource);
    }
    
	private static final String AUTOCOMPLETE_QUERY_SELECTOR = "SELECT ac_select_query FROM autocomplete_details WHERE ac_id = :ac_id";

	/**
	 * @param autocompleteParams
	 * @return
	 */
	public List<Map<String, Object>> getAutocompleteData(AutocompleteParams autocompleteParams) {
		NativeQuery sqlQuery = getCurrentSession().createNativeQuery(AUTOCOMPLETE_QUERY_SELECTOR);
		sqlQuery.setParameter("ac_id", autocompleteParams.getAutocompleteId());
		String list = (String) sqlQuery.uniqueResult();
		List<Map<String, Object>> displayList = getAutocompleteDetails(list, autocompleteParams);
		return displayList;
	}

	/**
	 * @param a_autocompleteQuery
	 * @param a_autocompleteParams
	 * @return
	 */
	private List<Map<String, Object>> getAutocompleteDetails(String a_autocompleteQuery, AutocompleteParams a_autocompleteParams) {

		boolean is_LimitPresent = true;
		Integer startIndex = a_autocompleteParams.getStartIndex();
		if(startIndex.intValue() == -1) {
			is_LimitPresent = false;
		}
		
		if(is_LimitPresent){
			a_autocompleteQuery = a_autocompleteQuery + " LIMIT :startIndex , :pageSize ";
		}
		
		Map<String, Object> namedParameters = new HashMap<String, Object> ();

		Set<String> additionalParameter = a_autocompleteParams.getCriteriaParams().keySet();
		for (Object object : additionalParameter) {
			namedParameters.put(object.toString(), a_autocompleteParams.getCriteriaParams().get(object.toString()));
		}
		namedParameters.put("searchText", escapeSql(a_autocompleteParams.getSearchText()));
		namedParameters.put("startIndex", a_autocompleteParams.getStartIndex());
		namedParameters.put("pageSize", a_autocompleteParams.getPageSize());
		List<Map<String, Object>> displayList = namedParameterJdbcTemplate.queryForList(a_autocompleteQuery, namedParameters);

		return displayList;
	}

	/**
	 * @param a_autocompleteParams
	 * @return
	 */
	public Integer getCountOfData(AutocompleteParams a_autocompleteParams) {
		String a_autocompleteQuery = "SELECT COUNT(*) FROM ("+getQueryForAutoComplete(a_autocompleteParams);
		boolean is_LimitPresent = true;
		Integer startIndex = a_autocompleteParams.getStartIndex();
		if(startIndex.intValue() == -1) {
			is_LimitPresent = false;
		}
		
		if(is_LimitPresent){
			a_autocompleteQuery = a_autocompleteQuery + " LIMIT :startIndex , :pageSize ";
		}
		a_autocompleteQuery += ") as count";
		Map<String,Object> namedParameters = new HashMap<String,Object>();

		Set<String> additionalParameter = a_autocompleteParams.getCriteriaParams().keySet();
		for (Object object : additionalParameter) {
			namedParameters.put(object.toString(), a_autocompleteParams.getCriteriaParams().get(object.toString()));
		}
		namedParameters.put("searchText", a_autocompleteParams.getSearchText());
		namedParameters.put("startIndex", a_autocompleteParams.getStartIndex());
		namedParameters.put("pageSize", a_autocompleteParams.getPageSize());
		Integer count = namedParameterJdbcTemplate.queryForObject(a_autocompleteQuery, namedParameters, Integer.class);
		return count;
	}
	
	/**
	 * {@code This method is used to fetch autocomplete query based on autocomplete id}
	 * @param autocompleteParams
	 * @return
	 */
	private String getQueryForAutoComplete(AutocompleteParams autocompleteParams) {
		NativeQuery sqlQuery = getCurrentSession().createNativeQuery(AUTOCOMPLETE_QUERY_SELECTOR);
		sqlQuery.setParameter("ac_id", autocompleteParams.getAutocompleteId());
		String list = (String) sqlQuery.uniqueResult();
		return list;
	}
	
	/**
	 * @param data
	 * @return
	 */
	private static String escapeSql(String data) {
		data = data.replace("\\", "\\\\\\\\");
		data = data.replace("%", "\\%");
		data = data.replace("_", "\\_");
		data = data.replace("'", "\\'");
		return data;
	}

}