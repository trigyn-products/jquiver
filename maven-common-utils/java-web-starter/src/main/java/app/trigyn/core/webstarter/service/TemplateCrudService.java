package app.trigyn.core.webstarter.service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.trigyn.common.dbutils.repository.PropertyMasterDAO;
import app.trigyn.common.dbutils.utils.FileUtilities;
import app.trigyn.core.templating.dao.DBTemplatingRepository;
import app.trigyn.core.templating.dao.TemplateDAO;
import app.trigyn.core.templating.entities.TemplateMaster;
import app.trigyn.core.templating.service.DBTemplatingService;
import app.trigyn.core.templating.vo.TemplateVO;

@Service
@Transactional
public class TemplateCrudService {

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
	
	public void downloadTemplates() throws Exception {
		
		List<TemplateMaster> templates = dbTemplatingService.getAllTemplates();
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
	
	public void uploadTemplates() throws Exception {
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
	                return name.toLowerCase().endsWith(ftlCustomExtension);
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

	
	   public String checkVelocityData(String velocityName) throws Exception {
		   return templateDAO.checkVelocityData(velocityName);
	   }
}
