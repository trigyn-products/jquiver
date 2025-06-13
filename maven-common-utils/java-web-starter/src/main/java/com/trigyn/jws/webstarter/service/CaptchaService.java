package com.trigyn.jws.webstarter.service;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.webstarter.dao.CaptchaDAO;
import com.trigyn.jws.webstarter.dao.ICaptchRepository;
import com.trigyn.jws.webstarter.vo.CaptchaDetails;

@Service
@Transactional
public class CaptchaService {
	
	private static final Logger logger = LoggerFactory.getLogger(CaptchaService.class);


	@Autowired
	private ICaptchRepository iCaptchRepository 				= null;
	
	@Autowired
	private CaptchaDAO captchaDAO				= null;
	
	@Autowired
	private PropertyMasterService				propertyMasterService			= null;
	
	public String saveCaptchDetails(CaptchaDetails captcha)
	{
		iCaptchRepository.save(captcha);
		return captcha.getRequestId();
		
	}
	
	public String updateCaptchDetails(CaptchaDetails captcha)
	{
		iCaptchRepository.save(captcha);
		return captcha.getRequestId();
		
	}
	
	public CaptchaDetails fetchCaptchDetailsById(String requestId)
	{
		CaptchaDetails captcha = null;
		if (StringUtils.isBlank(requestId) == false) {
			captcha = iCaptchRepository.findById(requestId).orElse(null);
		    if (null != captcha && null!=captcha.getRequestId()) {
			   return captcha;
			}
		}
		return null;
		
	}
	
	public void deleteExpiredCaptcha() {
		//logger.debug("Inside CaptchaService.deleteExpiredCaptcha()",null);
		Integer	captchExpiry	=0;
		try {
				captchExpiry	= Integer.valueOf(propertyMasterService.findPropertyMasterValue("captchaExpiry"));
				captchaDAO.deleteExpiredCaptcha(captchExpiry);
		} catch (Exception e) {
			logger.error("Error while getting deleting Expired Captcha,  ", e);
		}
		
	}

	public void deleteCaptcha(String captchaRequest_Id) {
		iCaptchRepository.deleteById(captchaRequest_Id);
		
	}
	
}
