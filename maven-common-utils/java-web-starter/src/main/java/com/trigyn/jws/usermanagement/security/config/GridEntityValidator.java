package com.trigyn.jws.usermanagement.security.config;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.usermanagement.repository.AuthorizedValidatorDAO;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationDAO;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationRepository;
import com.trigyn.jws.usermanagement.utils.Constants;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class GridEntityValidator implements EntityValidator {

	private final static Logger					logger							= LoggerFactory.getLogger(GridEntityValidator.class);

	@Autowired
	private AuthorizedValidatorDAO				authorizedValidatorDAO			= null;

	@Autowired
	private JwsEntityRoleAssociationRepository	entityRoleAssociationRepository	= null;
	
	@Autowired
	private JwsEntityRoleAssociationDAO entityRoleAssociationDAO = null;

	private String								primaryKeyName					= "gridId";

	private String								moduleId						= "07067149-098d-11eb-9a16-f48e38ab9348";

	@Override
	public boolean hasAccessToEntity(HttpServletRequest reqObject, List<String> roleNames, ProceedingJoinPoint a_joinPoint) {
		logger.debug("Inside GridEntityValidator.hasAccessToEntity(gridId - {}, reqObject - {}, roleNames - {}, a_joinPoint - {})",
				reqObject.getParameter(primaryKeyName), reqObject, roleNames, a_joinPoint);
		boolean	hasAccess	= false;
		Long	count		= authorizedValidatorDAO.hasAccessToGridUtils(reqObject.getParameter(primaryKeyName), roleNames, moduleId);
		if (count > 0) {
			hasAccess = true;
		}
		return hasAccess;
	}

	@Override
	public String getEntityName(HttpServletRequest reqObject, List<String> roleNameList, ProceedingJoinPoint a_joinPoint) {
		logger.debug("Inside GridEntityValidator.getEntityName(gridId - {}, reqObject - {}, roleNameList - {}, a_joinPoint - {})",
				reqObject.getParameter(primaryKeyName), reqObject, roleNameList, a_joinPoint);

		String	gridId		= reqObject.getParameter(primaryKeyName);
		String	gridName	= authorizedValidatorDAO.getGridNameByGridId(gridId);
		return entityRoleAssociationDAO.getEntityNameByEntityAndRoleId(Constants.Modules.GRIDUTILS.getModuleName(), gridName);
	}

}
