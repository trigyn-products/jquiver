package com.trigyn.jws.dynarest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dynarest.entities.JqScheduler;
import com.trigyn.jws.dynarest.repository.JqschedulerRepository;
import com.trigyn.jws.quartz.service.impl.JwsQuartzJobService;

@Service
@Transactional
public class SchedulerService {

	@Autowired
	private JqschedulerRepository	jqschedulerRepository	= null;

	@Autowired
	private JwsQuartzJobService		jobService				= null;

	@Transactional
	public boolean deleteScheduler(String schedulerID) {
		JqScheduler	jwsScheduler	= jqschedulerRepository.getOne(schedulerID);
		String		jobName			= jwsScheduler.getScheduler_name();
		String		jobGroup		= jwsScheduler.getJwsDynamicRestId();
		jqschedulerRepository.deleteById(schedulerID);
		return jobService.deleteJob(jobName, jobGroup);
	}
}
