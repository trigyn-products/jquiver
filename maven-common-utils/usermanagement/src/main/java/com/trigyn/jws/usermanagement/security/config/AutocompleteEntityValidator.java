package com.trigyn.jws.usermanagement.security.config;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.usermanagement.repository.AuthorizedValidatorDAO;

@Component
public class AutocompleteEntityValidator  implements EntityValidator{

	@Autowired
	private AuthorizedValidatorDAO authorizedValidatorDAO = null;
	
	private String primaryKeyName = "autocompleteId";
	
	private String moduleId = "91a81b68-0ece-11eb-94b2-f48e38ab9348";
	
	@Override
	public boolean hasAccessToEntity(HttpServletRequest reqObject, List<String> roleNames,ProceedingJoinPoint a_joinPoint) {
		boolean hasAccess = false;
		Long count = authorizedValidatorDAO.hasAccessToAutocomplete(reqObject.getParameter(primaryKeyName), roleNames, moduleId);
		if (count > 0) {
			hasAccess = true;
		}
		return hasAccess;
	}

}
