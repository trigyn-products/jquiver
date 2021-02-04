package com.trigyn.jws.usermanagement.security.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.usermanagement.repository.AuthorizedValidatorDAO;

@Component
public class DynamicFormEntityValidator implements EntityValidator {

	@Autowired
	private AuthorizedValidatorDAO	authorizedValidatorDAO	= null;

	private String					primaryKeyName			= "formId";

	@Override
	public boolean hasAccessToEntity(HttpServletRequest reqObject, List<String> roleNames,
			ProceedingJoinPoint a_joinPoint) {
		boolean	hasAccess	= false;
		Long	count		= authorizedValidatorDAO
				.hasAccessToCurrentDynamicForm(reqObject.getParameter(primaryKeyName), roleNames);
		if (count > 0) {
			hasAccess = true;
		}
		return hasAccess;
	}

}
