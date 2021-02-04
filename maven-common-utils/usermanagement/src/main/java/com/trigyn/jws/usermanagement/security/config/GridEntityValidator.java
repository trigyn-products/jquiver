package com.trigyn.jws.usermanagement.security.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.usermanagement.repository.AuthorizedValidatorDAO;

@Component
public class GridEntityValidator implements EntityValidator {

	@Autowired
	private AuthorizedValidatorDAO	authorizedValidatorDAO	= null;

	private String					primaryKeyName			= "gridId";

	private String					moduleId				= "07067149-098d-11eb-9a16-f48e38ab9348";

	@Override
	public boolean hasAccessToEntity(HttpServletRequest reqObject, List<String> roleNames,
			ProceedingJoinPoint a_joinPoint) {
		boolean	hasAccess	= false;
		Long	count		= authorizedValidatorDAO.hasAccessToGridUtils(reqObject.getParameter(primaryKeyName),
				roleNames, moduleId);
		if (count > 0) {
			hasAccess = true;
		}
		return hasAccess;
	}

}
