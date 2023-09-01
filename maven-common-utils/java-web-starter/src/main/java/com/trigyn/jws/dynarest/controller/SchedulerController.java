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
import org.quartz.CronExpression;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynarest.cipher.utils.JwsSchedulerJob;
import com.trigyn.jws.dynarest.entities.JqScheduler;
import com.trigyn.jws.dynarest.repository.JqschedulerRepository;
import com.trigyn.jws.dynarest.service.SchedulerService;
import com.trigyn.jws.quartz.service.impl.JwsQuartzJobService;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.utils.Constants;

@RestController
@RequestMapping("/cf")
public class SchedulerController {

	private final static Logger		logger					= LogManager.getLogger(SchedulerController.class);

	@Autowired
	private JqschedulerRepository	jqschedulerRepository	= null;

	@Autowired
	private SchedulerService		schedulerService		= null;

	@Autowired
	private PropertyMasterService	propertyMasterService	= null;

	@Autowired
	private MenuService				menuService				= null;

	@Autowired
	private ServletContext			servletContext			= null;

	@Autowired
	private IUserDetailsService		userDetailsService		= null;

	@Autowired
	private ActivityLog				activitylog				= null;

	@Autowired
	private JwsQuartzJobService		jobService				= null;

	@RequestMapping(value = "/sl", produces = MediaType.TEXT_HTML_VALUE)
	public String schedulerListing(HttpServletResponse httpServletResponse) throws IOException, CustomStopException {
		try {
			return menuService.getTemplateWithSiteLayout("jq-scheduler-listing", new HashMap<>());
		} catch (CustomStopException custStopException) {
			logger.error("Error occured while loading Scheduler Listing page.", custStopException);
			throw custStopException;
		} catch (Exception a_exception) {
			logger.error("Error occured while loading Scheduler Listing Page.", a_exception);
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
		boolean status = false;
		try {
			String	schedulerId		= httpServletRequest.getParameter("schedulerID");
			String	schedulerName	= httpServletRequest.getParameter("schedulerName");
			/* Method called for implementing Activity Log */
			logActivity(schedulerName, Constants.Action.DELETE.getAction());
			status = schedulerService.deleteScheduler(schedulerId);
		} catch (Exception a_exception) {
			logger.error("Error occured while deleting : Scheduler : " + "Scheduler Name : "
					+ httpServletRequest.getParameter("schedulerName"), a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return String.valueOf(status);
		}
		return String.valueOf(status);
	}

	/**
	 * Purpose of this method is to log activities</br>
	 * in Scheduler Module.
	 * 
	 * @author            Bibhusrita.Nayak
	 * @param  entityName
	 * 
	 * @throws Exception
	 */
	private void logActivity(String entityName, String action) throws Exception {
		Map<String, String>	requestParams		= new HashMap<>();
		UserDetailsVO		detailsVO			= userDetailsService.getUserDetails();
		Date				activityTimestamp	= new Date();
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
	public String schedule(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException {
		boolean status = false;
		try {
			String			cronExpression	= httpServletRequest.getParameter("cronscheduler");
			String			schedulerId		= httpServletRequest.getParameter("schedulerId");
			JqScheduler		jwsScheduler	= jqschedulerRepository.getOne(schedulerId);
			CronExpression	cronExp			= new CronExpression(cronExpression);
			if (jwsScheduler.getIsActive().equals(Constants.ISACTIVE)) {
				String		schedulerUrlProperty	= propertyMasterService.findPropertyMasterValue("scheduler-url");
				String		baseURL					= propertyMasterService.findPropertyMasterValue("base-url");
				JobDataMap	map						= new JobDataMap();
				map.put("baseURL", baseURL);
				map.put("schedulerId", schedulerId);
				map.put("schedulerUrlProperty", schedulerUrlProperty);
				map.put("contextPath", servletContext.getContextPath());
				map.put("userName", userDetailsService.getUserDetails().getUserName());
				String jobGroup = jwsScheduler.getJwsDynamicRestId();
				status = jobService.scheduleCronJob(jwsScheduler.getScheduler_name(), jobGroup, JwsSchedulerJob.class,
						new Date(), cronExp.getCronExpression(), map);
			}else {
				String jobGroup = jwsScheduler.getJwsDynamicRestId();
				jobService.deleteJob(jwsScheduler.getScheduler_name(), jobGroup);
			}
		} catch (Throwable a_thr) {
			logger.error(a_thr.getMessage());
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_thr.getMessage());
		}
		return String.valueOf(status);
	}

	@RequestMapping(value = "/execn", produces = MediaType.TEXT_HTML_VALUE)
	public String executeNow(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException {
		boolean status = false;
		try {
			String		schedulerId		= httpServletRequest.getParameter("schedulerID");
			JqScheduler	jwsScheduler	= jqschedulerRepository.getOne(schedulerId);
			if (jwsScheduler.getIsActive().equals(Constants.INACTIVE)) {
				httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Scheduler is In Active.");
				return String.valueOf(status);
			}
			String jobGroup = jwsScheduler.getJwsDynamicRestId();
			status = jobService.startJobNow(jwsScheduler.getScheduler_name(), jobGroup);
			return String.valueOf(status);
		} catch (Exception a_exception) {
			logger.error("Error occured while executing now : Scheduler :" + "Scheduler Id :"
					+ httpServletRequest.getParameter("schedulerID"), a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
		}
		return String.valueOf(status);
	}

}
