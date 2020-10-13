package com.trigyn.jws.usermanagement.controller;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.entities.JwsConfirmationToken;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.entities.JwsUserRoleAssociation;
import com.trigyn.jws.usermanagement.repository.JwsConfirmationTokenRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.utils.Constants;

@RestController
@RequestMapping("/cf")
public class JwsUserRegistrationController {

	
	@Autowired
	private JwsUserRepository userRepository =  null;
	
	@Autowired
	private JwsConfirmationTokenRepository confirmationTokenRepository =  null;
	
//	@Autowired
//	private JavaMailSender javaMailSender = null;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired 
	private MenuService  menuService = null;
	
	@Autowired
	private JwsUserRoleAssociationRepository   userRoleAssociationRepository = null;
	
	@GetMapping("/login")
	@ResponseBody
	public String userLoginPage(HttpServletRequest request,HttpSession session) throws Exception {
		
		Map<String, Object> mapDetails = new HashMap<>();
		
		mapDetails.put("loggedInUser", Boolean.FALSE);
		
		String queryString = request.getQueryString();
		if(StringUtils.isNotEmpty(request.getQueryString())) {
			mapDetails.put("queryString",queryString);
		}
		
		return menuService.getTemplateWithSiteLayout("jws-login",mapDetails);
		
	}

	
	@GetMapping("/register")
	@ResponseBody
	public String userRegistrationPage() throws Exception {
		Map<String, Object> mapDetails = new HashMap<>();
		mapDetails.put("loggedInUser", Boolean.FALSE);
		return menuService.getTemplateWithSiteLayout("jws-register",mapDetails);
	}
	
	@PostMapping(value="/register")
	@ResponseBody
    public String registerUser(ModelAndView modelAndView, JwsUser user) throws Exception
    {
		
		Map<String, Object> mapDetails =new HashMap<>();
		mapDetails.put("loggedInUser", Boolean.FALSE);
		String viewName=null;
		JwsUser existingUser = userRepository.findByEmailIgnoreCase(user.getEmail());
        if(existingUser != null)
        {
        	mapDetails.put("error","This email already exists!");
        	mapDetails.put("firstName", user.getFirstName().trim());
        	mapDetails.put("lastName", user.getLastName().trim());
            viewName = "jws-register";
        }
        else
        {
        	if(validatePassword(user.getPassword())) {
        		
        		user.setPassword(passwordEncoder.encode(user.getPassword()));
        		user.setIsActive(Constants.INACTIVE);
        		userRepository.save(user);
        		
        		
        		JwsConfirmationToken confirmationToken = new JwsConfirmationToken(user);
        		confirmationTokenRepository.save(confirmationToken);
        		
        		SimpleMailMessage mailMessage = new SimpleMailMessage();
        		mailMessage.setTo(user.getEmail());
        		mailMessage.setSubject("Complete Registration!");
        		mailMessage.setFrom("admin@gmail.com");
        		mailMessage.setText("To confirm your account, please click here : "
        				+"http://localhost:8080/cf/confirm-account?token="+confirmationToken.getConfirmationToken());
        		
        		
        		
        		CompletableFuture.runAsync( new Runnable() {
          			@Override
          			public void run() {
//          				javaMailSender.send(mailMessage);
          			}
          			});
        		
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

        return  menuService.getTemplateWithSiteLayout(viewName,mapDetails);
    }
	
	 private Boolean validatePassword(String password) {
		 	if(password == null || (password !=null && password.trim().isEmpty())) {
		 		return Boolean.FALSE;
		 	}else {
		 		String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,20}$"; 
		 		Pattern pattern = Pattern.compile(regex); 
		 		Matcher isMatches = pattern.matcher(password); 
		 		return isMatches.matches();
		 	}
	}


	@GetMapping(value="/confirm-account")
	 @ResponseBody
	 public String confirmUserAccount(ModelAndView modelAndView, @RequestParam("token") String confirmationToken) throws Exception{
	      
		 
		Map<String, Object> mapDetails =new HashMap<>();
		mapDetails.put("loggedInUser", Boolean.FALSE);
		String viewName=null;
		 JwsConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

	        if(token != null)
	        {
	            JwsUser user = userRepository.findByEmailIgnoreCase(token.getUserRegistration().getEmail());
	            user.setIsActive(Constants.ISACTIVE);
	            userRepository.save(user);
	            
	        	JwsUserRoleAssociation adminRoleAssociation = new JwsUserRoleAssociation();
        		adminRoleAssociation.setUserId(user.getUserId());
        		adminRoleAssociation.setRoleId(Constants.ADMIN_ROLE);
        		userRoleAssociationRepository.save(adminRoleAssociation);
        		
        		StringBuffer sb = new StringBuffer("First Name :"+user.getFirstName().trim());
        		sb.append("Last Name :"+ user.getLastName().trim());
        		sb.append("Email :"+user.getEmail().trim());
        		
        		
	            
	            viewName = "jws-accountVerified";
	        }
	        else
	        {
	        	mapDetails.put("message","The link is invalid or broken!");
	            viewName = "error";
	        }

	        return  menuService.getTemplateWithSiteLayout(viewName,mapDetails);
	    }
	

}
