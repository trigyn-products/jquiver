package com.trigyn.jws.webstarter.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.vo.ModuleVersionVO;
import com.trigyn.jws.dynamicform.service.DynamicFormService;
import com.trigyn.jws.typeahead.service.TypeAheadService;
import com.trigyn.jws.webstarter.utils.Constant;

@Service
public class ModuleRevisionService {

	@Autowired
	private ModuleVersionService moduleVersionService				= null;
	
	@Autowired
	private DynamicFormService dynamicFormService 					= null;
	
	@Autowired
	private TypeAheadService typeAheadService						= null;
	
	@Autowired
	private DynamicFormCrudService dynamicFormCrudService 			= null;
	
	@Autowired
	private PropertyMasterService propertyMasterService 			= null;
	
	@Autowired
	private DynarestCrudService dynarestCrudService 				= null;
	
	
	public void saveModuleVersioning( MultiValueMap<String, String> formData, Integer sourceTypeId) throws Exception{
		Map<String, Object> versioningData = new HashMap<>();
		String primaryKey = null;
		String entityName = null;
		for (Entry<String, List<String>> formDataMap : formData.entrySet())  {
			versioningData.put(formDataMap.getKey(), formDataMap.getValue().get(0));
		}
		if(versioningData.get("primaryKey") != null) {
			primaryKey = versioningData.get("primaryKey").toString();
		}
		if(versioningData.get("entityName") != null) {
			entityName = versioningData.get("entityName").toString();
		}
		moduleVersionService.saveModuleVersion(versioningData, null, primaryKey, entityName, sourceTypeId);
	}
	
	
	public void saveUpdatedContent(HttpServletRequest a_httpServletRequest) throws Exception{
		String modifiedContent 			= a_httpServletRequest.getParameter("modifiedContent");
		String moduleType				= a_httpServletRequest.getParameter("moduleType");
		ObjectMapper objectMapper		= new ObjectMapper();
		
		Map<String, String> formData = objectMapper.readValue(modifiedContent, Map.class);
		MultiValueMap<String, String> multivalueMap = new LinkedMultiValueMap<>();
		for (Entry<String, String> formDataMap : formData.entrySet())  {
			List<String> multiValueString = new ArrayList<>();
			if(formDataMap.getValue() != null) {
				Object formValue = formDataMap.getValue();
				multiValueString.add(formValue.toString());
				multivalueMap.put(formDataMap.getKey(), multiValueString);
			}
		}
		if(moduleType.equals(Constant.ModuleType.AUTOCOMPLETE.getModuleType())) {
			typeAheadService.saveAutocompleteDetails(multivalueMap, Constant.REVISION_SOURCE_VERSION_TYPE);
		}else if(moduleType.equals(Constant.ModuleType.DYNAMICFORM.getModuleType())){
			dynamicFormCrudService.saveDynamicFormDetails(multivalueMap,  Constant.REVISION_SOURCE_VERSION_TYPE);
		}else {
			dynamicFormService.saveDynamicForm(multivalueMap);
			if(moduleType.equals(Constant.ModuleType.DYNAREST.getModuleType())){
				dynarestCrudService.deleteDAOQueries(multivalueMap);
				dynarestCrudService.saveDAOQueries(multivalueMap);
			}
			saveModuleVersioning(multivalueMap, Constant.REVISION_SOURCE_VERSION_TYPE);
		}
	}
	
	
	public Map<String , Object> getModuleVersioningData(HttpServletRequest a_httpServletRequest) throws Exception {
		String moduleType			= a_httpServletRequest.getParameter("moduleType");
		String entityId 			= a_httpServletRequest.getParameter("entityId");
		String saveUrl		 		= a_httpServletRequest.getParameter("saveUrl");
		String previousPageUrl 		= a_httpServletRequest.getParameter("previousPageUrl");
		String formId 				= a_httpServletRequest.getParameter("formId");
		String dateFormat			= propertyMasterService.getDateFormatByName(Constant.PROPERTY_MASTER_OWNER_TYPE,
				Constant.PROPERTY_MASTER_OWNER_ID, Constant.JWS_DATE_FORMAT_PROPERTY_NAME, Constant.JWS_DB_DATE_FORMAT_PROPERTY_NAME);
	
		List<ModuleVersionVO> versionVOs = moduleVersionService.fetchModuleVersionDetails(entityId);
		String moduleName = a_httpServletRequest.getParameter("moduleName");
		
		Map<String , Object> templateMap = new HashMap<>();
		templateMap.put("moduleType", moduleType);
		templateMap.put("entityId", entityId);
		templateMap.put("revesionDetailsVOs", versionVOs);
		templateMap.put("moduleName", moduleName);
		templateMap.put("formId", formId);
		templateMap.put("saveUrl", saveUrl);
		templateMap.put("dateFormat", dateFormat);
		templateMap.put("previousPageUrl", previousPageUrl);
		return templateMap;
	}
	
}
