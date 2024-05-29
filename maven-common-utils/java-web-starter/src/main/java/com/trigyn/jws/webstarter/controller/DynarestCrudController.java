package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trigyn.jws.dbutils.cipher.utils.CipherUtilFactory;
import com.trigyn.jws.dbutils.cipher.utils.RSAKeyPairGeneratorUtil;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dynamicform.utils.Constant;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.usermanagement.security.config.JwtUtil;
import com.trigyn.jws.webstarter.service.DynarestCrudService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

	@Autowired
	private UserDetailsService	userDetailsService	= null;

	@Autowired
	private JwtUtil				jwtUtil				= null;

	@GetMapping(value = "/dynl", produces = MediaType.TEXT_HTML_VALUE)
	public String loadDynarestListing(HttpServletRequest httpServletRequest) throws Exception, CustomStopException {
		try {
			Map<String, Object>	modelMap	= new HashMap<>();
			String				environment	= propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
			modelMap.put("environment", environment);
			String			uri			= httpServletRequest.getRequestURI()
					.substring(httpServletRequest.getContextPath().length());
			String			url			= httpServletRequest.getRequestURL().toString();
			StringBuilder	urlPrefix	= new StringBuilder();
			url = url.replace(uri, "");
			urlPrefix.append(url).append("/api/");
			modelMap.put("urlPrefix", urlPrefix);
			return menuService.getTemplateWithSiteLayout("dynarest-details-listing", modelMap);
		} catch (CustomStopException custStopException) {
			logger.error("Error occured while Rest API Listing page.", custStopException);
			throw custStopException;
		}
	}

	@PostMapping(value = "/sdq", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String saveDynamicRestSaveQueries(@RequestBody MultiValueMap<String, String> formData) throws Exception {
		dynarestCrudService.deleteDAOQueries(formData, Constant.MASTER_SOURCE_VERSION_TYPE);
		return dynarestCrudService.saveDAOQueries(formData, Constant.MASTER_SOURCE_VERSION_TYPE);
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
	public String encrypt(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		String	algorithm	= httpServletRequest.getParameter("algo");
		String	mode		= httpServletRequest.getParameter("mode");
		String	padding		= httpServletRequest.getParameter("padding");
		Integer	keyLength	= Integer.parseInt(httpServletRequest.getParameter("keyLength"));
		String	resp		= null;
		try {
			if (algorithm != null && mode != null && padding != null && keyLength != null) {
				resp = CipherUtilFactory.getCipherUtil(algorithm, mode, padding, keyLength).encrypt(
						httpServletRequest.getParameter("inputData"), httpServletRequest.getParameter("pk"), algorithm);
				if (resp == null) {
					throw new Exception("Please provide the valid input");
				}
			} else {
				throw new Exception("Please provide the valid input");
			}

		} catch (Exception exec) {
			logger.error("Error occured while encrypting.", exec);
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exec.getMessage());
			exec.printStackTrace();
			return null;
			//exec.printStackTrace();
		}

		return resp;

	}

	@GetMapping(value = "/dec")
	@ResponseBody
	public String decrypt(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
		String	algorithm	= httpServletRequest.getParameter("algo");
		String	mode		= httpServletRequest.getParameter("mode");
		String	padding		= httpServletRequest.getParameter("padding");
		String	resp		= null;
		Integer	keyLength	= Integer.parseInt(httpServletRequest.getParameter("keyLength"));
		try {
			if (algorithm != null && mode != null && padding != null && keyLength != null) {
				resp = CipherUtilFactory.getCipherUtil(algorithm, mode, padding, keyLength).decrypt(
						httpServletRequest.getParameter("inputData"), httpServletRequest.getParameter("pk"), algorithm);
				if (resp == null) {
					throw new Exception("Please provide the valid input");
				}
			} else {
				throw new Exception("Please provide the valid input");
			}

		} catch (Exception exec) {
			logger.error("Error occured while decrypting.", exec);
			exec.printStackTrace();
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exec.getMessage());
			exec.printStackTrace();
			return null;
		}
		return resp;
	}

	@RequestMapping(value = "/getJwtToken/{userName}", method = RequestMethod.GET)
	public String getJwtToken(@PathVariable(name="userName") String userName)
			throws Exception {
		String token = jwtUtil.generateToken(userDetailsService.loadUserByUsername(userName));
		return token;
	}
}
