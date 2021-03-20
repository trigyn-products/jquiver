package com.trigyn.jws.dynamicform.dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.dynamicform.entities.ManualEntryDetails;
import com.trigyn.jws.dynamicform.entities.ManualEntryFileAssociation;

@Repository
public class HelpManualDAO extends DBConnection {

	@Autowired
	public HelpManualDAO(DataSource dataSource) {
		super(dataSource);
	}

	public void updateManualDetails(String manualId, String name) {
		String				updateQuery	= "UPDATE jq_manual_type SET name = :name WHERE manual_id = :manualId";
		Map<String, Object>	paramMap	= new HashMap<>();
		paramMap.put("name", name);
		paramMap.put("manualId", manualId);
		namedParameterJdbcTemplate.update(updateQuery, paramMap);
	}

	public void insertManualDetails(String manualId, String name) {
		String				insertQuery	= "INSERT INTO jq_manual_type(manual_id, name, is_system_manual) VALUES (UUID(), :name ,1)";
		Map<String, Object>	paramMap	= new HashMap<>();
		paramMap.put("name", name);
		namedParameterJdbcTemplate.update(insertQuery, paramMap);
	}

	public String getManualDetailByIdAndName(String manualId, String entryName) {
		String				getManualDetails	= "SELECT manual_entry_id FROM jq_manual_entry WHERE manual_type = :manualId AND entry_name = :entryName";
		Map<String, Object>	paramMap			= new HashMap<>();
		paramMap.put("manualId", manualId);
		paramMap.put("entryName", entryName);
		String manualEntryName = null;
		manualEntryName = namedParameterJdbcTemplate.queryForObject(getManualDetails, paramMap, String.class);
		return manualEntryName;
	}

	public void saveFileAssociation(ManualEntryFileAssociation entryFileAssociation) {
		getCurrentSession().saveOrUpdate(entryFileAssociation);
	}

	public void deleteFilesByManualEntryId(String manualEntryId) {
		String				deleteQuery	= "DELETE FROM jq_manual_entry_file_association WHERE manual_entry_id = :manualEntryId";
		Map<String, Object>	paramMap	= new HashMap<>();
		paramMap.put("manualEntryId", manualEntryId);
		namedParameterJdbcTemplate.update(deleteQuery, paramMap);
	}

	public void saveManualEntry(ManualEntryDetails manualEntryDetails) {
		getCurrentSession().saveOrUpdate(manualEntryDetails);
	}

	public void deleteManualEntryId(String manualType, String manualEntryId) {
		StringBuilder	deleteManualQuery	= new StringBuilder(
				"DELETE FROM ManualEntryDetails AS med WHERE med.manualEntryId = :manualEntryId AND med.manualType = :manualType ");
		Query			query				= getCurrentSession().createQuery(deleteManualQuery.toString());
		query.setParameter("manualType", manualType);
		query.setParameter("manualEntryId", manualEntryId);
		query.executeUpdate();
	}

	public void updateSortIndex(String manualType, Integer sortIndex) {
		StringBuilder	updateManualIndexQuery	= new StringBuilder(
				"UPDATE ManualEntryDetails AS med SET med.sortIndex = med.sortIndex - 1 WHERE med.manualType = :manualType AND med.sortIndex > :sortIndex");
		Query			query					= getCurrentSession().createQuery(updateManualIndexQuery.toString());
		query.setParameter("manualType", manualType);
		query.setParameter("sortIndex", sortIndex);
		query.executeUpdate();
	}

}
