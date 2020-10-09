package com.trigyn.jws.usermanagement.controller;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.trigyn.jws.usermanagement.entities.JwsResetPasswordToken;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.entities.JwsUserRoleAssociation;
import com.trigyn.jws.usermanagement.repository.JwsResetPasswordTokenRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.utils.Constants;

@RestController
@RequestMapping("/cf")
public class JwsResetPasswordController {

	@Autowired 
	private MenuService  menuService = null;
	
	@Autowired
	private JwsUserRepository userRepository =  null;
	
//	@Autowired
//	private JavaMailSender javaMailSender = null;
	
	@Autowired
	private JwsResetPasswordTokenRepository resetPasswordTokenRepository =  null;
	
	@Autowired
	private PasswordEncoder passwordEncoder = null;
	
	@Autowired
	private JwsUserRoleAssociationRepository   userRoleAssociationRepository = null;
	

	
	@GetMapping(value="/resetPasswordPage")
	@ResponseBody
    public String displayResetPasswordPage(ModelAndView modelAndView) throws Exception {
		Map<String, Object> mapDetails = new HashMap<>();
		mapDetails.put("loggedInUser", Boolean.FALSE);
		return menuService.getTemplateWithSiteLayout("password-reset-mail",mapDetails);
	}
	
	@PostMapping(value = "/sendResetPasswordMail")
	@ResponseBody
    public String sendResetPasswordMail(HttpServletRequest request,HttpSession session) throws Exception {
		
		String eMail=request.getParameter("email");
	
		Map<String, Object> mapDetails =new HashMap<>();
		mapDetails.put("loggedInUser", Boolean.FALSE);
		String viewName=null;
		JwsUser existingUser = userRepository.findByEmailIgnoreCase(eMail);
		
		if(existingUser !=null) {
			  JwsResetPasswordToken resetPassword=new JwsResetPasswordToken();
			  String tokenId=UUID.randomUUID().toString();
			  resetPassword.setTokenId(tokenId);
			  resetPassword.setPasswordResetTime(Calendar.getInstance());
			  resetPassword.setUserId(existingUser.getUserId());
			  resetPassword.setResetPasswordUrl("http://localhost:8080/resetPassword?token="+tokenId);
			  resetPassword.setIsResetUrlExpired(Boolean.FALSE);
			  resetPasswordTokenRepository.save(resetPassword);
			  
			  
			SimpleMailMessage mailMessage = new SimpleMailMessage();
      		mailMessage.setTo(eMail);
      		mailMessage.setSubject("[TSMS] Please reset your password");
      		mailMessage.setFrom("admin@gmail.com");
      		mailMessage.setText("To reset your TSMS user account password, please click here : "
      				+"http://localhost:8080/resetPassword?token="+tokenId);
      		
      		
      		CompletableFuture.runAsync( new Runnable() {
      			@Override
      			public void run() {
//      				javaMailSender.send(mailMessage);
      			}
      			});
      		
      		mapDetails.put("successResetPasswordMsg", "Check your email for a link to reset your password. If it doesn’t appear within a few minutes, check your spam folder.");
			viewName ="password-reset-mail-success";
		}else {
			mapDetails.put("nonRegisteredUser", "Enter registered user email id to send reset password mail");
			viewName ="password-reset-mail";
		}
		return  menuService.getTemplateWithSiteLayout(viewName,mapDetails);
    }
	
	@GetMapping(value = "/resetPassword")
	@ResponseBody
    public String resetPasswordByURL( @RequestParam("token") String tokenId) throws Exception {
		
		Map<String, Object> mapDetails =new HashMap<>();
		mapDetails.put("loggedInUser", Boolean.FALSE);
		String viewName=null;
		JwsResetPasswordToken tokenDetails = resetPasswordTokenRepository.findByTokenId(tokenId);
		Boolean isInvalidLink =false;
		 if(tokenDetails != null && tokenDetails.getPasswordResetTime() !=null && tokenDetails.getIsResetUrlExpired() != Boolean.TRUE){
				 long    linkSendTimestamp    = tokenDetails.getPasswordResetTime().getTimeInMillis();
				 long    maxLinkActiveTime    = TimeUnit.MINUTES.toMillis(20);
				 Timestamp    currentTimestamp    = new Timestamp(System.currentTimeMillis());
				 long        milliseconds        = currentTimestamp.getTime() - linkSendTimestamp;
				 
					 if(milliseconds > maxLinkActiveTime) {
						 isInvalidLink=Boolean.TRUE;
					 }else {
						 
						 JwsUser userDetails = userRepository.findByUserId(tokenDetails.getUserId());
			                if(userDetails !=null && userDetails.getEmail() !=null) {
			                	mapDetails.put("resetEmailId", userDetails.getEmail());
			                }
			                mapDetails.put("token", tokenId);
				            viewName = "password-reset-page";
					     }
	        } else{
	        	isInvalidLink=Boolean.TRUE;
	        }
		  
		 if(isInvalidLink) {
			 mapDetails.put("inValidLink","The link is expired/invalid/broken.Please enter mail id again to get reset password link!");
        	 viewName = "password-reset-mail";
		 }

	        return  menuService.getTemplateWithSiteLayout(viewName,mapDetails);
		
	}
	
	
	@PostMapping(value = "/createPassword")
	@ResponseBody
    public String createPassword( HttpServletRequest request,HttpSession session) throws Exception {
		Map<String, Object> mapDetails =new HashMap<>();
		mapDetails.put("loggedInUser", Boolean.FALSE);
		String viewName=null;
		
		String password=request.getParameter("password");
		String confirmpassword=request.getParameter("confirmpassword");
		String resetEmailId=request.getParameter("resetEmailId");
		String tokenId=request.getParameter("token");
		
		if(password == null || (password !=null && password.trim().isEmpty()) || confirmpassword == null ||(confirmpassword !=null && confirmpassword.trim().isEmpty())) {
			mapDetails.put("nonValidPassword","Enter valid password and confirm password");
			mapDetails.put("token", tokenId);
			 viewName = "password-reset-page";
		}else if(!password.equals(confirmpassword)){
			mapDetails.put("nonValidPassword","Enter same password and confirm password");
			mapDetails.put("token", tokenId);
			viewName = "password-reset-page";
		}else if(password.equals(confirmpassword) ){
			if(validatePassword(password)) {
				String encodedPassword=passwordEncoder.encode(password);
				JwsUser user = userRepository.findByEmailIgnoreCase(resetEmailId);
	            user.setIsActive(Constants.ISACTIVE);
	            user.setPassword(encodedPassword);
	            userRepository.save(user);
	            
        		JwsUserRoleAssociation adminRoleAssociation = new JwsUserRoleAssociation();
        		adminRoleAssociation.setUserId(user.getUserId());
        		adminRoleAssociation.setRoleId(Constants.ADMIN_ROLE);
        		userRoleAssociationRepository.save(adminRoleAssociation);
        		
//        		resetPasswordTokenRepository.updateUrlExpired(Boolean.TRUE, user.getUserId(),tokenId);
	            
	            mapDetails.put("resetPasswordSuccess","Congratulations.You have successfully changed your password.");
				viewName = "login";
			}else {
			 viewName = "password-reset-page";
			 mapDetails.put("token", tokenId);
       		 mapDetails.put("nonValidPassword","Password must contain atleast 6 characters including UPPER/lowercase/Special charcters and numbers ");
			}
			
		}
		mapDetails.put("resetEmailId", resetEmailId);
		return menuService.getTemplateWithSiteLayout(viewName,mapDetails);
		
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
}
