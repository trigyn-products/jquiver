package com.trigyn.jws.usermanagement.security.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.utils.LdapConfigHelper;
import com.trigyn.jws.usermanagement.vo.JwsRoleVO;
import com.trigyn.jws.usermanagement.vo.MultiAuthSecurityDetailsVO;

/**
 * 
 * 
 * @author Shrinath.Halki
 * @since  26-OCT-2022
 *
 */
@Service
public class LdapConfigService extends LdapConfigHelper {

	private final Log							logger							= LogFactory.getLog(getClass());

	@Autowired
	private final LdapUserAuthoritiesPopulator	ldapUserAuthoritiesPopulator	= null;

	@Autowired
	private ApplicationSecurityDetails			applicationSecurityDetails		= null;

	@Autowired
	private JwsUserRepository					jwsUserRepository				= null;

	@Autowired
	private JwsUserRoleAssociationRepository	userRoleAssociationRepository	= null;

	@SuppressWarnings("unchecked")
	public LdapAuthenticator getLdapAuthenticator(String ldapServerDisplayId) throws Exception {
		Map<String, Object>	authenticationDetails	= applicationSecurityDetails.getAuthenticationDetails();
		BindAuthenticator	authenticator			= null;
		if (authenticationDetails != null) {
			List<MultiAuthSecurityDetailsVO> multiAuthSecurityDetails = (List<MultiAuthSecurityDetailsVO>) authenticationDetails
					.get("authenticationDetails");
			if (multiAuthSecurityDetails != null && multiAuthSecurityDetails.isEmpty() == false) {
				for (MultiAuthSecurityDetailsVO multiAuthLogin : multiAuthSecurityDetails) {
					Integer authType = multiAuthLogin.getAuthenticationTypeVO().getId();
					if (authenticationDetails != null) {
						if (Constants.AuthType.LDAP.getAuthType() == authType) {
							authenticator = new BindAuthenticator(
									getLdapContextSource(multiAuthLogin, ldapServerDisplayId));
							authenticator.setUserSearch(buildUserSearchFilter(multiAuthLogin, ldapServerDisplayId));
							authenticator.afterPropertiesSet();
						}
					}
				}
			}
		}

		return authenticator;
	}

	public UserDetailsContextMapper getUserDetailsContextMapper() {
		return new LdapUserDetailsMapper() {
			@Override
			public UserInformation mapUserFromContext(DirContextOperations ctx, String username,
					Collection<? extends GrantedAuthority> authorities) {
				JwsUser				user		= jwsUserRepository.findByEmailIgnoreCase(username);
				List<JwsRoleVO>		rolesVOs	= userRoleAssociationRepository.getUserRoles(Constants.ISACTIVE,
						user.getUserId());
				Map<String, Object>	attributes	= new HashMap<String, Object>();
				return new UserInformation().create(user, attributes, rolesVOs);
			}
		};
	}

	@SuppressWarnings("unchecked")
	public LdapAuthoritiesPopulator getLdapAuthoritiesPopulator() throws Exception {
		Map<String, Object> authenticationDetails = applicationSecurityDetails.getAuthenticationDetails();
		if (authenticationDetails != null) {
			List<MultiAuthSecurityDetailsVO> multiAuthSecurityDetails = (List<MultiAuthSecurityDetailsVO>) authenticationDetails
					.get("authenticationDetails");
			if (multiAuthSecurityDetails != null && multiAuthSecurityDetails.isEmpty() == false) {
				for (MultiAuthSecurityDetailsVO multiAuthLogin : multiAuthSecurityDetails) {
					Integer authType = multiAuthLogin.getAuthenticationTypeVO().getId();
					if (authenticationDetails != null) {
						if (Constants.AuthType.LDAP.getAuthType() == authType) {
							return this.ldapUserAuthoritiesPopulator;
						}
					}
				}
			}
		}
		return null;

	}

	public Boolean checkLdapConnection(MultiValueMap<String, Object> formLdapData) {

		boolean isConnected = true;
		try {

			String	loginAttribute		= (String) formLdapData.getFirst("loginAttribute");
			String	baseDN				= (String) formLdapData.getFirst("basedn");
			String	binUserDN			= (String) formLdapData.getFirst("userdn");
			String	adminUserName		= (String) formLdapData.getFirst("adminUserName");
			String	adminPassword		= (String) formLdapData.getFirst("adminPassword");
			String	securityPrincipal	= (String) formLdapData.getFirst("securityPrincipal");
			if (loginAttribute.equalsIgnoreCase("mail")) {
				securityPrincipal = adminUserName;
			} else {
				securityPrincipal = loginAttribute + "=" + adminUserName + "," + binUserDN + "," + baseDN;
			}
			LdapContextSource	sourceLdapCtx		= getLdapContextSource(formLdapData);
			LdapTemplate		sourceLdapTemplate	= new LdapTemplate(sourceLdapCtx);
			sourceLdapTemplate.getContextSource().getContext(securityPrincipal, adminPassword);

		} catch (Exception exec) {
			isConnected = false;
		}
		return isConnected;
	}

	public LdapTemplate getLdapTemplate(MultiAuthSecurityDetailsVO ldapAuthSecurityDetails,
			String ldapServerDisplayId) {
		LdapTemplate ldapTemplate = ldapTemplate(ldapAuthSecurityDetails, ldapServerDisplayId);
		return ldapTemplate;
	}

}
