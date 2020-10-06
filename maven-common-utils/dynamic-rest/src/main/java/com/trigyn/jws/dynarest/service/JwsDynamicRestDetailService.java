package com.trigyn.jws.dynarest.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynarest.dao.JwsDynamicRestDAORepository;
import com.trigyn.jws.dynarest.dao.JwsDynamicRestDetailsRepository;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.dynarest.utils.Constants;
import com.trigyn.jws.dynarest.vo.RestApiDaoQueries;
import com.trigyn.jws.dynarest.vo.RestApiDetails;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;

import freemarker.template.TemplateException;

@Service
@Transactional
public class JwsDynamicRestDetailService {

    private static final String DYNAREST_CLASS_FILE_PATH 					= "dynarest-class-file-path";

	private static final String DYNAREST_CLASS_STRUCTURE_TEMPLATE 			= "dynarest-class-template-structure";

    @Autowired
    private DBTemplatingService dbTemplatingService 						= null;

    @Autowired
    private TemplatingUtils templatingUtils 								= null;

    @Autowired
    private JwsDynamicRestDAORepository dynamicRestDAORepository 			= null;

    @Autowired
    private JwsDynarestDAO dynarestDAO 										= null;

    @Autowired
    private JwsDynamicRestDetailsRepository dyanmicRestDetailsRepository 	= null;
    
    @Autowired
	private PropertyMasterService propertyMasterService 		 			= null;
    
    @Autowired
    private IUserDetailsService detailsService								= null;
    

    public Object createSourceCodeAndInvokeServiceLogic(Map<String, Object> requestParameterMap, Map<String, Object> daoResultSets, RestApiDetails restApiDetails) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> apiDetails = objectMapper.convertValue(restApiDetails, Map.class);
        requestParameterMap.putAll(apiDetails);
        if(restApiDetails.getPlatformId().equals(Constants.Platforms.JAVA.getPlatform())) {
            return invokeAndExecuteOnJava(requestParameterMap, daoResultSets, restApiDetails);
        } else if(restApiDetails.getPlatformId().equals(Constants.Platforms.FTL.getPlatform())) {
            return invokeAndExecuteFTL(requestParameterMap, daoResultSets, restApiDetails);
        } else {
            return null;
        }
    }


    private Object invokeAndExecuteFTL(Map<String, Object> requestParameterMap, Map<String, Object> daoResultSets, RestApiDetails restApiDetails) throws IOException, TemplateException {
        if(restApiDetails.getServiceLogic() != null || Boolean.FALSE.equals("".equals(restApiDetails.getServiceLogic()))) {
            requestParameterMap.putAll(daoResultSets);
            return templatingUtils.processTemplateContents(restApiDetails.getServiceLogic(), "service", requestParameterMap);
        }
        return null;
    }
    
    private Object invokeAndExecuteOnJava(Map<String, Object> requestParameterMap, Map<String, Object> daoResultSets, RestApiDetails restApiDetails) throws Exception, IOException, TemplateException, ClassNotFoundException,
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> serviceClass = getCompiledClassOfServiceLogic(restApiDetails);
        Method serviceLogicMethod = serviceClass.getDeclaredMethod(restApiDetails.getMethodName(), Map.class, Map.class, UserDetailsVO.class);
        return serviceLogicMethod.invoke(serviceClass.getDeclaredConstructor().newInstance(), requestParameterMap, daoResultSets, detailsService.getUserDetails());
    }
    

    private Class<?> getCompiledClassOfServiceLogic(RestApiDetails restApiDetails) throws ClassNotFoundException {
        return Class.forName(restApiDetails.getServiceLogic(), Boolean.TRUE, this.getClass().getClassLoader());
    }

    
    public RestApiDetails getRestApiDetails(String requestUri) {
        return dyanmicRestDetailsRepository.findByJwsDynamicRestUrl(requestUri);
    }
    
    public Map<String, Object> executeDAOQueries(Integer dynarestId, Map<String, Object> parameterMap) throws IOException, TemplateException {
        List<RestApiDaoQueries> apiDaoQueries = dynamicRestDAORepository.getRestApiDaoQueriesByApiId(dynarestId);
        Map<String, Object> resultSetMap = new HashMap<>();
        for (RestApiDaoQueries restApiDaoQueries : apiDaoQueries) {
            String query = templatingUtils.processTemplateContents(restApiDaoQueries.getJwsDaoQueryTemplate(), "apiQuery", parameterMap);
            List<Map<String, Object>> resultSet = dynarestDAO.executeQueries(query, parameterMap);
            resultSetMap.put(restApiDaoQueries.getJwsResultVariableName(), resultSet);
        }
		return resultSetMap;
	}
    
}
