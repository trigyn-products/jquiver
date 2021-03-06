package com.trigyn.jws.webstarter.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.util.StringUtils;
import com.trigyn.jws.resourcebundle.service.ResourceBundleService;
import com.trigyn.jws.resourcebundle.vo.LanguageVO;
import com.trigyn.jws.resourcebundle.vo.ResourceBundleVO;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.webstarter.utils.Constant;

@RestController
@RequestMapping(value = "/cf")
@PreAuthorize("hasPermission('module','Miltilingual')")
public class ResourceBundleCrudController {

	private final static Logger		logger					= LogManager.getLogger(ResourceBundleCrudController.class);

	@Autowired
	private ResourceBundleService	resourceBundleService	= null;

	@Autowired
	private MenuService				menuService				= null;

	@GetMapping(value = "/rb", produces = MediaType.TEXT_HTML_VALUE)
	public String dbResourceBundleListing(HttpServletResponse httpServletResponse) throws IOException {

		try {
			Map<String, Object>	templateMap		= new HashMap<>();
			List<LanguageVO>	languageVOList	= resourceBundleService.getLanguagesList();
			templateMap.put("languageVOList", languageVOList);
			return menuService.getTemplateWithSiteLayout("resource-bundle-listing", templateMap);
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}

	}

	@PostMapping(value = "/aerb")
	public String dbResourceJsp(HttpServletResponse httpServletResponse, @RequestParam("resource-key") String resourceBundleKey)
			throws Exception {
		Map<String, Object> templateMap = new HashMap<>();
		try {
			if (resourceBundleKey != null) {
				Map<Integer, ResourceBundleVO> resourceBundleVOMap = resourceBundleService.getResourceBundleVOMap(resourceBundleKey);
				templateMap.put("resourceBundleVOMap", resourceBundleVOMap);
			}
			List<LanguageVO> languageVOList = resourceBundleService.getLanguagesList();
			templateMap.put("languageVOList", languageVOList);
			templateMap.put("resourceBundleKey", resourceBundleKey);
			return menuService.getTemplateWithSiteLayout("resource-bundle-manage-details", templateMap);
		} catch (Exception a_exception) {
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}

	}

	@GetMapping(value = "/crbk", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Boolean> checkResourceData(@RequestParam("resourceKey") String resourceBundleKey) throws Exception {

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		try {
			Boolean keyAlreadyExist = resourceBundleService.checkResourceKeyExist(resourceBundleKey);
			return new ResponseEntity<>(keyAlreadyExist, httpHeaders, HttpStatus.OK);
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			return new ResponseEntity<>(true, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/srb", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Boolean> saveResourceDetails(@RequestBody List<ResourceBundleVO> dbResourceList) throws Exception {

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		try {
			resourceBundleService.saveResourceBundleDetails(dbResourceList, Constant.MASTER_SOURCE_VERSION_TYPE);
			return new ResponseEntity<>(true, httpHeaders, HttpStatus.OK);
		} catch (Exception a_exception) {
			logger.error("Error ", a_exception);
			return new ResponseEntity<>(false, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping(value = "/srbv", produces = { MediaType.APPLICATION_JSON_VALUE })
	public void saveResourceDetailsByVersion(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		TypeReference<List<ResourceBundleVO>>	resourceBundleType	= new TypeReference<List<ResourceBundleVO>>() {
																	};
		String									modifiedContent		= a_httpServletRequest.getParameter("modifiedContent");
		ObjectMapper							objectMapper		= new ObjectMapper();
		List<ResourceBundleVO>					resourceBundleList	= objectMapper.readValue(modifiedContent, resourceBundleType);
		resourceBundleService.saveResourceBundleDetails(resourceBundleList, Constant.REVISION_SOURCE_VERSION_TYPE);
	}

	@PostMapping(value = "/rtbkl")
	public String getTextByKeyAndLanguageId(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		String	resourceBundleKey	= a_httpServletRequest.getParameter("resourceBundleKey");
		Integer	languageId			= StringUtils.isBlank(a_httpServletRequest.getParameter("languageId")) == false
				? Integer.parseInt(a_httpServletRequest.getParameter("languageId"))
				: null;
		return resourceBundleService.findTextByKeyAndLanguageId(resourceBundleKey, languageId);
	}

}