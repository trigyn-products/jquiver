package com.trigyn.jws.dbutils.spi;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;

public class DefaultUserDetailsServiceImpl implements IUserDetailsService {

	private static final Logger		logger					= LogManager.getLogger(DefaultUserDetailsServiceImpl.class);

	@Autowired
	private PropertyMasterService	propertyMasterService	= null;

	@Override
	public UserDetailsVO getUserDetails() {
		return new UserDetailsVO("aar.dev@trigyn.com", "aar-dev", Arrays.asList("admin"), "aar-dev");
	}

	@Override
	public List<String> getUsersRoleHavingAccessToJWS() {
		List<String> accessRoles = Arrays.asList("admin");
		try {
			String roles = propertyMasterService.findPropertyMasterValue("acl-jws");
			if (roles == null) {
				return accessRoles;
			}
			return Arrays.asList(roles.split(","));
		} catch (Exception a_expection) {
			logger.error("Error while getting default access to jws, setting the value to admin, ", a_expection);
			return accessRoles;
		}

	}

}
