package com.trigyn.jws.webstarter.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dashboard.dao.DashletDAO;
import com.trigyn.jws.dashboard.entities.Dashlet;
import com.trigyn.jws.dashboard.repository.interfaces.IDashletRepository;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.utils.FileUtilities;

@Component
public class DashletModule implements DownloadUploadModule {

	@Autowired
    private DashletDAO dashletDAO = null;
    
	@Autowired
	private PropertyMasterDAO propertyMasterDAO	= null;
    
	@Autowired
	private FileUtilities fileUtilities = null;
	
	@Autowired
    private IDashletRepository iDashletRepository = null;
	
	
	@Override
	public void downloadCodeToLocal() throws Exception {
		List<Dashlet> dashlets =  dashletDAO.getAllDashlets();
		String ftlCustomExtension = ".tgn";
		String templateDirectory = "Dashlets";
		String selectQuery = "selectQuery";
		String htmlBody = "hmtlQuery";
		String folderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		folderLocation = folderLocation +File.separator+templateDirectory;
		
		for (Dashlet dashlet : dashlets) {
			boolean isCheckSumChanged = false;
			String dashletName = dashlet.getDashletName();
			String formFolder =  folderLocation + File.separator + dashletName;
			if(!new File(formFolder).exists()) {
				File fileDirectory = new File(formFolder);
				fileDirectory.mkdirs();
			}
			// html
			String dashletBodySum = fileUtilities.checkFileContents(htmlBody, formFolder,dashlet.getDashletBody() ,dashlet.getDashletBodyChecksum(),ftlCustomExtension);
			if(dashletBodySum!= null) {
				isCheckSumChanged = true;
				dashlet.setDashletBodyChecksum(dashletBodySum);
			}
			
			//query
			String dashletQueryCheckSum  = fileUtilities.checkFileContents(selectQuery, formFolder, dashlet.getDashletQuery() ,dashlet.getDashletQueryChecksum(),ftlCustomExtension);
			if(dashletQueryCheckSum!= null) {
				isCheckSumChanged = true;
				dashlet.setDashletQueryChecksum(dashletQueryCheckSum);
			}
			
			// save checksum
			if(isCheckSumChanged) {
				iDashletRepository.save(dashlet);
			}	
		}
		
	}

	@Override
	public void uploadCodeToLocal() throws Exception {
		String user ="admin";
		String ftlCustomExtension = ".tgn";
		String templateDirectory = "Dashlets";
		String selectQuery = "selectQuery";
		String htmlBody = "hmtlQuery";
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
        	Dashlet dashlet = dashletDAO.getDashletByName(currentDirectoryName);
        	
        	if(dashlet ==  null) {
        		dashlet = new Dashlet();
        		dashlet.setCreatedBy(user);
        		dashlet.setCreatedDate(new Date());
        		dashlet.setDashletName(currentDirectoryName);
        		dashlet.setDashletTitle("Uploaded from Local Directory");
        	}
				File[] directoryFiles = currentDirectory.listFiles(textFilter);
				Integer filesPresent = directoryFiles.length;
				if(filesPresent == 2) {
					File selectFile = new File(currentDirectory.getAbsolutePath()+File.separator+selectQuery+ftlCustomExtension);
					File hmtlBodyFile = new File(currentDirectory.getAbsolutePath()+File.separator+htmlBody+ftlCustomExtension);
					if(!selectFile.exists() || !hmtlBodyFile.exists()) {
						throw new Exception("selectQuery  file not and hmtlQueryfile are mandatory  for saving dashlet"+currentDirectoryName);
					}else {
						// set select
						selectCheckSum = fileUtilities.generateFileChecksum(selectFile);
						if(!selectCheckSum.equalsIgnoreCase(dashlet.getDashletQueryChecksum())) {
							
							dashlet.setDashletQuery(fileUtilities.readContentsOfFile(selectFile.getAbsolutePath()));
							dashlet.setDashletQueryChecksum(selectCheckSum);
						}
						
						// set html
						htmlCheckSum = fileUtilities.generateFileChecksum(hmtlBodyFile);
						if(!htmlCheckSum.equalsIgnoreCase(dashlet.getDashletBodyChecksum())) {
							
							dashlet.setDashletBody(fileUtilities.readContentsOfFile(hmtlBodyFile.getAbsolutePath()));
							dashlet.setDashletBodyChecksum(htmlCheckSum);
						}
						iDashletRepository.save(dashlet);
					}	// saveQuery
				}else {
					throw new Exception("Invalid count of files for saving dashlet"+currentDirectoryName);
				}
		
        }
		
	}

	
}
