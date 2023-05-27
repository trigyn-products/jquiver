package com.trigyn.jws.usermanagement.security.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.stereotype.Service;

import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.service.UserConfigService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.JwsUserLoginVO;
import com.trigyn.jws.usermanagement.vo.JwsUserVO;

/**
 * 
 * @author Shrinath.Halki
 * @since  26-OCT-2022
 *
 */

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private final Log								logger						= LogFactory.getLog(getClass());

	@Autowired(required = false)
	private HttpServletRequest						request						= null;

	private JwsUserRepository						userRepository				= null;

	private Map<String, LdapAuthenticationProvider>	ldapAuthenticationProviders	= new HashMap<>();

	private DaoAuthenticationProvider				daoAuthenticationProvider	= null;

	@Autowired
	private UserDetailsService						userDetailsService			= null;

	@Autowired
	private LdapConfigService						ldapConfigService			= null;

	@Autowired
	private PasswordEncoder							passwordEncoder				= null;

	@Autowired
	private UserConfigService						userConfigService			= null;

	@Autowired
	LdapUserService									ldapUserService				= null;

	public CustomAuthenticationProvider(JwsUserRepository userRepository,
			JwsUserRoleAssociationRepository userRoleAssociationRepository, UserConfigService userConfigService) {
		this.userRepository = userRepository;
	}

	@PostConstruct
	public void init() throws Exception {
		Map<String, Object> mapDetails = new HashMap<>();
		userConfigService.getConfigurableDetails(mapDetails);

		@SuppressWarnings("unchecked")
		List<JwsUserLoginVO> activeAutenticationDetails = (List<JwsUserLoginVO>) mapDetails
				.get("activeAutenticationDetails");
		for (JwsUserLoginVO jwsUserLoginVO : activeAutenticationDetails) {
			if (Constants.AuthType.DAO.getAuthType() == jwsUserLoginVO.getAuthenticationType()) {
				this.daoAuthenticationProvider = new DaoAuthenticationProvider();
				this.daoAuthenticationProvider.setUserDetailsService(userDetailsService);
				this.daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
			}
			if (Constants.AuthType.LDAP.getAuthType() == jwsUserLoginVO.getAuthenticationType()) {
				Map<String, Object> loginAttributes = jwsUserLoginVO.getLoginAttributes();
				for (Map.Entry<String, Object> entry : loginAttributes.entrySet()) {
					String displayKey = entry.getKey();
					if (displayKey.equalsIgnoreCase("ldapDisplayDetails")) {
						List<String> ldapDisplayDetails = (List<String>) entry.getValue();
						if (ldapDisplayDetails != null) {
							for (String ldapDisplayVal : ldapDisplayDetails) {
								LdapAuthenticationProvider ldapAuthenticationProvider = new LdapAuthenticationProvider(
										ldapConfigService.getLdapAuthenticator(ldapDisplayVal),
										ldapConfigService.getLdapAuthoritiesPopulator());
								ldapAuthenticationProvider.setUseAuthenticationRequestCredentials(true);
								ldapAuthenticationProvider
										.setUserDetailsContextMapper(ldapConfigService.getUserDetailsContextMapper());
								this.ldapAuthenticationProviders.put(ldapDisplayVal, ldapAuthenticationProvider);
							}
						}
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.authentication.AuthenticationProvider#
	 * authenticate(org.springframework.security.core.Authentication)
	 */
	@Override
	public Authentication authenticate(Authentication authentication) {

		Authentication authResponse = null;
		try {
			String	username	= authentication.getName();
			JwsUser	userEntity	= userRepository.findByEmailIgnoreCase(username);
			String	authType	= request.getParameter("enableAuthenticationType");
			String authTypeHeader = null;
			if (authType == null || authType.isEmpty()|| authType.isBlank()) {
				authTypeHeader = request.getHeader("at");
				if (authTypeHeader.equals(Constants.AuthTypeHeaderKey.DAO.getAuthTypeHeaderKey())) {
					authType = Constants.DAO_ID;
				} else if (authTypeHeader.equals(Constants.AuthTypeHeaderKey.LDAP.getAuthTypeHeaderKey())) {
					authType = Constants.LDAP_ID;
				} else if (authTypeHeader.equals(Constants.AuthTypeHeaderKey.OAUTH.getAuthTypeHeaderKey())) {
					authType = Constants.OAUTH_ID;
				}
			}
			if(authType == null && authTypeHeader==null) {
				throw new IllegalArgumentException("Authentication is required.");
			}
			if (userEntity == null && authType.equals(Constants.DAO_ID)) {
				throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
			} else if (authType.equals(Constants.LDAP_ID) && userEntity == null) {
				String		ldapServerDisplayId	= request.getParameter("ldapConfig");
				JwsUserVO	jwsUserVO			= ldapUserService.findUserInfoFromLdap(ldapServerDisplayId, username);
				if (jwsUserVO == null) {
					throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
				}
				JwsUser jwsUser = ldapUserService.createUserFromLdap(jwsUserVO);
				if (jwsUser == null) {
					throw new UsernameNotFoundException(
							String.format("Failed : Error while creating the user '%s'.", username));
				}
			}
			switch (authType) {
				case Constants.DAO_ID:
					return this.daoAuthenticationProvider.authenticate(authentication);
				case Constants.LDAP_ID:
					String ldapConfigType = request.getParameter("ldapConfig");
					if (StringUtils.isEmpty(ldapConfigType)) {
						throw new IllegalArgumentException(authType + " is not yet implemented!");
					}
					return this.ldapAuthenticationProviders.get(ldapConfigType).authenticate(authentication);
				default:
					throw new IllegalArgumentException(authType + " is not yet implemented!");
			}
		} catch (Exception exec) {
			logger.error("Failed : Error while authenticating " + exec.getMessage());
		}
		return authResponse;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.security.authentication.AuthenticationProvider#supports(
	 * java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
