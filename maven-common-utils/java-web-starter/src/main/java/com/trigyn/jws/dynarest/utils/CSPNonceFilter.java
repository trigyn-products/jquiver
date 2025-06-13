package com.trigyn.jws.dynarest.utils;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.trigyn.jws.dbutils.entities.CspConfig;
import com.trigyn.jws.dbutils.repository.IModuleListingRepository;
import com.trigyn.jws.dbutils.spi.PropertyMasterDetails;
import com.trigyn.jws.webstarter.utils.JQuiverProperties;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Rashmi Shirke
 * @since  17-Apr-2025
 */

@Component
public class CSPNonceFilter extends OncePerRequestFilter {

	public static final String			NONCE_ATTR					= "cspNonce";

	@Autowired
	private PropertyMasterDetails		propertyMasterDetails		= null;

	@Autowired
	private JQuiverProperties			jQuiverPropeties			= null;

	private List<String>				excludedUrls				= new ArrayList<>();

	@Autowired
	private IModuleListingRepository	iModuleListingRepository	= null;

	protected String generateNonce() {
		byte[] nonceBytes = new byte[16];
		new SecureRandom().nextBytes(nonceBytes);
		return Base64.getEncoder().encodeToString(nonceBytes);
	}

	protected boolean isExcluded(String path) {
		if (path == null)
			return false;
		return path.contains(jQuiverPropeties.getViewPath() + "/") && excludedUrls.stream().anyMatch(path::endsWith);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String		path		= request.getRequestURI();
		CspConfig	cspConfig	= propertyMasterDetails.getCspConfig();
		excludedUrls = propertyMasterDetails.getExclusionList();
		
		String[] url = path.split("/");
		
		Integer isFormIO = iModuleListingRepository.isFormIO(url[url.length-1]);
		if (null != cspConfig && cspConfig.isCSPEnable() && !isExcluded(path) && isFormIO == 0) {
			// Generate a random nonce
			// String nonce = UUID.randomUUID().toString().replace("-", "");
			String	nonce		= generateNonce();
			// Replace placeholder with actual nonce
			String	headerValue	= cspConfig.getCspHeader().replace("%nonce%", nonce).replace("%contextPath%",
					request.getContextPath());
			logger.debug("CSP Header set to: {} " + headerValue);
			// Set the header
			response.setHeader("Content-Security-Policy", headerValue);

			request.setAttribute(NONCE_ATTR, nonce);
		}
		filterChain.doFilter(request, response);
	}
}
