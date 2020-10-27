package com.trigyn.jws.webstarter.dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.webstarter.entities.ManualEntryDetails;
import com.trigyn.jws.webstarter.entities.ManualEntryFileAssociation;

@Repository
public class HelpManualDAO extends DBConnection {

	@Autowired
	public HelpManualDAO(DataSource dataSource) {
		super(dataSource);
	}

	public void updateManualDetails(String manualId, String name) {
		String updateQuery = "UPDATE manual_type SET name = :name WHERE manual_id = :manualId";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("name", name);
		paramMap.put("manualId", manualId);
		namedParameterJdbcTemplate.update(updateQuery, paramMap);
	}

	public void insertManualDetails(String manualId, String name) {
		String insertQuery = "INSERT INTO manual_type(manual_id, name, is_system_manual) VALUES (UUID(), :name ,0)";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("name", name);
		namedParameterJdbcTemplate.update(insertQuery, paramMap);
	}

	public String getManualDetailByIdAndName(String manualId, String entryName) {
		String getManualDetails = "SELECT manual_entry_id FROM manual_entry WHERE manual_type = :manualId AND entry_name = :entryName";
		Map<String, Object> paramMap = new HashMap<>();
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
		String deleteQuery = "DELETE FROM manual_entry_file_association WHERE manual_entry_id = :manualEntryId";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("manualEntryId", manualEntryId);
		namedParameterJdbcTemplate.update(deleteQuery, paramMap);
	}

	public void saveManualEntry(ManualEntryDetails manualEntryDetails) {
		getCurrentSession().saveOrUpdate(manualEntryDetails);
	}

}
