package com.trigyn.jws.dbutils.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.entities.ModuleListing;
import com.trigyn.jws.dbutils.entities.ModuleListingI18n;
import com.trigyn.jws.dbutils.utils.Constant;

@Repository
public class ModuleDAO extends DBConnection {

	public static final String	TARGET_MODULE_PROCEDURE_NAME						= "CALL moduleTargetType(:targetLookupId, :targetTypeId)";

	public static final String	HQL_QUERY_TO_GET_MAX_MODULE_SEQUENCE				= "SELECT MAX(ml.sequence) AS maxSequence FROM ModuleListing AS ml WHERE ml.parentId IS NULL AND "
			+ " ml.isHomePage = :isNotHomePage ";

	public static final String	HQL_QUERY_TO_GET_MAX_MODULE_SEQUENCE_BY_PARENT_ID	= "SELECT MAX(ml.sequence) AS maxSequence FROM ModuleListing AS ml WHERE ml.parentId = :parentModuleId ";

	@Autowired
	public ModuleDAO(DataSource dataSource) {
		super(dataSource);
	}

	public List<Map<String, Object>> findTargetTypeDetails(Integer targetLookupId, String targetTypeId) throws Exception {
		List<Map<String, Object>>	targetTypeList	= null;
		Map<String, Object>			inParamMap		= new HashMap<>();
		inParamMap.put("targetLookupId", targetLookupId);
		inParamMap.put("targetTypeId", targetTypeId);
		targetTypeList = namedParameterJdbcTemplate.queryForList(TARGET_MODULE_PROCEDURE_NAME, inParamMap);
		return targetTypeList;
	}

	public Integer getModuleMaxSequence() throws Exception {
		Integer	sequence	= null;
		Query	query		= getCurrentSession().createQuery(HQL_QUERY_TO_GET_MAX_MODULE_SEQUENCE);
		query.setParameter("isNotHomePage", Constant.IS_NOT_HOME_PAGE);
		Object versionIdObj = query.uniqueResult();
		if (versionIdObj != null) {
			sequence = Integer.parseInt(versionIdObj.toString());
		}
		return sequence;
	}

	public Integer getMaxSequenceByParent(String parentModuleId) throws Exception {
		Integer	sequence	= null;
		Query	query		= getCurrentSession().createQuery(HQL_QUERY_TO_GET_MAX_MODULE_SEQUENCE_BY_PARENT_ID);
		query.setParameter("parentModuleId", parentModuleId);
		Object versionIdObj = query.uniqueResult();
		if (versionIdObj != null) {
			sequence = Integer.parseInt(versionIdObj.toString());
		}
		return sequence;
	}

	public ModuleListing findModuleListingById(String moduleId) {
		ModuleListing moduleListing =  hibernateTemplate.get(ModuleListing.class, moduleId);
		if(moduleListing != null) getCurrentSession().evict(moduleListing);
		return moduleListing;
	}

	@Transactional(readOnly = false)
	public void saveModuleListing(ModuleListing moduleListing, List<ModuleListingI18n>	moduleListingI18ns) {
		if(moduleListing.getModuleId() == null || findModuleListingById(moduleListing.getModuleId()) == null) {
			getCurrentSession().save(moduleListing);			
		}else {
			getCurrentSession().saveOrUpdate(moduleListing);
		}
		
		deleteModuleListingI18n(moduleListing.getModuleId());
		for(ModuleListingI18n moduleI18ns : moduleListingI18ns) {
			getCurrentSession().saveOrUpdate(moduleI18ns);
		}
	}

	public void deleteModuleListingI18n(String moduleId) {
		Query query = getCurrentSession().createQuery("DELETE FROM ModuleListingI18n AS mli WHERE mli.id.moduleId = :moduleId");
		query.setParameter("moduleId", moduleId);
		query.executeUpdate();
	
	}


}
