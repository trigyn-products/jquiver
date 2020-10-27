package com.trigyn.jws.usermanagement.security.config;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.trigyn.jws.usermanagement.utils.Constants;


@Aspect
@Component
public class AuthorizedValidator {
	
	
	@Autowired
	private EntityValidatorFactory entityValidatorFactory = null;
	
	
	@Pointcut("@annotation(com.trigyn.jws.usermanagement.security.config.Authorized)")
	private void customHasPermission() {
	}
	
	
	@Around("com.trigyn.jws.usermanagement.security.config.AuthorizedValidator.customHasPermission()")
	public Object validateEntityPermission(ProceedingJoinPoint a_joinPoint) throws Throwable {
		
		MethodSignature signature = (MethodSignature) a_joinPoint.getSignature();
		Method method = signature.getMethod();
		
		List<String> roleNames = new ArrayList<>();
		HttpServletRequest requestObject = getRequest();
		HttpServletResponse responseObject = getResponse();
		Authorized myAnnotation = method.getAnnotation(Authorized.class);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String moduleName = myAnnotation.moduleName();
		
		if((authentication instanceof AnonymousAuthenticationToken)) {
	
			roleNames.add(Constants.ANONYMOUS_ROLE_NAME);
			
		}else {
			
			UserInformation userInformation = (UserInformation) authentication.getPrincipal();
			roleNames.addAll(userInformation.getRoles());
		}
		
		EntityValidator entityValidator =  entityValidatorFactory.createEntityValidator(moduleName);
		boolean hasAccess  = entityValidator.hasAccessToEntity(requestObject, roleNames, a_joinPoint);
		
		if(!hasAccess) {
			responseObject.sendError(HttpStatus.FORBIDDEN.value(), "You dont have rights to access this entity");
			//throw new AccessDeniedException("You dont have rights to access this entity");
		}
		
		
		
		
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
