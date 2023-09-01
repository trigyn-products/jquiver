package com.trigyn.jws.quartz.jobs.dao;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.repository.DBConnection;

@Repository
public class MailScheduleDao extends DBConnection {

	public MailScheduleDao(DataSource dataSource) {
		super(dataSource);
	}
	
	public void deleteEntityMailScheduleById(String mailScheduleId) {
		StringBuilder	deleteManualQuery	= new StringBuilder(
				"DELETE FROM MailSchedule AS ms WHERE ms.mailScheduleId = :mailScheduleId ");
		Query<?>			query				= getCurrentSession().createQuery(deleteManualQuery.toString());
		query.setParameter("mailScheduleId", mailScheduleId);
		query.executeUpdate();
	}
	
	public void deleteMailScheduleId(String mailScheduleId) {
		String				deleteQuery	= "DELETE FROM mail_schedule WHERE mail_schedule_id = :mailScheduleId";
		Map<String, Object>	paramMap	= new HashMap<>();
		paramMap.put("mailScheduleId", mailScheduleId);
		namedParameterJdbcTemplate.update(deleteQuery, paramMap);
	}

}
