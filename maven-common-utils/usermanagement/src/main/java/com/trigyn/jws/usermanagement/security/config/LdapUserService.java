package com.trigyn.jws.usermanagement.security.config;

import java.util.List;
import java.util.Map;

import javax.naming.directory.Attributes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.LdapUtils;
import org.springframework.stereotype.Service;

import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.repository.UserManagementDAO;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.JwsUserVO;
import com.trigyn.jws.usermanagement.vo.MultiAuthSecurityDetailsVO;

/**
 * 
 * This service is intended for User information related LDAP.
 * 
 * @author Shrinath.Halki
 * @since  23-DEC-2022
 *
 */

@Service
public class LdapUserService {

	private final Log					logger						= LogFactory.getLog(getClass());

	private LdapTemplate				ldapTemplate				= new LdapTemplate();
	
	@Autowired
	private UserManagementDAO			userManagementDAO			= null;

	@Autowired
	private PasswordEncoder				passwordEncoder				= null;

	@Autowired
	private ApplicationSecurityDetails	applicationSecurityDetails	= null;

	@Autowired
	private LdapConfigService			ldapConfigService			= null;
	

	/**
	 * Finding for user information, using userName
	 * @param ldapServerDisplayId
	 * @param userName
	 * @return JwsUserVO
	 */
	@SuppressWarnings("unchecked")
	public JwsUserVO findUserInfoFromLdap(String ldapServerDisplayId, String userName) {
		Map<String, Object> authenticationDetails = applicationSecurityDetails.getAuthenticationDetails();
		if (authenticationDetails != null) {
			List<MultiAuthSecurityDetailsVO> multiAuthSecurityDetails = (List<MultiAuthSecurityDetailsVO>) authenticationDetails
					.get("authenticationDetails");
			if (multiAuthSecurityDetails != null && multiAuthSecurityDetails.isEmpty() == false) {
				for (MultiAuthSecurityDetailsVO multiAuthLogin : multiAuthSecurityDetails) {
					Integer authTypeId = multiAuthLogin.getAuthenticationTypeVO().getId();
					if (Constants.AuthType.LDAP.getAuthType() == authTypeId) {
						ldapTemplate = ldapConfigService.getLdapTemplate(multiAuthLogin, ldapServerDisplayId);
						List<JwsUserVO>	person	= findPerson(userName, ldapTemplate);
						logger.info("person: " + person.toString());
						return person.get(0);
					}
				}
			}
		}
		return null;
	}

	/**
	 * Searching for one user, using {@link PersonAttributesMapper}
	 * 
	 * @param  dn distinguaged name for the person
	 * @return    person
	 */
	public List<JwsUserVO> findPerson(String userName, LdapTemplate ldapTemplate) {
		AndFilter andFilter = new AndFilter();
		andFilter.and(new EqualsFilter("objectclass", "person"));
		andFilter.and(new EqualsFilter("mail", userName));
		//andFilter.and(new EqualsFilter("memberof", "CN=TRIGYN BLR,CN=Users,DC=trigyn,DC=com"));
		//return ldapTemplate.search("", andFilter.encode(), new PersonAttributesMapper();});
		return ldapTemplate.search("", andFilter.encode(), new PersonAttributesMapper());
		//return ldapTemplate.lookup(dn, new PersonAttributesMapper());
	}

	/**
	 * Custom person attributes mapper, maps the attributes to the person POJO
	 */
	private class PersonAttributesMapper implements AttributesMapper<JwsUserVO> {
		public JwsUserVO mapFromAttributes(Attributes attrs) throws NamingException {
			JwsUserVO ldapUserInfo = new JwsUserVO();
			try {
				ldapUserInfo.setFirstName((String) attrs.get("givenname").get());
				ldapUserInfo.setLastName((String) attrs.get("sn").get());
				ldapUserInfo.setEmail((String) attrs.get("mail").get());
			} catch (javax.naming.NamingException e) {
				logger.error("Error : " + e.getMessage());
			}
			return ldapUserInfo;
		}
	}

	/**
	 * @param userVo
	 * @return
	 */
	public JwsUser createUserFromLdap(JwsUserVO userVo) {
		JwsUser user = new JwsUser();

		user.setFirstName(userVo.getFirstName());
		user.setLastName(userVo.getLastName());
		user.setEmail(userVo.getEmail());
		user.setIsActive(Constants.ISACTIVE);
		user.setPassword(passwordEncoder.encode(userVo.getEmail()));
		user.setForcePasswordChange(Constants.ISACTIVE);
		user.setRegisteredBy(3);
		userManagementDAO.saveUserData(user);
		userManagementDAO.saveAuthenticatedRole(user.getUserId());
		return user;
	}

}
