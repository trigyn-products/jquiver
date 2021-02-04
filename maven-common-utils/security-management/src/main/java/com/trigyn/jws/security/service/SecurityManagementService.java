package com.trigyn.jws.security.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.security.dao.DDOSDao;
import com.trigyn.jws.security.dao.DDOSDetailsRepository;
import com.trigyn.jws.security.dao.SecurityPropertiesRepository;
import com.trigyn.jws.security.dao.SecurityTypeRepository;
import com.trigyn.jws.security.entities.DDOSDetails;
import com.trigyn.jws.security.entities.SecurityProperties;
import com.trigyn.jws.security.entities.SecurityType;
import com.trigyn.jws.security.utility.Constant;
import com.trigyn.jws.templating.service.MenuService;

@Service
@Transactional
public class SecurityManagementService {

	@Autowired
	private MenuService						menuService				= null;

	@Autowired
	private PropertyMasterDetails			propertyMasterDetails	= null;

	// @Autowired
	// private PropertyMasterRepository propertyMasterRepository = null;

	@Autowired
	private SecurityTypeRepository			securityTypeRepository	= null;

	@Autowired
	private SecurityPropertiesRepository	propertiesRepository	= null;

	@Autowired
	private DDOSDetailsRepository			ddosDetailsRepository	= null;

	@Autowired
	private DDOSDao							ddosDao					= null;

	public String securityManagement() throws Exception {
		Map<String, Object> mapDetails = new HashMap<>();
		return menuService.getTemplateWithSiteLayout("jws-securtity-configuration", mapDetails);
	}

	public String loadDDOSConfiguration() throws Exception {
		Map<String, Object>	mapDetails			= new HashMap<>();
		SecurityType		securityType		= securityTypeRepository.findBySecurityName("DDOS");
		SecurityProperties	securityProperties	= propertiesRepository.findBySecurityPropertyName("In Memory");
		Integer				inMemory			= Integer.parseInt(securityProperties.getSecurityPropertyValue());
		Integer				isDDOSEnabled		= securityType.getIsActive();
		List<String>		blockedIPAddrList	= ddosDetailsRepository.getAllBlockedIPAddr();
		mapDetails.put("isDDOSEnabled", isDDOSEnabled);
		mapDetails.put("inMemory", inMemory);
		mapDetails.put("ddosPageCount", propertyMasterDetails.getSystemPropertyValue("ddos-page-count"));
		mapDetails.put("ddosSiteCount", propertyMasterDetails.getSystemPropertyValue("ddos-site-count"));
		mapDetails.put("ddosExcludedExtensions",
				propertyMasterDetails.getSystemPropertyValue("ddos-excluded-extensions"));
		mapDetails.put("ddosRefreshInterval", propertyMasterDetails.getSystemPropertyValue("ddos-refresh-interval"));
		mapDetails.put("blockedIpAddress", blockedIPAddrList);
		return menuService.getTemplateWithoutLayout("distributed-denial-of-service-configuration", mapDetails);
	}

	@Transactional(readOnly = false)
	public boolean saveDDOSDetails(HttpServletRequest a_httpServletRequest, Map<String, Object> dAOparameters,
			UserDetailsVO userDetails) {
		// Integer isDDOSEnabled =
		// Integer.parseInt(a_httpServletRequest.getParameter("isDDOSEnabled"));
		// SecurityType securityType =
		// securityTypeRepository.findBySecurityName("DDOS");
		// securityType.setIsActives(isDDOSEnabled);
		// managementRepository.save(securityType);
		propertyMasterDetails.resetPropertyMasterDetails();
		return Boolean.TRUE;
	}

	@Transactional(readOnly = false)
	public void saveBlockedIPDetailsInDB(String ipAddr, HttpSession a_HttpSession) throws Exception {
		SecurityProperties	securityProperties	= propertiesRepository.findBySecurityPropertyName("In Memory");
		Integer				inMemory			= Integer.parseInt(securityProperties.getSecurityPropertyValue());
		if (inMemory != null && inMemory.equals(Constant.IN_DATABASE)) {
			Gson		gson				= new Gson();
			Date		date				= new Date();
			String		sessionDetailsJson	= gson.toJson(a_HttpSession);
			DDOSDetails	ddosDetails			= new DDOSDetails();
			ddosDetails.setIpAddress(ipAddr);
			ddosDetails.setSessionDetails(sessionDetailsJson);
			ddosDetails.setIsBlocked(Constant.IS_BLOCKED);
			ddosDetails.setLastAttackedDate(date);
			ddosDao.saveDDOSDetails(ddosDetails);
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.menuService			= applicationContext.getBean(MenuService.class);
		this.propertyMasterDetails	= applicationContext.getBean(PropertyMasterDetails.class);
		// this.propertyMasterRepository =
		// applicationContext.getBean(PropertyMasterRepository.class);
		this.securityTypeRepository	= applicationContext.getBean(SecurityTypeRepository.class);
		this.propertiesRepository	= applicationContext.getBean(SecurityPropertiesRepository.class);
		this.ddosDao				= applicationContext.getBean(DDOSDao.class);
	}

}
