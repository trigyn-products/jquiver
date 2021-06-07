package com.trigyn.jws.security.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;
import com.trigyn.jws.security.dao.SecurityTypeRepository;
import com.trigyn.jws.security.entities.SecurityType;
import com.trigyn.jws.security.service.SecurityManagementService;

@WebFilter(urlPatterns = { "/*" })
@Component
public class SecurityFilter implements Filter {

	@Autowired
	private PropertyMasterDetails				propertyMasterDetails		= null;

	@Autowired
	private SecurityTypeRepository				securityTypeRepository		= null;

	@Autowired
	private SecurityManagementService			securityManagementService	= null;

	private Map<String, Map<String, Integer>>	ipDetailsMap				= new HashMap<>();
	private Map<String, String>					blockedIPAddrMap			= new HashMap<>();
	private static Set<String>					excluded;
	private static Semaphore					semaphore					= new Semaphore(1);

	private final ScheduledThreadPoolExecutor	scheduler					= (ScheduledThreadPoolExecutor) Executors
			.newScheduledThreadPool(1);

	@Override
	public void init(FilterConfig paramFilterConfig) throws ServletException {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		
		if(securityTypeRepository==null){
            ServletContext servletContext = paramFilterConfig.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            securityTypeRepository = webApplicationContext.getBean(SecurityTypeRepository.class);
        }
		
		SecurityType	securityType	= securityTypeRepository.findBySecurityName("DDOS");
		Integer			isDDOSEnabled	= securityType.getIsActive();
		if (isDDOSEnabled != null && isDDOSEnabled.equals(Constant.IS_DDOS_ENABLED)) {
			getExcludedExtensions();
			Integer dosRefreshInterval = propertyMasterDetails.getSystemPropertyValue("ddos-refresh-interval") == null ? 60
					: Integer.parseInt(propertyMasterDetails.getSystemPropertyValue("ddos-refresh-interval"));
			resetSessionDetailsMap();
			scheduler.scheduleWithFixedDelay(new Runnable() {

				@Override
				public void run() {
					resetSessionDetailsMap();
				}

			}, dosRefreshInterval, dosRefreshInterval, TimeUnit.SECONDS);
		}
	}

	private void getExcludedExtensions() {
		String excludedString = propertyMasterDetails.getSystemPropertyValue("ddos-excluded-extensions");
		if (excludedString != null) {
			excluded = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(excludedString.split(", ", 0))));
		} else {
			excluded = Collections.<String>emptySet();
		}
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {

		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		SecurityType	securityType	= securityTypeRepository.findBySecurityName("DDOS");
		Integer			isDDOSEnabled	= securityType.getIsActive();
		if (isDDOSEnabled != null && isDDOSEnabled.equals(Constant.IS_DDOS_ENABLED)) {
			HttpServletRequest	httpServletRequest	= (HttpServletRequest) servletRequest;
			HttpServletResponse	httpServletResponse	= (HttpServletResponse) servletResponse;
			HttpSession			httpSession			= httpServletRequest.getSession(false);
			ObjectMapper		objMapper			= new ObjectMapper();
			String				blockedIpAddStr		= propertyMasterDetails.getSystemPropertyValue("blocked-ip-address");
			List<String>		blockedIpAddList	= new ArrayList<>();
			if (StringUtils.isBlank(blockedIpAddStr) == false) {
				blockedIpAddList = objMapper.readValue(blockedIpAddStr, List.class);
			}
			String ipAddr = getUserIpAddress(httpServletRequest);

			if (isExcluded(httpServletRequest) == false && blockedIPAddrMap.containsKey(ipAddr) == false
					&& (CollectionUtils.isEmpty(blockedIpAddList) == true || blockedIpAddList.contains(ipAddr) == false)) {
				if (ipDetailsMap.containsKey(ipAddr)) {
					try {
						// System.out.println("Inside method : " + semaphore.availablePermits());
						semaphore.acquire();
						updateDDOSDetails(httpServletRequest, httpSession, ipAddr);
					} catch (Exception a_exc) {
						a_exc.printStackTrace();
					} finally {
						semaphore.release();
					}
					// System.out.println("Inside method : " + semaphore.availablePermits());
				} else {
					Map<String, Integer>	ipInfo		= new HashMap<>();
					String					resourceUrl	= httpServletRequest.getRequestURI();
					ipInfo.put(resourceUrl, 1);
					ipInfo.put("siteCount", 1);
					ipDetailsMap.put(ipAddr, ipInfo);
				}
				chain.doFilter(servletRequest, servletResponse);
			} else if (isExcluded(httpServletRequest) == false) {
				httpServletResponse.sendError(HttpStatus.SERVICE_UNAVAILABLE.value(), "Service unavailable");
			} else {
				chain.doFilter(servletRequest, servletResponse);
			}
		} else {
			chain.doFilter(servletRequest, servletResponse);
		}
	}

	private void updateDDOSDetails(HttpServletRequest httpServletRequest, HttpSession httpSession, String ipAddr) throws Exception {
		Integer					ddosPageCount	= propertyMasterDetails.getSystemPropertyValue("ddos-page-count") == null ? 10
				: Integer.parseInt(propertyMasterDetails.getSystemPropertyValue("ddos-page-count"));
		Integer					ddosSiteCount	= propertyMasterDetails.getSystemPropertyValue("ddos-site-count") == null ? 30
				: Integer.parseInt(propertyMasterDetails.getSystemPropertyValue("ddos-site-count"));

		Map<String, Integer>	ipInfo			= ipDetailsMap.get(ipAddr);
		String					resourceUrl		= httpServletRequest.getRequestURI();
		Integer					pageCount		= ipInfo.get(resourceUrl);
		Integer					siteCount		= ipInfo.get("siteCount");
		if (ddosPageCount != null && pageCount != null && pageCount.equals(ddosPageCount - 1)) {
			blockedIPAddrMap.put(ipAddr, "true");
			securityManagementService.saveBlockedIPDetailsInDB(ipAddr, httpSession);
			// System.out.println("Blocked Time: " + new Date());
		} else {
			pageCount = pageCount == null ? 1 : (pageCount + 1);
			ipInfo.put(resourceUrl, pageCount);
			ipDetailsMap.put(ipAddr, ipInfo);
		}
		if (ddosSiteCount != null && siteCount != null && siteCount.equals(ddosSiteCount - 1)) {
			blockedIPAddrMap.put(ipAddr, "true");
			securityManagementService.saveBlockedIPDetailsInDB(ipAddr, httpSession);
			// System.out.println("Blocked Time: " + new Date());
		} else {
			siteCount = siteCount == null ? 1 : (siteCount + 1);
			ipInfo.put("siteCount", siteCount);
			ipDetailsMap.put(ipAddr, ipInfo);
		}
	}

	@Override
	public void destroy() {

	}

	public void resetSessionDetailsMap() {
		// System.out.println("Inside thread : " + semaphore.availablePermits());
		try {
			semaphore.acquire();
			blockedIPAddrMap	= new HashMap<>();
			ipDetailsMap		= new HashMap<>();
			// System.out.println("Released Time: " + new Date());
		} catch (InterruptedException a_ie) {
			a_ie.printStackTrace();
		} finally {
			semaphore.release();
		}
		// System.out.println("Inside thread : " + semaphore.availablePermits());
	}

	public static String getUserIpAddress(HttpServletRequest a_httpServletRequest) {
		String ip = a_httpServletRequest.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = a_httpServletRequest.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = a_httpServletRequest.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = a_httpServletRequest.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = a_httpServletRequest.getHeader("HTTP_X_FORWARDED");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = a_httpServletRequest.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = a_httpServletRequest.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = a_httpServletRequest.getHeader("HTTP_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = a_httpServletRequest.getHeader("HTTP_FORWARDED");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = a_httpServletRequest.getHeader("HTTP_VIA");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = a_httpServletRequest.getHeader("REMOTE_ADDR");
		}
		if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown")) {
			ip = a_httpServletRequest.getRemoteAddr();
		}
		return ip;
	}

	private boolean isExcluded(HttpServletRequest request) {
		String path = request.getRequestURI();
		if (CollectionUtils.isEmpty(excluded)) {
			getExcludedExtensions();
		}
		if (path.contains("/webjars")) {
			return Boolean.TRUE;
		}
		String extension = path.substring(path.lastIndexOf('.') + 1).toLowerCase();

		return excluded.contains(extension);
	}

}
