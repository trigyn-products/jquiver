package com.trigyn.jws.webstarter.dao;

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
public class CaptchaDAO extends DBConnection {

	public CaptchaDAO(DataSource dataSource) {
		super(dataSource);
	}

//	public void updateCaptchaDetails(String manualId, String name) {
//		String updateQuery = "UPDATE jq_manual_type SET name = :name WHERE manual_id = :manualId";
//		Map<String, Object> paramMap = new HashMap<>();
//		paramMap.put("name", name);
//		paramMap.put("manualId", manualId);
//		namedParameterJdbcTemplate.update(updateQuery, paramMap);
//	}


	public void deleteExpiredCaptcha(int captchExpiry) {
		String deleteQuery = "DELETE FROM jq_captcha WHERE request_time < NOW() - INTERVAL :interval MINUTE";
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("interval", captchExpiry);
		namedParameterJdbcTemplate.update(deleteQuery, paramMap);
	}

	
	
	

}
