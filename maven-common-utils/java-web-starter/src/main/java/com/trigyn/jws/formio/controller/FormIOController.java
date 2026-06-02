package com.trigyn.jws.formio.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.utils.CustomStopException;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.formio.dao.IFormIORepository;
import com.trigyn.jws.formio.entities.FormIO;
import com.trigyn.jws.formio.service.FormIOService;
import com.trigyn.jws.formio.utils.FormIOUtils;
import com.trigyn.jws.formio.vo.FormIOVO;
import com.trigyn.jws.templating.service.MenuService;
import com.trigyn.jws.webstarter.service.DynamicFormCrudService;
import com.trigyn.jws.webstarter.utils.Constant;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/cf")
// @PreAuthorize("hasPermission('module','Form IO')")
public class FormIOController {

	private final static Logger		logger				= LoggerFactory.getLogger(FormIOController.class);

	@Autowired
	private MenuService				menuService			= null;

	@Autowired
	private PropertyMasterDAO		propertyMasterDAO	= null;

	@Autowired
	private FormIOService			formIOService		= null;

	@Autowired
	private DynamicFormCrudService	dynamicService		= null;
	
	@Autowired
	private FileUtilities			fileUtilities			= null;
	
	@Autowired
	private IFormIORepository		iFormIORepository		= null;

	@Autowired
	private PropertyMasterService	propertyMasterService	= null;
	
	@GetMapping(value = "/fiol", produces = MediaType.TEXT_HTML_VALUE)
	public String formIoListingPages(HttpServletRequest request, HttpServletResponse httpServletResponse)
			throws IOException {
		try {

			Map<String, Object>	modelMap	= new HashMap<>();
			String				environment	= propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
			modelMap.put("environment", environment);
			return menuService.getTemplateWithSiteLayout("form-io-listing-template", modelMap);
		} catch (Exception a_exception) {
			logger.error("Error occured while loading Form IO Listing page.", a_exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			fileUtilities.customSendError(httpServletResponse,HttpStatus.INTERNAL_SERVER_ERROR.value(), a_exception.getMessage());
			return null;
		}
	}

	@GetMapping(value = "/fmios", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<FormIOVO> getFormIExportOData(@RequestParam String formId) throws Exception {
		if (!StringUtils.isBlank(formId)) {
			DynamicForm dynamicForm = dynamicService.getDynamicFormById(formId);
			List<FormIOVO> formIos = formIOService.findFormIOByFormId(dynamicForm.getFormIoId());
			return formIos;
		}
		return null;
	}
	
	/** This method is called for saving revision through Revision History and Comparison Editors Save method **/
	@PostMapping(value = "/sfiov")
	public void saveFormIOVersion(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		String			modifiedContent	= a_httpServletRequest.getParameter("modifiedContent");							
		ObjectMapper	objectMapper	= new ObjectMapper().configure(SerializationFeature.FAIL_ON_SELF_REFERENCES,
				false);
		String			dbDateFormat	= propertyMasterService.getDateFormatByName(Constant.PROPERTY_MASTER_OWNER_TYPE,
				Constant.PROPERTY_MASTER_OWNER_ID, Constant.JWS_DATE_FORMAT_PROPERTY_NAME,
				com.trigyn.jws.dbutils.utils.Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);
		DateFormat		dateFormat		= new SimpleDateFormat(dbDateFormat);
		objectMapper.setDateFormat(dateFormat);
		FormIO		formIoEntity;
		FormIOVO	formIoVo;
		try {
			formIoVo		= objectMapper.readValue(modifiedContent, FormIOVO.class);
			formIoEntity	= FormIOUtils.convertToFormIoEntity(formIoVo);
			FormIOVO formIoVO = FormIOUtils.convertToFormIoVO(formIoEntity);
			formIOService.saveFormIOByVersion(formIoEntity, formIoVO);
		} catch (CustomStopException cse) {
			throw new CustomStopException("Error while saving the contents.");
		} catch (Exception e) {
			throw new Exception("Error while saving the contents.", e);
		}
	}
	
	/** This method is called for saving revision after main FormIO save method through onSuccess() **/
	@PostMapping(value = "/safiov")
	public void saveFormIOByVersion(HttpServletRequest a_httpServletRequest, HttpServletResponse a_httpServletResponse)
			throws Exception {
		String			formIoId	= a_httpServletRequest.getParameter("formIoId");
		FormIO  formIoEntity = iFormIORepository.findById(formIoId).orElse(null);

		try {
			FormIOVO    formIoVO = FormIOUtils.convertToFormIoVO(formIoEntity);
			formIOService.saveFormIOByVersion(formIoEntity, formIoVO);
		} catch (CustomStopException cse) {
			throw new CustomStopException("Error while saving the contents.");
		}
	}
	
	@PostMapping(value = "/fiogadc")
	public String getAutocompleteDefaultContent(HttpSession a_httpSession, HttpServletRequest a_httpServletRequest)
			throws Exception {
		String templateName = a_httpServletRequest.getParameter("templateName");
		String selectedTab = a_httpServletRequest.getParameter("selectedTab");
		String filebinId = a_httpServletRequest.getParameter("selectedTab");
		Map<String, Object> templateParamMap = new HashMap<>();
		templateParamMap.put("selectedTab", selectedTab);
		String jsContentDetails =  menuService.getTemplateWithoutLayout(templateName, templateParamMap);
		return jsContentDetails;

	}
	
	@PostMapping(value = "/cucl")
	public JsonNode checkAndUpdateCustomLogic(HttpServletRequest a_httpServletRequest,
			HttpServletResponse a_httpServletResponse) throws Exception {
		String			modifiedContent	= a_httpServletRequest.getParameter("formiojson");
		ObjectMapper	objectMapper	= new ObjectMapper().configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
		try {
			Map<String, Object> additionalParams =  new HashMap<>();
			FormIOUtils formIOUtils = new FormIOUtils();
			additionalParams.put("isExist", Boolean.FALSE);
			JsonNode			jsonNode			= objectMapper.readTree(modifiedContent);
			FormIOUtils.checkJsonNodeExist(jsonNode, "logic", additionalParams);
			Boolean isExist = (Boolean) additionalParams.get("isExist");
			if(isExist.equals(Boolean.FALSE)) {
				formIOUtils.updateJsonNode(jsonNode, modifiedContent);
			}
			return jsonNode;
		} catch (CustomStopException cse) {
			throw new CustomStopException("Error while saving the contents.");
		}
	}
	
	
}
