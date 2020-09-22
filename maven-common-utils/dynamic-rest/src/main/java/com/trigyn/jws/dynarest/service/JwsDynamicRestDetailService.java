package com.trigyn.jws.dynarest.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
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

    private static final String DYNAREST_CLASS_STRUCTURE_TEMPLATE 			= "dynarest-class-template-structure";

    private static final String SERVICE_CLASS_NAME 							= "ServiceLogic";

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
        TemplateVO templateVO = dbTemplatingService.getTemplateByName(DYNAREST_CLASS_STRUCTURE_TEMPLATE);
        File sourceFile = File.createTempFile(SERVICE_CLASS_NAME, ".java");
        String className = sourceFile.getName().replaceAll(".java", "");
        requestParameterMap.put("className", className);
        String sourceCode = templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), requestParameterMap);
        Class<?> serviceClass = getCompiledClassOfServiceLogic(sourceCode, sourceFile, className);
        Method serviceLogicMethod = serviceClass.getDeclaredMethod(restApiDetails.getMethodName(), Map.class, Map.class);
        return serviceLogicMethod.invoke(serviceClass.getDeclaredConstructor().newInstance(), requestParameterMap, daoResultSets);
    }
    
    

    private Class<?> getCompiledClassOfServiceLogic(String sourceCode, File sourceFile, String className) throws IOException, ClassNotFoundException {
        sourceFile.deleteOnExit();
        FileWriter writer = new FileWriter(sourceFile);
        writer.write(sourceCode);
        writer.close();

        // compile the source file
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        File parentDirectory = sourceFile.getParentFile();
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(parentDirectory));
        Iterable<? extends JavaFileObject> compilationUnits = fileManager
                .getJavaFileObjectsFromFiles(Arrays.asList(sourceFile));
        compiler.getTask(null, fileManager, null, null, null, compilationUnits).call();
        fileManager.close();

        // load the compiled class
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[] { parentDirectory.toURI().toURL() });
        return classLoader.loadClass(className);
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
