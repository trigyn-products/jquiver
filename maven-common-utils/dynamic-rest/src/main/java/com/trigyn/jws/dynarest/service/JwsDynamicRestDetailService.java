package com.trigyn.jws.dynarest.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynarest.dao.JwsDynamicRestDAORepository;
import com.trigyn.jws.dynarest.dao.JwsDynamicRestDetailsRepository;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.dynarest.utils.Constants;
import com.trigyn.jws.dynarest.vo.RestApiDaoQueries;
import com.trigyn.jws.dynarest.vo.RestApiDetails;
import com.trigyn.jws.templating.utils.TemplatingUtils;

@Service
@Transactional
public class JwsDynamicRestDetailService {
	
	private final static Logger logger = LogManager.getLogger(JwsDynamicRestDetailService.class);

    @Autowired
    private TemplatingUtils templatingUtils 								= null;

    @Autowired
    private JwsDynamicRestDAORepository dynamicRestDAORepository 			= null;

    @Autowired
    private JwsDynarestDAO dynarestDAO 										= null;

    @Autowired
    private JwsDynamicRestDetailsRepository dyanmicRestDetailsRepository 	= null;
    
    @Autowired
    private IUserDetailsService detailsService								= null;
    
    @Autowired
    private ApplicationContext applicationContext							= null;
    

    public Object createSourceCodeAndInvokeServiceLogic(HttpServletRequest httpServletRequest, Map<String, Object> requestParameterMap, Map<String, Object> daoResultSets, RestApiDetails restApiDetails) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> apiDetails = objectMapper.convertValue(restApiDetails, Map.class);
        requestParameterMap.putAll(apiDetails);
        if(restApiDetails.getPlatformId().equals(Constants.Platforms.JAVA.getPlatform())) {
            return invokeAndExecuteOnJava(httpServletRequest, daoResultSets, restApiDetails);
        } else if(restApiDetails.getPlatformId().equals(Constants.Platforms.FTL.getPlatform())) {
            return invokeAndExecuteFTL(httpServletRequest, requestParameterMap, daoResultSets, restApiDetails);
        } else if(restApiDetails.getPlatformId().equals(Constants.Platforms.JAVASCRIPT.getPlatform())) {
            return invokeAndExecuteJavascript(httpServletRequest, requestParameterMap, daoResultSets, restApiDetails);
        } else {
            return null;
        }
    }


    private Object invokeAndExecuteFTL(HttpServletRequest httpServletRequest, Map<String, Object> requestParameterMap, Map<String, Object> daoResultSets, RestApiDetails restApiDetails) throws Exception {
        if(restApiDetails.getServiceLogic() != null || Boolean.FALSE.equals("".equals(restApiDetails.getServiceLogic()))) {
            requestParameterMap.putAll(daoResultSets);
            return templatingUtils.processTemplateContents(restApiDetails.getServiceLogic(), "service", requestParameterMap);
        }
        return null;
    }
    
    private Object invokeAndExecuteOnJava(HttpServletRequest httpServletRequest, Map<String, Object> daoResultSets, RestApiDetails restApiDetails) throws Exception, ClassNotFoundException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> serviceClass = Class.forName(restApiDetails.getServiceLogic(), Boolean.TRUE, this.getClass().getClassLoader());
        Object classInstance = serviceClass.getDeclaredConstructor().newInstance();
        Method serviceLogicMethod = serviceClass.getDeclaredMethod(restApiDetails.getMethodName(), HttpServletRequest.class, Map.class, UserDetailsVO.class);
        try {
			Method applicationContextMethod = serviceClass.getDeclaredMethod("setApplicationContext", ApplicationContext.class);
			applicationContextMethod.invoke(classInstance, applicationContext);
		} catch (NoSuchMethodException expection) {
			logger.warn("No method found for setting application context. Create method setApplicationContext to set applicationContext");
		} catch (SecurityException expection) {
			logger.error("Security exception occured while invoking setApplication context ", expection);
		} catch (IllegalAccessException expection) {
			logger.error("IllegalAccessException occured while invoking setApplication context ", expection);
		} catch (IllegalArgumentException expection) {
			logger.error("IllegalArgumentException occured while invoking setApplication context ", expection);
		} catch (InvocationTargetException expection) {
			logger.error("InvocationTargetException occured while invoking setApplication context ", expection);
		}
        return serviceLogicMethod.invoke(classInstance, httpServletRequest, daoResultSets, detailsService.getUserDetails());
    }
    
    public Object invokeAndExecuteOnFileJava(MultipartFile[] files, HttpServletRequest httpServletRequest, Map<String, Object> daoResultSets, RestApiDetails restApiDetails) throws Exception, ClassNotFoundException,
    		NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
		Class<?> serviceClass = Class.forName(restApiDetails.getServiceLogic(), Boolean.TRUE, this.getClass().getClassLoader());
		Object classInstance = serviceClass.getDeclaredConstructor().newInstance();
		Method serviceLogicMethod = serviceClass.getDeclaredMethod(restApiDetails.getMethodName(), MultipartFile.class, HttpServletRequest.class, Map.class, UserDetailsVO.class);
		try {
			Method applicationContextMethod = serviceClass.getDeclaredMethod("setApplicationContext", ApplicationContext.class);
			applicationContextMethod.invoke(classInstance, applicationContext);
		} catch (NoSuchMethodException expection) {
			logger.warn("No method found for setting application context. Create method setApplicationContext to set applicationContext");
		} catch (SecurityException expection) {
			logger.error("Security exception occured while invoking setApplication context ", expection);
		} catch (IllegalAccessException expection) {
			logger.error("IllegalAccessException occured while invoking setApplication context ", expection);
		} catch (IllegalArgumentException expection) {
			logger.error("IllegalArgumentException occured while invoking setApplication context ", expection);
		} catch (InvocationTargetException expection) {
			logger.error("InvocationTargetException occured while invoking setApplication context ", expection);
		}
		return serviceLogicMethod.invoke(classInstance, files, httpServletRequest, daoResultSets, detailsService.getUserDetails());
	}

    private Object invokeAndExecuteJavascript(HttpServletRequest httpServletRequest, Map<String, Object> requestParameterMap, Map<String, Object> daoResultSets, RestApiDetails restApiDetails) throws ScriptException {
       ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
       ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("javascript");
       scriptEngine.put("requestDetails", requestParameterMap);
       scriptEngine.put("daoResults", daoResultSets);
       return scriptEngine.eval(restApiDetails.getServiceLogic());
    }

    
    public RestApiDetails getRestApiDetails(String requestUri) {
        return dyanmicRestDetailsRepository.findByJwsDynamicRestUrl(requestUri);
    }
    
    public Map<String, Object> executeDAOQueries(String dynarestId, Map<String, Object> parameterMap) throws Exception {
        List<RestApiDaoQueries> apiDaoQueries = dynamicRestDAORepository.getRestApiDaoQueriesByApiId(dynarestId);
        Map<String, Object> resultSetMap = new HashMap<>();
        for (RestApiDaoQueries restApiDaoQueries : apiDaoQueries) {
            String query = templatingUtils.processTemplateContents(restApiDaoQueries.getJwsDaoQueryTemplate(), "apiQuery", parameterMap);
            List<Map<String, Object>> resultSet = new ArrayList<>(); 
            if(Constants.QueryType.DML.getQueryType() == restApiDaoQueries.getQueryType()) {
            	dynarestDAO.executeDMLQueries(query, parameterMap);
            }else {
            	resultSet = dynarestDAO.executeQueries(query, parameterMap);
            }
            resultSetMap.put(restApiDaoQueries.getJwsResultVariableName(), resultSet);
            parameterMap.put(restApiDaoQueries.getJwsResultVariableName(), resultSet);
        }
		return resultSetMap;
	}
    
}
