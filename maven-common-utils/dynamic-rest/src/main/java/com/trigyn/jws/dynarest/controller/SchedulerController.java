package com.trigyn.jws.dynarest.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynarest.cipher.utils.JwsSchedulerJob;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.dynarest.entities.JqScheduler;
import com.trigyn.jws.dynarest.repository.JqschedulerRepository;
import com.trigyn.jws.dynarest.service.SchedulerService;
import com.trigyn.jws.dynarest.service.SendMailService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.utils.Constants;

@RestController
@RequestMapping("/cf")
public class SchedulerController {

	private final static Logger logger = LogManager.getLogger(SchedulerController.class);

	@Autowired
	private JqschedulerRepository jqschedulerRepository = null;

	@Autowired
	private JwsDynarestDAO jwsDynarestDAO = null;

	@Autowired
	private SchedulerService schedulerService = null;

	@Autowired
	private PropertyMasterService propertyMasterService = null;

	@Autowired
	private MenuService menuService = null;

	@Autowired
	private ServletContext servletContext = null;

	@Autowired
	private SendMailService sendMailService = null;

	@Autowired
	private IUserDetailsService userDetailsService = null;

	@Autowired
	private ActivityLog activitylog = null;

	@RequestMapping(value = "/sl", produces = MediaType.TEXT_HTML_VALUE)
	public String schedulerListing(HttpServletResponse httpServletResponse) throws IOException {
		try {
			return menuService.getTemplateWithSiteLayout("jq-scheduler-listing", new HashMap<>());
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@RequestMapping(value = "/delS", produces = MediaType.TEXT_HTML_VALUE)
	public String deleteScheduler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException {
		try {
			String schedulerId = httpServletRequest.getParameter("schedulerID");
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			String schedulerName = httpServletRequest.getParameter("schedulerName");
			/* Method called for implementing Activity Log */
			logActivity(schedulerName, Constants.Action.DELETE.getAction());
			JobDetail jobDetail = JobBuilder.newJob(JwsSchedulerJob.class).withIdentity(schedulerId).build();
			if (scheduler.checkExists(jobDetail.getKey())) {
				scheduler.deleteJob(jobDetail.getKey());
			}

			schedulerService.deleteScheduler(schedulerId);
			return "1";
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	/**
	 * Purpose of this method is to log activities</br>
	 * in Scheduler Module.
	 * 
	 * @author Bibhusrita.Nayak
	 * @param entityName
	 * 
	 * @throws Exception
	 */
	private void logActivity(String entityName, String action) throws Exception {
		Map<String, String> requestParams = new HashMap<>();
		UserDetailsVO detailsVO = userDetailsService.getUserDetails();
		Date activityTimestamp = new Date();
		requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
		requestParams.put("entityName", entityName);
		requestParams.put("masterModuleType", Constants.Modules.SCHEDULER.getModuleName());
		requestParams.put("userName", detailsVO.getUserName());
		requestParams.put("message", "");
		requestParams.put("date", activityTimestamp.toString());
		requestParams.put("action", action);
		activitylog.activitylog(requestParams);
	}

	@PostMapping("/schedule")
	public String schedule(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		try {

			String cronExpression = httpServletRequest.getParameter("cronscheduler");
			String schedulerId = httpServletRequest.getParameter("schedulerId");

			JqScheduler jwsScheduler = jqschedulerRepository.getOne(schedulerId);

			String schedulerUrlProperty = propertyMasterService.findPropertyMasterValue("scheduler-url");

			String baseURL = propertyMasterService.findPropertyMasterValue("base-url");

			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

			JobDataMap map = new JobDataMap();
			map.put("baseURL", baseURL);
			map.put("schedulerId", schedulerId);
			map.put("jwsDynarestDAO", jwsDynarestDAO);
			map.put("schedulerUrlProperty", schedulerUrlProperty);
			map.put("jwsScheduler", jwsScheduler);
			map.put("contextPath", servletContext.getContextPath());
			map.put("sendMailService", sendMailService);
			map.put("activityLog", activitylog);
			map.put("userName", userDetailsService.getUserDetails().getUserName());
			JobDetail jobDetail = JobBuilder.newJob(JwsSchedulerJob.class).withIdentity(schedulerId).usingJobData(map)
					.build();
			if (scheduler.checkExists(jobDetail.getKey())) {
				scheduler.deleteJob(jobDetail.getKey());
			}

			if (jwsScheduler.getIsActive() == 1) {

				CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(schedulerId)
						.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
				scheduler.scheduleJob(jobDetail, trigger);
				scheduler.start();
			}

		} catch (Throwable a_thr) {
			logger.error(a_thr.getMessage());
		} finally {
		}

		return "1";
	}

	@RequestMapping(value = "/execn", produces = MediaType.TEXT_HTML_VALUE)
	public String executeNow(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException {

		try {
			String schedulerId = httpServletRequest.getParameter("schedulerID");
			JqScheduler jwsScheduler = jqschedulerRepository.getOne(schedulerId);

			String schedulerUrlProperty = propertyMasterService.findPropertyMasterValue("scheduler-url");

			String baseURL = propertyMasterService.findPropertyMasterValue("base-url");

			JwsSchedulerJob scheduler = new JwsSchedulerJob();
			UserDetailsVO detailsVO = userDetailsService.getUserDetails();

			scheduler.execute(jwsDynarestDAO, jwsScheduler, schedulerId, baseURL, schedulerUrlProperty,
					servletContext.getContextPath(), sendMailService, detailsVO.getUserName(), activitylog);

			return "1";
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

}
