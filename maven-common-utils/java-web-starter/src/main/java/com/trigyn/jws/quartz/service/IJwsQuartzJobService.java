package com.trigyn.jws.quartz.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.scheduling.quartz.QuartzJobBean;

public interface IJwsQuartzJobService {

	boolean scheduleOneTimeJob(String jobName, String jobGroup, Class<? extends QuartzJobBean> jobClass, Date date,
			JobDataMap jobDetailMap);

	boolean scheduleCronJob(String jobName, String jobGroup, Class<? extends Job> jobClass, Date date,
			String cronExpression, JobDataMap jobDetailMap);

	boolean updateOneTimeJob(String jobName, Date date);

	boolean updateCronJob(String jobName, Date date, String cronExpression);

	boolean unScheduleJob(String jobName);

	boolean deleteJob(String jobName, String jobGroup);

	boolean pauseJob(String jobName, String	jobGroup);

	boolean resumeJob(String jobName, String jobGroup);

	boolean startJobNow(String jobName, String	jobGroup);

	boolean isJobRunning(String jobName, String	jobGroup);

	List<Map<String, Object>> getAllJobs();

	boolean isJobWithNamePresent(String jobName, String jobGroup);

	String getJobState(String jobName, String jobGroup);

	boolean stopJob(String jobName, String jobGroup);
	
	//JobDetail findJobDetails(String jobName);

}
