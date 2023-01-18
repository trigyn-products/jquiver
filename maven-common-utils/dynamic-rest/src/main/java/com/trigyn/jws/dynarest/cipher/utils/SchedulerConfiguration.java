package com.trigyn.jws.dynarest.cipher.utils;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.dynarest.entities.JqScheduler;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;
import com.trigyn.jws.dynarest.repository.JqschedulerRepository;
import com.trigyn.jws.dynarest.service.SendMailService;

@Configuration
public class SchedulerConfiguration {

	@Autowired
	private JqschedulerRepository	jqschedulerRepository	= null;

	@Autowired
	private JwsDynarestDAO			jwsDynarestDAO			= null;

	@Autowired
	private PropertyMasterService	propertyMasterService	= null;

	@Autowired
	private ServletContext			servletContext			= null;

	@Autowired
	private SendMailService			sendMailService			= null;

	@Autowired
	private ActivityLog				activitylog				= null;

	@PostConstruct
	public void initialiseScheduler() {

		try {

			List<JqScheduler> schedulers = jqschedulerRepository.retrieveSchedulers(1);

			for (JqScheduler jwsScheduler : schedulers) {
				try {

					String		cronExpression			= jwsScheduler.getCronScheduler();
					String		schedulerId				= jwsScheduler.getSchedulerId();

					String		schedulerUrlProperty	= propertyMasterService
							.findPropertyMasterValue("scheduler-url");

					String		baseURL					= propertyMasterService.findPropertyMasterValue("base-url");

					Scheduler	scheduler				= StdSchedulerFactory.getDefaultScheduler();

					JobDataMap	map						= new JobDataMap();
					map.put("baseURL", baseURL);
					map.put("schedulerId", schedulerId);
					map.put("jwsDynarestDAO", jwsDynarestDAO);
					map.put("schedulerUrlProperty", schedulerUrlProperty);
					map.put("jwsScheduler", jwsScheduler);
					map.put("contextPath", servletContext.getContextPath());
					map.put("sendMailService", sendMailService);
					map.put("activityLog", activitylog);

					JobDetail	jobDetail	= JobBuilder.newJob(JwsSchedulerJob.class).withIdentity(schedulerId)
							.usingJobData(map).build();
					CronTrigger	trigger		= TriggerBuilder.newTrigger().withIdentity(schedulerId)
							.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
					//
					scheduler.scheduleJob(jobDetail, trigger);
					scheduler.start();

				} catch (Exception e) {
					e.printStackTrace();
				} finally {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
