package com.trigyn.jws.menu.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.menu.utils.Constants;

@Repository
public class ModuleDAO extends DBConnection{

	@Autowired
	public ModuleDAO(DataSource dataSource) {
		super(dataSource);
	}



	
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> findTargetTypeDetails(Integer targetTypeId) throws Exception{
		List<Map<String, Object>> targetTypeList 		= null;
		SimpleJdbcCall simpleJdbcCall 					= new SimpleJdbcCall(jdbcTemplate).withProcedureName(Constants.TARGET_MODULE_PROCEDURE_NAME);
		Map<String, Object> inParamMap 					= new HashMap<>();
		inParamMap.put("targetTypeId", targetTypeId);
		SqlParameterSource sqlInParameter				= new MapSqlParameterSource(inParamMap);

		Map<String, Object> simpleJdbcCallResult = simpleJdbcCall.execute(sqlInParameter);
		targetTypeList = (List<Map<String, Object>>) simpleJdbcCallResult.get("#result-set-1");
		return targetTypeList;
	}
	
}
