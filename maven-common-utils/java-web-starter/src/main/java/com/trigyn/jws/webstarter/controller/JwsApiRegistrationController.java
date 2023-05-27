package com.trigyn.jws.webstarter.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
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

import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.security.config.AuthenticationRequest;
import com.trigyn.jws.usermanagement.security.config.AuthenticationResponse;
import com.trigyn.jws.usermanagement.security.config.CustomAuthenticationProvider;
import com.trigyn.jws.usermanagement.security.config.JwtUtil;
import com.trigyn.jws.usermanagement.security.config.TwoFactorGoogleUtil;
import com.trigyn.jws.usermanagement.service.UserConfigService;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.JwsUserLoginVO;
import com.trigyn.jws.usermanagement.vo.JwsUserVO;

@RestController
@RequestMapping("/japi")
public class JwsApiRegistrationController {

	@Autowired
	@Lazy
	private AuthenticationManager		authenticationManager		= null;

	@Autowired
	private JwtUtil						jwtTokenUtil				= null;

	@Autowired
	@Lazy
	private UserDetailsService			userDetailsService			= null;

	@Autowired
	private JwsUserRepository			userRepository				= null;

	@Autowired
	private PasswordEncoder				passwordEncoder				= null;

	@Autowired
	private CustomAuthenticationProvider customAuthProvider 		= null;
	
	@Autowired
	private UserConfigService	userConfigService					= null;

	@PostMapping(value = "/login")
	public ResponseEntity<?> authenticateUser(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			@RequestBody AuthenticationRequest authenticationRequest) {

		Map<String, Object> authDetails = new HashMap<>();
		try {
			userConfigService.getConfigurableDetails(authDetails);
			Map<String, Object> responseDetails = validateLoginDetails(authDetails, authenticationRequest,
					httpServletRequest);
			if (responseDetails.isEmpty() && responseDetails.containsKey("errorCode") == false) {
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());
				token.setDetails(new WebAuthenticationDetails(httpServletRequest));
				Authentication auth = customAuthProvider.authenticate(token);
				if (auth != null && auth.isAuthenticated()) {
					UserDetails	userDetails	= userDetailsService
							.loadUserByUsername(authenticationRequest.getUsername());

					String		jwt			= jwtTokenUtil.generateToken(userDetails);
					return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse(jwt), HttpStatus.OK);
				}else {
					return new ResponseEntity<String>("Bad credentials", HttpStatus.BAD_REQUEST);
				}

			} else {
				httpServletResponse.sendError(Integer.parseInt(String.valueOf(responseDetails.get("errorCode"))),
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

	@PostMapping(value = "/register")
	public ResponseEntity<String> registerUser(HttpServletResponse httpServletResponse, @RequestBody JwsUserVO user) throws Exception {

		//Integer authType = Integer.parseInt(applicationSecurityDetails.getAuthenticationType());
		//if (authType == Constants.AuthType.DAO.getAuthType() || authType == Constants.AuthType.LDAP.getAuthType()) {
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
		//} else {
			//httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), "You do not have enough privilege to access this module");
			//return null;
		//}

	}
	
	private Map<String, Object> validateLoginDetails(Map<String, Object> authDetails, AuthenticationRequest authenticationRequest, HttpServletRequest httpServletRequest) {
		Map<String, Object> responseDetails = new HashMap<>();
		/*
		 * if(authenticationRequest == null) { responseDetails.put("errorMessage",
		 * "Email/Password is required"); responseDetails.put("errorCode",
		 * HttpStatus.BAD_REQUEST); return responseDetails; }
		 */
		if(authenticationRequest != null && authenticationRequest.getUsername().isEmpty()) {
			responseDetails.put("errorMessage", "Email is required");
			responseDetails.put("errorCode", HttpStatus.BAD_REQUEST.value());
			return responseDetails;
		}
		
		if(authenticationRequest != null && authenticationRequest.getPassword().isEmpty()) {
			responseDetails.put("errorMessage", "Password is required");
			responseDetails.put("errorCode", HttpStatus.PRECONDITION_FAILED.value());
			return responseDetails;
		}
		
		List<JwsUserLoginVO> multiAuthLoginVOs = (List<JwsUserLoginVO>) authDetails
				.get("activeAutenticationDetails");
		JwsUserLoginVO		daoAuthDetails	= multiAuthLoginVOs.stream()
				.filter(loginVO -> loginVO.getAuthenticationType().equals(Constants.AuthType.DAO.getAuthType()))
				.findAny().orElse(null);
		
		if(daoAuthDetails == null) {
			responseDetails.put("errorMessage", "Authentication not supported.");
			responseDetails.put("errorCode", HttpStatus.NOT_IMPLEMENTED.value());
			return responseDetails;
		}else {
			String verificationType = httpServletRequest.getParameter("verificationType");
			if(verificationType!=null && daoAuthDetails.getVerificationType().compareTo(Integer.valueOf(verificationType)) !=0 ) {
				responseDetails.put("errorMessage", "Authentication not supported.");
				responseDetails.put("errorCode", HttpStatus.NOT_IMPLEMENTED.value());
				return responseDetails;
			}
		}
		
		Map<String, Object>	daoAuthAttributes	= daoAuthDetails.getLoginAttributes();
		if (daoAuthAttributes != null && daoAuthAttributes.containsKey("enableCaptcha") && daoAuthAttributes.get("enableCaptcha") !=null && daoAuthAttributes.get("enableCaptcha").toString().equals("true")) {
			// TODO  : check the captcha value from session.
			// TODO  : check the session and passed captcha are same or not if same clear the captcha from session 
			// else the return the below code.
			HttpSession					session		= httpServletRequest.getSession();
			if (session!=null && session.getAttribute("loginCaptcha") == null) {
				responseDetails.put("errorMessage", "Invalid captcha.");
				responseDetails.put("errorCode", HttpStatus.PRECONDITION_FAILED.value());
				return responseDetails;
			}
			if(authenticationRequest != null && authenticationRequest.getCaptcha()==null || authenticationRequest.getCaptcha().isEmpty()) {
				responseDetails.put("errorMessage", "Captca is required.");
				responseDetails.put("errorCode", HttpStatus.PRECONDITION_FAILED.value());
				return responseDetails;
			}
			
			if (session.getAttribute("loginCaptcha")!=null && !(authenticationRequest.getCaptcha()!=null && authenticationRequest.getCaptcha()
					.equals(session.getAttribute("loginCaptcha").toString()))) {
				session.removeAttribute("loginCaptcha");
				responseDetails.put("errorMessage", "Please verify captcha !");
				responseDetails.put("errorCode", HttpStatus.PRECONDITION_FAILED.value());
			}
		}
		return responseDetails;
	}

}
