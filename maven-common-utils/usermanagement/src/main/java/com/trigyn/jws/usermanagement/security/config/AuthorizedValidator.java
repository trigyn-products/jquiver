package com.trigyn.jws.usermanagement.security.config;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trigyn.jws.dbutils.repository.IModuleListingRepository;
import com.trigyn.jws.dbutils.utils.ApplicationContextUtils;
import com.trigyn.jws.usermanagement.utils.Constants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Aspect
@Component
public class AuthorizedValidator {

	private final static Logger			logger						= LogManager.getLogger(AuthorizedValidator.class);

	@Autowired
	private EntityValidatorFactory		entityValidatorFactory		= null;

	@Autowired
	private IModuleListingRepository	moduleListingRepository	= null;

	@Autowired
	private ServletContext							servletContext					= null;

	@Pointcut("@annotation(com.trigyn.jws.usermanagement.security.config.Authorized)")
	private void customHasPermission() {
	}

	@Around("com.trigyn.jws.usermanagement.security.config.AuthorizedValidator.customHasPermission()")
	public Object validateEntityPermission(ProceedingJoinPoint a_joinPoint) throws Throwable {

		/*
		 * if (!applicationSecurityDetails.getIsAuthenticationEnabled()) { return a_joinPoint.proceed(); }
		 */

		MethodSignature		signature		= (MethodSignature) a_joinPoint.getSignature();
		Method				method			= signature.getMethod();

		List<String>		roleNames		= new ArrayList<>();
		HttpServletRequest	requestObject	= getRequest();
		HttpServletResponse	responseObject	= getResponse();
		Authorized			myAnnotation	= method.getAnnotation(Authorized.class);
		Authentication		authentication	= null;
		if(requestObject.getSession().getAttribute("SPRING_SECURITY_CONTEXT") != null) {
			authentication = ((SecurityContextImpl) requestObject.getSession().getAttribute("SPRING_SECURITY_CONTEXT")).getAuthentication();
		}
		if(authentication == null) {
			authentication = SecurityContextHolder.getContext().getAuthentication();
		}
 		
		String				moduleName		= myAnnotation.moduleName();

		if (authentication == null || (authentication instanceof AnonymousAuthenticationToken)) {

			roleNames.add(Constants.ANONYMOUS_ROLE_NAME);

		} else {
			UserInformation userInformation = (UserInformation) authentication.getPrincipal();
			roleNames.addAll(userInformation.getRoles());
		}

		EntityValidator	entityValidator	= entityValidatorFactory.createEntityValidator(moduleName);
		boolean			hasAccess		= entityValidator.hasAccessToEntity(requestObject, roleNames, a_joinPoint);
		if (hasAccess == Boolean.FALSE) {
			String entityName = entityValidator.getEntityName(requestObject, roleNames, a_joinPoint);
			if (entityName == null) {
				logger.error("No record found for "+ moduleName, moduleName);
				responseObject.sendError(HttpStatus.NOT_FOUND.value());
				return null;
			} else {
				logger.error("You do not have enough privilege to access: "+ moduleName + " : " +entityName, entityName);
				String	requestUri	= requestObject.getRequestURI().substring(requestObject.getContextPath().length());
				if (requestUri.startsWith("/error")) {
					if(moduleListingRepository.getIsHomePageByUrl(entityName) == 1) {
						ApplicationContextUtils.getThreadLocal().set(403);
					} else {
						responseObject.sendError(HttpStatus.FORBIDDEN.value(), "You do not have enough privilege to access this module");
					}
				} else {
					if(roleNames.contains("ANONYMOUS")) {
						requestObject.getSession().setAttribute("CUSTOM_REDIRECT_URL", requestObject.getRequestURL());
						StringBuilder	redirectUrl		= new StringBuilder().append(servletContext.getContextPath()).append("/cf/login");
						responseObject.sendRedirect(redirectUrl.toString());
					} else {
						responseObject.sendError(HttpStatus.FORBIDDEN.value(), "You do not have enough privilege to access this module");
					}
				}
			// throw new AccessDeniedException("You dont have rights to access this
			// entity");
			return null;
			}
		}
		ApplicationContextUtils.getThreadLocal().set(200);
		return a_joinPoint.proceed();

	}

	private HttpServletRequest getRequest() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return sra.getRequest();
	}

	private HttpServletResponse getResponse() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return sra.getResponse();
	}

}
