package com.trigyn.jws.usermanagement.security.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.usermanagement.repository.AuthorizedValidatorDAO;

@Component
public class TemplateEntityValidator implements EntityValidator {

	@Autowired
	private AuthorizedValidatorDAO authorizedValidatorDAO = null;

	@Override
	public boolean hasAccessToEntity(HttpServletRequest reqObject, List<String> roleNames,
			ProceedingJoinPoint a_joinPoint) {

		String	templateName	= a_joinPoint.getArgs()[0].toString();

		boolean	hasAccess		= false;
		Long	count			= authorizedValidatorDAO.hasAccessToTemplate(templateName, roleNames);
		if (count > 0) {
			hasAccess = true;
		}
		return hasAccess;
	}

}
