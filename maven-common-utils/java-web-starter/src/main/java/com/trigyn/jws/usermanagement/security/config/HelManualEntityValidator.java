package com.trigyn.jws.usermanagement.security.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.usermanagement.repository.AuthorizedValidatorDAO;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationRepository;
import com.trigyn.jws.usermanagement.utils.Constants;

@Component
public class HelManualEntityValidator implements EntityValidator {

	private final static Logger					logger							= LogManager.getLogger(HelManualEntityValidator.class);

	@Autowired
	private AuthorizedValidatorDAO				authorizedValidatorDAO			= null;

	@Autowired
	private JwsEntityRoleAssociationRepository	entityRoleAssociationRepository	= null;

	@Override
	public boolean hasAccessToEntity(HttpServletRequest reqObject, List<String> roleNames, ProceedingJoinPoint a_joinPoint) {
		logger.debug("Inside HelManualEntityValidator.hasAccessToEntity(manualType - {}, reqObject - {}, roleNames - {}, a_joinPoint - {})",
				reqObject.getParameter("mt"), reqObject, roleNames, a_joinPoint);

		String	manualType	= reqObject.getParameter("mt");
		String	isNew		= reqObject.getParameter("ie");

		boolean	hasAccess	= false;
		Long	count		= authorizedValidatorDAO.hasAccessToEntity(Constants.Modules.HELPMANUAL.getModuleName(), manualType, roleNames);
		if (count > 0 || (StringUtils.isBlank(isNew) == false && isNew.equals("0"))) {
			hasAccess = true;
		}
		return hasAccess;
	}

	@Override
	public String getEntityName(HttpServletRequest reqObject, List<String> roleNameList, ProceedingJoinPoint a_joinPoint) {
		logger.debug("Inside HelManualEntityValidator.getEntityName(manualType - {}, reqObject - {}, roleNameList - {}, a_joinPoint - {})",
				reqObject.getParameter("mt"), reqObject, roleNameList, a_joinPoint);

		String	manualType	= reqObject.getParameter("mt");
		String	manualName	= authorizedValidatorDAO.getManualNameById(manualType);
		return entityRoleAssociationRepository.getEntityNameByEntityAndRoleId(Constants.Modules.HELPMANUAL.getModuleName(), manualName);
	}

}
