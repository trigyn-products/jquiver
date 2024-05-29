package com.trigyn.jws.usermanagement.security.config.oauth;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.exception.OAuth2AuthenticationProcessingException;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.repository.UserManagementDAO;
import com.trigyn.jws.usermanagement.security.config.UserInformation;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.JwsRoleVO;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final static Logger					logger							= LogManager.getLogger(CustomOAuth2UserService.class);

	@Autowired
	private JwsUserRepository					jwsUserRepository				= null;

	@Autowired
	private UserManagementDAO					userManagementDAO				= null;

	@Autowired
	private JwsUserRoleAssociationRepository	userRoleAssociationRepository	= null;
	
	@Autowired
	private PasswordEncoder						passwordEncoder					= null;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

		OAuth2User oAuth2User = super.loadUser(userRequest);

		try {
			return processOAuth2User(userRequest, oAuth2User);
		} catch (AuthenticationException a_excep) {
			logger.error("Error ocurred.", a_excep);
			throw a_excep;
		} catch (Exception a_excep) {
			logger.error("Error ocurred.", a_excep);
			// Throwing an instance of AuthenticationException will trigger the
			// OAuth2AuthenticationFailureHandler
			throw new InternalAuthenticationServiceException(a_excep.getMessage(), a_excep.getCause());
		}
	}

	private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

		OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(userRequest.getClientRegistration().getRegistrationId(),
				oAuth2User.getAttributes());

		if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
			throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
		}

		JwsUser user = jwsUserRepository.findByEmailIgnoreCase(oAuth2UserInfo.getEmail());
		if (user != null) {
			if (user.getIsActive() == Constants.INACTIVE) {
				throw new OAuth2AuthenticationProcessingException("Access Denied Please contact admin");
			}
			user = updateExistingUser(user, oAuth2UserInfo);
		} else {
			user = registerNewUser(userRequest, oAuth2UserInfo);
		}

		List<JwsRoleVO> rolesVOs = userRoleAssociationRepository.getUserRoles(Constants.ISACTIVE, user.getUserId());
		return new UserInformation().create(user, oAuth2User.getAttributes(), rolesVOs);
	}

	private JwsUser registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
		JwsUser user = new JwsUser();

		// user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
		// user.setProviderId(oAuth2UserInfo.getId());
		user.setFirstName(oAuth2UserInfo.getName() != null ? oAuth2UserInfo.getName().split(" ")[0] : oAuth2UserInfo.getEmail());
		user.setLastName(oAuth2UserInfo.getName() != null ? oAuth2UserInfo.getName().split(" ")[1] : oAuth2UserInfo.getEmail());
		user.setEmail(oAuth2UserInfo.getEmail());
		user.setIsActive(Constants.ISACTIVE);
		user.setPassword(passwordEncoder.encode(oAuth2UserInfo.getEmail()));
		user.setForcePasswordChange(Constants.INACTIVE);
		// user.setImageUrl(oAuth2UserInfo.getImageUrl());

		userManagementDAO.saveUserData(user);
		userManagementDAO.saveAuthenticatedRole(user.getUserId());

		return user;
	}

	private JwsUser updateExistingUser(JwsUser existingUser, OAuth2UserInfo oAuth2UserInfo) {
		existingUser.setFirstName(oAuth2UserInfo.getName() != null ? oAuth2UserInfo.getName().split(" ")[0] : oAuth2UserInfo.getEmail());
		existingUser.setLastName(oAuth2UserInfo.getName() != null ? oAuth2UserInfo.getName().split(" ")[1] : oAuth2UserInfo.getEmail());
		// existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
		return userManagementDAO.saveUserData(existingUser);
	}

}
