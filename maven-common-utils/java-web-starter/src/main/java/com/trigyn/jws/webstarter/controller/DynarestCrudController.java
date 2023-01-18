package com.trigyn.jws.webstarter.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.cipher.utils.CipherUtilFactory;
import com.trigyn.jws.dbutils.cipher.utils.RSAKeyPairGeneratorUtil;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.webstarter.service.DynarestCrudService;

@RestController
@RequestMapping("/cf")
@PreAuthorize("hasPermission('module','REST API')")
public class DynarestCrudController {

	private final static Logger	logger				= LogManager.getLogger(DynarestCrudController.class);

	@Autowired
	private PropertyMasterDAO	propertyMasterDAO	= null;

	@Autowired
	private DynarestCrudService	dynarestCrudService	= null;

	@Autowired
	private MenuService			menuService			= null;

	@GetMapping(value = "/dynl", produces = MediaType.TEXT_HTML_VALUE)
	public String loadDynarestListing(HttpServletRequest httpServletRequest) throws Exception {
		Map<String, Object>	modelMap	= new HashMap<>();
		String				environment	= propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
		modelMap.put("environment", environment);
		String			uri			= httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length());
		String			url			= httpServletRequest.getRequestURL().toString();
		StringBuilder	urlPrefix	= new StringBuilder();
		url = url.replace(uri, "");
		urlPrefix.append(url).append("/api/");
		modelMap.put("urlPrefix", urlPrefix);
		return menuService.getTemplateWithSiteLayout("dynarest-details-listing", modelMap);
	}

	@PostMapping(value = "/sdq", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String saveDynamicRestSaveQueries(@RequestBody MultiValueMap<String, String> formData) throws Exception {
		dynarestCrudService.deleteDAOQueries(formData);
		return dynarestCrudService.saveDAOQueries(formData);
	}

	@GetMapping(value = "/grsakp")
	@ResponseBody
	public Map<String, String> getRSAKeyPair(HttpServletRequest httpServletRequest) throws Exception {
		RSAKeyPairGeneratorUtil keyPairGenerator = new RSAKeyPairGeneratorUtil();
        keyPairGenerator.writeToFile("RSA/publicKey", keyPairGenerator.getPublicKey().getEncoded());
        keyPairGenerator.writeToFile("RSA/privateKey", keyPairGenerator.getPrivateKey().getEncoded());
        
        Map<String, String> keyPairMap = new HashMap<>();
        keyPairMap.put("publicKey", Base64.getEncoder().encodeToString(keyPairGenerator.getPublicKey().getEncoded()));
        keyPairMap.put("privateKey", Base64.getEncoder().encodeToString(keyPairGenerator.getPrivateKey().getEncoded()));
		return keyPairMap;
	}

	@GetMapping(value = "/enc")
	@ResponseBody
	public String encrypt(HttpServletRequest httpServletRequest) throws Exception {
			String algorithm = httpServletRequest.getParameter("algo");
			return CipherUtilFactory.getCipherUtil(algorithm).encrypt(httpServletRequest.getParameter("inputData"), httpServletRequest.getParameter("pk"));
	}
	
	@GetMapping(value = "/dec")
	@ResponseBody
	public String decrypt(HttpServletRequest httpServletRequest) throws Exception {
		String algorithm = httpServletRequest.getParameter("algo");
		return CipherUtilFactory.getCipherUtil(algorithm).decrypt(httpServletRequest.getParameter("inputData"), httpServletRequest.getParameter("pk"));
	}
}
