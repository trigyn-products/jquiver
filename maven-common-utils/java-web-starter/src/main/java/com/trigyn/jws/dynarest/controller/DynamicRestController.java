package com.trigyn.jws.dynarest.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dynarest.service.JwsDynamicRestDetailService;
import com.trigyn.jws.dynarest.vo.RestApiDetails;
import com.trigyn.jws.usermanagement.security.config.Authorized;
import com.trigyn.jws.usermanagement.utils.Constants;

@RestController
public class DynamicRestController {

	@Autowired
	private JwsDynamicRestDetailService jwsService = null;
	
	private static final Logger LOGGER = LogManager.getLogger(DynamicRestController.class);

	@RequestMapping(value = { "/api/**", "/japi/**" })
	@Authorized(moduleName = Constants.DYNAMICREST)
	@ResponseBody
	public ResponseEntity<?> callDynamicEntity(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws Exception, CustomStopException {
		try {
			String requestUri = httpServletRequest.getRequestURI()
					.substring(httpServletRequest.getContextPath().length());

			if (requestUri.startsWith("/japi/")) {
				requestUri = requestUri.replaceFirst("/japi/", "");
			} else {
				requestUri = requestUri.replaceFirst("/api/", "");
			}
			RestApiDetails restApiDetails = jwsService.getRestApiDetails(requestUri);

			return jwsService.loadDynamicRestDetails(httpServletRequest, httpServletResponse, restApiDetails);
		} catch (CustomStopException custStopException) {
			LOGGER.error("Error occured in callDynamicEntity.", custStopException);
			httpServletResponse.sendError(custStopException.getStatusCode(), custStopException.getMessage());
			return null;
		}
	}
}
