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
public class DynamicFormEntityValidator implements EntityValidator {

	private final static Logger					logger							= LogManager
			.getLogger(DynamicFormEntityValidator.class);

	@Autowired
	private AuthorizedValidatorDAO				authorizedValidatorDAO			= null;

	@Autowired
	private JwsEntityRoleAssociationRepository	entityRoleAssociationRepository	= null;

	private String								primaryKeyName					= "formId";

	@Override
	public boolean hasAccessToEntity(HttpServletRequest reqObject, List<String> roleNames,
			ProceedingJoinPoint a_joinPoint) {
		logger.debug(
				"Inside DynamicFormEntityValidator.hasAccessToEntity(formId - {}, reqObject - {}, roleNames - {}, a_joinPoint - {})",
				reqObject.getParameter(primaryKeyName), reqObject, roleNames, a_joinPoint);

		boolean	hasAccess	= false;
		Long	count		= authorizedValidatorDAO
				.hasAccessToCurrentDynamicForm(reqObject.getParameter(primaryKeyName), roleNames);
		if (count > 0) {
			hasAccess = true;
		}
		return hasAccess;
	}

	@Override
	public String getEntityName(HttpServletRequest reqObject, List<String> roleNameList,
			ProceedingJoinPoint a_joinPoint) {
		logger.debug(
				"Inside DynamicFormEntityValidator.getEntityName(formId - {}, reqObject - {}, roleNameList - {}, a_joinPoint - {})",
				reqObject.getParameter(primaryKeyName), reqObject, roleNameList, a_joinPoint);

		String	dynamicFormId	= reqObject.getParameter(primaryKeyName);
		String	dynamicFormName	= authorizedValidatorDAO.getDynamicFormNameById(dynamicFormId);
		return entityRoleAssociationRepository
				.getEntityNameByEntityAndRoleId(Constants.Modules.DYNAMICFORM.getModuleName(), dynamicFormName);
	}

}
