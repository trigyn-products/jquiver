package com.trigyn.jws.usermanagement.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.DefaultDirObjectFactory;
import org.springframework.ldap.core.support.DefaultTlsDirContextAuthenticationStrategy;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.core.support.SimpleDirContextAuthenticationStrategy;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.InetOrgPersonContextMapper;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.trigyn.jws.usermanagement.vo.AuthenticationDetails;
import com.trigyn.jws.usermanagement.vo.JwsAuthConfiguration;
import com.trigyn.jws.usermanagement.vo.MultiAuthSecurityDetailsVO;

public class LdapConfigHelper {

	private final static Logger logger = LogManager.getLogger(LdapConfigHelper.class);

	/**
	 * @param  ldapAuthSecurityDetails is the security details object
	 * @param  ldapServerDisplayId     is the ldap server display id
	 * @return                         LdapContextSource or null
	 */
	public LdapContextSource getLdapContextSource(final MultiAuthSecurityDetailsVO ldapAuthSecurityDetails,
			String ldapServerDisplayId) {

		AuthenticationDetails authenticationDetail = ldapAuthSecurityDetails.getConnectionDetailsVO()
				.getAuthenticationDetails();

		for (List<JwsAuthConfiguration> configurationDetails : authenticationDetail.getConfigurations()) {

			LdapContextSource		ldapContextSource	= new LdapContextSource();
			JwsAuthConfiguration	ldapDisplayName		= configurationDetails.stream()
					.filter(config -> config.getName() != null).filter(config -> config.getName().equals("displayName"))
					.findAny().orElse(null);

			if (StringUtils.isNotEmpty(ldapDisplayName.getValue())
					&& ldapDisplayName.getValue().equals(ldapServerDisplayId)) {
				MultiValueMap<String, Object> formLdapData = getLdapContextParameters(configurationDetails);
				ldapContextSource = getLdapContextSource(formLdapData);
			}
			return ldapContextSource;
		}
		return null;

	}

	public FilterBasedLdapUserSearch buildUserSearchFilter(MultiAuthSecurityDetailsVO multiAuthLogin,
			String ldapServerDisplayId) {
		AuthenticationDetails ldapConfigurations = multiAuthLogin.getConnectionDetailsVO().getAuthenticationDetails();
		for (List<JwsAuthConfiguration> configurationDetails : ldapConfigurations.getConfigurations()) {
			JwsAuthConfiguration ldapDisplayName = configurationDetails.stream()
					.filter(config -> config.getName() != null).filter(config -> config.getName().equals("displayName"))
					.findAny().orElse(null);
			if (ldapDisplayName != null && StringUtils.isNotEmpty(ldapDisplayName.getValue())
					&& ldapDisplayName.getValue().equals(ldapServerDisplayId)) {

				FilterBasedLdapUserSearch	userSearch		= new FilterBasedLdapUserSearch("",
						"(&(|(samAccountName={0})(userPrincipalName={0})(cn={0}))(objectClass=user))",
						getLdapContextSource(multiAuthLogin, ldapServerDisplayId));
				JwsAuthConfiguration		ldapSearchScope	= configurationDetails.stream()
						.filter(config -> config.getName() != null)
						.filter(config -> config.getName().equals("ldapSearchScope")).findAny().orElse(null);
				if (ldapSearchScope != null && ldapSearchScope.getValue().equals("2")) {
					userSearch.setSearchSubtree(true);
				} else {
					userSearch.setSearchSubtree(false);
				}
				return userSearch;
			}
		}
		return null;
	}

	public MultiValueMap<String, Object> getLdapContextParameters(List<JwsAuthConfiguration> configurationDetails) {
		MultiValueMap<String, Object> formLdapData = new LinkedMultiValueMap<>();
		for (JwsAuthConfiguration jwsAuthConfiguration : configurationDetails) {
			formLdapData.add(jwsAuthConfiguration.getName(), jwsAuthConfiguration);
		}
		return formLdapData;
	}

	/**
	 * @param  formLdapData hold the ldap details data
	 * @return              LdapContextSource or null
	 */
	public LdapContextSource getLdapContextSource(MultiValueMap<String, Object> formLdapData) {
		LdapContextSource	sourceLdapCtx		= new LdapContextSource();
		Map<String, Object>	baseEnvironment		= new HashMap<>();
		String				sourceHost			= "";
		String				sourcePort			= "";
		String				baseDN				= "";
		String				binUserDN			= "";
		String				adminPassword		= "";
		String				adminUserName		= "";
		String				ldapSecurityType	= "";
		String				loginAttribute		= "";
		String				securityPrincipal	= "";
		try {
			for (Entry<String, List<Object>> entry : formLdapData.entrySet()) {
				String			key	= entry.getKey();

				List<Object>	val	= entry.getValue();
				for (Object object : val) {
					String condition = ((JwsAuthConfiguration) object).getCondition();
					if (condition != null && condition.equalsIgnoreCase("row-added")) {
						String addParams = ((JwsAuthConfiguration) object).getValue();
						baseEnvironment.put(key, addParams);
					} else if(key != null){
						if (key.equalsIgnoreCase("ldapAddress")) {
							sourceHost = ((JwsAuthConfiguration) object).getValue();
						}
						if (key.equalsIgnoreCase("ldapPort")) {
							sourcePort = ((JwsAuthConfiguration) object).getValue();
						}
						if (key.equalsIgnoreCase("basedn")) {
							baseDN = ((JwsAuthConfiguration) object).getValue();
						}
						if (key.equalsIgnoreCase("userdn")) {
							binUserDN = ((JwsAuthConfiguration) object).getValue();
						}
						if (key.equalsIgnoreCase("adminPassword")) {
							adminPassword = ((JwsAuthConfiguration) object).getValue();
						}
						if (key.equalsIgnoreCase("adminUserName")) {
							adminUserName = ((JwsAuthConfiguration) object).getValue();
						}
						if (key.equalsIgnoreCase("ldapSecurityType")) {
							ldapSecurityType = ((JwsAuthConfiguration) object).getValue();
						}
						if (key.equalsIgnoreCase("loginAttribute")) {
							loginAttribute = ((JwsAuthConfiguration) object).getValue();
						}
					}

				}
			}

			logger.info("Connecting to LDAP ..." + sourceHost + ":" + sourcePort + "...");

			if (loginAttribute!=null && loginAttribute.equalsIgnoreCase("mail")) {
				securityPrincipal = adminUserName;
			} else {
				securityPrincipal = loginAttribute + "=" + adminUserName + "," + binUserDN + "," + baseDN;
			}
			sourceLdapCtx.setUserDn(securityPrincipal);
			// sourceLdapCtx.setBase(baseDN);
			sourceLdapCtx.setPassword(adminPassword);
			sourceLdapCtx.setDirObjectFactory(DefaultDirObjectFactory.class);
			String url = "";
			switch (ldapSecurityType) {
				case "0":
					sourceLdapCtx.setAuthenticationStrategy(new SimpleDirContextAuthenticationStrategy());
					url = "ldap://" + sourceHost + ":" + sourcePort + "/";
					break;
				case "1":
					sourceLdapCtx.setAuthenticationStrategy(new SimpleDirContextAuthenticationStrategy());
					url = "ldaps://" + sourceHost + ":" + sourcePort + "/";
					sourceLdapCtx = new SSLLdapContextSource();
					// indicate a secure connection
					break;
				case "2":
					url = "ldaps://" + sourceHost + ":" + sourcePort + "/";
					// shutdown gracefully
					final DefaultTlsDirContextAuthenticationStrategy authenticationStrategy = new DefaultTlsDirContextAuthenticationStrategy();
					sourceLdapCtx = new SSLLdapContextSource();
					authenticationStrategy.setShutdownTlsGracefully(true);
					sourceLdapCtx.setAuthenticationStrategy(authenticationStrategy);
					break;
			}
			sourceLdapCtx.setUrl(url);
			sourceLdapCtx.setBase(baseDN);
			sourceLdapCtx.setUserDn(securityPrincipal);
			sourceLdapCtx.setDirObjectFactory(DefaultDirObjectFactory.class);
			sourceLdapCtx.setPassword(adminPassword);
			sourceLdapCtx.setAnonymousReadOnly(false);
			sourceLdapCtx.setPooled(false);
			sourceLdapCtx.setBaseEnvironmentProperties(baseEnvironment);
			sourceLdapCtx.afterPropertiesSet();
		} catch (Exception exec) {
			logger.error("Failed : Connecting to LDAP " + sourceHost + ":" + sourcePort + "...");
		}
		return sourceLdapCtx;
	}

	public LdapTemplate ldapTemplate(MultiAuthSecurityDetailsVO ldapAuthSecurityDetails, String ldapServerDisplayId) {
		LdapContextSource	sourceLdapCtx	= getLdapContextSource(ldapAuthSecurityDetails, ldapServerDisplayId);
		LdapTemplate		ldapTemplate	= new LdapTemplate(sourceLdapCtx);
		ldapTemplate.setIgnorePartialResultException(true);
		return ldapTemplate;
	}

	public InetOrgPersonContextMapper inetOrgPersonContextMapper() {
		InetOrgPersonContextMapper contextMapper = new InetOrgPersonContextMapper();
		return contextMapper;
	}

}
