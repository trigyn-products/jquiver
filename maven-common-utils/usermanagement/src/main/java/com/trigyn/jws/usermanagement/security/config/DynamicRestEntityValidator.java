package com.trigyn.jws.usermanagement.security.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// import com.trigyn.jws.dynarest.service.JwsDynamicRestDetailService;
// import com.trigyn.jws.dynarest.vo.RestApiDetails;
import com.trigyn.jws.usermanagement.repository.AuthorizedValidatorDAO;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationRepository;
import com.trigyn.jws.usermanagement.utils.Constants;

@Component
public class DynamicRestEntityValidator implements EntityValidator {

	private final static Logger					logger							= LogManager.getLogger(DynamicRestEntityValidator.class);

	@Autowired
	private AuthorizedValidatorDAO				authorizedValidatorDAO			= null;

	@Autowired
	private JwsEntityRoleAssociationRepository	entityRoleAssociationRepository	= null;

	// @Autowired
	// private JwsDynamicRestDetailService jwsService = null;

	@Override
	public boolean hasAccessToEntity(HttpServletRequest reqObject, List<String> roleNames, ProceedingJoinPoint a_joinPoint) {
		logger.debug(
				"Inside DynamicRestEntityValidator.hasAccessToEntity(requestURI - {}, reqObject - {}, roleNames - {}, a_joinPoint - {})",
				reqObject.getRequestURI(), reqObject, roleNames, a_joinPoint);

		boolean	hasAccess	= false;

		String	requestUri	= reqObject.getRequestURI().substring(reqObject.getContextPath().length());
		if (requestUri.startsWith("/japi/")) {
			requestUri = requestUri.replaceFirst("/japi/", "");
		} else {
			requestUri = requestUri.replaceFirst("/api/", "");
		}
		
		String	requestType	= reqObject.getMethod();
		// RestApiDetails restApiDetails = jwsService.getRestApiDetails(requestUri);
		// Long count =
		// authorizedValidatorDAO.hasAccessToDynamicRest(restApiDetails.getDynamicId(),
		// roleNames);
		Long	count		= authorizedValidatorDAO.hasAccessToDynamicRest(requestUri, requestType, roleNames);
		if (count > 0) {
			hasAccess = true;
		}
		return hasAccess;
	}

	@Override
	public String getEntityName(HttpServletRequest reqObject, List<String> roleNameList, ProceedingJoinPoint a_joinPoint) {
		logger.debug(
				"Inside DynamicRestEntityValidator.getEntityName(requestURI - {}, reqObject - {}, roleNameList - {}, a_joinPoint - {})",
				reqObject.getRequestURI(), reqObject, roleNameList, a_joinPoint);

		String requestUri = reqObject.getRequestURI().substring(reqObject.getContextPath().length());
		if (requestUri.startsWith("/japi/")) {
			requestUri = requestUri.replaceFirst("/japi/", "");
		} else {
			requestUri = requestUri.replaceFirst("/api/", "");
		}
		return entityRoleAssociationRepository.getEntityNameByEntityAndRoleId("REST API", requestUri);
	}

}
