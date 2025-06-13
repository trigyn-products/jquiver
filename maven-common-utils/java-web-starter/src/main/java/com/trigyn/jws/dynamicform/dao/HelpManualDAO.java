package com.trigyn.jws.dynamicform.dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.query.MutationQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.dbutils.vo.xml.ManualEntryDetailsExportVO;
import com.trigyn.jws.dynamicform.entities.ManualEntryDetails;
import com.trigyn.jws.dynamicform.entities.ManualEntryFileAssociation;
import com.trigyn.jws.dynamicform.entities.ManualType;

@Repository
public class HelpManualDAO extends DBConnection {

	public HelpManualDAO(DataSource dataSource) {
		super(dataSource);
	}

	public void updateManualDetails(String manualId, String name) {
		String updateQuery = "UPDATE jq_manual_type SET name = :name WHERE manual_id = :manualId";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("name", name);
		paramMap.put("manualId", manualId);
		namedParameterJdbcTemplate.update(updateQuery, paramMap);
	}

	public void insertManualDetails(String manualId, String name) {
		String insertQuery = "INSERT INTO jq_manual_type(manual_id, name, is_system_manual) VALUES (UUID(), :name ,1)";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("name", name);
		namedParameterJdbcTemplate.update(insertQuery, paramMap);
	}

	public String getManualDetailByIdAndName(String manualId, String entryName) {
		String getManualDetails = "SELECT manual_entry_id FROM jq_manual_entry WHERE manual_Id = :manualId AND entry_name = :entryName";
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
		String deleteQuery = "DELETE FROM jq_manual_entry_file_association WHERE manual_entry_id = :manualEntryId";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("manualEntryId", manualEntryId);
		namedParameterJdbcTemplate.update(deleteQuery, paramMap);
	}

	public void saveManualEntry(ManualEntryDetails manualEntryDetails) {
		getCurrentSession().saveOrUpdate(manualEntryDetails);
	}
	
	public void saveManualEntryExportVO(ManualEntryDetailsExportVO manualEntryDetailsExportVO) {
		getCurrentSession().saveOrUpdate(manualEntryDetailsExportVO);
	}

	public void deleteManualEntryId(String manualType, String manualEntryId) {
		StringBuilder deleteManualQuery = new StringBuilder(
				"DELETE FROM ManualEntryDetails AS med WHERE med.manualEntryId = :manualEntryId AND med.manualId = :manualType ");
		MutationQuery query = getCurrentSession().createMutationQuery(deleteManualQuery.toString());
		query.setParameter("manualType", manualType);
		query.setParameter("manualEntryId", manualEntryId);
		query.executeUpdate();
	}

	public void updateSortIndex(String manualType, Integer sortIndex) {
		StringBuilder updateManualIndexQuery = new StringBuilder(
				"UPDATE ManualEntryDetails AS med SET med.sortIndex = med.sortIndex - 1 WHERE med.manualId = :manualType AND med.sortIndex > :sortIndex");
		MutationQuery query = getCurrentSession().createMutationQuery(updateManualIndexQuery.toString());
		query.setParameter("manualType", manualType);
		query.setParameter("sortIndex", sortIndex);
		query.executeUpdate();
	}

	public ManualType getManualType(String manualTypeId) {
		ManualType manualType = getCurrentSession().get(ManualType.class, manualTypeId);
		if (manualType != null)
			getCurrentSession().evict(manualType);
		return manualType;
	}

	@Transactional(readOnly = false)
	public void saveManualType(ManualType manualType) {
		if (manualType.getManualId() == null || getManualType(manualType.getManualId()) == null) {
			getCurrentSession().persist(manualType);
		} else {
			getCurrentSession().merge(manualType);
		}
	}
	
	public ManualEntryDetails getManualEntryDetails(String medId) {
		ManualEntryDetails med = getCurrentSession().get(ManualEntryDetails.class, medId);
		if (med != null)
			getCurrentSession().evict(med);
		return med;
	}

	@Transactional(readOnly = false)
	public void saveManualEntryDetails(ManualEntryDetails med) {
		if (med.getManualEntryId() == null || getManualEntryDetails(med.getManualEntryId()) == null) {
			getCurrentSession().persist(med);
		} else {
			getCurrentSession().merge(med);
		}
	}

	public String getSortIndex(String manualTypeId) {
		String getManualDetails = "SELECT MAX(sort_index) FROM jq_manual_entry WHERE manual_id = :manualId ";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("manualId", manualTypeId);
		String sortIndex = null;
		sortIndex = namedParameterJdbcTemplate.queryForObject(getManualDetails, paramMap, String.class);
		if (null == sortIndex) {
			return "0";
		}
		return sortIndex;
	}

	public void deleteHelpManualEntryId(String manualType, String manualEntryId) {
		StringBuilder deleteManualQuery = new StringBuilder(
				"DELETE FROM ManualEntryDetails AS med WHERE med.manualEntryId = :manualEntryId AND med.manualId = :manualType ");
		MutationQuery query = getCurrentSession().createMutationQuery(deleteManualQuery.toString());
		query.setParameter("manualType", manualType);
		query.setParameter("manualEntryId", manualEntryId);
		query.executeUpdate();
	}
	
	public void deleteHelpManualIdEntries(String manualType) {
		StringBuilder deleteManualQuery = new StringBuilder(
				"DELETE FROM ManualEntryDetails AS med WHERE med.manualId = :manualType ");
		MutationQuery query = getCurrentSession().createMutationQuery(deleteManualQuery.toString());
		query.setParameter("manualType", manualType);
		query.executeUpdate();
	}

	public void updateHelpManualDetails(String manualEntryId, String entryName, String entryContent, String manualId) {
		String updateQuery = "UPDATE jq_manual_entry SET entry_name = :entryName , entry_content = :entryContent WHERE manual_id = :manualId and manual_entry_id = :manualEntryId";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("entryName", entryName);
		paramMap.put("entryContent", entryContent);
		paramMap.put("manualEntryId", manualEntryId);
		paramMap.put("manualId", manualId);
		namedParameterJdbcTemplate.update(updateQuery, paramMap);
	}

}
