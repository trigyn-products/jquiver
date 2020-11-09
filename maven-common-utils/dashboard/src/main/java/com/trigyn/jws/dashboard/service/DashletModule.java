package com.trigyn.jws.dashboard.service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dashboard.dao.DashletDAO;
import com.trigyn.jws.dashboard.entities.Dashlet;
import com.trigyn.jws.dashboard.repository.interfaces.IDashletRepository;
import com.trigyn.jws.dashboard.utility.Constants;
import com.trigyn.jws.dashboard.vo.DashletVO;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.DownloadUploadModule;
import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.templating.utils.Constant;

@Component("dashlet")
public class DashletModule implements DownloadUploadModule<Dashlet> {

	@Autowired
    private DashletDAO dashletDAO 						= null;
    
	@Autowired
	private PropertyMasterDAO propertyMasterDAO			= null;
    
	@Autowired
	private FileUtilities fileUtilities 				= null;
	
	@Autowired
    private IDashletRepository iDashletRepository 		= null;
    
	@Autowired
	private ModuleVersionService moduleVersionService	= null;

	private Map<String, String> moduleDetailsMap 		= new HashMap<>();
	
	@Override
	public void downloadCodeToLocal(Dashlet a_dashlet, String folderLocation) throws Exception {
		
		List<Dashlet> dashlets = new ArrayList<>();
		if(a_dashlet != null) {
			dashlets.add(a_dashlet);
		}else {
			dashlets =  dashletDAO.getAllDashlets(Constants.DEFAULT_DASHLET_TYPE_ID);
		}
		
		String ftlCustomExtension 		= ".tgn";
		String templateDirectory 		= Constants.DASHLET_DIRECTORY_NAME;
		String selectQuery 				= "selectQuery";
		String htmlBody 				= "htmlContent";
//		String folderLocation 			= propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		folderLocation 					= folderLocation +File.separator+templateDirectory;
		
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
			moduleDetailsMap.put(dashlet.getDashletId(), dashletName);
		}
		
	}


	@Override
	public void uploadCodeToDB(String uploadFileName) throws Exception {
		String user 				= "admin";
		String ftlCustomExtension 	= Constants.CUSTOM_FILE_EXTENSION;
		String templateDirectory 	= Constants.DASHLET_DIRECTORY_NAME;
		String selectQuery 			= Constants.DASHLET_QUERY_FILE_NAME;
		String htmlBody 			= Constants.DASHLET_HTML_FILE_NAME;
		String folderLocation 		= propertyMasterDAO.findPropertyMasterValue("system", "system", Constants.TEMPORARY_STORAGE_PATH);
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
						saveDashletVersioning(dashlet);
					}	// saveQuery
				}else {
					throw new Exception("Invalid count of files for saving dashlet"+currentDirectoryName);
				}
		
        }
		
	}

	public Map<String, String> getModuleDetailsMap() {
		return moduleDetailsMap;
	}

	public void setModuleDetailsMap(Map<String, String> moduleDetailsMap) {
		this.moduleDetailsMap = moduleDetailsMap;
	}
	
	public void saveDashletVersioning(Dashlet dashlet) throws Exception {
		DashletVO dashletVO = new DashletVO();
		dashletVO.setDashletId(dashletVO.getDashletId());
		dashletVO.setDashletName(dashlet.getDashletName());
		dashletVO.setDashletTitle(dashlet.getDashletTitle());
		dashletVO.setXCoordinate(dashlet.getXCoordinate());
		dashletVO.setYCoordinate(dashlet.getYCoordinate());
		dashletVO.setWidth(dashlet.getWidth());
		dashletVO.setHeight(dashlet.getHeight());
		dashletVO.setHeight(dashlet.getHeight());
		dashletVO.setDashletQuery(dashlet.getDashletQuery());
		dashletVO.setDashletBody(dashlet.getDashletBody());
		dashletVO.setShowHeader(dashlet.getShowHeader());
		dashletVO.setContextId(dashlet.getContextId());
		dashletVO.setIsActive(dashletVO.getIsActive());
		moduleVersionService.saveModuleVersion(dashletVO,null, dashlet.getDashletId(), "dashlet", Constants.UPLOAD_SOURCE_VERSION_TYPE);
	}
}
