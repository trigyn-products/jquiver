package com.trigyn.jws.dynarest.repository;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.dynarest.entities.JqScheduler;

@Repository
public class SchedulerDAO extends DBConnection {

	public SchedulerDAO(DataSource dataSource) {
		super(dataSource);
	}

	private final static Logger				logger							= LoggerFactory
			.getLogger(SchedulerDAO.class);

	public JqScheduler findJqSchedulerById(String schedulerId) {
		JqScheduler scheduler =  getCurrentSession().get(JqScheduler.class, schedulerId);
		if(scheduler != null) getCurrentSession().evict(scheduler);
		return scheduler;
	
	}

	@Transactional(readOnly = false)
	public void saveScheduler(JqScheduler scheduler) {
		if(scheduler.getSchedulerId() == null || findJqSchedulerById(scheduler.getSchedulerId()) == null) {
			getCurrentSession().persist(scheduler);			
		}else {
			getCurrentSession().merge(scheduler);
		}
	}

}
