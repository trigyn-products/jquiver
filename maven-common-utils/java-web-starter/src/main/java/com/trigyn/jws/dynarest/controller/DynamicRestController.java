package com.trigyn.jws.dynarest.controller;

import java.util.Arrays;
import java.util.List;
import java.util.logging.LogManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.dynarest.service.JwsDynamicRestDetailService;
import com.trigyn.jws.dynarest.vo.RestApiDetails;
import com.trigyn.jws.usermanagement.security.config.Authorized;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.webstarter.utils.JQuiverProperties;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class DynamicRestController {

	@Autowired
	private JwsDynamicRestDetailService	jwsService			= null;

	private static final Logger logger 						= LoggerFactory.getLogger(DynamicRestController.class);

	@Autowired
	private FileUtilities				fileUtilities = null;
	
	@Autowired
	private JQuiverProperties 			jQuiverPropeties 			= null;
	
	@Autowired
	private JwsDynarestDAO				jwsDynarestDAO		= null;

	
	@Authorized(moduleName = Constants.DYNAMICREST)
	@ResponseBody
	public ResponseEntity<?> callDynamicEntity(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws Exception, CustomStopException {
		try {
			RestApiDetails	restApiDetails	= null;
			String			requestUri		= httpServletRequest.getRequestURI()
					.substring(httpServletRequest.getContextPath().length());

			if (requestUri.startsWith("/japi/")) {
				requestUri = requestUri.replaceFirst("/japi/", "");
			} else {
				requestUri = requestUri.replaceFirst(jQuiverPropeties.getApiPath() + "/", "");
			}
			String requestURI = jwsDynarestDAO.matchDynaRestUrl(requestUri);
			if (requestURI != null) {
				restApiDetails = jwsService.getRestApiDetails(requestURI);
			}

			return jwsService.loadDynamicRestDetails(httpServletRequest, httpServletResponse, restApiDetails);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured in callDynamicEntity.", custStopException);
			fileUtilities.customSendError(httpServletResponse, custStopException.getStatusCode(),
					custStopException.getMessage());
			return null;
		}
	}
}
