package com.trigyn.jws.webstarter.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.mail.internet.InternetAddress;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dynarest.service.SendMailService;
import com.trigyn.jws.dynarest.vo.Email;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.exception.InvalidLoginException;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.security.config.ApplicationSecurityDetails;

@Description(value = "Service responsible for handling OTP related functionality.")
@Transactional
@Service
public class OtpService implements InitializingBean{

    private final static Logger				logger					=	LogManager.getLogger(OtpService.class);
    
    private LoadingCache<String, Integer> 	otpCache				=	null;

    @Autowired
	private SendMailService					sendMailService			=	null;
    
    @Autowired
	private UserManagementService			userManagementService	=	null;
    
    @Autowired
	private PasswordEncoder					passwordEncoder			=	null;
    
    @Autowired
	private JwsUserRepository				userRepository			=	null;
    
    @Autowired
	private DBTemplatingService				templatingService		=	null;
    
    @Autowired
	private TemplatingUtils					templatingUtils			=	null;
    
    @Autowired
	private PropertyMasterService			propertyMasterService	= 	null;
    
    @Autowired
   	private ApplicationSecurityDetails applicationSecurityDetails = null;
       
    @Autowired
   	private IUserDetailsService					userDetailsService						= null;
    
    
    public OtpService() {
		super();
	}
    
 	public JwsUser saveOtp(Map<String, Object> mapDetails) {  

        // Update OTP and Requested Time to user e-mail into database
    	JwsUser	updatedUserInfo	= null;
        JwsUser	existingUser	= userRepository.findByEmailIgnoreCase((String) mapDetails.get("email"));
        if (existingUser != null) {
        	existingUser.setOneTimePassword(passwordEncoder.encode(String.valueOf(mapDetails.get("oneTimePassword"))));
			existingUser.setOtpRequestedTime(new Date());
			updatedUserInfo	=  userRepository.save(existingUser);
			if (updatedUserInfo != null) {
				updateCatche(existingUser.getEmail(), Integer.valueOf(String.valueOf(mapDetails.get("oneTimePassword"))));
			}
        }
        return updatedUserInfo;
    }
   
    public Boolean validateOTP(String email, Integer otpNumber) throws Exception{
        Boolean isOtpValid 		= false;
        JwsUser	existingUser	= userManagementService.findByEmailIgnoreCase(email);
        if(existingUser == null) {
        	throw new InvalidLoginException("User not found");
        }
        isOtpValid = validateOtpFromDb(email, otpNumber);
        if(!isOtpValid) {
        	Integer cacheOTP = getOPTByKey(email);
            if (cacheOTP!=null && !cacheOTP.equals(otpNumber)) {      			
            	clearOTPFromCache(email);
            }
        }
        return isOtpValid;
    }

	private Boolean validateOtpFromDb(String email, Integer otpNumber) {
		JwsUser	existingUser		= userManagementService.findByEmailIgnoreCase(email);
		Date 	otpSentTime 		= existingUser.getOtpRequestedTime();
		if(null == otpSentTime)
			return false;
		try {
			Integer otpExpiryTime 		= Integer.valueOf(propertyMasterService.findPropertyMasterValue("otp_expiry_time"));
			Date 	currentTime   		= java.util.Calendar.getInstance().getTime();
			long 	maxOtpActiveTime 	= Long.valueOf(otpExpiryTime);
			long 	diffInMinutes 		= TimeUnit.MILLISECONDS.toMinutes(currentTime.getTime() - otpSentTime.getTime());
			otpNumber = getOPTByKey(email);
			if (passwordEncoder.matches(String.valueOf(otpNumber), existingUser.getOneTimePassword()) && diffInMinutes < maxOtpActiveTime)
				return true;
		
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
		}
		
		return false;
	}
    
    public void sendMailForOtpAuthentication(Map<String, Object> mapDetails) throws Exception {
		Map<String, Object> mailDetails = new HashMap<>();
		Email email = new Email();

		email.setInternetAddressToArray(InternetAddress.parse((String) mapDetails.get("email")));
		/*For inserting notification in case of mail failure only on access of Admin*/
		email.setIsAuthenticationEnabled(applicationSecurityDetails.getIsAuthenticationEnabled());
		email.setLoggedInUserRole(userDetailsService.getUserDetails().getRoleIdList());
		
		TemplateVO			subjectTemplateVO	= templatingService.getTemplateByName("otp-mail-subject");
		String				subject				= templatingUtils.processTemplateContents(subjectTemplateVO.getTemplate(),
				subjectTemplateVO.getTemplateName(), mailDetails);
		email.setSubject(subject);

		TemplateVO	templateVO	= templatingService.getTemplateByName("otp-mail");
		String		mailBody	= templatingUtils.processTemplateContents(templateVO.getTemplate(),
				templateVO.getTemplateName(), mailDetails);
		email.setBody(mailBody);
		sendMailService.sendTestMail(email);
	}
    
    public Integer updateCatche(String email, Integer otpNumber) {
        otpCache.put(email, otpNumber);
        return otpNumber;
    }
    
    public Integer generateOTP(String key) {
        Random random = new Random();
        int OTP = 100000 + random.nextInt(900000);
       // otpCache.put(key, OTP);
        return OTP;
    }

    public Integer getOPTByKey(String key) {
        return otpCache.getIfPresent(key);
    }

    public void clearOTPFromCache(String key) {
        otpCache.invalidate(key);
    }

	@Override
	public void afterPropertiesSet() {
		try {
			if (propertyMasterService != null
					&& propertyMasterService.findPropertyMasterValue("otp_expiry_time") != null) {
				String strOtpInMinutes = propertyMasterService.findPropertyMasterValue("otp_expiry_time").toString();
				if (!StringUtils.isBlank(strOtpInMinutes)) {
					Integer otpExpiryTime = Integer.parseInt(strOtpInMinutes);
					otpCache = CacheBuilder.newBuilder().expireAfterWrite(otpExpiryTime, TimeUnit.MINUTES)
							.build(new CacheLoader<String, Integer>() {
								@Override
								public Integer load(String s) throws Exception {
									return 0;
								}
							});
				}

			}
		} catch (NumberFormatException a_exception) {
			logger.error("Error ", a_exception);
		} catch (Exception e) {
			logger.error("Error ", e);
		}

	}
	
	public void sendMailForOtp(Map<String, Object> mapDetails) throws Exception {
		Email email 					= new Email();
		email.setInternetAddressToArray(InternetAddress.parse((String) mapDetails.get("email")));
		TemplateVO	subjectTemplate		= templatingService.getTemplateByName("otp-mail-subject");
		String		subject				= templatingUtils.processTemplateContents(subjectTemplate.getTemplate(),
				subjectTemplate.getTemplateName(), mapDetails);
		email.setSubject(subject);

		TemplateVO	templateVO			= templatingService.getTemplateByName("otp-mail");
		String		mailBody			= templatingUtils.processTemplateContents(templateVO.getTemplate(),
				templateVO.getTemplateName(), mapDetails);
		email.setBody(mailBody);
		sendMailService.sendTestMail(email);
	}
}
