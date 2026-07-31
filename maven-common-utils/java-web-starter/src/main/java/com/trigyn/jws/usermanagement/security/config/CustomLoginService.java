package com.trigyn.jws.usermanagement.security.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.entities.JwsUserRoleAssociation;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.vo.JwtRequestDetails;
import com.trigyn.jws.webstarter.utils.JQuiverProperties;

import io.jsonwebtoken.Claims;
import jakarta.servlet.ServletContext;
import reactor.core.publisher.Mono;



@Service
public class CustomLoginService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwsUserRepository userRepository;

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private PropertyMasterService propertyMasterService;

	@Autowired
	private JQuiverProperties 			jQuiverPropeties 			= null;
	
	@Autowired
	private PasswordEncoder							passwordEncoder				= null;
	
	@Autowired
	private JwsUserRoleAssociationRepository			userRoleRepository				= null;

    public void authenticate(String email, String password) throws Exception {

        System.out.println("Inside Custom Authentication Service");

        // 1. Call GJCA
        String jwt = getJwtForCustomAuth(email, password);

        System.out.println("JWT : " + jwt);

        if (jwt == null || jwt.isBlank()) {
            throw new RuntimeException("Invalid Credentials");
        }

        // 2. Encrypt JWT and generate requestId
        JwtRequestDetails jwtRequestDetails = jwtUtil.createJwtRequestDetails(jwt);

        // 3. Validate JWT
        boolean valid = jwtUtil.validateToken(
                jwtRequestDetails.getToken(),
                jwtRequestDetails.getRequestId(),
                "/cf/login");

        if (!valid) {
            throw new RuntimeException("Invalid JWT");
        }

        System.out.println("JWT Validated Successfully");

        // 4. Extract Claims
        Claims claims = jwtUtil.getClaimsFromToken(
                jwtRequestDetails.getToken(),
                jwtRequestDetails.getRequestId(),
                "/cf/login");

        String userEmail = claims.get("email", String.class);
        String fullName = claims.get("fullName", String.class);

        // 5. Check User
        JwsUser existingUser = userRepository.findByEmailIgnoreCase(userEmail);

        if (existingUser == null) {

            String firstName = "";
            String lastName = "";

            if (fullName != null) {
                String[] names = fullName.trim().split("\\s+", 2);
                firstName = names[0];
                lastName = names.length > 1 ? names[1] : "";
            }

            JwsUser user = new JwsUser();

            user.setUserId(UUID.randomUUID().toString());
            user.setEmail(userEmail);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(passwordEncoder.encode(password));
            user.setIsActive(1);
            user.setFailedAttempt(0);
            user.setForcePasswordChange(0);
            user.setIsCustomUpdated(1);

            userRepository.save(user);
            
            JwsUserRoleAssociation	userRoleAssociation	= new JwsUserRoleAssociation();
            Date					currentDate			= new Date();
			userRoleAssociation.setRoleId("2ace542e-0c63-11eb-9cf5-f48e38ab9348");
			userRoleAssociation.setUserId(user.getUserId());
			userRoleAssociation.setUpdatedDate(currentDate);
			userRoleRepository.save(userRoleAssociation);

            System.out.println("Custom User Created");
        }

        System.out.println("Custom Authentication Completed Successfully");
    }

    public String getJwtForCustomAuth(String email, String password) throws Exception {

        String contextPath = servletContext.getContextPath();
        String baseUrl = propertyMasterService.findPropertyMasterValue("base-url");

        StringBuilder fullRestApiUrl = new StringBuilder(baseUrl);

        if (contextPath != null && !contextPath.isEmpty()) {
            fullRestApiUrl.append(contextPath);
        }

        fullRestApiUrl
                .append(jQuiverPropeties.getApiPath())
                .append("/gjca");

        String restApiUrl = fullRestApiUrl.toString();

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("email", email);
        requestBody.put("password", password);

        WebClient webClient = WebClient.builder()
                .baseUrl(restApiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.USER_AGENT, "JQuiver")
                .build();

        ResponseEntity<String> response = webClient.post()
                .uri(restApiUrl)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new RuntimeException(error))))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(error -> Mono.error(new RuntimeException(error))))
                .toEntity(String.class)
                .block();

        return response.getBody();
    }
}