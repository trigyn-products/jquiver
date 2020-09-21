package app.trigyn.core.menu.exception;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import app.trigyn.core.templating.service.DBTemplatingService;
import app.trigyn.core.templating.utils.TemplatingUtils;
import app.trigyn.core.templating.vo.TemplateVO;

@EnableWebMvc
@ControllerAdvice
public class URLExceptionHandler {

    @Autowired
    private DBTemplatingService templateService 	= null;

    @Autowired
    private TemplatingUtils templateEngine 			= null;
    
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(value=HttpStatus.NOT_FOUND)
    @ResponseBody
	public String handleNoResourceFoundException(HttpServletRequest httpServletRequest) throws Exception {
        TemplateVO templateVO = templateService.getTemplateByName("error-page-not-found");
        return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), new HashMap<>());
	}

}
