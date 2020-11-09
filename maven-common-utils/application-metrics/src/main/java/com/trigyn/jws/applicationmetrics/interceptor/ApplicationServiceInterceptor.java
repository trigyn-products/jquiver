package com.trigyn.jws.applicationmetrics.interceptor;

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

	private static LinkedHashMap<String, LinkedList<HttpTraceEntity>> requestInformation = new LinkedHashMap<>();
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {

		if(handler instanceof HandlerMethod) {
		    HandlerMethod handlerMethod = (HandlerMethod) handler;
			JsonObject requestJson = new JsonObject();
			requestJson.addProperty("request-url", request.getRequestURL().toString());
			requestJson.addProperty("request-type", request.getMethod());
			
			JsonObject responseJson = new JsonObject();
			responseJson.addProperty("response-status", response.getStatus());
			responseJson.addProperty("response-type", response.getContentType());
			if(exception != null) {
				String message = HttpStatus.resolve(response.getStatus()).getReasonPhrase() + ": " + exception.getMessage();
				responseJson.addProperty("response-message", message);
			} else {
				responseJson.addProperty("response-message", HttpStatus.resolve(response.getStatus()).getReasonPhrase());
			}
			
			JsonObject auxJsonObject = new JsonObject();
			auxJsonObject.addProperty("method-description", handlerMethod.getResolvedFromHandlerMethod().toString());
			auxJsonObject.addProperty("method-parameter", handlerMethod.getMethodParameters().toString());
			auxJsonObject.addProperty("method-log", handlerMethod.getShortLogMessage());
			
			HttpTraceEntity httpTraceEntity = new HttpTraceEntity(requestJson.toString(), responseJson.toString(), auxJsonObject.toString(), null, new Date());
			
			LinkedList<HttpTraceEntity> entities = requestInformation.get(request.getRequestURL().toString());
			if(entities == null) {
				entities = new LinkedList<>();
			}
			entities.add(httpTraceEntity);
			requestInformation.put(request.getRequestURL().toString(), entities);
		}
	}
	
	public static LinkedHashMap<String, LinkedList<HttpTraceEntity>> getRequestInformation() {
		return requestInformation;
	}

}
