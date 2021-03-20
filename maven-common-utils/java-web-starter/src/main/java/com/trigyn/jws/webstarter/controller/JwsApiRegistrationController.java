package com.trigyn.jws.webstarter.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dynamicform.utils.CryptoUtils;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;
import com.trigyn.jws.usermanagement.security.config.AuthenticationRequest;
import com.trigyn.jws.usermanagement.security.config.AuthenticationResponse;
import com.trigyn.jws.usermanagement.security.config.JwtUtil;
import com.trigyn.jws.usermanagement.security.config.TwoFactorGoogleUtil;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.JwsUserVO;

@RestController
@RequestMapping("/japi")
public class JwsApiRegistrationController {

	@Autowired
	@Lazy
	private AuthenticationManager		authenticationManager		= null;

	@Autowired
	private ApplicationSecurityDetails	applicationSecurityDetails	= null;

	@Autowired
	private JwtUtil						jwtTokenUtil				= null;

	@Autowired
	@Lazy
	private UserDetailsService			userDetailsService			= null;

	@Autowired
	private JwsUserRepository			userRepository				= null;

	@Autowired
	private PasswordEncoder				passwordEncoder				= null;

	private final static String			JWS_SALT					= "main alag duniya";

	@PostMapping(value = "/login")
	public ResponseEntity<AuthenticationResponse> loadCaptcha(HttpServletResponse httpServletResponse,
			@RequestBody AuthenticationRequest authenticationRequest) throws Throwable {

		Integer authType = Integer.parseInt(applicationSecurityDetails.getAuthenticationType());
		if (authType == Constants.AuthType.DAO.getAuthType() || authType == Constants.AuthType.LDAP.getAuthType()) {
			String decryptedText = CryptoUtils.decrypt(JWS_SALT, authenticationRequest.getPassword());

			try {
				authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), decryptedText));
			} catch (BadCredentialsException exception) {
				throw new Exception("Bad Credentials", exception);
			}
			UserDetails	userDetails	= userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

			String		jwt			= jwtTokenUtil.generateToken(userDetails);

			return new ResponseEntity<AuthenticationResponse>(new AuthenticationResponse(jwt), HttpStatus.OK);
		} else {
			httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(),
					"You do not have enough privilege to access this module");
			return null;
		}
	}

	@PostMapping(value = "/register")
	public ResponseEntity<String> registerUser(HttpServletResponse httpServletResponse, @RequestBody JwsUserVO user)
			throws Exception {

		Integer authType = Integer.parseInt(applicationSecurityDetails.getAuthenticationType());
		if (authType == Constants.AuthType.DAO.getAuthType() || authType == Constants.AuthType.LDAP.getAuthType()) {
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
		} else {
			httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(),
					"You do not have enough privilege to access this module");
			return null;
		}

	}

}
