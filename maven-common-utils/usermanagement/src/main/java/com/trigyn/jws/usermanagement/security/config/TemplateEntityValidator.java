package com.trigyn.jws.usermanagement.security.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.usermanagement.repository.AuthorizedValidatorDAO;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationRepository;
import com.trigyn.jws.usermanagement.utils.Constants;

@Component
public class TemplateEntityValidator implements EntityValidator {

	private final static Logger					logger							= LogManager
			.getLogger(TemplateEntityValidator.class);

	@Autowired
	private AuthorizedValidatorDAO				authorizedValidatorDAO			= null;

	@Autowired
	private JwsEntityRoleAssociationRepository	entityRoleAssociationRepository	= null;

	@Override
	public boolean hasAccessToEntity(HttpServletRequest reqObject, List<String> roleNames,
			ProceedingJoinPoint a_joinPoint) {
		logger.debug(
				"Inside TemplateEntityValidator.hasAccessToEntity(templateName - {}, reqObject - {}, roleNames - {}, a_joinPoint - {})",
				a_joinPoint.getArgs()[0].toString(), reqObject, roleNames, a_joinPoint);

		String	templateName	= a_joinPoint.getArgs()[0].toString();

		boolean	hasAccess		= false;
		Long	count			= authorizedValidatorDAO.hasAccessToTemplate(templateName, roleNames);
		if (count > 0) {
			hasAccess = true;
		}
		return hasAccess;
	}

	@Override
	public String getEntityName(HttpServletRequest reqObject, List<String> roleNameList,
			ProceedingJoinPoint a_joinPoint) {
		logger.debug(
				"Inside TemplateEntityValidator.getEntityName(templateName - {}, reqObject - {}, roleNameList - {}, a_joinPoint - {})",
				a_joinPoint.getArgs()[0].toString(), reqObject, roleNameList, a_joinPoint);

		String templateName = a_joinPoint.getArgs()[0].toString();
		return entityRoleAssociationRepository
				.getEntityNameByEntityAndRoleId(Constants.Modules.TEMPLATING.getModuleName(), templateName);
	}

}
