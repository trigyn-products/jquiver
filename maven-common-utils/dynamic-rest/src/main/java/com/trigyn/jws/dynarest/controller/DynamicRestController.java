package com.trigyn.jws.dynarest.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dynarest.service.JwsDynamicRestDetailService;
import com.trigyn.jws.dynarest.vo.RestApiDetails;

@RestController
public class DynamicRestController {

	private static final Logger LOGGER = LogManager.getLogger(DynamicRestController.class);
	
    private static final String METHOD_SIGNATURE_MESSAGE = "Make sure you have the method signature correct. Signature should be similar to : - public T methodName(Map<String, Object> requestParameters, Map<String, Object> resultSetParameters, UserDetailsVO, details) {}";
	
    @Autowired
    private JwsDynamicRestDetailService jwsService = null;
    
    @RequestMapping("/api/**")
    public ResponseEntity<?> callDynamicEntity(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String requestUri = httpServletRequest.getRequestURI();
        requestUri = requestUri.replaceFirst("/api/", "");
        RestApiDetails restApiDetails = jwsService.getRestApiDetails(requestUri);
        Map<String, Object> requestParams;

        if (restApiDetails == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND.getReasonPhrase(), HttpStatus.NOT_FOUND);
        }

        String requestType = httpServletRequest.getMethod();
        
        if(Boolean.FALSE.equals(requestType.equals(restApiDetails.getMethodType()))) {
            return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(), HttpStatus.METHOD_NOT_ALLOWED);
        }

        requestParams = validateAndProcessRequestParams(httpServletRequest, restApiDetails);

        try {
            Map<String, Object> queriesResponse = jwsService.executeDAOQueries(restApiDetails.getDynamicId(), requestParams);
            Object response = null;
			try {
				response = jwsService.createSourceCodeAndInvokeServiceLogic(requestParams, queriesResponse, restApiDetails);
			} catch (IllegalArgumentException exception) {
				LOGGER.error("Error occured while invoking the method ", exception);
				httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(), METHOD_SIGNATURE_MESSAGE);
			} catch (InvocationTargetException exception) {
				LOGGER.error("Error occured while invoking the method ", exception);
			} catch (NoSuchMethodException exception) {
				LOGGER.error("Error occured while invoking the method ", exception);
				httpServletResponse.sendError(HttpStatus.PRECONDITION_FAILED.value(), METHOD_SIGNATURE_MESSAGE);
			} catch (ClassNotFoundException exception) {
				LOGGER.error("Error occured while invoking the method ", exception);
				httpServletResponse.sendError(HttpStatus.NOT_FOUND.value(), "The class was not found in the mentioned package.");
			}
            buildResponseEntity(httpServletResponse, restApiDetails);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } 


    private Map<String, Object> validateAndProcessRequestParams(HttpServletRequest httpServletRequest, RestApiDetails restDetails) {
        Map<String, Object> requestParams = new HashMap<>();
        for (String requestParamKey : httpServletRequest.getParameterMap().keySet()) {
            requestParams.put(requestParamKey, httpServletRequest.getParameter(requestParamKey));
        }
        return requestParams;
    }
 
    private void buildResponseEntity(HttpServletResponse httpServletResponse, RestApiDetails restDetails) {
        httpServletResponse.setHeader("content-type", restDetails.getReponseType());
    }
}
