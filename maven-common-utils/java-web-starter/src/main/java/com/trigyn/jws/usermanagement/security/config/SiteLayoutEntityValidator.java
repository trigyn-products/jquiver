package com.trigyn.jws.usermanagement.security.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.trigyn.jws.usermanagement.repository.AuthorizedValidatorDAO;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationDAO;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationRepository;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.SiteLayoutVO;
import com.trigyn.jws.webstarter.utils.JQuiverProperties;
import com.trigyn.jws.webstarter.utils.RedissonQueryCacheManagerUtil;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class SiteLayoutEntityValidator implements EntityValidator {

	private final static Logger					logger							= LoggerFactory
			.getLogger(SiteLayoutEntityValidator.class);

	@Autowired
	private AuthorizedValidatorDAO				authorizedValidatorDAO			= null;

	@Autowired
	private JwsEntityRoleAssociationRepository	entityRoleAssociationRepository	= null;

	@Autowired
	private JQuiverProperties					jQuiverPropeties				= null;

	@Autowired
	private JwsEntityRoleAssociationDAO			entityRoleAssociationDAO		= null;

	@Autowired
	private RedissonQueryCacheManagerUtil		cacheManager					= null;

	@Value("${jquiver.redis.cache.ttl:1800}") // 30 minutes
	private int									defaultTTLMinutes;

	@Override
	public boolean hasAccessToEntity(HttpServletRequest reqObject, List<String> roleNames,
			ProceedingJoinPoint a_joinPoint) {
		logger.debug(
				"Inside SiteLayoutEntityValidator.hasAccessToEntity(requestURI - {}, reqObject - {}, roleNames - {}, a_joinPoint - {})",
				reqObject.getRequestURI(), reqObject, roleNames, a_joinPoint);

		boolean	hasAccess		= false;
		Long	count			= 0L;
		String	moduleUrlStr	= reqObject.getRequestURI().substring(reqObject.getContextPath().length());
		moduleUrlStr = moduleUrlStr.replaceFirst(jQuiverPropeties.getViewPath() + "/", "");
		if (jQuiverPropeties.getViewPath().equals(moduleUrlStr)) {
			moduleUrlStr = moduleUrlStr.replaceFirst(jQuiverPropeties.getViewPath() + "", "");
		}
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

	private SiteLayoutVO hasAccessToSiteLayout(HttpServletRequest reqObject, String moduleUrlStr,
			List<String> roleNames) {
		SiteLayoutVO		siteLayoutVO		= null;
		List<SiteLayoutVO>	siteLayoutVOList	= new ArrayList<>();
		StringBuilder		moduleUrl			= new StringBuilder();
		StringBuilder		fixedModuleUrl		= new StringBuilder();

		if (roleNames != null) {
			// siteLayoutVOList =
			// entityRoleAssociationRepository.hasAccessToSiteLayout(moduleUrlStr,
			// roleNames,
			// Constants.ISACTIVE);
			List<String> sortedRoles = roleNames != null ? new ArrayList<>(roleNames) : Collections.emptyList();
			Collections.sort(sortedRoles);
			String key = "url:" + moduleUrl + "|roles:" + String.join(",", sortedRoles) + "|active:"
					+ Constants.ISACTIVE;

			siteLayoutVOList = cacheManager.fetchJpaDtoList("siteLayoutAccessCache", // cache name
					key, () -> entityRoleAssociationRepository.hasAccessToSiteLayout(moduleUrlStr, roleNames,
							Constants.ISACTIVE),
					defaultTTLMinutes);
		} else {
			String	cacheName	= "siteLayoutCache";
			String	key			= "siteLayout:" + moduleUrl;

			siteLayoutVOList = cacheManager.fetchJpaDtoList(cacheName, key,
					() -> entityRoleAssociationRepository.getSiteLayoutNameByUrl(moduleUrlStr), defaultTTLMinutes);
		}

		List<String> pathVariableList = getPathVariables(reqObject);
		for (String pathVariable : pathVariableList) {
			if (StringUtils.isBlank(moduleUrl) == false) {
				moduleUrl.append("/");
				fixedModuleUrl.append("/");
			}
			moduleUrl.append(pathVariable);
			fixedModuleUrl.append(pathVariable);
			moduleUrl.append("/**");

			for (SiteLayoutVO siteLayoutVODB : siteLayoutVOList) {
				String moduleUrlDB = siteLayoutVODB.getModuleUrl();
				if (StringUtils.isBlank(moduleUrlDB) == false && (moduleUrlDB.equals(moduleUrl.toString())
						|| moduleUrlDB.equals(fixedModuleUrl.toString()))) {
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
	public String getEntityName(HttpServletRequest reqObject, List<String> roleNameList,
			ProceedingJoinPoint a_joinPoint) {
		logger.debug(
				"Inside SiteLayoutEntityValidator.getEntityName(requestURI - {}, reqObject - {}, roleNameList - {}, a_joinPoint - {})",
				reqObject.getRequestURI(), reqObject, roleNameList, a_joinPoint);

		String moduleUrl = reqObject.getRequestURI().substring(reqObject.getContextPath().length());
		moduleUrl = moduleUrl.replaceFirst(jQuiverPropeties.getViewPath() + "/", "");
		if (jQuiverPropeties.getViewPath().equals(moduleUrl)) {
			moduleUrl = moduleUrl.replaceFirst(jQuiverPropeties.getViewPath(), "");
		}
		String requestUrl = moduleUrl;

		if (moduleUrl.indexOf("/") != -1) {
			moduleUrl = moduleUrl.substring(0, moduleUrl.indexOf("/"));
		}

		SiteLayoutVO	siteLayoutVO	= hasAccessToSiteLayout(reqObject, moduleUrl, null);
		String			moduleName		= null;
		if (siteLayoutVO != null) {
			moduleName = siteLayoutVO.getModuleName();
		} else {
			if (reqObject.getQueryString() != null) {
				StringBuilder moduleUrlStr = new StringBuilder(requestUrl).append("?")
						.append(reqObject.getQueryString());
				moduleName = authorizedValidatorDAO.getSiteLayoutModuleNameByUrl(moduleUrlStr.toString());
			}
		}

		return entityRoleAssociationRepository.getEntityNameByEntityAndRoleId(Constants.Modules.ROUTER.getModuleName(),
				moduleName);
	}

	private List<String> getPathVariables(HttpServletRequest httpServletRequest) {
		List<String>	pathVariableList	= new ArrayList<>();
		String			moduleUrl			= httpServletRequest.getRequestURI()
				.substring(httpServletRequest.getContextPath().length());
		moduleUrl = moduleUrl.replaceFirst(jQuiverPropeties.getViewPath() + "/", "");
		if (jQuiverPropeties.getViewPath().equals(moduleUrl)) {
			moduleUrl = moduleUrl.replaceFirst(jQuiverPropeties.getViewPath(), "");
		}

		if (moduleUrl.indexOf("/") != -1) {
			pathVariableList = Stream.of(moduleUrl.split("/")).map(urlElement -> new String(urlElement))
					.collect(Collectors.toList());
		}
		return pathVariableList;
	}

}
