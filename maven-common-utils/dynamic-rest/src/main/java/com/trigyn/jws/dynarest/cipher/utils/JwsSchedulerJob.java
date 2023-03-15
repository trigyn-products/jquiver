package com.trigyn.jws.dynarest.cipher.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import com.google.gson.Gson;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.ActivityLog;
import com.trigyn.jws.dbutils.utils.CustomResponseEntity;
import com.trigyn.jws.dbutils.utils.CustomRuntimeException;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.dynarest.entities.JqScheduler;
import com.trigyn.jws.dynarest.entities.JqSchedulerLog;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;
import com.trigyn.jws.dynarest.service.SendMailService;
import com.trigyn.jws.dynarest.vo.Email;
import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;
import com.trigyn.jws.usermanagement.utils.Constants;

import reactor.core.publisher.Mono;

public class JwsSchedulerJob implements Job {

	private static Logger logger = LogManager.getLogger(JwsSchedulerJob.class);
	
	@Autowired
	private IUserDetailsService detailsService = null;
	
	@Autowired
	private ApplicationSecurityDetails applicationSecurityDetails = null;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JwsDynarestDAO jwsDynarestDAO = (JwsDynarestDAO) context.getJobDetail().getJobDataMap().get("jwsDynarestDAO");
		JqScheduler jqScheduler = (JqScheduler) context.getJobDetail().getJobDataMap().get("jwsScheduler");
		String schedulerId = jqScheduler.getSchedulerId();
		String baseURL = (String) context.getJobDetail().getJobDataMap().get("baseURL");
		String schedulerUrlProperty = (String) context.getJobDetail().getJobDataMap().get("schedulerUrlProperty");
		String contextPath = (String) context.getJobDetail().getJobDataMap().get("contextPath");
		SendMailService sendMailService = (SendMailService) context.getJobDetail().getJobDataMap()
				.get("sendMailService");
		String userName = (String) context.getJobDetail().getJobDataMap().get("userName");
		ActivityLog activityLog = (ActivityLog) context.getJobDetail().getJobDataMap().get("activityLog");

		try {

			execute(jwsDynarestDAO, jqScheduler, schedulerId, baseURL, schedulerUrlProperty, contextPath,
					sendMailService, userName, activityLog);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void execute(JwsDynarestDAO jwsDynarestDAO, JqScheduler jqScheduler, String schedulerId, String baseURL,
			String schedulerUrlProperty, String contextPath, SendMailService sendMailService, String userName,
			ActivityLog activityLog) throws AddressException, Exception {
		Date requestTime = null;
		Gson g = new Gson();
		try {

			Map<String, String> headerMap = g.fromJson(jqScheduler.getHeaderJson().toString(), Map.class);
			StringBuilder fullRestApiUrl = new StringBuilder().append(baseURL);
			JwsDynamicRestDetail jwsDynamicRestDetail = jwsDynarestDAO
					.findDynamicRestById(jqScheduler.getJwsDynamicRestId());

			if (contextPath != null && contextPath.isEmpty() == false) {
				fullRestApiUrl = fullRestApiUrl.append(contextPath);
			}
			fullRestApiUrl = fullRestApiUrl.append("/api/").append(jwsDynamicRestDetail.getJwsDynamicRestUrl());

			String requestType = jwsDynamicRestDetail.getJwsRequestTypeDetail().getJwsRequestType();

			String restApiUrl = fullRestApiUrl.toString();
			restApiUrl = restApiUrl.replace("/api/", "/sch-api/" + schedulerUrlProperty + "/");

			MultiValueMap<String, String> multipvalueMap = new LinkedMultiValueMap<String, String>();
			if (jqScheduler.getRequestParamJson() != null && jqScheduler.getRequestParamJson().isEmpty() == false) {
				Map<String, String> requestBodyMap = g.fromJson(jqScheduler.getRequestParamJson().toString(),
						Map.class);
				if (requestBodyMap != null && requestBodyMap.isEmpty() == false) {
					for (Entry<String, String> entry : requestBodyMap.entrySet()) {
						List<String> list = new ArrayList<>();
						list.add(entry.getValue());
						multipvalueMap.put(entry.getKey(), list);
					}
				}
			}

			Builder builder = WebClient.builder().baseUrl(restApiUrl)
					.defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
					.defaultHeader(HttpHeaders.USER_AGENT, "JQuiver").defaultHeader("schId", schedulerId);

			if (headerMap != null && headerMap.isEmpty() == false) {
				for (Entry<String, String> entry : headerMap.entrySet()) {
					builder.defaultHeader(entry.getKey(), entry.getValue());
				}
			}

			WebClient webClient = builder.build();

			Mono<ResponseEntity<String>> responseContent = webClient.method(HttpMethod.resolve(requestType))
					.uri(restApiUrl, uri -> uri.queryParams(multipvalueMap).build()).retrieve()
					.onStatus(HttpStatus::is4xxClientError, response -> {
						return response.bodyToMono(CustomRuntimeException.class).flatMap(error -> {
							return Mono.error(new CustomRuntimeException(error));
						});
					}).onStatus(HttpStatus::is5xxServerError, response -> {
						return response.bodyToMono(CustomRuntimeException.class).flatMap(error -> {
							return Mono.error(new CustomRuntimeException(error));
						});
					}).toEntity(String.class);

			requestTime = new Date();
			ResponseEntity<String> responseString = responseContent.block();
			CustomResponseEntity customResponseEntity = convertResponseToVO(responseString);

			sendFailedMailNotification(jqScheduler, sendMailService, g, customResponseEntity.getResponseBody(), false,
					userName, activityLog);

			if (customResponseEntity.getResponseStatusCode() == 200) {
				/* Method called for implementing Activity Log */
				logActivity(jqScheduler.getScheduler_name(), Constants.Action.SCHEDULEREXECUTED.getAction(),
						jqScheduler.getSchedulerTypeId(), userName, activityLog);
			} else {
				logActivity(jqScheduler.getScheduler_name(), Constants.Action.SCHEDULEREXECUTIONFAILED.getAction(),
						jqScheduler.getSchedulerTypeId(), userName, activityLog);
			}
			if (customResponseEntity.getResponseBody() != null) {
				JqSchedulerLog log = new JqSchedulerLog();
				log.setResponseBody(customResponseEntity.getResponseBody());
				log.setResponseCode(String.valueOf(customResponseEntity.getResponseStatusCode()));
				log.setResponseTime(customResponseEntity.getResponseTimestamp());
				log.setSchedulerId(schedulerId);
				log.setRequestTime(requestTime);
				jwsDynarestDAO.saveJqSchedulerLog(log);
			}
			logger.log(Level.INFO, "JwsScheduler completed susccesfully. Scheduler ID: " + schedulerId);
		} catch (CustomRuntimeException crte) {
			crte.printStackTrace();
			JqSchedulerLog log = new JqSchedulerLog();
			String stacktrace = ExceptionUtils.getStackTrace(crte);
			/* Method called for implementing Activity Log */
			logActivity(jqScheduler.getScheduler_name(), Constants.Action.SCHEDULEREXECUTIONFAILED.getAction(),
					jqScheduler.getSchedulerTypeId(), userName, activityLog);
			log.setResponseBody(stacktrace);
			log.setResponseCode(crte.getStatusCode().name());
			log.setResponseTime(new Date());
			log.setSchedulerId(schedulerId);
			log.setRequestTime(requestTime);
			jwsDynarestDAO.saveJqSchedulerLog(log);
			sendFailedMailNotification(jqScheduler, sendMailService, g, stacktrace, true, userName, activityLog);
			logger.log(Level.ERROR,
					"Exception occurred while executing JwsScheduler. " + crte + " Scheduler ID: " + schedulerId);
		} catch (Throwable e) {
			e.printStackTrace();
			JqSchedulerLog log = new JqSchedulerLog();
			String stacktrace = ExceptionUtils.getStackTrace(e);
			/* Method called for implementing Activity Log */
			logActivity(jqScheduler.getScheduler_name(), Constants.Action.SCHEDULEREXECUTIONFAILED.getAction(),
					jqScheduler.getSchedulerTypeId(), userName, activityLog);
			log.setResponseBody(stacktrace);
			log.setResponseCode("500");
			log.setResponseTime(new Date());
			log.setSchedulerId(schedulerId);
			log.setRequestTime(requestTime);
			jwsDynarestDAO.saveJqSchedulerLog(log);
			sendFailedMailNotification(jqScheduler, sendMailService, g, stacktrace, true, userName, activityLog);
			logger.log(Level.ERROR,
					"Exception occurred while executing JwsScheduler. " + e + " Scheduler ID: " + schedulerId);
		}
	}

	/**
	 * Purpose of this method is to log activities</br>
	 * in Scheduler Execution Module.
	 * 
	 * @author Bibhusrita.Nayak
	 * @param entityName
	 * @param typeSelect
	 * @param action
	 * @throws Exception
	 */
	private void logActivity(String entityName, String action, Integer typeSelect, String userName,
			ActivityLog activityLog) throws Exception {
		Map<String, String> requestParams = new HashMap<>();
		Date activityTimestamp = new Date();
		if (typeSelect == Constants.Changetype.CUSTOM.getChangeTypeInt()) {
			requestParams.put("typeSelect", Constants.Changetype.CUSTOM.getChangetype());
		} else {
			requestParams.put("typeSelect", Constants.Changetype.SYSTEM.getChangetype());
		}
		requestParams.put("entityName", entityName);
		requestParams.put("masterModuleType", Constants.Modules.SCHEDULER.getModuleName());
		requestParams.put("userName", userName);
		requestParams.put("message", "");
		requestParams.put("date", activityTimestamp.toString());
		requestParams.put("action", action);
		activityLog.activitylog(requestParams);
	}

	private void sendFailedMailNotification(JqScheduler jqScheduler, SendMailService sendMailService, Gson g,
			String responseBody, boolean isResponseCodeNot200, String userName, ActivityLog activityLog)
			throws AddressException, Exception {
		String failedNotificationJsonStr = jqScheduler.getFailedNotificationParamJson();

		if (failedNotificationJsonStr != null) {
			Map<String, String> failedNotificationMap = g.fromJson(failedNotificationJsonStr, Map.class);
			if (failedNotificationMap != null) {
				Integer regexCondition = null;
				if (failedNotificationMap.get("regexCondition") != null) {
					regexCondition = (Double.valueOf(String.valueOf(failedNotificationMap.get("regexCondition"))))
							.intValue();
				}

				String regex = failedNotificationMap.get("regex");
				String recepients = failedNotificationMap.get("recepients");
				String isResponseCodeNot200FromJson = failedNotificationMap.get("responseCodeNot200");
				Integer conjuctionCode = null;
				if (failedNotificationMap.get("conjuctionCode") != null) {
					conjuctionCode = (Double.valueOf(String.valueOf(failedNotificationMap.get("conjuctionCode"))))
							.intValue();
				}

				if (recepients != null && StringUtils.isBlank(recepients) == false) {
					Email email = new Email();
					email.setInternetAddressToArray(InternetAddress.parse(recepients));
					/*For inserting notification in case of mail failure only on access of Admin*/
					email.setIsAuthenticationEnabled(applicationSecurityDetails.getIsAuthenticationEnabled());
					email.setLoggedInUserRole(detailsService.getUserDetails().getRoleIdList());
					email.setBody(responseBody);
					email.setSubject("Failed Mail Notification");

					if (conjuctionCode != null && conjuctionCode != 0) {
						boolean regexMatches = Pattern.compile(regex).matcher(responseBody).find();
						if (conjuctionCode == 1) {
							if (isResponseCodeNot200 && "true".equals(isResponseCodeNot200FromJson)
									&& (regexCondition != null && ((regexCondition == 0 && regexMatches)
											|| (regexCondition == 1 && !regexMatches)))) {
								sendMailService.sendTestMail(email);
							}
						} else {
							if (isResponseCodeNot200 && "true".equals(isResponseCodeNot200FromJson)
									|| (regexCondition != null && ((regexCondition == 0 && regexMatches)
											|| (regexCondition == 1 && !regexMatches)))) {
								sendMailService.sendTestMail(email);
							}
						}
					} else {
						if (isResponseCodeNot200 && "true".equals(isResponseCodeNot200FromJson)) {
							sendMailService.sendTestMail(email);
						} else if (regexCondition != null && ((regexCondition == 0
								&& Pattern.compile(regex).matcher(responseBody).find())
								|| (regexCondition == 1 && !Pattern.compile(regex).matcher(responseBody).find()))) {
							sendMailService.sendTestMail(email);
						}
					}
				}
			}
		}
	}

	private CustomResponseEntity convertResponseToVO(ResponseEntity<String> responseString) {
		CustomResponseEntity customResponseEntity = new CustomResponseEntity();
		String responseBody = responseString.getBody();
		Integer responseStatusCode = responseString.getStatusCode().value();
		Map<String, String> headerMap = responseString.getHeaders().toSingleValueMap();
		customResponseEntity.setResponseStatusCode(responseStatusCode);
		customResponseEntity.setResponseBody(responseBody);
		customResponseEntity.setHeaders(headerMap);
		customResponseEntity.setResponseTimestamp(new Date());
		return customResponseEntity;
	}

}
