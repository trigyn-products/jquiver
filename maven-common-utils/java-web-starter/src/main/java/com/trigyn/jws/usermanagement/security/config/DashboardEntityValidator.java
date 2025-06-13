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
public class DashboardEntityValidator implements EntityValidator {

	private final static Logger					logger							= LoggerFactory.getLogger(DashboardEntityValidator.class);

	@Autowired
	private AuthorizedValidatorDAO				authorizedValidatorDAO			= null;

	@Autowired
	private JwsEntityRoleAssociationRepository	entityRoleAssociationRepository	= null;

	private String								primaryKeyName					= "dashboardId";
	
	@Autowired
	private JwsEntityRoleAssociationDAO entityRoleAssociationDAO = null;

	@Override
	public boolean hasAccessToEntity(HttpServletRequest reqObject, List<String> roleNames, ProceedingJoinPoint a_joinPoint) {
		logger.debug(
				"Inside DashboardEntityValidator.hasAccessToEntity(dashboardId - {}, reqObject - {}, roleNames - {}, a_joinPoint - {})",
				reqObject.getParameter(primaryKeyName), reqObject, roleNames, a_joinPoint);
		boolean	hasAccess	= false;
		Long	count;
		if(null == reqObject.getParameter(primaryKeyName) || reqObject.getParameter(primaryKeyName).isEmpty() == true ) {
			count		= authorizedValidatorDAO.hasAccessToDashboard(a_joinPoint.getArgs()[2].toString(), roleNames);
		} else {
			count		= authorizedValidatorDAO.hasAccessToDashboard(reqObject.getParameter(primaryKeyName), roleNames);
		}
		if (count > 0) {
			hasAccess = true;
		}
		return hasAccess;
	}

	@Override
	public String getEntityName(HttpServletRequest reqObject, List<String> roleNameList, ProceedingJoinPoint a_joinPoint) {
		logger.debug("Inside DashboardEntityValidator.getEntityName(dashboardId - {}, reqObject - {}, roleNameList - {}, a_joinPoint - {})",
				reqObject.getParameter(primaryKeyName), reqObject, roleNameList, a_joinPoint);
		String	dashoardId		= "";
		if(null == reqObject.getParameter(primaryKeyName) || reqObject.getParameter(primaryKeyName).isEmpty() == true ) {
			dashoardId		= a_joinPoint.getArgs()[2].toString();
		} else {
			dashoardId		= reqObject.getParameter(primaryKeyName);
		}
		
		String	dashoardName	= authorizedValidatorDAO.getDashboardNameById(dashoardId);
		return entityRoleAssociationDAO.getEntityNameByEntityAndRoleId(Constants.Modules.DASHBOARD.getModuleName(), dashoardName);

	}

}
