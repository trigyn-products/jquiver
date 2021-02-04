package com.trigyn.jws.usermanagement.security.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// import com.trigyn.jws.dynarest.service.JwsDynamicRestDetailService;
// import com.trigyn.jws.dynarest.vo.RestApiDetails;
import com.trigyn.jws.usermanagement.repository.AuthorizedValidatorDAO;

@Component
public class DynamicRestEntityValidator implements EntityValidator {

	@Autowired
	private AuthorizedValidatorDAO authorizedValidatorDAO = null;

	// @Autowired
	// private JwsDynamicRestDetailService jwsService = null;

	@Override
	public boolean hasAccessToEntity(HttpServletRequest reqObject, List<String> roleNames,
			ProceedingJoinPoint a_joinPoint) {
		boolean	hasAccess	= true;

		String	requestUri	= reqObject.getRequestURI();
		requestUri = requestUri.replaceFirst("/api/", "");
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

}
