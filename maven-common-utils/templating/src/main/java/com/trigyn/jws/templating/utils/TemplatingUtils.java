package com.trigyn.jws.templating.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.templating.vo.TemplateVO;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
public class TemplatingUtils {

    @Autowired
    private FreeMarkerConfigurer    freeMarkerConfigurer = null;

    @Autowired
    private ServletContext          servletContext      = null;

    @Autowired
    private SessionLocaleResolver   localeResolver      = null;

    @Autowired
    private MessageSource           messageSource       = null;
	
	@Autowired
	private FileUtilities fileUtilities  = null;

    public String processTemplateContents(String templateContent, String templateName, Map<String, Object> modelMap) throws IOException, TemplateException {
        templateContent = StringEscapeUtils.unescapeHtml4(templateContent);
        addTemplateProperties(modelMap);
        Template templateObj = new Template(templateName, new StringReader(templateContent),
                freeMarkerConfigurer.getConfiguration());
        Writer writer = new StringWriter();
        templateObj.process(modelMap, writer);
        return writer.toString();
    }

    public String processMultipleTemplateContents(String mainTemplateContent,  String templateName, Map<String, Object> modelMap, Map<String, String> childTemplateDetails)
            throws IOException, TemplateException {
        addTemplateProperties(modelMap);
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate(templateName, mainTemplateContent);
        for (Entry<String, String> templates : childTemplateDetails.entrySet()) {
            stringTemplateLoader.putTemplate(templates.getKey(), templates.getValue());
        }
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        configuration.setTemplateLoader(stringTemplateLoader);
        Template templateObj = configuration.getTemplate(templateName);
        Writer writer = new StringWriter();
        templateObj.process(modelMap, writer);
        return writer.toString();
    }

    private void addTemplateProperties(Map<String, Object> modelMap) {
        String contextPath = servletContext.getContextPath();
        modelMap.put("contextPath", contextPath);
        Locale locale = localeResolver.resolveLocale(getRequest());
        modelMap.put("messageSource", MessageSourceUtils.getMessageSource(messageSource, locale));
    }

    private HttpServletRequest getRequest() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return sra.getRequest();
	}

	public  void writeFileContents(TemplateVO templateVO, File file) throws Exception  {
		file.createNewFile();
		try(FileWriter fileWriter = new FileWriter(file)){
			fileWriter.write(templateVO.getTemplate());
		} catch (IOException e) {
			throw new Exception("Error while writing contents to file ",e);
		}
		String generateFileCheckSum = fileUtilities.generateFileChecksum(file);
		templateVO.setChecksum(generateFileCheckSum);
	}
    
}