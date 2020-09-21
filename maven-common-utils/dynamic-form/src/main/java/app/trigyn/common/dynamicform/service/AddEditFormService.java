package app.trigyn.common.dynamicform.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.trigyn.common.dynamicform.dao.DynamicFormDAO;
import app.trigyn.common.dynamicform.dao.IDynamicFormQueriesRepository;
import app.trigyn.common.dynamicform.entities.DynamicForm;
import app.trigyn.common.dynamicform.entities.DynamicFormSaveQuery;
import app.trigyn.common.dynamicform.vo.DynamicFormSaveQueryVO;
import app.trigyn.core.templating.service.DBTemplatingService;
import app.trigyn.core.templating.utils.TemplatingUtils;
import app.trigyn.core.templating.vo.TemplateVO;

@Service
@Transactional
public class AddEditFormService {

	@Autowired
	private TemplatingUtils templateEngine = null;

	@Autowired
	private DBTemplatingService templatingService = null;

	@Autowired
	private DynamicFormDAO formDAO = null;

	@Autowired
	private IDynamicFormQueriesRepository dynamicFormQueriesRepository = null;

	@Autowired
	private DynamicFormDAO dynamicFormDAO = null;
	
	@Autowired
	private DynamicFormService dynamicFormService = null;

	public String addEditForm(String formId) throws Exception {

		Map<String, Object> templateMap = new HashMap<>();
		TemplateVO templateVO = templatingService.getTemplateByName("dynamic-form-manage-details");
		DynamicForm dynamicForm = new DynamicForm();
		if (StringUtils.isNotEmpty(formId)) {
			dynamicForm = formDAO.findDynamicFormById(formId);
		}
		templateMap.put("dynamicForm", dynamicForm);
		return templateEngine.processTemplateContents(templateVO.getTemplate(), templateVO.getTemplateName(),
				templateMap);
	}

	@Transactional(readOnly = false)
	public void saveDynamicFormDetails(MultiValueMap<String, String> formData) throws Exception {
		DynamicForm dynamicForm = new DynamicForm();
		List<DynamicFormSaveQuery> dynamicFormSaveQueries = new ArrayList<>();
		String formId = formData.getFirst("formId").toString();
		if (StringUtils.isNotEmpty(formId)) {
			dynamicForm = formDAO.findDynamicFormById(formId);

		} else {
			dynamicForm.setCreatedBy("admin");
			dynamicForm.setCreatedDate(new Date());
		}
		dynamicForm.setFormDescription(formData.getFirst("formDescription").toString());
		dynamicForm.setFormSelectQuery(formData.getFirst("formSelectQuery").toString());
		dynamicForm.setFormBody(formData.getFirst("formBody").toString());
		dynamicForm.setFormName(formData.getFirst("formName").toString());
		dynamicForm.setDynamicFormSaveQueries(dynamicFormSaveQueries);
		formDAO.saveDynamicFormData(dynamicForm);
		if(!dynamicForm.getDynamicFormSaveQueries().isEmpty()) {
			formDAO.deleteFormQueries(formId);
		}	
		String queriesList = formData.getFirst("formSaveQuery");
		List<String> queries = new ObjectMapper().readValue(queriesList, List.class);
		int counter = 1;
		for (String query : queries) {
			DynamicFormSaveQuery dynamicFormSaveQuery = new DynamicFormSaveQuery();
			//dynamicFormSaveQuery.setDynamicFormQueryId(UUID.randomUUID().toString());
			dynamicFormSaveQuery.setDynamicFormId(dynamicForm.getFormId());
			dynamicFormSaveQuery.setDynamicFormSaveQuery(query);
			dynamicFormSaveQuery.setSequence(counter);
			counter++;
			dynamicFormSaveQueries.add(dynamicFormSaveQuery);
		}

		dynamicFormQueriesRepository.saveAll(dynamicFormSaveQueries);
		List<DynamicForm> form = new ArrayList<DynamicForm>();
		form.add(dynamicForm);
		dynamicFormService.downloadFormsToLocal(form);
	}

	public List<DynamicFormSaveQueryVO> getAllFormQueriesById(String formId) {
		return dynamicFormDAO.findDynamicFormQueriesById(formId).stream()
							 .map(queries -> new DynamicFormSaveQueryVO(queries.getDynamicFormSaveQuery(), queries.getSequence()))
							 .collect(Collectors.toList());
	}

}
