package com.trigyn.jws.usermanagement.security.config;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// import com.trigyn.jws.dynarest.service.JwsDynamicRestDetailService;
// import com.trigyn.jws.dynarest.vo.RestApiDetails;
import com.trigyn.jws.usermanagement.repository.AuthorizedValidatorDAO;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationDAO;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationRepository;
import com.trigyn.jws.webstarter.utils.JQuiverProperties;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class DynamicRestEntityValidator implements EntityValidator {
	private final static Logger					logger							= LoggerFactory.getLogger(DynamicRestEntityValidator.class);
	
	@Autowired
	private AuthorizedValidatorDAO				authorizedValidatorDAO			= null;

	@Autowired
	private JwsEntityRoleAssociationRepository	entityRoleAssociationRepository	= null;

	@Autowired
	private JQuiverProperties 			jQuiverPropeties 			= null;
	
	// @Autowired
	// private JwsDynamicRestDetailService jwsService = null;
	
	@Autowired
	private JwsEntityRoleAssociationDAO entityRoleAssociationDAO = null;

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
			requestUri = requestUri.replaceFirst(jQuiverPropeties.getApiPath()+"/", "");
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
			requestUri = requestUri.replaceFirst(jQuiverPropeties.getApiPath()+"/", "");
		}
		return entityRoleAssociationDAO.getEntityNameByEntityAndRoleId("REST API", requestUri);
	}

}
