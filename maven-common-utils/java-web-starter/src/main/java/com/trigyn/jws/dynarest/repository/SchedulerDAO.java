package com.trigyn.jws.dynarest.repository;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.dynarest.entities.JqScheduler;

@Repository
public class SchedulerDAO extends DBConnection {

	private final static Logger				logger							= LogManager
			.getLogger(SchedulerDAO.class);

	@Autowired
	public SchedulerDAO(DataSource dataSource) {
		super(dataSource);
	}

	public JqScheduler findJqSchedulerById(String schedulerId) {
		JqScheduler scheduler =  hibernateTemplate.get(JqScheduler.class, schedulerId);
		if(scheduler != null) getCurrentSession().evict(scheduler);
		return scheduler;
	
	}

	@Transactional(readOnly = false)
	public void saveScheduler(JqScheduler scheduler) {
		if(scheduler.getSchedulerId() == null || findJqSchedulerById(scheduler.getSchedulerId()) == null) {
			getCurrentSession().save(scheduler);			
		}else {
			getCurrentSession().saveOrUpdate(scheduler);
		}
	}

}
