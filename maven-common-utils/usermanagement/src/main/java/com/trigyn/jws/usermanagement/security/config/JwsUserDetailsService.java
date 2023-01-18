package com.trigyn.jws.usermanagement.security.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;

@Component
public class JwsUserDetailsService implements IUserDetailsService {

	private UserDetailsVO detailsVO = null;

	@Override
	public UserDetailsVO getUserDetails() {
 		HttpServletRequest	requestObject	= getRequest();
		Authentication		authentication	= null;
		if(requestObject != null && requestObject.getSession().getAttribute("SPRING_SECURITY_CONTEXT") != null) {
			authentication = ((SecurityContextImpl) requestObject.getSession().getAttribute("SPRING_SECURITY_CONTEXT")).getAuthentication();
		}
		if(authentication == null) {
			authentication = SecurityContextHolder.getContext().getAuthentication();
		}
 		
		if (authentication != null && Boolean.FALSE.equals(authentication instanceof AnonymousAuthenticationToken)) {
			detailsVO = new UserDetailsVO();
 			UserInformation userInformation = (UserInformation) authentication.getPrincipal();
			detailsVO.setUserName(userInformation.getUsername());
			detailsVO.setUserId(userInformation.getUserId());
			detailsVO.setFullName(userInformation.getFullName());
			List<String> roleList = new ArrayList<String>();
			roleList.addAll(AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
			detailsVO.setRoleIdList(roleList);
		} else {
			detailsVO = new UserDetailsVO("anonymous-user", "anonymous", Arrays.asList("anonymous"), "anonymous-user");
		}

		return detailsVO;
	}

	@Override
	public List<String> getUsersRoleHavingAccessToJWS() {
		return detailsVO.getRoleIdList();
	}

	private HttpServletRequest getRequest() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if(sra != null) {
			return sra.getRequest();
		}
		return null;
	}

}
