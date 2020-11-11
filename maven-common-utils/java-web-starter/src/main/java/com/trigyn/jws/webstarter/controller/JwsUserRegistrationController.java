package com.trigyn.jws.webstarter.controller;


import java.awt.Dimension;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.entities.JwsAuthenticationType;
import com.trigyn.jws.usermanagement.entities.JwsConfirmationToken;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.entities.JwsUserRoleAssociation;
import com.trigyn.jws.usermanagement.repository.JwsAuthenticationTypeRepository;
import com.trigyn.jws.usermanagement.repository.JwsConfirmationTokenRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;
import com.trigyn.jws.usermanagement.security.config.CaptchaUtil;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.JwsUserVO;
import com.trigyn.jws.webstarter.service.SendMailService;
import com.trigyn.jws.webstarter.service.UserManagementService;
import com.trigyn.jws.webstarter.utils.Email;

@RestController
@RequestMapping("/cf")
public class JwsUserRegistrationController {

	
	@Autowired
	private JwsUserRepository userRepository =  null;
	
	@Autowired
	private JwsConfirmationTokenRepository confirmationTokenRepository =  null;
	
	@Autowired
	private PasswordEncoder passwordEncoder =  null;
	
	@Autowired
	private JwsUserRoleAssociationRepository   userRoleAssociationRepository = null;
	
	@Autowired
	private ApplicationSecurityDetails applicationSecurityDetails = null;
	
	@Autowired
	private JwsAuthenticationTypeRepository authenticationTypeRepository = null;
	
	@Autowired
	private UserManagementService userManagementService = null;
	
	@Autowired
    private DBTemplatingService templatingService     = null;
	
	@Autowired
	private TemplatingUtils templatingUtils = null;

	@Autowired
	private SendMailService sendMailService = null;
	
	@GetMapping("/login")
	@ResponseBody
	public String userLoginPage(HttpServletRequest request,HttpSession session,HttpServletResponse response) throws Exception {
		
		Map<String, Object> mapDetails = new HashMap<>();
		
		String queryString = request.getQueryString();
		if(StringUtils.isNotEmpty(request.getQueryString())) {
			mapDetails.put("queryString",queryString);
		}
		if(applicationSecurityDetails.getIsAuthenticationEnabled()) {
			userManagementService.getConfigurableDetails(mapDetails);
		}else {
			response.sendError(HttpStatus.FORBIDDEN.value(),"You dont have rights to access these module");
			return null;
		}
		TemplateVO templateVO =  templatingService.getTemplateByName("jws-login");
		return	templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), mapDetails);
		
	}

	
	@GetMapping("/register")
	@ResponseBody
	public String userRegistrationPage(HttpServletResponse response) throws Exception {
		Map<String, Object> mapDetails = new HashMap<>();
		String enableRegistrationPropertyName = "enableRegistration";
		if(applicationSecurityDetails.getIsAuthenticationEnabled()) {
			Integer authType = Integer.parseInt(applicationSecurityDetails.getAuthenticationType());
			JwsAuthenticationType authenticationType = authenticationTypeRepository.findById(authType)
					.orElseThrow(() -> new Exception("No auth type found with id : " + authType));
			JSONArray jsonArray = new JSONArray(authenticationType.getAuthenticationProperties());
			
			JSONObject jsonObject = null ;
			jsonObject = getJsonObjectFromPropertyValue(jsonObject, jsonArray, enableRegistrationPropertyName);
			if(jsonObject!= null && jsonObject.getString("value").equalsIgnoreCase("false")){
				response.sendError(HttpStatus.FORBIDDEN.value(),"You dont have rights to access these module");
			}
			userManagementService.getConfigurableDetails(mapDetails);
		}else {
			response.sendError(HttpStatus.FORBIDDEN.value(),"You dont have rights to access these module");
			return null;
		}
		TemplateVO templateVO =  templatingService.getTemplateByName("jws-register");
		return	templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), mapDetails);
		
	}
	
	@PostMapping(value="/register")
	@ResponseBody
    public String registerUser(HttpServletRequest request, JwsUserVO user,HttpServletResponse response) throws Exception
    {
		
		Map<String, Object> mapDetails =new HashMap<>();
		String viewName=null;
		JwsUser existingUser = userRepository.findByEmailIgnoreCase(user.getEmail());
		if(applicationSecurityDetails.getIsAuthenticationEnabled()) {
			userManagementService.getConfigurableDetails(mapDetails);
		}else {
			response.sendError(HttpStatus.FORBIDDEN.value(),"You dont have rights to access these module");
			return null;
		}
		
        if(existingUser != null)
        {
        	mapDetails.put("error","This email already exists!");
        	mapDetails.put("firstName", user.getFirstName().trim());
        	mapDetails.put("lastName", user.getLastName().trim());
            viewName = "jws-register";
        }
        else
        {
        	HttpSession session = request.getSession();
        	if(mapDetails.get("enableCaptcha").toString().equalsIgnoreCase("true") && session.getAttribute("captcha")!=null &&
        			!(user.getCaptcha().equals(session.getAttribute("captcha").toString()))) {
        		mapDetails.put("error","Please verify captcha!");
            	mapDetails.put("firstName", user.getFirstName().trim());
            	mapDetails.put("lastName", user.getLastName().trim());
                viewName = "jws-register";
                TemplateVO templateVO =  templatingService.getTemplateByName(viewName);
        		return	templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), mapDetails);
        	}
        	
        	
        	if(userManagementService.validatePassword(user.getPassword())) {
        		
        		user.setPassword(passwordEncoder.encode(user.getPassword()));
        		user.setIsActive(Constants.INACTIVE);
        		user.setForcePasswordChange(Constants.INACTIVE);
        		JwsUser userEntityFromVo = user.convertVOToEntity(user);
        		userEntityFromVo.setForcePasswordChange(Constants.INACTIVE);
        		userRepository.save(userEntityFromVo);
        		
        		
        		JwsConfirmationToken confirmationToken = new JwsConfirmationToken(userEntityFromVo);
        		confirmationTokenRepository.save(confirmationToken);
        		

         		Email email = new Email();
				email.setInternetAddressToArray(InternetAddress.parse(user.getEmail()));  
				email.setSubject("Complete Registration!");  
				email.setMailFrom("admin@jquiver.com");
				email.setBody("To confirm your account, please click here : "
        				+"http://localhost:8080/cf/confirm-account?token="+confirmationToken.getConfirmationToken());
         
         
				System.out.println(email.getBody());
        		sendMailService.sendTestMail(email);
        		
        		
        		mapDetails.put("emailId", user.getEmail());
        		
        		viewName = "jws-successfulRegisteration";
        	}else {
        		 viewName = "jws-register";
        		 mapDetails.put("firstName", user.getFirstName().trim());
             	 mapDetails.put("lastName", user.getLastName().trim());
             	 mapDetails.put("emailId", user.getEmail());
        		 mapDetails.put("errorPassword","Password must contain atleast 6 characters including UPPER/lowercase/Special charcters and numbers!");
        	}
        }
        
        TemplateVO templateVO =  templatingService.getTemplateByName(viewName);
		return	templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), mapDetails);
        
    }


	
	


	private JSONObject getJsonObjectFromPropertyValue(JSONObject jsonObject, JSONArray jsonArray, String propertyName)
			throws JSONException {
		for (int i = 0; i < jsonArray.length(); i++) {
			 jsonObject = jsonArray.getJSONObject(i);
			if(jsonObject.get("name").toString().equalsIgnoreCase(propertyName)) {
				break;
			}else {
				jsonObject = null ;
			}
		}
		return jsonObject;
	}


	@GetMapping(value="/confirm-account")
	 @ResponseBody
	 public String confirmUserAccount(ModelAndView modelAndView, @RequestParam("token") String confirmationToken,HttpServletResponse response) throws Exception{
	      
		 
		Map<String, Object> mapDetails =new HashMap<>();
		String viewName=null;
		
		if(applicationSecurityDetails.getIsAuthenticationEnabled()) {
			
		 JwsConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

	        if(token != null)
	        {
	            JwsUser user = userRepository.findByEmailIgnoreCase(token.getUserRegistration().getEmail());
	            user.setIsActive(Constants.ISACTIVE);
	            userRepository.save(user);
	            
	        	JwsUserRoleAssociation adminRoleAssociation = new JwsUserRoleAssociation();
        		adminRoleAssociation.setUserId(user.getUserId());
        		adminRoleAssociation.setRoleId(Constants.AUTHENTICATED_ROLE_ID);
        		userRoleAssociationRepository.save(adminRoleAssociation);
        		
        		StringBuffer sb = new StringBuffer("First Name :"+user.getFirstName().trim());
        		sb.append("Last Name :"+ user.getLastName().trim());
        		sb.append("Email :"+user.getEmail().trim());
	            viewName = "jws-accountVerified";
	        }
	        else
	        {
	            response.sendError(HttpStatus.BAD_REQUEST.value(), "The link is invalid or broken!");
	            return null;
	        }

	        TemplateVO templateVO =  templatingService.getTemplateByName(viewName);
			return	templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(), mapDetails);
	        
			}else {
				
				response.sendError(HttpStatus.FORBIDDEN.value(),"You dont have rights to access these module");
				return null;
			}
		
	    }
	
	
	@GetMapping(value="/captcha")
	public void loadCaptcha(HttpServletRequest request,HttpServletResponse response) throws Throwable {

		String captchaStr = CaptchaUtil.getCaptchaString();
		System.out.println(captchaStr);
		int width = 130;
		int height = 59;
		HttpSession session = request.getSession();
		session.setAttribute("captcha", captchaStr);

		OutputStream outputStream = response.getOutputStream();
		CaptchaUtil.generateCaptcha(new Dimension(width, height), captchaStr, outputStream);
		outputStream.close();
	}
	
}
