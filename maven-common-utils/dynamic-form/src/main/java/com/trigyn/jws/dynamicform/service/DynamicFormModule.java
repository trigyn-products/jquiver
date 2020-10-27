package com.trigyn.jws.dynamicform.service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.DownloadUploadModule;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dynamicform.dao.DynamicFormCrudDAO;
import com.trigyn.jws.dynamicform.dao.IDynamicFormQueriesRepository;
import com.trigyn.jws.dynamicform.entities.DynamicForm;
import com.trigyn.jws.dynamicform.entities.DynamicFormSaveQuery;
import com.trigyn.jws.dynamicform.utils.Constant;

@Component("dynamic-form")
public class DynamicFormModule implements DownloadUploadModule<DynamicForm> {

	@Autowired
	private PropertyMasterDAO propertyMasterDAO 						= null;
	
	@Autowired
	private IDynamicFormQueriesRepository dynamicFormQueriesRepository 	= null;
	
	@Autowired
	private FileUtilities fileUtilities  								= null;
	
	@Autowired
	private DynamicFormCrudDAO dynamicFormDAO 							= null;
	
	@Override
	public void downloadCodeToLocal(DynamicForm a_dynamicForm) throws Exception {
		List<DynamicForm>  formList = new ArrayList<>();
		if(a_dynamicForm != null) {
			formList.add(a_dynamicForm);
		}else {
			formList = dynamicFormDAO.getAllDynamicForms(Constant.DEFAULT_FORM_TYPE);
		}
		
		String templateDirectory 		= Constant.DYNAMIC_FORM_DIRECTORY_NAME;
		String ftlCustomExtension 		= Constant.CUSTOM_FILE_EXTENSION;
		String selectQuery 				= Constant.DYNAMIC_FORM_SELECT_FILE_NAME;
		String htmlBody 				= Constant.DYNAMIC_FORM_HTML_FILE_NAME;
		String saveQuery 				= Constant.DYNAMIC_FORM_SAVE_FILE_NAME;
		String folderLocation 			= propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		folderLocation 					= folderLocation +File.separator+templateDirectory;
		
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

	@Override
	public void uploadCodeToDB(String uploadFileName) throws Exception {
		String user 				="admin";
		String ftlCustomExtension 		= Constant.DYNAMIC_FORM_DIRECTORY_NAME;
		String templateDirectory 		= Constant.CUSTOM_FILE_EXTENSION;
		String selectQuery 				= Constant.DYNAMIC_FORM_SELECT_FILE_NAME;
		String htmlBody 				= Constant.DYNAMIC_FORM_HTML_FILE_NAME;
		String saveQuery 				= Constant.DYNAMIC_FORM_SAVE_FILE_NAME;
		
		String folderLocation 		= propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		folderLocation 				= folderLocation +File.separator+templateDirectory;
		File directory 				= new File(folderLocation);
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
        		  if(!StringUtils.isBlank(uploadFileName)) {
        			  if(name.equalsIgnoreCase(uploadFileName)) {
        				  return new File(current, name).isDirectory();  
        			  }
        		  }else {
        			  return new File(current, name).isDirectory();
        		  }
				return false;
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
	
}
