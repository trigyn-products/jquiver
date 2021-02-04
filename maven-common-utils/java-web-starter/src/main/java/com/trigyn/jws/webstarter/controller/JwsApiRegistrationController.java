package com.trigyn.jws.webstarter.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dynamicform.utils.CryptoUtils;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
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
	private AuthenticationManager	authenticationManager	= null;

	@Autowired
	private JwtUtil					jwtTokenUtil			= null;

	@Autowired
	@Lazy
	private UserDetailsService		userDetailsService		= null;

	@Autowired
	private JwsUserRepository		userRepository			= null;

	@Autowired
	private PasswordEncoder			passwordEncoder			= null;

	private final static String		JWS_SALT				= "main alag duniya";

	@PostMapping(value = "/login")
	public ResponseEntity<AuthenticationResponse> loadCaptcha(@RequestBody AuthenticationRequest authenticationRequest)
			throws Throwable {

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
	}

	@GetMapping("/hello")
	public String helloWorld() {
		return "Hello World";

	}

	@PostMapping(value = "/register")
	public ResponseEntity<String> registerUser(@RequestBody JwsUserVO user) throws Exception {

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

	}

}
