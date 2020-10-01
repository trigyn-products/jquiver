package com.trigyn.jws.dbutils.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ModuleDAO extends DBConnection{

	public static final String TARGET_MODULE_PROCEDURE_NAME = "CALL moduleTargetType(:targetLookupId, :targetTypeId)";
	
	@Autowired
	public ModuleDAO(DataSource dataSource) {
		super(dataSource);
	}
	
	public List<Map<String, Object>> findTargetTypeDetails(Integer targetLookupId, String targetTypeId) throws Exception{
		List<Map<String, Object>> targetTypeList 		= null;
		Map<String, Object> inParamMap 					= new HashMap<>();
		inParamMap.put("targetLookupId", targetLookupId);
		inParamMap.put("targetTypeId", targetTypeId);
		targetTypeList = namedParameterJdbcTemplate.queryForList(TARGET_MODULE_PROCEDURE_NAME, inParamMap);
		return targetTypeList;
	}
	
}
