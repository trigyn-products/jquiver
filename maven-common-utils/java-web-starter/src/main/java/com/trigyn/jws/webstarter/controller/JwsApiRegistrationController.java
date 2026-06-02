package com.trigyn.jws.webstarter.controller;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.SaltDetailsRepository;
import com.trigyn.jws.usermanagement.security.config.AuthenticationRequest;
import com.trigyn.jws.usermanagement.security.config.AuthenticationResponse;
import com.trigyn.jws.usermanagement.security.config.CustomAuthenticationProvider;
import com.trigyn.jws.usermanagement.security.config.JwtUtil;
import com.trigyn.jws.usermanagement.security.config.SaltDetails;
import com.trigyn.jws.usermanagement.security.config.TwoFactorGoogleUtil;
import com.trigyn.jws.usermanagement.service.UserConfigService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.JwsUserLoginVO;
import com.trigyn.jws.usermanagement.vo.JwsUserVO;
import com.trigyn.jws.usermanagement.vo.JwtRequestDetails;
import com.trigyn.jws.webstarter.dao.ICaptchRepository;
import com.trigyn.jws.webstarter.service.CaptchaService;
import com.trigyn.jws.webstarter.vo.CaptchaDetails;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/japi")
public class JwsApiRegistrationController {

	private final static Logger			logger						= LoggerFactory.getLogger(JwsApiRegistrationController.class);

	@Autowired
	@Lazy
	private AuthenticationManager			authenticationManager	= null;

	@Autowired
	private JwtUtil							jwtTokenUtil			= null;

	@Autowired
	@Lazy
	private UserDetailsService				userDetailsService		= null;

	@Autowired
	private JwsUserRepository				userRepository			= null;

	@Autowired
	private PasswordEncoder					passwordEncoder			= null;

	@Autowired
	private CustomAuthenticationProvider	customAuthProvider		= null;

	@Autowired
	private UserConfigService				userConfigService		= null;

	@Autowired
	private FileUtilities					fileUtilities			= null;

	@Autowired
	private CaptchaService					captchaService			= null;

	@Autowired
	private ICaptchRepository				iCaptchRepository		= null;

	@Autowired
	private SaltDetailsRepository		saltDetailsRepository		= null;

	@PostMapping(value = "/login")
	public ResponseEntity<?> authenticateUser(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, @RequestBody AuthenticationRequest authenticationRequest) {

		Map<String, Object> authDetails = new HashMap<>();
		try {
			userConfigService.getConfigurableDetails(authDetails);
			Map<String, Object> responseDetails = validateLoginDetails(authDetails, authenticationRequest,
					httpServletRequest);
			if (responseDetails.isEmpty() && responseDetails.containsKey("errorCode") == false) {
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
						authenticationRequest.getUsername(), authenticationRequest.getPassword());
				token.setDetails(new WebAuthenticationDetails(httpServletRequest));
				Authentication auth = customAuthProvider.authenticate(token);
				if (auth != null && auth.isAuthenticated()) {
					UserDetails	userDetails	= userDetailsService
							.loadUserByUsername(authenticationRequest.getUsername());

					JwtRequestDetails jwtRequestDetails				= jwtTokenUtil.generateToken(userDetails);
					return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse(jwtRequestDetails.getToken()), HttpStatus.OK);
				} else {
					return new ResponseEntity<String>("Bad credentials", HttpStatus.BAD_REQUEST);
				}

			} else {
				fileUtilities.customSendError(httpServletResponse,
						Integer.parseInt(String.valueOf(responseDetails.get("errorCode"))),
						String.valueOf(responseDetails.get("errorMessage")));
			}
		} catch (BadCredentialsException exception) {
			return new ResponseEntity<String>("Bad credentials", HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@PostMapping(value = "/rt")
	public ResponseEntity<?> refreshToken(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws Exception {

		try {
//			String requestBody = "";
//			if (httpServletRequest != null) {
//				BufferedReader reader = httpServletRequest.getReader();
//				if (reader != null) {
//					requestBody = IOUtils.toString(reader);
//
//				}
//			}
//
//			if (requestBody.isEmpty()) {
//				throw new Exception("Invalid refresh token.");
//			}
//
//			JSONObject json = new JSONObject(requestBody);
//			String encryptedRefreshToken = json.getString("rt");
//			String saltRequestId = json.getString("requestId");
//
//			String bearerToken = userConfigService.decryptPassword(encryptedRefreshToken, saltRequestId);

			String bearerToken = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("Authorization");

			if(bearerToken != null && bearerToken.isEmpty() == false && bearerToken.startsWith("Bearer ") != false) {
				bearerToken = bearerToken.substring(7);
			}
			String saltRequestId = jwtTokenUtil.extractJwtFromCookie(httpServletRequest, "r");
			boolean isValidRefreshToken = jwtTokenUtil.validateToken(bearerToken,saltRequestId,httpServletRequest.getRequestURI());
			if (isValidRefreshToken == false) {
				throw new Exception("Invalid refresh token.");
			}
			UserDetails userDetails = jwtTokenUtil.fetchUserInfoFromToken(bearerToken,saltRequestId,httpServletRequest.getRequestURI());
			JwtRequestDetails jwtRequestDetails = jwtTokenUtil.generateToken(userDetails);
			return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse(jwtRequestDetails.getToken()),
					HttpStatus.OK);

		} catch (Exception excp) {
			excp.printStackTrace();
			logger.error("Error Occured while refreshtoken ## .", excp);
			throw new Exception("Invalid refresh token.");
		}
	}

	@PostMapping(value = "/register")
	public ResponseEntity<String> registerUser(HttpServletResponse httpServletResponse, @RequestBody JwsUserVO user)
			throws Exception {

		// Integer authType =
		// Integer.parseInt(applicationSecurityDetails.getAuthenticationType());
		// if (authType == Constants.AuthType.DAO.getAuthType() || authType ==
		// Constants.AuthType.LDAP.getAuthType()) {
		JwsUser existingUser = new JwsUser();
		if (StringUtils.isNotBlank(user.getEmail()) && StringUtils.isNotBlank(user.getFirstName())
				&& StringUtils.isNotBlank(user.getLastName()) && StringUtils.isNotBlank(user.getPassword())) {

			existingUser = userRepository.findByEmailIgnoreCase(user.getEmail());
			if (existingUser != null) {
				return new ResponseEntity<String>("User already exist with these email", HttpStatus.CONFLICT);
			}
			JwsUser jwsUser = user.convertVOToEntity(user);
			jwsUser.setPassword(passwordEncoder.encode(jwsUser.getPassword()));
			jwsUser.setIsActive(Constants.ISACTIVE);
			jwsUser.setForcePasswordChange(Constants.INACTIVE);

			jwsUser.setSecretKey(new TwoFactorGoogleUtil().generateSecretKey());
			userRepository.save(jwsUser);
			return new ResponseEntity<String>("User Created Successfully", HttpStatus.OK);

		} else {
			return new ResponseEntity<String>("Necessary Parameters missing ", HttpStatus.PRECONDITION_FAILED);
		}
		// } else {
		// httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), "You do not have
		// enough privilege to access this module");
		// return null;
		// }

	}

	private Map<String, Object> validateLoginDetails(Map<String, Object> authDetails,
			AuthenticationRequest authenticationRequest, HttpServletRequest httpServletRequest) {
		Map<String, Object> responseDetails = new HashMap<>();

		if (authenticationRequest != null && authenticationRequest.getUsername().isEmpty()) {
			responseDetails.put("errorMessage", "Email is required");
			responseDetails.put("errorCode", HttpStatus.BAD_REQUEST.value());
			return responseDetails;
		}

		if (authenticationRequest != null && authenticationRequest.getPassword().isEmpty()) {
			responseDetails.put("errorMessage", "Password is required");
			responseDetails.put("errorCode", HttpStatus.PRECONDITION_FAILED.value());
			return responseDetails;
		}
		
		List<JwsUserLoginVO>	multiAuthLoginVOs	= (List<JwsUserLoginVO>) authDetails
				.get("activeAutenticationDetails");
		JwsUserLoginVO			daoAuthDetails		= multiAuthLoginVOs.stream()
				.filter(loginVO -> loginVO.getAuthenticationType().equals(Constants.AuthType.DAO.getAuthType()))
				.findAny().orElse(null);

		if (daoAuthDetails == null) {
			responseDetails.put("errorMessage", "Authentication not supported.");
			responseDetails.put("errorCode", HttpStatus.NOT_IMPLEMENTED.value());
			return responseDetails;
		}

		Map<String, Object> daoAuthAttributes = daoAuthDetails.getLoginAttributes();
		if (daoAuthAttributes != null) {

			String saltRequestId = httpServletRequest.getParameter("requestId");
			if(saltRequestId != null) {
				SaltDetails saltDetails = saltDetailsRepository.findByRequestId(saltRequestId);
				if (saltDetails == null) {
					responseDetails.put("errorMessage", "Login page expired. Please relogin.");
					responseDetails.put("errorCode", HttpStatus.PRECONDITION_FAILED.value());
					return responseDetails;
				}
			}

			
			if(daoAuthAttributes.containsKey("enableCaptcha")
				&& daoAuthAttributes.get("enableCaptcha") != null
				&& daoAuthAttributes.get("enableCaptcha").toString().equals("true")) {
				captchaService.deleteExpiredCaptcha();
				String			generatedCaptcha	= null;
				CaptchaDetails	captchaDetails		= null;
				String			captchaRequest_Id	= httpServletRequest.getHeader("r");
				captchaDetails = iCaptchRepository.findById(captchaRequest_Id).orElse(null);
				if (null != captchaDetails) {
					generatedCaptcha = captchaDetails.getCaptcha();
				} else {
					responseDetails.put("errorMessage", "Captcha expired.");
					responseDetails.put("errorCode", HttpStatus.PRECONDITION_FAILED.value());
					return responseDetails;
				}
				
				if (generatedCaptcha != null && authenticationRequest.getCaptcha() != null
						&& generatedCaptcha.equals(authenticationRequest.getCaptcha()) == false) {
					responseDetails.put("errorMessage", "Invalid captcha.");
					responseDetails.put("errorCode", HttpStatus.PRECONDITION_FAILED.value());
					return responseDetails;
				}
				if (authenticationRequest != null && authenticationRequest.getCaptcha() == null
						|| authenticationRequest.getCaptcha().isEmpty()) {
					responseDetails.put("errorMessage", "Captca is required.");
					responseDetails.put("errorCode", HttpStatus.PRECONDITION_FAILED.value());
					return responseDetails;
				}
	
				if (generatedCaptcha != null && !(authenticationRequest.getCaptcha() != null
						&& authenticationRequest.getCaptcha().equals(generatedCaptcha.toString()))) {
					// session.removeAttribute("loginCaptcha");
					responseDetails.put("errorMessage", "Please verify captcha !");
					responseDetails.put("errorCode", HttpStatus.PRECONDITION_FAILED.value());
				}
			}
		}
		return responseDetails;
	}

}
