package com.trigyn.jws.usermanagement.security.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;

@Component
public class JwsUserDetailsService implements IUserDetailsService {

	private UserDetailsVO detailsVO = null;

	@Override
	public UserDetailsVO getUserDetails() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (Boolean.FALSE.equals(authentication instanceof AnonymousAuthenticationToken)) {
			detailsVO = new UserDetailsVO();
			UserInformation userInformation = (UserInformation) authentication.getPrincipal();
			detailsVO.setUserName(userInformation.getUsername());
			detailsVO.setUserId(userInformation.getUserId());
			List<String> roleList = new ArrayList<String>();
			roleList.addAll(AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
			detailsVO.setRoleIdList(roleList);
		}else {
			detailsVO = new UserDetailsVO("anonymous-user", "anonymous", Arrays.asList("anonymous"));
		}

		return detailsVO;
	}

	@Override
	public List<String> getUsersRoleHavingAccessToJWS() {
		return detailsVO.getRoleIdList();
	}

}
