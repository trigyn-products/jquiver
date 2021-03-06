
package com.trigyn.jws.applicationmetrics.interceptor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.google.gson.JsonObject;
import com.trigyn.jws.applicationmetrics.entities.HttpTraceEntity;

@Component
public class ApplicationServiceInterceptor implements HandlerInterceptor {

	private static LinkedHashMap<String, LinkedList<HttpTraceEntity>>	requestInformation	= new LinkedHashMap<>();

	private static LinkedHashMap<String, HttpTraceEntity>				timeInfo			= new LinkedHashMap<>();

	private LinkedHashMap<String, Long>									sessionInfo			= new LinkedHashMap<>();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Long time = request.getSession().getLastAccessedTime();
		sessionInfo.put(request.getRequestURL().toString(), time);
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception)
			throws Exception {

		if (handler instanceof HandlerMethod) {
			HandlerMethod	handlerMethod	= (HandlerMethod) handler;
			JsonObject		requestJson		= new JsonObject();
			requestJson.addProperty("request-url", request.getRequestURL().toString());
			requestJson.addProperty("request-type", request.getMethod());

			JsonObject responseJson = new JsonObject();
			responseJson.addProperty("response-status", response.getStatus());
			responseJson.addProperty("response-type", response.getContentType());
			if (exception != null) {
				String message = HttpStatus.resolve(response.getStatus()).getReasonPhrase() + ": " + exception.getMessage();
				responseJson.addProperty("response-message", message);
			} else {
				responseJson.addProperty("response-message", HttpStatus.resolve(response.getStatus()).getReasonPhrase());
			}
			JsonObject auxJsonObject = new JsonObject();
			auxJsonObject.addProperty("method-description", handlerMethod.getResolvedFromHandlerMethod().toString());
			auxJsonObject.addProperty("method-parameter", handlerMethod.getMethodParameters().toString());
			auxJsonObject.addProperty("method-log", handlerMethod.getShortLogMessage());

			Date						date			= new Date();
			SimpleDateFormat			formatter		= new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			String						strDate			= formatter.format(date);
			HttpTraceEntity				httpTraceEntity	= new HttpTraceEntity(requestJson.toString(), responseJson.toString(),
					auxJsonObject.toString(), strDate);

			LinkedList<HttpTraceEntity>	entities		= requestInformation.get(request.getRequestURL().toString());
			if (entities == null) {
				entities = new LinkedList<>();
			}
			entities.add(httpTraceEntity);
			requestInformation.put(request.getRequestURL().toString(), entities);

			String modifiedRequestURL = getModifiedRequestURL(request.getRequestURL().toString());
			requestJson.addProperty("updated-request-url", modifiedRequestURL);
			Long			time				= new Date().getTime() - sessionInfo.get(request.getRequestURL().toString());
			HttpTraceEntity	httpTraceEntity1	= new HttpTraceEntity(requestJson.toString(), responseJson.toString(),
					auxJsonObject.toString(), strDate, time, time, time);

			if (timeInfo.get(modifiedRequestURL) != null) {
				HttpTraceEntity	httpTraceEnt	= timeInfo.get(modifiedRequestURL);
				Long			minTime			= httpTraceEnt.getMinRequestDuration();
				Long			maxTime			= httpTraceEnt.getMaxRequestDuration();

				if (time < minTime) {
					minTime = time;
				}

				if (time > maxTime) {
					maxTime = time;
				}

				Long avgTime = (maxTime + minTime) / 2;
				httpTraceEntity1 = new HttpTraceEntity(requestJson.toString(), responseJson.toString(), auxJsonObject.toString(), strDate,
						minTime, maxTime, avgTime);

			}
			timeInfo.put(modifiedRequestURL, httpTraceEntity1);
		}
	}

	private String getModifiedRequestURL(String requestURL) {
		String modifiedRequestURL = requestURL;

		if (requestURL.contains("/view/")) {
			modifiedRequestURL	= requestURL.substring(0, requestURL.lastIndexOf("/view/"));
			modifiedRequestURL	= modifiedRequestURL.concat("/view");
		} else if (requestURL.contains("/api/")) {
			modifiedRequestURL	= requestURL.substring(0, requestURL.indexOf("/api/"));
			modifiedRequestURL	= modifiedRequestURL.concat("/api");
		} else if (requestURL.contains("/files/")) {
			modifiedRequestURL	= requestURL.substring(0, requestURL.indexOf("/files/"));
			modifiedRequestURL	= modifiedRequestURL.concat("/files");
		} else if (requestURL.contains("/aet?")) {
			modifiedRequestURL	= requestURL.substring(0, requestURL.indexOf("/aet?"));
			modifiedRequestURL	= modifiedRequestURL.concat("/aet");
		} else if (requestURL.contains("/aea?")) {
			modifiedRequestURL	= requestURL.substring(0, requestURL.indexOf("/aea?"));
			modifiedRequestURL	= modifiedRequestURL.concat("/aea");
		}

		return modifiedRequestURL;
	}

	public static LinkedHashMap<String, LinkedList<HttpTraceEntity>> getRequestInformation() {
		return requestInformation;
	}

	public static LinkedHashMap<String, HttpTraceEntity> getTimeInfo() {
		return timeInfo;
	}

}
