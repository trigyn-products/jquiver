package com.trigyn.jws.quartz.service.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.quartz.config.QuartzSchedulerConfig;
import com.trigyn.jws.quartz.service.IJwsQuartzJobService;
import com.trigyn.jws.quartz.util.JobUtil;

@Service
@Transactional
public class JwsQuartzJobService implements IJwsQuartzJobService {

	private static Logger		logger					= LogManager.getLogger(JwsQuartzJobService.class);

	@Autowired
	@Lazy
	QuartzSchedulerConfig		schedulerFactoryBean	= null;

	@Autowired
	private ApplicationContext	context					= null;
	
	@Autowired
	private ServerProperties serverProperties 			= null;

	/**
	 * Schedule a job by jobName at given date.
	 */
	@Override
	public boolean scheduleOneTimeJob(String jobName, String jobGroup, Class<? extends QuartzJobBean> jobClass,
			Date date, JobDataMap jobDetailMap) {
		logger.log(Level.DEBUG, "Request received to scheduleJob");

		String		triggerKey	= jobGroup;
		JobDetail	jobDetail	= JobUtil.createJob(jobClass, false, context, jobName, jobGroup, jobDetailMap);
		logger.log(Level.DEBUG, "creating trigger for key :" + jobName + " at date :" + date);
		Trigger cronTriggerBean = JobUtil.createSingleTrigger(jobName, triggerKey, date,
				SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
		try {
			Scheduler scheduler = schedulerFactoryBean.schedulerFactoryBean().getScheduler();
			if (scheduler.checkExists(jobDetail.getKey())) {
				scheduler.deleteJob(jobDetail.getKey());
			}
			Date dt = scheduler.scheduleJob(jobDetail, cronTriggerBean);
			logger.log(Level.DEBUG, "Job with key jobKey :" + jobName + " and group :" + jobGroup
					+ " scheduled successfully for date :" + dt);
			return true;
		} catch (SchedulerException sche) {
			logger.error(
					"SchedulerException while scheduling job with key :" + jobName + " message :" + sche.getMessage());
			sche.printStackTrace();
		} catch (IOException ioe) {
			logger.error(
					"SchedulerException while scheduling job with key :" + jobName + " message :" + ioe.getMessage());
			ioe.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean scheduleCronJob(String jobName, String jobGroup, Class<? extends Job> jobClass, Date date,
			String cronExpression, JobDataMap jobDetailMap) {
		logger.log(Level.DEBUG, "Request received to scheduleJob");

		String		triggerKey	= jobName;
		JobDetail	jobDetail	= JobUtil.createJob(jobClass, false, context, jobName, jobGroup, jobDetailMap);

		logger.log(Level.DEBUG, "creating trigger for key :" + jobName + " at date :" + date);
		Trigger cronTriggerBean = JobUtil.createCronTrigger(triggerKey, jobGroup, date, cronExpression,
				SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);

		try {
			Scheduler scheduler = schedulerFactoryBean.schedulerFactoryBean().getScheduler();
			if (scheduler.checkExists(jobDetail.getKey())) {
				scheduler.deleteJob(jobDetail.getKey());
			}
			Date dt = scheduler.scheduleJob(jobDetail, cronTriggerBean);
			logger.log(Level.DEBUG, "Job with key jobKey :" + jobName + " and group :" + jobGroup
					+ " scheduled successfully for date :" + dt);
			return true;
		} catch (SchedulerException sche) {
			logger.error(
					"SchedulerException while scheduling job with key :" + jobName + " message :" + sche.getMessage());
			sche.printStackTrace();
		} catch (IOException ioe) {
			logger.error(
					"SchedulerException while scheduling job with key :" + jobName + " message :" + ioe.getMessage());
			ioe.printStackTrace();
		}

		return false;
	}

	/**
	 * Update one time scheduled job.
	 */
	@Override
	public boolean updateOneTimeJob(String jobName, Date date) {

		logger.log(Level.DEBUG, "Request received for updating one time job.");
		logger.log(Level.DEBUG,
				"Parameters received for updating one time job : jobKey :" + jobName + ", date: " + date);
		try {
			Trigger		newTrigger	= JobUtil.createSingleTrigger(jobName, null, date,
					SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
			Scheduler	scheduler	= schedulerFactoryBean.schedulerFactoryBean().getScheduler();
			Date		dt			= scheduler.rescheduleJob(TriggerKey.triggerKey(jobName), newTrigger);
			logger.info("Trigger associated with jobKey :" + jobName + " rescheduled successfully for date :" + dt);
			return true;
		} catch (Exception sche) {
			logger.error("SchedulerException while updating one time job with key :" + jobName + " message :"
					+ sche.getMessage());
			sche.printStackTrace();
			return false;
		}
	}

	/**
	 * Update scheduled cron job.
	 */
	@Override
	public boolean updateCronJob(String jobName, String jobGroup, Date date, String cronExpression) {
		logger.log(Level.DEBUG, "Request received for updating cron job.");

		logger.log(Level.DEBUG, "Parameters received for updating cron job : jobKey :" + jobName + ", date: " + date);
		try {
			Trigger		newTrigger	= JobUtil.createCronTrigger(jobName, jobGroup, date, cronExpression,
					SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
			Scheduler	scheduler	= schedulerFactoryBean.schedulerFactoryBean().getScheduler();
			Date		dt			= scheduler.rescheduleJob(TriggerKey.triggerKey(jobName), newTrigger);
			logger.info("Trigger associated with jobKey :" + jobName + " rescheduled successfully for date :" + dt);
			return true;
		} catch (Exception sche) {
			logger.error("SchedulerException while updating cron job with key :" + jobName + " message :"
					+ sche.getMessage());
			sche.printStackTrace();
			return false;
		}
	}

	/**
	 * Remove the indicated Trigger from the scheduler. If the related job does not
	 * have any other triggers, and the job is not durable, then the job will also
	 * be deleted.
	 */
	@Override
	public boolean unScheduleJob(String jobName) {
		logger.log(Level.DEBUG, "Request received for Unscheduleding job.");

		TriggerKey tkey = new TriggerKey(jobName);
		logger.log(Level.DEBUG, "Parameters received for unscheduling job : tkey :" + jobName);
		try {
			Scheduler	scheduler	= schedulerFactoryBean.schedulerFactoryBean().getScheduler();
			boolean		status		= scheduler.unscheduleJob(tkey);
			logger.log(Level.DEBUG,
					"Trigger associated with jobKey :" + jobName + " unscheduled with status :" + status);
			return status;
		} catch (SchedulerException sche) {
			logger.error("SchedulerException while unscheduling job with key :" + jobName + " message :"
					+ sche.getMessage());
			sche.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Delete the identified Job from the Scheduler - and any associated Triggers.
	 */
	@Override
	public boolean deleteJob(String jobName, String jobGroup) {

		logger.log(Level.DEBUG, "Request received for deleting job.");
		JobKey jkey = new JobKey(jobName, jobGroup);
		logger.log(Level.DEBUG, "Parameters received for deleting job : jobKey :" + jobName);

		try {
			Scheduler	scheduler	= schedulerFactoryBean.schedulerFactoryBean().getScheduler();
			boolean		status		= scheduler.deleteJob(jkey);
			logger.log(Level.DEBUG, "Job with jobKey :" + jobName + " deleted with status :" + status);
			return status;
		} catch (SchedulerException sche) {
			logger.error(
					"SchedulerException while deleting job with key :" + jobName + " message :" + sche.getMessage());
			sche.printStackTrace();
			return false;
		} catch (IOException ioe) {
			logger.error(
					"SchedulerException while scheduling job with key :" + jobName + " message :" + ioe.getMessage());
			ioe.printStackTrace();
		}
		return false;
	}

	/**
	 * Pause a job
	 */
	@Override
	public boolean pauseJob(String jobName, String jobGroup) {
		logger.log(Level.DEBUG, "Request received for pausing job.");

		JobKey jkey = new JobKey(jobName, jobGroup);
		logger.log(Level.DEBUG, "Parameters received for pausing job : jobKey :" + jobName + ", jobGroup :" + jobGroup);

		try {
			Scheduler scheduler = schedulerFactoryBean.schedulerFactoryBean().getScheduler();
			scheduler.pauseJob(jkey);
			logger.log(Level.DEBUG, "Job with jobKey :" + jobName + " paused succesfully.");
			return true;
		} catch (SchedulerException sche) {
			logger.error(
					"SchedulerException while pausing job with key :" + jobName + " message :" + sche.getMessage());
			sche.printStackTrace();
			return false;
		} catch (IOException ioe) {
			logger.error(
					"SchedulerException while scheduling job with key :" + jobName + " message :" + ioe.getMessage());
			ioe.printStackTrace();
		}
		return false;
	}

	/**
	 * Resume paused job
	 */
	@Override
	public boolean resumeJob(String jobName, String jobGroup) {
		logger.log(Level.DEBUG, "Request received for resuming job.");

		JobKey jKey = new JobKey(jobName, jobGroup);
		logger.log(Level.DEBUG, "Parameters received for resuming job : jobKey :" + jobName);
		try {
			Scheduler scheduler = schedulerFactoryBean.schedulerFactoryBean().getScheduler();
			scheduler.resumeJob(jKey);
			logger.log(Level.DEBUG, "Job with jobKey :" + jobName + " resumed succesfully.");
			return true;
		} catch (SchedulerException sche) {
			logger.error(
					"SchedulerException while resuming job with key :" + jobName + " message :" + sche.getMessage());
			sche.printStackTrace();
			return false;
		} catch (IOException ioe) {
			logger.error(
					"SchedulerException while scheduling job with key :" + jobName + " message :" + ioe.getMessage());
			ioe.printStackTrace();
		}
		return false;
	}

	/**
	 * Start a job now
	 */
	@Override
	public boolean startJobNow(String jobName, String jobGroup) {
		logger.log(Level.DEBUG, "Request received for starting job now.");

		JobKey jKey = new JobKey(jobName, jobGroup);
		logger.log(Level.DEBUG, "Parameters received for starting job now : jobKey :" + jobName);
		try {
			Scheduler scheduler = schedulerFactoryBean.schedulerFactoryBean().getScheduler();
			scheduler.triggerJob(jKey);
			logger.log(Level.DEBUG, "Job with jobKey :" + jobName + " started now succesfully.");
			return true;
		} catch (SchedulerException sche) {
			logger.error("SchedulerException while starting job now with key :" + jobName + " message :"
					+ sche.getMessage());
			sche.printStackTrace();
			return false;
		} catch (IOException ioe) {
			logger.error(
					"SchedulerException while scheduling job with key :" + jobName + " message :" + ioe.getMessage());
			ioe.printStackTrace();
		}
		return false;
	}

	/**
	 * Check if job is already running
	 */
	@Override
	public boolean isJobRunning(String jobName, String jobGroup) {
		logger.log(Level.DEBUG, "Request received to check if job is running");

		logger.log(Level.DEBUG, "Parameters received for checking job is running now : jobKey :" + jobName);
		try {
			Scheduler					scheduler	= schedulerFactoryBean.schedulerFactoryBean().getScheduler();
			List<JobExecutionContext>	currentJobs	= scheduler.getCurrentlyExecutingJobs();
			if (currentJobs != null) {
				for (JobExecutionContext jobCtx : currentJobs) {
					String	jobNameDB	= jobCtx.getJobDetail().getKey().getName();
					String	groupNameDB	= jobCtx.getJobDetail().getKey().getGroup();
					if (jobName.equalsIgnoreCase(jobNameDB) && jobGroup.equalsIgnoreCase(groupNameDB)) {
						return true;
					}
				}
			}
		} catch (SchedulerException sche) {
			logger.error("SchedulerException while checking job with key :" + jobName + " is running. error message :"
					+ sche.getMessage());
			sche.printStackTrace();
			return false;
		} catch (IOException ioe) {
			logger.error(
					"SchedulerException while scheduling job with key :" + jobName + " message :" + ioe.getMessage());
			ioe.printStackTrace();
		}
		return false;
	}

	/**
	 * Get all jobs
	 */
	@Override
	public List<Map<String, Object>> getAllJobs() {
		List<Map<String, Object>>	list		= new ArrayList<Map<String, Object>>();
		String						jobGroup	= "";
		try {
			Scheduler scheduler = schedulerFactoryBean.schedulerFactoryBean().getScheduler();

			for (String groupName : scheduler.getJobGroupNames()) {
				for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

					String jobName = jobKey.getName();
					jobGroup = jobKey.getGroup();

					// get job's trigger
					List<Trigger>		triggers		= (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
					Date				scheduleTime	= triggers.get(0).getStartTime();
					Date				nextFireTime	= triggers.get(0).getNextFireTime();
					Date				lastFiredTime	= triggers.get(0).getPreviousFireTime();

					Map<String, Object>	map				= new HashMap<String, Object>();
					map.put("jobName", jobName);
					map.put("groupName", jobGroup);
					map.put("scheduleTime", scheduleTime);
					map.put("lastFiredTime", lastFiredTime);
					map.put("nextFireTime", nextFireTime);

					if (isJobRunning(jobName, jobGroup)) {
						map.put("jobStatus", "RUNNING");
					} else {
						String jobState = getJobState(jobName, jobGroup);
						map.put("jobStatus", jobState);
					}

					list.add(map);
					logger.log(Level.DEBUG, "Job details:");
					logger.log(Level.DEBUG,
							"Job Name:" + jobName + ", Group Name:" + groupName + ", Schedule Time:" + scheduleTime);
				}

			}
		} catch (SchedulerException sche) {
			logger.error("SchedulerException while fetching all jobs. error message :" + sche.getMessage());
			sche.printStackTrace();
		} catch (IOException ioe) {
			logger.error(
					"SchedulerException while scheduling job with key :" + jobGroup + " message :" + ioe.getMessage());
			ioe.printStackTrace();
		}
		return list;
	}

	/**
	 * Check job exist with given name
	 */
	@Override
	public boolean isJobWithNamePresent(String jobName, String jobGroup) {
		try {
			JobKey		jobKey		= new JobKey(jobName, jobGroup);
			Scheduler	scheduler	= schedulerFactoryBean.schedulerFactoryBean().getScheduler();
			if (scheduler.checkExists(jobKey)) {
				return true;
			}
		} catch (SchedulerException sche) {
			logger.error("SchedulerException while checking job with name and group exist:" + sche.getMessage());
			sche.printStackTrace();
		} catch (IOException ioe) {
			logger.error(
					"SchedulerException while scheduling job with key :" + jobGroup + " message :" + ioe.getMessage());
			ioe.printStackTrace();
		}
		return false;
	}

	/**
	 * Get the current state of job
	 */
	public String getJobState(String jobName, String jobGroup) {
		logger.log(Level.DEBUG, "JwsQuartzJobService.getJobState()");
		try {

			JobKey					jobKey		= new JobKey(jobName, jobGroup);
			Scheduler				scheduler	= schedulerFactoryBean.schedulerFactoryBean().getScheduler();
			JobDetail				jobDetail	= scheduler.getJobDetail(jobKey);

			List<? extends Trigger>	triggers	= scheduler.getTriggersOfJob(jobDetail.getKey());
			if (triggers != null && triggers.size() > 0) {
				for (Trigger trigger : triggers) {
					TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());

					if (TriggerState.PAUSED.equals(triggerState)) {
						return "PAUSED";
					} else if (TriggerState.BLOCKED.equals(triggerState)) {
						return "BLOCKED";
					} else if (TriggerState.COMPLETE.equals(triggerState)) {
						return "COMPLETE";
					} else if (TriggerState.ERROR.equals(triggerState)) {
						return "ERROR";
					} else if (TriggerState.NONE.equals(triggerState)) {
						return "NONE";
					} else if (TriggerState.NORMAL.equals(triggerState)) {
						return "SCHEDULED";
					}
				}
			}
		} catch (SchedulerException sche) {
			logger.error("SchedulerException while checking job with name and group exist:" + sche.getMessage());
			sche.printStackTrace();
		} catch (IOException ioe) {
			logger.error(
					"SchedulerException while scheduling job with key :" + jobGroup + " message :" + ioe.getMessage());
			ioe.printStackTrace();
		}
		return null;
	}

	/**
	 * Stop a job
	 */
	@Override
	public boolean stopJob(String jobName, String jobGroup) {
		logger.log(Level.DEBUG, "JwsQuartzJobService.stopJob()");
		try {

			Scheduler	scheduler	= schedulerFactoryBean.schedulerFactoryBean().getScheduler();
			JobKey		jkey		= new JobKey(jobName, jobGroup);
			return scheduler.interrupt(jkey);

		} catch (SchedulerException sche) {
			logger.error("SchedulerException while stopping job. error message :" + sche.getMessage());
			sche.printStackTrace();
		} catch (IOException ioe) {
			logger.error("SchedulerException while stopping job. error message :" + ioe.getMessage());
			ioe.printStackTrace();
		}
		return false;
	}
	
	public String getBaseUrl() throws UnknownHostException {
        String	resultPath = "";
		if (serverProperties != null) {
			String	scheme		= serverProperties.getSsl() != null && serverProperties.getSsl().isEnabled() ? "https" : "http";
			
			String	serverName	= InetAddress.getLocalHost().getHostAddress();
			int		serverPort	= serverProperties.getPort();
			resultPath	= scheme + "://" + serverName + ":" + serverPort;
		}
		return resultPath;
  }

}
