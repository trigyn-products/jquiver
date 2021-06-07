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
public class AutocompleteEntityValidator implements EntityValidator {

	@Autowired
	private AuthorizedValidatorDAO				authorizedValidatorDAO			= null;

	@Autowired
	private JwsEntityRoleAssociationRepository	entityRoleAssociationRepository	= null;

	private final static Logger					logger							= LogManager.getLogger(AutocompleteEntityValidator.class);

	private String								primaryKeyName					= "autocompleteId";

	private String								moduleId						= "91a81b68-0ece-11eb-94b2-f48e38ab9348";

	@Override
	public boolean hasAccessToEntity(HttpServletRequest reqObject, List<String> roleNames, ProceedingJoinPoint a_joinPoint) {
		logger.debug(
				"Inside AutocompleteEntityValidator.hasAccessToEntity(autocompleteId - {}, reqObject - {}, roleNames - {}, a_joinPoint - {})",
				reqObject.getParameter(primaryKeyName), reqObject, roleNames, a_joinPoint);

		boolean	hasAccess	= false;
		Long	count		= authorizedValidatorDAO.hasAccessToAutocomplete(reqObject.getParameter(primaryKeyName), roleNames, moduleId);
		if (count > 0) {
			hasAccess = true;
		}
		return hasAccess;
	}

	@Override
	public String getEntityName(HttpServletRequest reqObject, List<String> roleNameList, ProceedingJoinPoint a_joinPoint) {
		logger.debug(
				"Inside AutocompleteEntityValidator.getEntityName(autocompleteId - {}, reqObject - {}, roleNameList - {}, a_joinPoint - {})",
				reqObject.getParameter(primaryKeyName), reqObject, roleNameList, a_joinPoint);

		String autocompleteId = reqObject.getParameter(primaryKeyName);
		return entityRoleAssociationRepository.getEntityNameByEntityAndRoleId(Constants.Modules.AUTOCOMPLETE.getModuleName(),
				autocompleteId);
	}

}
