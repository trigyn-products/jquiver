package com.trigyn.jws.usermanagement.security.config;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;

public interface EntityValidator {

	boolean hasAccessToEntity(HttpServletRequest reqObject, List<String> roleNames,
			ProceedingJoinPoint a_joinPoint);

	String getEntityName(HttpServletRequest reqObject, List<String> roleNameList, ProceedingJoinPoint a_joinPoint);
}
