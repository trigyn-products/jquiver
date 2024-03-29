package com.trigyn.jws.templating.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;
import com.trigyn.jws.dbutils.spi.PropertyMasterKeyVO;
import com.trigyn.jws.dbutils.utils.ApplicationContextUtils;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.vo.TemplateVO;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.StopException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
public class TemplatingUtils {

	private final static Logger		logger					= LogManager.getLogger(TemplatingUtils.class);

//	@Autowired
	private FreeMarkerConfigurer	freeMarkerConfigurer	= null;

	@Autowired
	private ServletContext			servletContext			= null;

	@Autowired
	private SessionLocaleResolver	localeResolver			= null;

	@Autowired
	private MessageSource			messageSource			= null;

	@Autowired
	private DBTemplatingService		templatingService		= null;

	@Autowired
	private DynamicTemplate			dynamicTemplate			= null;

	@Autowired
	private IUserDetailsService		detailsService			= null;

	@Autowired
	private PropertyMasterDetails	propertyMasterDetails	= null;
	
	public String processTemplateContents(String templateContent, String templateName, Map<String, Object> modelMap)
			throws CustomStopException, Exception {
		synchronized (this) {
			logger.debug(
					"Inside TemplatingUtils.processTemplateContents(templateContent: {}, templateName: {}, modelMap: {})",
					templateContent, templateName, modelMap);
			try {
				templateContent = StringEscapeUtils.unescapeHtml4(templateContent);
				StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
				stringTemplateLoader.putTemplate("templateUtils", templateUtils());
				Configuration configuration = getFreemarkerConfigDetails();
				configuration.addAutoInclude("templateUtils");
				configuration.setTemplateLoader(stringTemplateLoader);

				addTemplateProperties(modelMap);
				Template templateObj = new Template(templateName, new StringReader(templateContent), configuration);
				Writer writer = new StringWriter();
				templateObj.process(modelMap, writer);
				return writer.toString();
			} catch (StopException stopException) {
				throw new CustomStopException(stopException.getMessageWithoutStackTop());
			}
		}
	}

	public String processMultipleTemplateContents(String mainTemplateContent, String templateName,
			Map<String, Object> modelMap, Map<String, String> childTemplateDetails)
			throws CustomStopException, Exception {
		synchronized (this) {
			logger.debug(
					"Inside TemplatingUtils.processMultipleTemplateContents(mainTemplateContent: {}, templateName: {}, modelMap: {}, childTemplateDetails: {})",
					mainTemplateContent, templateName, modelMap, childTemplateDetails);
			try {
				addTemplateProperties(modelMap);
				StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
				stringTemplateLoader.putTemplate(templateName, mainTemplateContent);
				stringTemplateLoader.putTemplate("templateUtils", templateUtils());
				for (Entry<String, String> templates : childTemplateDetails.entrySet()) {
					stringTemplateLoader.putTemplate(templates.getKey(), templates.getValue());
				}
				Configuration configuration = getFreemarkerConfigDetails();
				configuration.addAutoInclude("templateUtils");
				configuration.setTemplateLoader(stringTemplateLoader);
				Template templateObj = configuration.getTemplate(templateName);
				Writer writer = new StringWriter();
				templateObj.process(modelMap, writer);
				return writer.toString();
			} catch (StopException stopException) {
				throw new CustomStopException(stopException.getMessageWithoutStackTop());
			}
		}
	}

	private void addTemplateProperties(Map<String, Object> modelMap) {
		synchronized(this){ 
			logger.debug("Inside TemplatingUtils.addTemplateProperties(modelMap: {})", modelMap);
			String								contextPath			= servletContext.getContextPath();
			Map<PropertyMasterKeyVO, String>	propertyMasterMap	= propertyMasterDetails.getAllProperties();
			Locale								locale				= null;
			
			HttpServletRequest requestObject = getRequest();
			
			if(requestObject == null) {
				locale = Locale.US;
			} else {
				locale = localeResolver.resolveLocale(requestObject);
				modelMap.put("httpRequestObject", requestObject);
			}
			modelMap.put("contextPath", contextPath);
			modelMap.put("messageSource", MessageSourceUtils.getMessageSource(messageSource, locale));
			modelMap.put("dynamicTemplate", dynamicTemplate);
			modelMap.put("systemProperties", propertyMasterMap);
			Object scriptUtil = ApplicationContextUtils.getApplicationContext().getBean("scriptUtil");
			modelMap.put("scriptUtil", scriptUtil);
	
			UserDetailsVO detailsVO = detailsService.getUserDetails();
			if (detailsVO != null) {
				modelMap.put("loggedInUserName", detailsVO.getUserName());
				modelMap.put("loggedInUserRoleList", detailsVO.getRoleIdList());
				modelMap.put("loggedInUserId", detailsVO.getUserId());
				modelMap.put("fullName", detailsVO.getFullName());
				modelMap.put("userObject", detailsVO);
			}
		}
		
	}

	private HttpServletRequest getRequest() {
		synchronized(this){ 
			ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
			if(sra != null) {
				return sra.getRequest();
			} else 
				return null;
		}
	}

	public String processFtl(String templateName, String templateContent, Map<String, Object> modelMap)
			throws IOException, TemplateException {
		logger.debug("Inside TemplatingUtils.processFtl(templateName: {}, templateContent: {}, modelMap: {})", templateName,
				templateContent, modelMap);
		synchronized(this){ 
			Template	templateObj	= new Template(templateName, new StringReader(templateContent), getFreemarkerConfigDetails());
			Writer		writer		= new StringWriter();
			templateObj.process(modelMap, writer);
			return writer.toString();
		}
	}

	public String processTemplate(String templateName, Map<String, Object> modelMap, Boolean includeLayout) throws Exception {
		logger.debug("Inside TemplatingUtils.processTemplate(templateName: {}, modelMap: {}, includeLayout: {})", templateName, modelMap,
				includeLayout);

		synchronized(this){ 
			TemplateVO templateVO = templatingService.getTemplateByName(templateName);
			addTemplateProperties(modelMap);
			Template	templateObj	= new Template(templateName, new StringReader(templateVO.getTemplate()),
					getFreemarkerConfigDetails());
			Writer		writer		= new StringWriter();
			templateObj.process(modelMap, writer);
			return writer.toString();
		}
	}

	public String templateUtils() throws Exception {

		synchronized(this){ 
			URL url = TemplatingUtils.class.getResource("/templates/template.ftl");
			return Resources.toString(url, Charsets.UTF_8);
		}
	}

	@Bean
	public FreeMarkerConfigurer freemarkerConfiguration() {
		freeMarkerConfigurer	= new FreeMarkerConfigurer();
		Properties				properties				= new Properties();
		properties.put("auto_import", "spring.ftl as spring");
		freeMarkerConfigurer.setFreemarkerSettings(properties);
		freeMarkerConfigurer.setConfiguration(getFreemarkerConfigDetails());
		return freeMarkerConfigurer;
	}

	public Configuration getFreemarkerConfigDetails() {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
		cfg.setAPIBuiltinEnabled(Boolean.TRUE);
		cfg.setNumberFormat("0.####");
		return cfg;
	}

}