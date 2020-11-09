package com.trigyn.jws.webstarter.dao;

import java.util.List;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.repository.DBConnection;

@Repository
public class ImportExportCrudDAO extends DBConnection {
	
	private final static Logger logger = LogManager.getLogger(ImportExportCrudDAO.class);

    @Autowired
    public ImportExportCrudDAO(DataSource dataSource) {
        super(dataSource);
    }

	public List<Object> getAllExportableData(String querySQL, List<String> includeSystemConfigList, Integer systemConfigType,
			List<String> excludeCustomConfigList, Integer customConfigType)  throws Exception{
		
		Query query = getCurrentSession().createQuery(querySQL);		
		if(excludeCustomConfigList != null) query.setParameterList("excludeCustomConfigList", excludeCustomConfigList);
		if(customConfigType != null) query.setParameter("customConfigType", customConfigType);
		if(includeSystemConfigList != null) query.setParameterList("includeSystemConfigList", includeSystemConfigList);
		if(systemConfigType != null) query.setParameter("systemConfigType", systemConfigType);
		
		return query.list();
	}

	public List<Object> getExportableDataWithIntegerList(String querySQL, List<Integer> includeSystemConfigList, Integer systemConfigType, 
			List<Integer> excludeCustomConfigList, Integer customConfigType)  throws Exception{
		
		Query query = getCurrentSession().createQuery(querySQL);		
		if(excludeCustomConfigList != null) query.setParameterList("excludeCustomConfigList", excludeCustomConfigList);
		if(customConfigType != null) query.setParameter("customConfigType", customConfigType);
		if(includeSystemConfigList != null) query.setParameterList("includeSystemConfigList", includeSystemConfigList);
		if(systemConfigType != null) query.setParameter("systemConfigType", systemConfigType);
		
		return query.list();
	}
    
	public List<Object> getRBExportableData(String querySQL, List<String> includeSystemConfigList, 
			List<String> excludeCustomConfigList)  throws Exception{
		
		Query query = getCurrentSession().createQuery(querySQL);		
		if(excludeCustomConfigList != null) query.setParameterList("excludeCustomConfigList", excludeCustomConfigList);
		query.setParameter("customConfigType", "jws.%");
		if(includeSystemConfigList != null) query.setParameterList("includeSystemConfigList", includeSystemConfigList);
		query.setParameter("systemConfigType", "jws.%");
		
		return query.list();
	}
    
}
