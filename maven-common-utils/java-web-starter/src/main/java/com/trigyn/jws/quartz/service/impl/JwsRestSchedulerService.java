package com.trigyn.jws.quartz.service.impl;

import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.trigyn.jws.dbutils.service.ApplicationContextProvider;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dynarest.cipher.utils.JwsSchedulerJob;
import com.trigyn.jws.dynarest.entities.JqScheduler;
import com.trigyn.jws.dynarest.repository.JqschedulerRepository;
import com.trigyn.jws.quartz.service.IJwsJobService;
import com.trigyn.jws.quartz.service.IJwsQuartzJobService;

@Transactional
public class JwsRestSchedulerService implements IJwsJobService {

	private final static Logger		LOGGER					= LogManager.getLogger(JwsRestSchedulerService.class);

	private PropertyMasterService	propertyMasterService	= ApplicationContextProvider
			.getBean(PropertyMasterService.class);

	private ServletContext			servletContext			= ApplicationContextProvider.getBean(ServletContext.class);

	private JqschedulerRepository	jqschedulerRepository	= ApplicationContextProvider
			.getBean(JqschedulerRepository.class);

	private IJwsQuartzJobService	jobService				= ApplicationContextProvider
			.getBean(IJwsQuartzJobService.class);

	@Lazy
	private UserDetailsService		userDetailsService		= ApplicationContextProvider
			.getBean(UserDetailsService.class);

	public JobDataMap getJobDetails(JqScheduler jwsScheduler) throws Exception {
		String		schedulerUrlProperty	= propertyMasterService.findPropertyMasterValue("scheduler-url");
		String		baseURL					= propertyMasterService.findPropertyMasterValue("base-url");
		String		schedulerId				= jwsScheduler.getSchedulerId();
		JobDataMap	jobDataMap				= new JobDataMap();
		jobDataMap.put("baseURL", baseURL);
		jobDataMap.put("schedulerId", schedulerId);
		jobDataMap.put("contextPath", servletContext.getContextPath());
		jobDataMap.put("schedulerUrlProperty", schedulerUrlProperty);
		return jobDataMap;
	}

	@Override
	public void executeSchedular(String schedulerId) {
		JqScheduler jwsScheduler = jqschedulerRepository.retrieveSchedulerById(1, schedulerId);
		if (jwsScheduler != null && jwsScheduler.getIsActive() == 1) {
			try {
				String		schedulerUrlProperty	= propertyMasterService.findPropertyMasterValue("scheduler-url");
				String		baseURL					= propertyMasterService.findPropertyMasterValue("base-url");

				JobDataMap	jobDataMap				= new JobDataMap();
				jobDataMap.put("baseURL", baseURL);
				jobDataMap.put("schedulerId", schedulerId);
				jobDataMap.put("contextPath", servletContext.getContextPath());
				jobDataMap.put("schedulerUrlProperty", schedulerUrlProperty);
				// jobDataMap.put("jwsScheduler", jwsScheduler);
				String jobGroup = jwsScheduler.getJwsDynamicRestId();
				boolean status = jobService.scheduleCronJob(jwsScheduler.getScheduler_name(), jobGroup, JwsSchedulerJob.class,
						new Date(), jwsScheduler.getCronScheduler(), jobDataMap);
				if (status) {
					LOGGER.info("Scheduler executed successfully.");
				} else {
					LOGGER.info("Could not start job ");
				}
			} catch (SchedulerException sce) {
				LOGGER.error("Could not start job with due to error - {}", sce.getLocalizedMessage());
			} catch (Exception exec) {
				LOGGER.error("Could not start job with due to error - {}", exec.getLocalizedMessage());
			}
		}

	}

	public void authCustomUserProfile(String email) {

		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (sra != null) {

			UserDetails userDetails = userDetailsService.loadUserByUsername(email);
			HttpServletRequest					request								= sra.getRequest();
			UsernamePasswordAuthenticationToken	usernamePasswordAuthenticationToken	= new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
			usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

		}
	}
}
