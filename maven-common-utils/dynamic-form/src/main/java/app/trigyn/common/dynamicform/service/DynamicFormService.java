package app.trigyn.common.dynamicform.service;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import app.trigyn.common.dbutils.repository.PropertyMasterDAO;
import app.trigyn.common.dbutils.utils.FileUtilities;
import app.trigyn.common.dynamicform.dao.DynamicFormDAO;
import app.trigyn.common.dynamicform.dao.IDynamicFormQueriesRepository;
import app.trigyn.common.dynamicform.entities.DynamicForm;
import app.trigyn.common.dynamicform.entities.DynamicFormSaveQuery;
import app.trigyn.core.templating.utils.TemplatingUtils;

@Service
@Transactional
public class DynamicFormService {

	@Autowired
	private DynamicFormDAO dynamicFormDAO = null;

	@Autowired
	private TemplatingUtils templateEngine = null;
	
	@Autowired
	private PropertyMasterDAO propertyMasterDAO = null;
	
	@Autowired
	private IDynamicFormQueriesRepository dynamicFormQueriesRepository = null;
	
	@Autowired
	private FileUtilities fileUtilities  = null;

	public String loadDynamicForm(String formId, String primaryId, Map<String, Object> additionalParam)
			throws Exception {

		String selectTemplateQuery = null;
		String templateHtml = null;
		String selectQuery = null;
		String formBody = null;
		Map<String, Object> formHtmlTemplateMap = new HashMap<>();
		String selectQueryFile = "selectQuery";
		String htmlBodyFile = "hmtlQuery";

		DynamicForm form = dynamicFormDAO.findDynamicFormById(formId);
		String formName = form.getFormName();
		
		 String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
	     if(environment.equalsIgnoreCase("dev") ) {
	         selectQuery = getContentForDevEnvironment(form.getFormName(),selectQueryFile);
	         formBody = getContentForDevEnvironment(form.getFormName(),htmlBodyFile);
	      }else {
	         selectQuery = form.getFormSelectQuery();
	         formBody = form.getFormBody();
	      }
		

		if (StringUtils.isNotEmpty(primaryId)) {

			List<Map<String, Object>> selectResultSet = null;
			Map<String, Object> selectTemplateMap = new HashMap<>();

			selectTemplateMap.put("primaryId", primaryId);
			if (additionalParam != null) {
				selectTemplateMap.putAll(additionalParam);
			}

			selectTemplateQuery = templateEngine.processTemplateContents(selectQuery, formName, selectTemplateMap);

			if (StringUtils.isNotEmpty(selectTemplateQuery)) {
				selectResultSet = dynamicFormDAO.getFormData(selectTemplateQuery.toString());
			}
			formHtmlTemplateMap.put("resultSet", selectResultSet);

		}
		formHtmlTemplateMap.put("formId", formId);
		templateHtml = templateEngine.processTemplateContents(formBody, formName, formHtmlTemplateMap);

		return templateHtml;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Boolean saveDynamicForm(MultiValueMap<String, String> formData) throws Exception {
		String saveTemplateQuery = null;
		DynamicForm form = dynamicFormDAO.findDynamicFormById(formData.getFirst("formId"));
		String formName = form.getFormName();
		Map<String, Object> saveTemplateMap = new HashMap<>();
		saveTemplateMap.put("formData", formData);
		String environment = propertyMasterDAO.findPropertyMasterValue("system", "system", "profile");
		String saveQuery = "saveQuery-";
		List<DynamicFormSaveQuery> dynamicFormSaveQueries = dynamicFormDAO.findDynamicFormQueriesById(formData.getFirst("formId"));
		for (DynamicFormSaveQuery dynamicFormSaveQuery : dynamicFormSaveQueries) {
			String formSaveQuery = null;
			if(environment.equalsIgnoreCase("dev") ) {
			  formSaveQuery = getContentForDevEnvironment(form.getFormName(),saveQuery+dynamicFormSaveQuery.getSequence());
			}else {
			  formSaveQuery = dynamicFormSaveQuery.getDynamicFormSaveQuery();
			}
			saveTemplateQuery = templateEngine.processTemplateContents(formSaveQuery, formName, saveTemplateMap);
			dynamicFormDAO.saveFormData(saveTemplateQuery);
		}
		return true;
	}

	@Transactional(readOnly = true)
	public String checkFormName(String formName) {
		return dynamicFormDAO.checkFormName(formName);
	}

	public void downloadDynamicFormTemplates() throws Exception {
	
		List<DynamicForm>  formList = dynamicFormDAO.getAllDynamicForms();
		downloadFormsToLocal(formList);
		
	}

	public void downloadFormsToLocal(List<DynamicForm> formList) throws Exception, IOException {
		String templateDirectory = "DynamicForm";
		String ftlCustomExtension = ".tgn";
		String selectQuery = "selectQuery";
		String htmlBody = "hmtlQuery";
		String saveQuery = "saveQuery-";
		String folderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		folderLocation = folderLocation +File.separator+templateDirectory;
		
		for (DynamicForm dynamicForm : formList) {
			boolean isCheckSumChanged = false;
				String formName = dynamicForm.getFormName();
				String formFolder =  folderLocation + File.separator + formName;
				if(!new File(formFolder).exists()) {
					File fileDirectory = new File(formFolder);
					fileDirectory.mkdirs();
				}
				// select
				String selectCheckSum = fileUtilities.checkFileContents(selectQuery, formFolder, dynamicForm.getFormSelectQuery(),dynamicForm.getFormSelectChecksum(),ftlCustomExtension);
				if(selectCheckSum!= null) {
					isCheckSumChanged = true;
					dynamicForm.setFormSelectChecksum(selectCheckSum);
				}
				
				//htmlBody
				String htmlBodyCheckSum  = fileUtilities.checkFileContents(htmlBody, formFolder, dynamicForm.getFormBody(),dynamicForm.getFormBodyChecksum(),ftlCustomExtension);
				if(htmlBodyCheckSum!= null) {
					isCheckSumChanged = true;
					dynamicForm.setFormBodyChecksum(htmlBodyCheckSum);
				}
				
				//save
				for (DynamicFormSaveQuery formSaveQuery : dynamicForm.getDynamicFormSaveQueries()) {
					String sequence = saveQuery+formSaveQuery.getSequence();
					String checksum =	fileUtilities.checkFileContents(sequence , formFolder, formSaveQuery.getDynamicFormSaveQuery(),formSaveQuery.getChecksum(),ftlCustomExtension);
					if(checksum!= null) {
						isCheckSumChanged = true;
						formSaveQuery.setChecksum(checksum);
					}
				}
			// save checksum
				if(isCheckSumChanged) {
					dynamicFormDAO.saveDynamicFormData(dynamicForm);
				}	
		}
	}



	public void uploadAllFormsToDB() throws Exception {
		
		String user ="admin";
		String ftlCustomExtension = ".tgn";
		String templateDirectory = "DynamicForm";
		String selectQuery = "selectQuery";
		String htmlBody = "hmtlQuery";
		String saveQuery = "saveQuery-";
		String folderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		folderLocation = folderLocation +File.separator+templateDirectory;
		File directory = new File(folderLocation);
		if(!directory.exists()) {
			throw new Exception("No such directory present");
		}
		FilenameFilter textFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(ftlCustomExtension);
            }
        };
        
        File[] directories = directory.listFiles((new FilenameFilter() {
        	  @Override
        	  public boolean accept(File current, String name) {
        	    return new File(current, name).isDirectory();
        	  }
        	}));
        for (File currentDirectory : directories) {
        	String selectCheckSum = null;
        	String htmlCheckSum = null;
        	String currentDirectoryName = currentDirectory.getName();
        	DynamicForm dynamicForm = dynamicFormDAO.getFormDetailsByName(currentDirectoryName);
        	
        	if(dynamicForm ==  null) {
        		dynamicForm = new DynamicForm();
        		dynamicForm.setCreatedBy(user);
    			dynamicForm.setCreatedDate(new Date());
    			dynamicForm.setFormName(currentDirectoryName);
    			dynamicForm.setFormDescription("Uploaded from Local Directory");
        	}
				File[] directoryFiles = currentDirectory.listFiles(textFilter);
				Integer filesPresent = directoryFiles.length;
				if(filesPresent >= 3) {
					File selectFile = new File(currentDirectory.getAbsolutePath()+File.separator+selectQuery+ftlCustomExtension);
					File hmtlBodyFile = new File(currentDirectory.getAbsolutePath()+File.separator+htmlBody+ftlCustomExtension);
					if(!selectFile.exists() || !hmtlBodyFile.exists()) {
						throw new Exception("selectQuery  file not and hmtlQueryfile are mandatory  for saving dynamic form"+currentDirectoryName);
					}else {
						// set select
						selectCheckSum = fileUtilities.generateFileChecksum(selectFile);
						if(!selectCheckSum.equalsIgnoreCase(dynamicForm.getFormSelectChecksum())) {
							
							dynamicForm.setFormSelectQuery(fileUtilities.readContentsOfFile(selectFile.getAbsolutePath()));
							dynamicForm.setFormSelectChecksum(selectCheckSum);
						}
						
						// set html
						htmlCheckSum = fileUtilities.generateFileChecksum(hmtlBodyFile);
						if(!htmlCheckSum.equalsIgnoreCase(dynamicForm.getFormBodyChecksum())) {
							
							dynamicForm.setFormBody(fileUtilities.readContentsOfFile(hmtlBodyFile.getAbsolutePath()));
							dynamicForm.setFormBodyChecksum(htmlCheckSum);
						}
						dynamicFormDAO.saveDynamicFormData(dynamicForm);
						// saveQuery
						if(dynamicForm.getDynamicFormSaveQueries()!=null) {
							dynamicFormDAO.deleteFormQueries(dynamicForm.getFormId());
						}
						List<DynamicFormSaveQuery> dynamicFormSaveQueries = new ArrayList<>();
						int i=0;
						for (File file : directoryFiles) {
							Integer saveQueryFiles = filesPresent - 2;
							if(saveQueryFiles >= 1) {
								if(file.getName().contains(saveQuery)) {
									i++;
										DynamicFormSaveQuery formSaveQuery = new DynamicFormSaveQuery();
										File saveQueryFile = new File(currentDirectory.getAbsolutePath()+File.separator+saveQuery+i+ftlCustomExtension);
										if(saveQueryFile.exists()) {
											formSaveQuery.setDynamicFormId(dynamicForm.getFormId());
											formSaveQuery.setChecksum(fileUtilities.generateFileChecksum(saveQueryFile));
											formSaveQuery.setSequence(i);
											formSaveQuery.setDynamicFormSaveQuery(fileUtilities.readContentsOfFile(saveQueryFile.getAbsolutePath()));
											dynamicFormSaveQueries.add(formSaveQuery);
										}else {
											throw new Exception("saveQuery file sequence is incorrect"+currentDirectoryName);
										}
								}
								
							}else {
								throw new Exception("saveQuery file is mandatory  for saving dynamic form"+currentDirectoryName);
							}
						
						}
						dynamicFormQueriesRepository.saveAll(dynamicFormSaveQueries);
						
					}
							
				}else {
					throw new Exception("Invalid count of files for saving dynamic form"+currentDirectoryName);
				}
		}
	}

	public  String getContentForDevEnvironment(String formName, String fileName) throws Exception {
		
		String ftlCustomExtension = ".tgn";
		String templateDirectory = "DynamicForm";
		String folderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		folderLocation = folderLocation +File.separator+templateDirectory+File.separator+formName;
		File directory = new File(folderLocation);
		if(!directory.exists()) {
			throw new Exception("No such directory present");
		}
		
		File selectFile = new File(folderLocation+File.separator+fileName+ftlCustomExtension);
		if(selectFile.exists()) {
			return fileUtilities.readContentsOfFile(selectFile.getAbsolutePath());
		}else {
			throw new Exception("Please download the forms from dynamic form  listing  " + formName);
		}
	}
}
