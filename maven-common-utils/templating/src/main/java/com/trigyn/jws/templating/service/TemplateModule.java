package com.trigyn.jws.templating.service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.DownloadUploadModule;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.templating.dao.DBTemplatingRepository;
import com.trigyn.jws.templating.dao.TemplateDAO;
import com.trigyn.jws.templating.entities.TemplateMaster;
import com.trigyn.jws.templating.vo.TemplateVO;

@Component("template")
public class TemplateModule implements DownloadUploadModule {

	@Autowired
	private DBTemplatingService dbTemplatingService = null;
	
	@Autowired
	private PropertyMasterDAO propertyMasterDAO = null;
	
	@Autowired
	private TemplateDAO templateDAO = null;
	
	@Autowired
	private DBTemplatingRepository dbTemplatingRepository = null;
	
	@Autowired
	private FileUtilities fileUtilities  = null;
	
	@Override
	public void downloadCodeToLocal(Object templateObj) throws Exception {
		List<TemplateMaster> templates = new ArrayList<>();
		if(templateObj != null) {
			TemplateMaster templateMaster = (TemplateMaster) templateObj;
			templates.add(templateMaster);
		}else {
			templates = dbTemplatingService.getAllTemplates();
		}
		
		List<TemplateVO> templateVOs = templates.stream().map((template) -> new TemplateVO(template.getTemplateId(),
                template.getTemplateName(), template.getTemplate(), template.getChecksum())).collect(Collectors.toList());
		
		String ftlCustomExtension = ".tgn";
		String templateDirectory = "Templates";
		String folderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		folderLocation = folderLocation +File.separator+templateDirectory;
		
		if(!new File(folderLocation).exists()) {
			File fileDirectory = new File(folderLocation);
			fileDirectory.mkdirs();
		}
		
		for (TemplateVO templateVO : templateVOs) {
			String newFileCheckSum = null;
			File file = new File(folderLocation+ File.separator+templateVO.getTemplateName()+ftlCustomExtension);
			if(file.exists()) {
			
				String generatedFileCheckSum = fileUtilities.generateFileChecksum(file);
				String existingCheckSum =  templateVO.getChecksum();
				if(!generatedFileCheckSum.equalsIgnoreCase(existingCheckSum)){
					file.delete();
					newFileCheckSum = fileUtilities.writeFileContents(templateVO.getTemplate(), file);
					templateVO.setChecksum(newFileCheckSum);
				}
				else {
					templateVO.setChecksumChanged(false);
				}
			}else {
				newFileCheckSum = fileUtilities.writeFileContents(templateVO.getTemplate(), file);
				templateVO.setChecksum(newFileCheckSum);
			}
		}
		for (TemplateVO templateVO : templateVOs) {
			if(templateVO.isChecksumChanged()) {
				templateDAO.updateChecksum(templateVO);
			}	
		}
		
	}

	@Override
	public void uploadCodeToDB(String uploadFileName) throws Exception {
		String user ="admin";
		String ftlCustomExtension = ".tgn";
		String templateDirectory = "Templates";
		String folderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		folderLocation = folderLocation +File.separator+templateDirectory;
		File directory = new File(folderLocation);
		if(!directory.exists()) {
			throw new Exception("No such directory present");
		}
		FilenameFilter textFilter = new FilenameFilter() {
	            public boolean accept(File dir, String name) {
	            	if(!StringUtils.isBlank(uploadFileName)) {
	            		return name.toLowerCase().equalsIgnoreCase(uploadFileName + ftlCustomExtension);
	            	}else {
	            		return name.toLowerCase().endsWith(ftlCustomExtension);
	            	}
	            }
	        };
        File[] files = directory.listFiles(textFilter);
        for (File file : files) {
			if(file.isFile()) {
				String fileName = file.getName().replace(ftlCustomExtension,"");
				TemplateMaster template = templateDAO.getTemplateDetailsByName(fileName);
				String content = fileUtilities.readContentsOfFile(file.getAbsolutePath());
				String generateFileCheckSum = fileUtilities.generateFileChecksum(file);
				if(template == null) {
					template = new TemplateMaster();
					template.setTemplate(content);
					template.setChecksum(generateFileCheckSum);
					template.setTemplateName(fileName);
					template.setUpdatedDate(new Date());
					template.setUpdatedBy(user);
					template.setCreatedBy(user);
					dbTemplatingRepository.save(template);
				}else{
					if(!generateFileCheckSum.equalsIgnoreCase(template.getChecksum())) {
							template.setTemplate(content);
							template.setChecksum(generateFileCheckSum);
							template.setUpdatedBy(user);
							template.setUpdatedDate(new Date());
							dbTemplatingRepository.save(template);
						}
					}	
				}
			}

	}


}
