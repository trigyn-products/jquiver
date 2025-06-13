package com.trigyn.jws.webstarter.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dbutils.vo.UserDetailsVO;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class DynaServiceLayer {

	private ApplicationContext applicationContext = null;

	public ResponseEntity<String> getEmployeeDetails(HttpServletRequest a_httpServletRequest,
			Map<String, Object> dAOparameters, UserDetailsVO userDetails) {
		Map<String, Object> response = new HashMap<>();
		response.put("response", dAOparameters.get("employees"));
		System.out.println(applicationContext.getBean("filesStorageServiceImpl"));
		return new ResponseEntity<String>("Error FORBIDDEN ", HttpStatus.FORBIDDEN);
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}
