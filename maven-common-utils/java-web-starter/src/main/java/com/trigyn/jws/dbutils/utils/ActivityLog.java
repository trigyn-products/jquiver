package com.trigyn.jws.dbutils.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import com.trigyn.jws.dbutils.service.PropertyMasterService;

import reactor.core.publisher.Mono;

/**
 * 
 * Purpose of this class is to log all activities
 * in every module
 * 
 * @author Bibhusrita.Nayak
 * @since 12-Sep-2022
 *
 */
@Component
public class ActivityLog {
	private static final Logger logger = LogManager.getLogger(ActivityLog.class);
	@Autowired
	private ServletContext servletContext = null;
	@Autowired
	private PropertyMasterService propertyMasterService = null;

	@ResponseBody
	public void activitylog(Map<String, String> requestParams) throws Exception {
		activitylog(requestParams, "false");
	}

	@ResponseBody
	public void activitylog(Map<String, String> requestParams, String isFromRestAPI) throws CustomStopException,Exception {
		String isActivityLogEnabled = propertyMasterService.findPropertyMasterValue("isActivityLogEnabled");
		if (isActivityLogEnabled != null && isActivityLogEnabled.equalsIgnoreCase("true")) {
			String contextPath = servletContext.getContextPath();
			String baseUrl = propertyMasterService.findPropertyMasterValue("base-url");
			StringBuilder fullRestApiUrl = new StringBuilder().append(baseUrl);
			if (contextPath != null && contextPath.isEmpty() == false) {
				fullRestApiUrl = fullRestApiUrl.append(contextPath);
			}
			fullRestApiUrl = fullRestApiUrl.append("/api/activityLog");
			String restApiUrl = fullRestApiUrl.toString();
			requestParams.put("isFromRestAPI", isFromRestAPI);
			try {
				MultiValueMap<String, String> multipvalueMap = new LinkedMultiValueMap<String, String>();
				for (Entry<String, String> entry : requestParams.entrySet()) {
					List<String> list = new ArrayList<>();
					list.add(entry.getValue());
					multipvalueMap.put(entry.getKey(), list);
				}
				Builder builder = WebClient.builder().baseUrl(restApiUrl)
						.defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
						.defaultHeader(HttpHeaders.USER_AGENT, "JQuiver");
				WebClient webClient = builder.build();
				Mono<ResponseEntity<String>> responseContent = webClient.method(HttpMethod.resolve("POST"))
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
				ResponseEntity<String> responseString = responseContent.block();
				CustomResponseEntity customResponseEntity = convertResponseToVO(responseString);
			} catch (CustomStopException custStopException) {
				logger.error("Error occured in activitylog for Stop Exception.", custStopException);
				throw custStopException;
			} catch (Exception a_exception) {
				logger.error("Error occured in activitylog.", a_exception);

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
