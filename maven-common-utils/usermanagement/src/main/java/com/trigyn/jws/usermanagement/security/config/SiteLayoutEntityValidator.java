package com.trigyn.jws.usermanagement.security.config;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.trigyn.jws.usermanagement.repository.AuthorizedValidatorDAO;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationRepository;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.SiteLayoutVO;

@Component
public class SiteLayoutEntityValidator implements EntityValidator {

	private final static Logger					logger							= LogManager.getLogger(SiteLayoutEntityValidator.class);

	@Autowired
	private AuthorizedValidatorDAO				authorizedValidatorDAO			= null;

	@Autowired
	private JwsEntityRoleAssociationRepository	entityRoleAssociationRepository	= null;

	@Override
	public boolean hasAccessToEntity(HttpServletRequest reqObject, List<String> roleNames, ProceedingJoinPoint a_joinPoint) {
		logger.debug(
				"Inside SiteLayoutEntityValidator.hasAccessToEntity(requestURI - {}, reqObject - {}, roleNames - {}, a_joinPoint - {})",
				reqObject.getRequestURI(), reqObject, roleNames, a_joinPoint);

		boolean	hasAccess		= false;
		Long	count			= 0L;
		String	moduleUrlStr	= reqObject.getRequestURI().substring(reqObject.getContextPath().length());
		moduleUrlStr = moduleUrlStr.replaceFirst("/view/", "");
		String requestUrl = moduleUrlStr;

		if (moduleUrlStr.indexOf("/") != -1) {
			moduleUrlStr = moduleUrlStr.substring(0, moduleUrlStr.indexOf("/"));
		}

		SiteLayoutVO siteLayoutVO = hasAccessToSiteLayout(reqObject, moduleUrlStr, roleNames);
		if (siteLayoutVO != null && siteLayoutVO.getRoleCount() != null) {
			count = siteLayoutVO.getRoleCount();
		} else {
			if (reqObject.getQueryString() != null) {
				StringBuilder moduleUrl = new StringBuilder(requestUrl).append("?").append(reqObject.getQueryString());
				count = authorizedValidatorDAO.hasAccessToSiteLayout(moduleUrl.toString(), roleNames);
			}
		}
		if (count > 0) {
			hasAccess = true;
		}
		return hasAccess;
	}

	private SiteLayoutVO hasAccessToSiteLayout(HttpServletRequest reqObject, String moduleUrlStr, List<String> roleNames) {
		SiteLayoutVO		siteLayoutVO		= null;
		List<SiteLayoutVO>	siteLayoutVOList	= entityRoleAssociationRepository.hasAccessToSiteLayout(moduleUrlStr, roleNames,
				Constants.ISACTIVE);

		StringBuilder		moduleUrl			= new StringBuilder();
		List<String>		pathVariableList	= getPathVariables(reqObject);
		for (String pathVariable : pathVariableList) {
			if (StringUtils.isBlank(moduleUrl) == false) {
				moduleUrl.append("/");
			}
			moduleUrl.append(pathVariable);
			moduleUrl.append("/**");
			for (SiteLayoutVO siteLayoutVODB : siteLayoutVOList) {
				String moduleUrlDB = siteLayoutVODB.getModuleUrl();
				if (StringUtils.isBlank(moduleUrlDB) == false && moduleUrlDB.equals(moduleUrl.toString())) {
					return siteLayoutVODB;
				}
			}
			moduleUrl.delete(moduleUrl.indexOf("/**"), moduleUrl.length());
		}
		if (siteLayoutVO == null && CollectionUtils.isEmpty(pathVariableList) == true
				&& CollectionUtils.isEmpty(siteLayoutVOList) == false) {
			siteLayoutVO = siteLayoutVOList.get(0);
		}
		return siteLayoutVO;
	}

	@Override
	public String getEntityName(HttpServletRequest reqObject, List<String> roleNameList, ProceedingJoinPoint a_joinPoint) {
		logger.debug("Inside SiteLayoutEntityValidator.getEntityName(requestURI - {}, reqObject - {}, roleNameList - {}, a_joinPoint - {})",
				reqObject.getRequestURI(), reqObject, roleNameList, a_joinPoint);

		String moduleUrl = reqObject.getRequestURI().substring(reqObject.getContextPath().length());
		moduleUrl = moduleUrl.replaceFirst("/view/", "");
		String requestUrl = moduleUrl;

		if (moduleUrl.indexOf("/") != -1) {
			moduleUrl = moduleUrl.substring(0, moduleUrl.indexOf("/"));
		}

		SiteLayoutVO	siteLayoutVO	= getSiteLayoutName(reqObject, moduleUrl);
		String			moduleName		= null;
		if (siteLayoutVO != null) {
			moduleName = siteLayoutVO.getModuleName();
		} else {
			if (reqObject.getQueryString() != null) {
				StringBuilder moduleUrlStr = new StringBuilder(requestUrl).append("?").append(reqObject.getQueryString());
				moduleName = authorizedValidatorDAO.getSiteLayoutModuleNameByUrl(moduleUrlStr.toString());
			}
		}

		return entityRoleAssociationRepository.getEntityNameByEntityAndRoleId(Constants.Modules.SITELAYOUT.getModuleName(), moduleName);
	}

	private SiteLayoutVO getSiteLayoutName(HttpServletRequest reqObject, String moduleUrlStr) {
		SiteLayoutVO		siteLayoutVO		= null;
		List<SiteLayoutVO>	siteLayoutVOList	= entityRoleAssociationRepository.getSiteLayoutNameByUrl(moduleUrlStr);

		StringBuilder		moduleUrl			= new StringBuilder();
		List<String>		pathVariableList	= getPathVariables(reqObject);
		for (String pathVariable : pathVariableList) {
			if (StringUtils.isBlank(moduleUrl) == false) {
				moduleUrl.append("/");
			}
			moduleUrl.append(pathVariable);
			moduleUrl.append("/**");
			for (SiteLayoutVO siteLayoutVODB : siteLayoutVOList) {
				String moduleUrlDB = siteLayoutVODB.getModuleUrl();
				if (StringUtils.isBlank(moduleUrlDB) == false && moduleUrlDB.equals(moduleUrl.toString())) {
					return siteLayoutVODB;
				}
			}
			moduleUrl.delete(moduleUrl.indexOf("/**"), moduleUrl.length());
		}
		if (siteLayoutVO == null && CollectionUtils.isEmpty(pathVariableList) == true
				&& CollectionUtils.isEmpty(siteLayoutVOList) == false) {
			siteLayoutVO = siteLayoutVOList.get(0);
		}
		return siteLayoutVO;
	}

	private List<String> getPathVariables(HttpServletRequest httpServletRequest) {
		List<String>	pathVariableList	= new ArrayList<>();
		String			moduleUrl			= httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length());
		moduleUrl = moduleUrl.replaceFirst("/view/", "");

		if (moduleUrl.indexOf("/") != -1) {
			pathVariableList = Stream.of(moduleUrl.split("/")).map(urlElement -> new String(urlElement)).collect(Collectors.toList());
		}
		return pathVariableList;
	}

}
