package com.trigyn.jws.usermanagement.security.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.usermanagement.repository.AuthorizedValidatorDAO;

@Component
public class SiteLayoutEntityValidator implements EntityValidator {

	@Autowired
	private AuthorizedValidatorDAO authorizedValidatorDAO = null;

	@Override
	public boolean hasAccessToEntity(HttpServletRequest reqObject, List<String> roleNames,
			ProceedingJoinPoint a_joinPoint) {
		boolean	hasAccess	= false;
		String	moduleUrl	= reqObject.getRequestURI();
		moduleUrl = moduleUrl.replaceFirst("/view/", "");
		Long count = authorizedValidatorDAO.hasAccessToSiteLayout(moduleUrl, roleNames);
		if (count > 0) {
			hasAccess = true;
		}
		return hasAccess;
	}

}
