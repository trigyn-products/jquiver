package com.trigyn.jws.usermanagement.security.config;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.usermanagement.entities.JwsRole;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.exception.InvalidLoginException;
import com.trigyn.jws.usermanagement.repository.JwsRoleRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.service.UserConfigService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.utils.Constants.VerificationType;
import com.trigyn.jws.usermanagement.vo.JwsRoleVO;
import com.trigyn.jws.usermanagement.vo.JwsUserLoginVO;

import io.jsonwebtoken.ExpiredJwtException;

public class DefaultUserDetailsServiceImpl implements UserDetailsService {

	private final static Logger					logger							= LogManager
			.getLogger(DefaultUserDetailsServiceImpl.class);

	private JwsUserRepository					userRepository					= null;

	private JwsUserRoleAssociationRepository	userRoleAssociationRepository	= null;

	private UserConfigService					userConfigService				= null;

	@Autowired
	private PropertyMasterService				propertyMasterService			= null;
	
	@Autowired
	private JwsRoleRepository			jwsRoleRepository						= null;
	

	public DefaultUserDetailsServiceImpl() {
		super();
	}

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
		
		Map<String, Object>			mapDetails	= new HashMap<>();
		try {
			userConfigService.getConfigurableDetails(mapDetails);
		} catch (Exception a_excep) {
			logger.error("Error ocurred.", a_excep);
		}
		JwsUser user = new JwsUser();
		if(email!=null && email.equalsIgnoreCase("anonymous")) {
			return getAnonymousUserInformation();
		}else {
			user = userRepository.findByEmailIgnoreCase(email);
		}
		 
		if (user == null) {
			throw new UsernameNotFoundException("Not found!");
		}
		
		if(sra != null) {
			HttpServletRequest			request		= sra.getRequest();
			HttpSession					session		= request.getSession();
			@SuppressWarnings("unchecked")
			List<JwsUserLoginVO> multiAuthLoginVOs = (List<JwsUserLoginVO>) mapDetails.get("activeAutenticationDetails");
			if (multiAuthLoginVOs != null && multiAuthLoginVOs.isEmpty() == false) {
				String					loginType			= request.getParameter("enableAuthenticationType");
				if(null == loginType) {
					// TODO :: Restructure the code using ternary operator.
					loginType = request.getHeader("at");
					if (Constants.AuthTypeHeaderKey.DAO.getAuthTypeHeaderKey().equals(loginType)) {
						loginType = Constants.DAO_ID;
					} else if (Constants.AuthTypeHeaderKey.LDAP.getAuthTypeHeaderKey().equals(loginType)) {
						loginType = Constants.LDAP_ID;
					} else if (Constants.AuthTypeHeaderKey.OAUTH.getAuthTypeHeaderKey().equals(loginType)) {
						loginType = Constants.OAUTH_ID;
					}
				}
				
				if (multiAuthLoginVOs != null && multiAuthLoginVOs.isEmpty() == false && loginType!=null && Constants.DAO_ID.equals(loginType)) {
					for (JwsUserLoginVO multiAuthLogin : multiAuthLoginVOs) {
						if (Constants.AuthType.DAO.getAuthType() == multiAuthLogin.getAuthenticationType()) {
							Map<String, Object> loginAttributes = multiAuthLogin.getLoginAttributes();
							if (loginAttributes != null && loginAttributes.isEmpty() == false) {
								if (loginAttributes.containsKey("enableCaptcha")) {
									String captcaValue = (String) loginAttributes.get("enableCaptcha");
									if (captcaValue != null && captcaValue.equalsIgnoreCase("true")) {
										if ((session.getAttribute("loginCaptcha")!=null && (request.getParameter("captcha")!=null && request.getParameter("captcha")
												.equals(session.getAttribute("loginCaptcha").toString())== false))) {
											session.removeAttribute("loginCaptcha");
											throw new InvalidLoginException("Please verify captcha!");
										}
									}
									if (loginAttributes.containsKey("verificationType")) {
										String verificationTypeValue = (String) loginAttributes.get("verificationType");
										if (verificationTypeValue != null
												&& verificationTypeValue.equals(VerificationType.TOTP.getVerificationType())) {
											user.setPassword(new BCryptPasswordEncoder()
													.encode(new TwoFactorGoogleUtil().getTOTPCode(user.getSecretKey())));
										}
										if (verificationTypeValue != null
												&& verificationTypeValue.equals(VerificationType.OTP.getVerificationType())
												&& user.getOneTimePassword() != null && user.getOtpRequestedTime() != null) {
											try {
												Date	otpSentTime			= user.getOtpRequestedTime();
												Date	currentTime			= java.util.Calendar.getInstance().getTime();
												long	maxOtpActiveTime	= Long.valueOf(
														propertyMasterService.findPropertyMasterValue("otp_expiry_time"));
												long	diffInMinutes		= TimeUnit.MILLISECONDS
														.toMinutes(currentTime.getTime() - otpSentTime.getTime());
												if (diffInMinutes > maxOtpActiveTime) {
													throw new InvalidLoginException("OTP Expired ! Try again with new OTP.");
												}
												user.setPassword(user.getOneTimePassword());
											} catch (InvalidLoginException exception) {
												throw new ExpiredJwtException(null, null, exception.getMessage(), exception);
											} catch (Exception exception) {
												throw new RuntimeException("Error while getting user.");
											}
										}
									}

								}
							}
						}
					}
				}
			}
		}
		List<JwsRoleVO> rolesVOs = userRoleAssociationRepository.getUserRoles(Constants.ISACTIVE, user.getUserId());
		return new UserInformation(user, rolesVOs);
	}
	
	public UserInformation getAnonymousUserInformation() {
		JwsUser user = new JwsUser();
		Map<String, Object>	attributes	= new HashMap<String, Object>();
		List<JwsRoleVO> rolesVOs = new ArrayList<>();
		user.setEmail("anonymous");
		user.setFirstName("anonymous");
		user.setLastName("anonymous");
		user.setUserId("anonymous");
		JwsRole jwsRole = jwsRoleRepository.findByRoleName("ANONYMOUS");
		if(jwsRole!=null) {
			JwsRoleVO vo = jwsRole.convertEntityToVO(jwsRole);
			rolesVOs.add(vo);
		}
		return new UserInformation().create(user, attributes, rolesVOs);
	}

}
