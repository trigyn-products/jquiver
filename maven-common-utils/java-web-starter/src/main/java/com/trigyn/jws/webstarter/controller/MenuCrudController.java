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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.menu.service.ModuleService;
import com.trigyn.jws.menu.vo.ModuleDetailsVO;
import com.trigyn.jws.menu.vo.ModuleTargetLookupVO;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;

@RestController
@RequestMapping("/cf")
public class MenuCrudController {
	
	private final static Logger logger = LogManager.getLogger(MenuCrudController.class);

    @Autowired
    private ModuleService moduleService 			= null;
    
    @Autowired
    private DBTemplatingService templateService 	= null;

    @Autowired
    private TemplatingUtils templateEngine 			= null;
    
	/**
	 * @param moduleId
	 * @return {@link String}
	 * @throws Exception
	 */
	@PostMapping(value = "/aem", produces = { MediaType.TEXT_HTML_VALUE })
	public String addEditModule(@RequestParam(value = "module-id") String moduleId) throws Exception {
		Map<String, Object> templateMap = new HashMap<>();
		ModuleDetailsVO moduleDetailsVO = moduleService.getModuleDetails(moduleId);
		List<ModuleDetailsVO> moduleListingVOList = moduleService.getAllModules(moduleId);	
		List<ModuleTargetLookupVO> moduleTargetLookupVOList = moduleService.getAllModuleLookUp();
		templateMap.put("moduleDetailsVO", moduleDetailsVO);
		templateMap.put("moduleListingVOList", moduleListingVOList);
		templateMap.put("moduleTargetLookupVOList", moduleTargetLookupVOList);
		TemplateVO templateVO = templateService.getTemplateByName("addEditModule");
		return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
				templateMap);
	}
	
	
	/**
	 * @param targetTypeId
	 * @return {@link List}
	 * @throws Exception
	 */
	@GetMapping(value = "/ltlm", produces = { MediaType.APPLICATION_JSON_VALUE })
	public List<Map<String, Object>> getTargetTypes(
			@RequestHeader(value = "target-lookup-id", required = true) Integer targetTypeId)
			throws Exception {
		return moduleService.getTargetTypes(targetTypeId);
	}

	/**
	 * @param moduleDetailsVO
	 * @return {@link ResponseEntity}
	 * @throws Exception
	 */
	@GetMapping(value = "/cms")
	@ResponseBody
	public String checkSequenceNumber(
			@RequestHeader(name = "parent-module-id", required = false) String parentModuleId
			, @RequestHeader(name = "sequence", required = true) Integer sequence) throws Exception {
		try {
			return moduleService.getModuleIdBySequence(parentModuleId, sequence);
		} catch (Exception e) {
			return "Error occurred";
		}
	}
	
	
	/**
	 * @param moduleDetailsVO
	 * @return {@link ResponseEntity}
	 * @throws Exception
	 */
	@GetMapping(value = "/cmurl")
	@ResponseBody
	public String checkModuleURL(
			@RequestHeader(name = "module-url", required = false) String moduleURL) throws Exception {
		return moduleService.getModuleIdByURL(moduleURL);
	}
	

	/**
	 * @param moduleDetailsVO
	 * @return {@link ResponseEntity}
	 * @throws Exception
	 */
	@PostMapping(value = "/sm", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<String> saveModule(@RequestBody ModuleDetailsVO moduleDetailsVO) throws Exception {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		try {
			String moduleId = moduleService.saveModuleDetails(moduleDetailsVO);
			return new ResponseEntity<>(moduleId, httpHeaders, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
		}
    }
}
