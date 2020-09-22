package com.trigyn.jws.webstarter.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.resourcebundle.service.ResourceBundleService;
import com.trigyn.jws.resourcebundle.vo.LanguageVO;
import com.trigyn.jws.resourcebundle.vo.ResourceBundleVO;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;

@RestController
@RequestMapping(value = "/cf")
public class ResourceBundleCrudController {
	
	private final static Logger logger = LogManager.getLogger(ResourceBundleCrudController.class);

	@Autowired
	private ResourceBundleService resourceBundleService 		= null;

	@Autowired
	private DBTemplatingService templatingService 				= null;

	@Autowired
	private TemplatingUtils templateEngine 						= null;
	

	
	@GetMapping(value = "/rb", produces = MediaType.TEXT_HTML_VALUE)
	public String dbResourceBundleListing() throws Exception {
		
		Map<String, Object> templateMap = new HashMap<>();
		List<LanguageVO> languageVOList = resourceBundleService.getLanguagesList();
		templateMap.put("languageVOList", languageVOList);
		TemplateVO templateVO = templatingService.getTemplateByName("resource-bundle-listing");
		return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
				templateMap);
	}


	

	
	@PostMapping(value = "/aerb")
	public String dbResourceJsp(@RequestParam("resource-key") String resourceBundleKey) throws Exception {
		Map<String, Object> templateMap = new HashMap<>();
		if (resourceBundleKey != null) {
			Map<Integer, ResourceBundleVO> resourceBundleVOMap = resourceBundleService.getResourceBundleVOMap(resourceBundleKey);
			templateMap.put("resourceBundleVOMap", resourceBundleVOMap);
		}
		List<LanguageVO> languageVOList = resourceBundleService.getLanguagesList();
		templateMap.put("languageVOList", languageVOList);
		templateMap.put("resourceBundleKey", resourceBundleKey);
		TemplateVO templateVO = templatingService.getTemplateByName("resource-bundle-manage-details");
		return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
				templateMap);

	}


	
	@GetMapping(value = "/crbk", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Boolean> checkResourceData(@RequestParam("resourceKey") String resourceBundleKey) throws Exception {
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		try {
			Boolean keyAlreadyExist = resourceBundleService.checkResourceKeyExist(resourceBundleKey);
			return new ResponseEntity<>(keyAlreadyExist, httpHeaders, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(true, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	
	@PostMapping(value = "/srb", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Boolean> saveResourceDetails(@RequestParam("resourceBundleKey") String resourceBundleKey,
			@RequestBody List<ResourceBundleVO> dbResourceList) throws Exception {
		
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		try {
			resourceBundleService.saveResourceBundleDetails(resourceBundleKey, dbResourceList);
			return new ResponseEntity<>(true, httpHeaders, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(false, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	

}