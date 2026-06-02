package com.trigyn.jws.webstarter.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dynamicform.dao.DynamicFormCrudDAO;
import com.trigyn.jws.dynamicform.service.DynamicFormHelperService;
import com.trigyn.jws.dynamicform.service.DynamicFormIoService;
import com.trigyn.jws.formio.dao.IFormIORepository;
import com.trigyn.jws.formio.entities.FormIO;
import com.trigyn.jws.formio.utils.Constants;
import com.trigyn.jws.formio.utils.FormFieldFactory;
import com.trigyn.jws.formio.utils.FormFieldGenerator;
import com.trigyn.jws.formio.utils.FormIOUtils;
import com.trigyn.jws.formio.vo.FormIOLogicAction;
import com.trigyn.jws.templating.service.DBTemplatingService;
import com.trigyn.jws.templating.utils.TemplatingUtils;
import com.trigyn.jws.templating.vo.TemplateVO;
import com.trigyn.jws.webstarter.utils.Constant;

import jakarta.servlet.http.HttpServletRequest;

@Service
@Transactional(readOnly = false)
public class FormIOMasterCreatorService {

	@Autowired
	private DynamicFormCrudDAO		dynamicFormDAO			= null;

	@Autowired
	private IFormIORepository		formIORepository		= null;

	@Autowired
	private DBTemplatingService		dbTemplatingService		= null;

	@Autowired
	private TemplatingUtils			templatingUtils			= null;
	
	@Autowired
	private DynamicFormIoService dynamicFormIoService = null;
	@Autowired
	private DynamicFormHelperService dynamicFormHelperService =null;
	
	@Autowired
	private HttpServletRequest 			request 			= null;


	String generateSelectQueryForFormIO(String tableName, List<Map<String, Object>> formDetails,
			String primaryKey, String dataSourceId, String dbProductName) throws Exception {
		List<Map<String, Object>>	tableInformation		= dynamicFormDAO.getTableDetailsByTableName(dataSourceId,
				tableName);
		Map<String, Object>			selectParameters		= new HashMap<>();
		List<String>				colmnsAs				= new ArrayList<String>();
		StringJoiner				selectQueryJoiner		= new StringJoiner("");
		int							selectColoumnCounter	= tableInformation.size();
		Map<String, Object>			saveQueryparameters		= new HashMap<>();
		for (Map<String, Object> info : tableInformation) {
			if(info != null && info.get("columnType") !=null && info.get("columnType").toString() !=null ) {
				String	columnName		= info.get("tableColumnName").toString();
				String	dataType		= info.get("dataType").toString();
				String	columnKey		= info.get("columnKey").toString();
				String	columnType		= info.get("columnType").toString();
				String	isAutoIncrement	= info.get("autoIncrement").toString();
				if ("PK".equals(columnKey)) {
					selectParameters.put("primaryKeyColumnName", columnName);
				}
				dynamicFormIoService.selectFormIoQueryBuilder(selectQueryJoiner, columnName, dataType, true, columnType,
						dbProductName, selectColoumnCounter, isAutoIncrement, saveQueryparameters, colmnsAs);
			}
		}
		String columsAsCsv = StringUtils.join(colmnsAs, ",");
		selectParameters.put("tableName", tableName);
		selectParameters.put("columnNames", columsAsCsv);
		if (dataSourceId != null && !dataSourceId.isBlank()) {
		    selectParameters.put("dataSourceId", dataSourceId);
		} else {
			selectParameters.put("dataSourceId", null);
		}
		selectParameters.put("dbProductName", dbProductName.replaceAll("[\\[\\]]", ""));
		//selectParameters.put("dataSourceId", dataSourceId);
		
		TemplateVO	templateVO;
		String		template;
		templateVO	= dbTemplatingService.getTemplateByName("formio-select-template");
		template	= templatingUtils.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
				selectParameters);
		return template;

	}

	public FormIO generateFormIoStructure(List<Map<String, Object>> matchedColumnDetails, Map<String, Object> formData, boolean isCaptchaEnabled, String fileBinId) 
	        throws Exception {
		
	    List<Map<String, Object>> nestedComponents = new ArrayList<>();
	    for (Map<String, Object> columns : matchedColumnDetails) {
	        String columnType = (String) columns.get("columnType");
	        String columnKey = (String) columns.get("columnKey");
	        String isAutoIncrement	= columns.get("autoIncrement").toString();
	        if (columnType != null) {
	        	if (columnKey != null && isAutoIncrement !=null && isAutoIncrement.equalsIgnoreCase("true")) {
	        		columnType = "hidden";
	        	}
	            FormFieldGenerator generator = FormFieldFactory.getFieldGenerator(columnType);
	            Map<String, Object> fieldMap = generator.generateField(columns);
	            nestedComponents.add(fieldMap);
	        }
	    }
	    if (isCaptchaEnabled) {
			Map<String, Object>	columns		= new HashMap<>();
			String				columnType	= "captchaelement";
			String fieldName = Constant.Captcha;
			columns.put("columnType", columnType);
			columns.put("fieldName", fieldName);
			columns.put("columnName", fieldName.toLowerCase());
			FormFieldGenerator	generator	= FormFieldFactory.getFieldGenerator(columnType);
			Map<String, Object>	fieldMap	= generator.generateField(columns);
			nestedComponents.add(fieldMap);
		}
	    
	    if (fileBinId != null && fileBinId.isBlank() == false) {
	    	Map<String, Object>	columns		= new HashMap<>();
			String				columnType	= "filebincomponent";
			String fieldName 				= fileBinId;
			columns.put("columnType", columnType);
			columns.put("fieldName", fieldName);
			columns.put("columnName", fieldName.toLowerCase());
			columns.put("content", Constants.FILE_BIN_HTML_CONTENT);
			String fileBinType 		= fieldName;
			String contextPath = request.getContextPath();
			FormIOUtils fmioUtils = new FormIOUtils();
			List<FormIOLogicAction> logic = fmioUtils.injectLogic(fileBinType, Constants.FILE_BIN_JS_CONTENT,
					"yourFileBinId", fileBinType, contextPath);
			columns.put("jsContent", logic.get(0).getTrigger().getJavascript());
			
			FormFieldGenerator	generator	= FormFieldFactory.getFieldGenerator(columnType);
			Map<String, Object>	fieldMap	= generator.generateField(columns);
			nestedComponents.add(fieldMap);
		}

	    String jsonStr = FormIOJsonGenerator.getFormIOJson(nestedComponents);
	    JSONObject form = new JSONObject(jsonStr);
	    String id = UUID.randomUUID().toString();

	    FormIO fmio = new FormIO();
	    fmio.setFormIoId(id);
	    String moduleName = formData.get("moduleName") + "-form-io";
	    fmio.setFormName(moduleName);
	    String description = formData.get("moduleName") + " Form";
	    fmio.setFormDescription(description);
	    fmio.setFormIoJson(form.toString());
	    fmio.setPersistenceType("1");
	    fmio.setFormIoType(1);
	    fmio.setIsCustomUpdated(1);
	    FormIO io = formIORepository.save(fmio);
	    return io;
	}
	
	public Map<String, String> generateHtmlTemplateForFormIo(String dataSourceId, String dbProductName,
			String tableName, List<Map<String, Object>> formDetails, String moduleURL, Boolean toggleCaptcha,
			Boolean toggleCsrf, Boolean toggleFileBin, String fileBinId, String fileAssociationId) throws Exception {

		List<Map<String, Object>>	tableDetails			= dynamicFormDAO.getTableDetailsByTableName(dataSourceId,
				tableName);
		dynamicFormHelperService.getMatchedColumnTableDetails(formDetails, tableDetails, true);
		Map<String, String> templateDetails = dynamicFormIoService.createFormIoHtmlByTableName(tableName, tableDetails,
				moduleURL, dataSourceId, dbProductName, toggleCaptcha, toggleCsrf, toggleFileBin, fileBinId,
				fileAssociationId);
		return templateDetails;
	}
	
	public FormIO updateFormIoDetails(MultiValueMap<String, String> inputDetails, Map<String, Object> formData,
			String dataSourceId, String fileBinId) throws JsonProcessingException, JsonMappingException, Exception {
		FormIO fmio;
		String						formIoId;
		String						tableName;
		List<Map<String, Object>>	tableDetails;
		boolean isCaptchaEnabled = false;
		if (formData.get("dataSourceId") != null) {
			dataSourceId = formData.get("dataSourceId").toString();
		}
		tableName = formData.get("selectTable").toString();
		List<String> formDetailsString = new ObjectMapper().convertValue(inputDetails.get("formDetails"), List.class);
		String formDetailsJsonString = formDetailsString.get(0).toString();
		List<Map<String, Object>> formDetails = new ObjectMapper().readValue(formDetailsJsonString, List.class);
		tableDetails = dynamicFormDAO.getTableDetailsByTableName(dataSourceId, tableName);
		dynamicFormHelperService.getMatchedColumnTableDetails(formDetails, tableDetails, true);
		if (formData.get("toggleCaptcha").toString() != null && formData.get("toggleCaptcha").toString().equalsIgnoreCase("1")) {
			isCaptchaEnabled = true;
		}
		fmio = generateFormIoStructure(tableDetails, formData, isCaptchaEnabled, fileBinId);
		formData.put("formIoId", fmio.getFormIoId());
		return fmio;
	}

}
