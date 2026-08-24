package com.trigyn.jws.usermanagement.security.config.oauth;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.AcceptPendingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;
import org.springframework.security.saml2.provider.service.web.DefaultRelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.RelyingPartyRegistrationResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.repository.UserManagementDAO;
import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;
import com.trigyn.jws.usermanagement.security.config.UserInformation;
import com.trigyn.jws.usermanagement.service.UserConfigService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.JwsRoleVO;

@Service
public class CustomSAMLUserService {

	private final static Logger logger = LoggerFactory.getLogger(CustomSAMLUserService.class);

	@Autowired
	private JwsUserRepository jwsUserRepository = null;

	@Autowired
	private UserManagementDAO userManagementDAO = null;

	@Autowired
	private JwsUserRoleAssociationRepository userRoleAssociationRepository = null;

	@Autowired
	private PasswordEncoder passwordEncoder = null;
	
	@Autowired
	private ApplicationSecurityDetails applicationSecurityDetails = null;
	
	@Autowired
	private UserConfigService userConfigService = null;

	public UserInformation samlUser(Map<String, Object> attributeDetails) {

		if (StringUtils.isEmpty(attributeDetails.get("userName").toString())) {
			throw new UsernameNotFoundException("Email not found from SAML provider");
		}

		JwsUser user = jwsUserRepository.findByEmailIgnoreCase(attributeDetails.get("userName").toString());
		if (user != null) {
			if (user.getIsActive() == Constants.INACTIVE) {
				throw new AcceptPendingException();
			}
			user = updateExistingUser(user, attributeDetails);
		} else {
			user = registerNewUser(attributeDetails);
		}

		List<JwsRoleVO> rolesVOs = userRoleAssociationRepository.getUserRoles(Constants.ISACTIVE, user.getUserId());
		return new UserInformation().create(user, attributeDetails, rolesVOs);
	}

	private JwsUser registerNewUser(Map<String, Object> attributeDetails) {
		JwsUser user = new JwsUser();
		if (null == attributeDetails.get("firstName") || null == attributeDetails.get("lastName")
				|| null == attributeDetails.get("email")) {
			logger.error("Please Contact the administrator for correct configurations");
		} else {
			user.setFirstName(attributeDetails.get("firstName").toString());
			user.setLastName(attributeDetails.get("lastName").toString());
			user.setEmail(attributeDetails.get("email").toString());
			user.setIsActive(Constants.ISACTIVE);
			user.setPassword(passwordEncoder.encode(attributeDetails.get("email").toString()));
			user.setForcePasswordChange(Constants.INACTIVE);
			userManagementDAO.saveUserData(user);
			userManagementDAO.saveAuthenticatedRole(user.getUserId());
		}
		return user;
	}

	private JwsUser updateExistingUser(JwsUser existingUser, Map<String, Object> attributeDetails) {
		existingUser.setFirstName(attributeDetails.get("firstName").toString());
		existingUser.setLastName(attributeDetails.get("lastName").toString());
		return userManagementDAO.saveUserData(existingUser);
	}
	
	RelyingPartyRegistrationResolver relyingPartyRegistrationResolver(
	    RelyingPartyRegistrationRepository registrations) {
	    return new DefaultRelyingPartyRegistrationResolver((id) -> registrations.findByRegistrationId("core2"));
	}


	public RelyingPartyRegistrationRepository relyingPartyRegistrationRepository() throws Exception  {
		if (applicationSecurityDetails.getIsAuthenticationEnabled()) {
			Map<String, Object> mapDetails = new HashMap<>();
			userConfigService.getConfigurableDetails(mapDetails);
		    RelyingPartyRegistration core2 = RelyingPartyRegistrations
		    .fromMetadataLocation(mapDetails.get("metadataURL").toString())
		    .assertionConsumerServiceLocation(mapDetails.get("assertionConsumerServiceLocation").toString()) 
		    .registrationId(mapDetails.get("registrationId").toString())
		    .build();
		    return new InMemoryRelyingPartyRegistrationRepository(core2);
		} else {
			return null;
		}
	} 
	
	public Boolean checkSamlConnection(MultiValueMap<String, Object> formSamlData) {
		boolean isConnected = true;
		try {
			String metadataUrl = (String) formSamlData.getFirst("metadataURL");
			if (metadataUrl == null || metadataUrl.trim().isEmpty()) {
				return false;
			}
			logger.info("Checking SAML Metadata URL : " + metadataUrl);
			URL url = new URL(metadataUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			int responseCode = connection.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK) {
				logger.error("Unable to access Metadata URL. Response Code : " + responseCode);
				return false;
			}
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(connection.getInputStream());
			Element root = document.getDocumentElement();
			if (root == null) {
				logger.error("Invalid Metadata XML.");
				return false;
			}
			String rootName = root.getLocalName();
			if ("EntityDescriptor".equals(rootName) == false) {
				logger.error("Metadata does not contain EntityDescriptor.");
				return false;
			}
			NodeList idpList = document.getElementsByTagNameNS("*", "IDPSSODescriptor");
			if (idpList.getLength() == 0) {
				logger.error("IDPSSODescriptor not found.");
				return false;
			}
			NodeList ssoList = document.getElementsByTagNameNS("*", "SingleSignOnService");
			if (ssoList.getLength() == 0) {
				logger.error("SingleSignOnService not found.");
				return false;
			}
			logger.info("SAML configuration verified successfully.");
		} catch (Exception ex) {
			logger.error("Failed to verify SAML configuration.", ex);
			isConnected = false;
		}
		return isConnected;
	}

}
