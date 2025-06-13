package com.trigyn.jws.webstarter.dao;

import java.util.Date;
import java.util.List;

import com.trigyn.jws.dbutils.vo.xml.XMLVO;

public interface GenerateModuleMasterQueries {

	List<Object> generateDynamicModuleQuery(List<String> systemConfigIncludeList, List<String> customConfigExcludeList,
			String moduleType, List<String> exportedList, XMLVO xmlVO,Date modifiedAfter,String entityType,String name,boolean autoExport) throws Exception;


	
}
