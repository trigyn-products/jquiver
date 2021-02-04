package com.trigyn.jws.usermanagement.security.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.exception.InvalidLoginException;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.service.UserConfigService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.JwsRoleVO;

public class DefaultUserDetailsServiceImpl implements UserDetailsService {

	private final static Logger					logger							= LogManager
			.getLogger(DefaultUserDetailsServiceImpl.class);

	private JwsUserRepository					userRepository					= null;

	private JwsUserRoleAssociationRepository	userRoleAssociationRepository	= null;

	private UserConfigService					userConfigService				= null;

	public DefaultUserDetailsServiceImpl(JwsUserRepository userRepository,
			JwsUserRoleAssociationRepository userRoleAssociationRepository, UserConfigService userConfigService) {
		this.userRepository					= userRepository;
		this.userRoleAssociationRepository	= userRoleAssociationRepository;
		this.userConfigService				= userConfigService;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		ServletRequestAttributes	sra			= (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest			request		= sra.getRequest();
		HttpSession					session		= request.getSession();
		Map<String, Object>			mapDetails	= new HashMap<>();
		try {
			userConfigService.getConfigurableDetails(mapDetails);
		} catch (Exception a_excep) {
			logger.error("Error ocurred.", a_excep);
		}

		JwsUser user = userRepository.findByEmailIgnoreCase(email);
		if (user == null) {
			throw new UsernameNotFoundException("Not found!");
		}

		if (mapDetails.get("enableCaptcha").toString().equalsIgnoreCase("true")
				&& session.getAttribute("loginCaptcha") != null
				&& !(request.getParameter("captcha").equals(session.getAttribute("loginCaptcha").toString()))) {

			throw new InvalidLoginException("Please verify captcha!");
		}
		if (mapDetails.get("enableGoogleAuthenticator").toString().equalsIgnoreCase("true")) {

			user.setPassword(
					new BCryptPasswordEncoder().encode(new TwoFactorGoogleUtil().getTOTPCode(user.getSecretKey())));
		}

		List<JwsRoleVO> rolesVOs = userRoleAssociationRepository.getUserRoles(Constants.ISACTIVE, user.getUserId());
		return new UserInformation(user, rolesVOs);
	}

}
