package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dynamicform.controller.DynamicFormController;
import com.trigyn.jws.dynarest.cipher.utils.ParameterWrappedRequest;
import com.trigyn.jws.usermanagement.security.config.Authorized;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.webstarter.service.MasterModuleService;
import com.trigyn.jws.webstarter.utils.Constant;

@RestController
@RequestMapping(value = "/view/**")
public class MasterModuleController {

	private final static Logger		logger					= LogManager.getLogger(MasterCreatorController.class);

	@Autowired
	private MasterModuleService		masterModuleService		= null;

	@Autowired
	private DynamicFormController	dynamicFormController	= null;

	@RequestMapping()
	@Authorized(moduleName = Constants.SITELAYOUT)
	public Object loadModuleContent(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
			throws IOException, CustomStopException {
		try {
			String moduleUrl = httpServletRequest.getRequestURI()
					.substring(httpServletRequest.getContextPath().length());
			moduleUrl = moduleUrl.replaceFirst("/view/", "");
			if (moduleUrl.indexOf("/") != -1) {
				moduleUrl = moduleUrl.substring(0, moduleUrl.indexOf("/"));
			}
			Map<String, Object>	moduleDetailsMap	= masterModuleService.getModuleDetails(moduleUrl,
					httpServletRequest);
			if(moduleDetailsMap.get("targetLookupId")!=null && moduleDetailsMap.get("targetLookupId").toString()!=null) {
				Integer				targetLookupId		= Integer.parseInt(moduleDetailsMap.get("targetLookupId").toString());
				if ("POST".equals(httpServletRequest.getMethod())
						&& targetLookupId.equals(Constant.TargetLookupId.DYANMICFORM.getTargetLookupId())) {
					Map<String, Object>	parameterMap	= masterModuleService
							.validateAndProcessRequestParams(httpServletRequest);
					Map<String, String>	requestMap		= new HashMap<String, String>();
					if (moduleDetailsMap != null && moduleDetailsMap.containsKey("requestParamJson")
							&& moduleDetailsMap.get("requestParamJson") != null) {
						String	requestParam	= String.valueOf(moduleDetailsMap.get("requestParamJson"));

						Gson	gsonReqParamMap				= new Gson();
						if (requestParam != null && requestParam.isEmpty() == false) {
							Map<String, String> requestBodyMap = gsonReqParamMap.fromJson(requestParam.toString(), Map.class);
							if (requestBodyMap != null && requestBodyMap.isEmpty() == false) {
								for (Entry<String, String> entry : requestBodyMap.entrySet()) {
									requestMap.put(entry.getKey(), entry.getValue());
								}
							}
						}
					}
					parameterMap.putAll(requestMap);
					
					Map<String, String[]> extraParams = new TreeMap<String, String[]>();
					extraParams.put("formId", new String[]{moduleDetailsMap.get("targetTypeId").toString()});
					HttpServletRequest wrappedRequest = new ParameterWrappedRequest(httpServletRequest, extraParams);
					ServletRequestAttributes attributes = new ServletRequestAttributes(wrappedRequest);
					RequestContextHolder.setRequestAttributes(attributes);
					
					List<String> pathVariableList = masterModuleService.getPathVariables(httpServletRequest);
					if (CollectionUtils.isEmpty(pathVariableList) == false) {
						pathVariableList.remove(0);
					}
					parameterMap.put("pathVariableList", pathVariableList);

					return dynamicFormController.saveDynamicFormV2(wrappedRequest, httpServletResponse, parameterMap);
				} else if ("GET".equals(httpServletRequest.getMethod())) {
					return masterModuleService.loadTemplate(httpServletRequest, moduleUrl, httpServletResponse);
				} else {
					httpServletResponse.sendError(HttpStatus.METHOD_NOT_ALLOWED.value(), "Request not supported");
					return null;
				}
			} else {
				httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/");
				return null;
			}
		} catch (CustomStopException custStopException) {

			logger.error("Error occured in loadModuleContent.", custStopException);
			throw custStopException;
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
		}
		return null;
	}

}
