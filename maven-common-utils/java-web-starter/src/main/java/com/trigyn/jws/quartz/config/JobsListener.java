package com.trigyn.jws.quartz.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.springframework.stereotype.Component;

@Component
public class JobsListener implements JobListener{
	
	private final static Logger LOGGER = LogManager.getLogger(JobsListener.class);

	@Override
	public String getName() {
		return "globalJob";
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		LOGGER.debug("JobsListener.jobToBeExecuted()");
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		LOGGER.debug("JobsListener.jobExecutionVetoed()");
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		LOGGER.debug("JobsListener.jobWasExecuted()");
	}

}
