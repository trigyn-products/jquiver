package com.trigyn.jws.usermanagement.security.config;

import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.ldap.CommunicationException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.exception.InvalidLoginException;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.service.UserConfigService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.JwsUserLoginVO;
import com.trigyn.jws.usermanagement.vo.JwsUserVO;
import com.trigyn.jws.webstarter.dao.ICaptchRepository;
import com.trigyn.jws.webstarter.service.CaptchaService;
import com.trigyn.jws.webstarter.vo.CaptchaDetails;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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

	@Autowired
	private JwsUserRepository						userRepository				= null;

	private Map<String, LdapAuthenticationProvider>	ldapAuthenticationProviders	= new HashMap<>();

	private DaoAuthenticationProvider				daoAuthenticationProvider	= null;

	@Autowired
	@Lazy
	private UserDetailsService						userDetailsService			= null;

	@Autowired
	private LdapConfigService						ldapConfigService			= null;

	@Autowired
	private PasswordEncoder							passwordEncoder				= null;

	@Autowired
	private UserConfigService						userConfigService			= null;

	@Autowired
	LdapUserService									ldapUserService				= null;
	
	@Autowired
	private ICaptchRepository iCaptchRepository 				= null;
	
	@Autowired
	private CaptchaService 						captchaService 					= null;
	
	private static final Pattern BASE64_PATTERN = Pattern.compile("^[A-Za-z0-9+/=]+$");
	
	public CustomAuthenticationProvider() {
		// TODO Auto-generated constructor stub
	}
	
	public CustomAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

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
					if (null != displayKey && displayKey.equalsIgnoreCase("ldapDisplayDetails")) {
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

		try {
			String decryptedPassword = null;
			String requestId = null;
			String password = null;
			String username = authentication.getName();
			if (request.getParameter("requestId") != null) {
				requestId = request.getParameter("requestId");
			}
			if (authentication.getCredentials().toString() != null) {
				password = authentication.getCredentials().toString();
			}
			int blockSize = 16;
			if (isBase64Encoded(password) && isMultipleOfBlockSize(password, blockSize)) {
				decryptedPassword = userConfigService.decryptPassword(password, requestId);
			}
			JwsUser	userEntity	= userRepository.findByEmailIgnoreCase(username);
	        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
	        Object details = authentication.getDetails();
	        if(decryptedPassword != null) {
		        UsernamePasswordAuthenticationToken newAuth = 
		            new UsernamePasswordAuthenticationToken(username, decryptedPassword, authorities);
		        newAuth.setDetails(details);
		        authentication = newAuth;
	        }
			String	authType	= request.getParameter("enableAuthenticationType");
			String authTypeHeader = request.getHeader("at");
			authType = Constants.getAuthType(authType, authTypeHeader);
			if(authType != null || authType == Constants.DAO_ID) {
				validateAuthentication();
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
		} catch (InternalAuthenticationServiceException ce) {
			logger.error("Failed : Error while authenticating " + ce.getMessage());
			if (ce.getCause().getClass().equals(CommunicationException.class)) {
				String		ldapServerDisplayId	= request.getParameter("ldapConfig");
				throw new InvalidLoginException("Error while connecting "+ ldapServerDisplayId +" network! Make sure logged into domain");
			} else {
				ce.printStackTrace();
			}
		} catch (Exception exec) {
			logger.error("Failed : Error while authenticating " + exec.getMessage());
			throw new InvalidLoginException(exec.getMessage());
		}
		return authentication;

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
	
	private Boolean validateAuthentication() {

		Map<String, Object>			mapDetails	= new HashMap<>();
		try {
			userConfigService.getConfigurableDetails(mapDetails);
		} catch (Exception a_excep) {
			logger.error("Error ocurred.", a_excep);
		}
		if (mapDetails.get("activeAutenticationDetails") instanceof List) {
			List<JwsUserLoginVO> multiAuthLoginVOs = (List<JwsUserLoginVO>) mapDetails.get("activeAutenticationDetails");
			JwsUserLoginVO  jwsUserLoginVO = multiAuthLoginVOs.stream().	filter(multiAuthLogin -> Constants.AuthType.DAO.getAuthType() == multiAuthLogin.getAuthenticationType()).
			findAny().orElse(null);
			if (jwsUserLoginVO != null && jwsUserLoginVO.getLoginAttributes().isEmpty() == false) {
				Map<String, Object> loginAttributes = jwsUserLoginVO.getLoginAttributes();
				ServletRequestAttributes	sra			= (ServletRequestAttributes) RequestContextHolder
						.getRequestAttributes();
				HttpServletRequest			request		= sra.getRequest();
				HttpSession					session		= request.getSession();
				String						generatedCaptcha	= null;
				String captchaRequest_Id=request.getHeader("r");
				if (loginAttributes.containsKey("enableCaptcha")) {
					String captcaValue = (String) loginAttributes.get("enableCaptcha");
					if (captcaValue != null && captcaValue.equalsIgnoreCase("true")) {
						//fetch captcha for database
						captchaService.deleteExpiredCaptcha();
						CaptchaDetails captchaDetails = null;
						if (request.getParameter("captcha")!=null && StringUtils.isBlank(captchaRequest_Id) == false && captchaRequest_Id!=null ) {
							captchaDetails = iCaptchRepository.findById(captchaRequest_Id).orElse(null);;
							if (null != captchaDetails)
							{
							 generatedCaptcha=captchaDetails.getCaptcha();
							}
							else {
								throw new InvalidLoginException("Captcha has Expired!");
							}
						}
						if ((generatedCaptcha!=null && (request.getParameter("captcha")!=null && request.getParameter("captcha")
						.equals(generatedCaptcha.toString())== false))) {
							if (StringUtils.isBlank(captchaRequest_Id) == false && captchaRequest_Id!=null ) {
								   captchaService.deleteCaptcha(captchaRequest_Id);
								}
							throw new InvalidLoginException("Please verify captcha!");
						}
						else
						{
							if (StringUtils.isBlank(captchaRequest_Id) == false) {
								   captchaService.deleteCaptcha(captchaRequest_Id);
								}
						}
						//End of Fetch captch
					}
				}
			}
		}
	
		return Boolean.TRUE;
	}
	
	public static boolean isBase64Encoded(String password) {
        return BASE64_PATTERN.matcher(password).matches();
    }

    public static boolean isMultipleOfBlockSize(String password, int blockSize) {
        try {
            byte[] decoded = Base64.getDecoder().decode(password);
            return decoded.length % blockSize == 0;
        } catch (IllegalArgumentException exce) {
            return false;
        }
    }

}
