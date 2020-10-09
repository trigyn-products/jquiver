package com.trigyn.jws.dynarest.service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.DownloadUploadModule;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDaoDetail;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;

@Component("dynamic-rest")
public class DynamicRestModule implements DownloadUploadModule<JwsDynamicRestDetail> {

	@Autowired
	private PropertyMasterDAO propertyMasterDAO = null;
	
	@Autowired
	private JwsDynarestDAO dynarestDAO = null;
	
	@Autowired
	private FileUtilities fileUtilities  = null;
	
	
	@Override
	public void downloadCodeToLocal(JwsDynamicRestDetail a_dynarestDetails) throws Exception {
		List<JwsDynamicRestDetail>  dynamicRestDetails = new ArrayList<>();
		if(a_dynarestDetails != null) {
			dynamicRestDetails.add(a_dynarestDetails);
		}else {
			dynamicRestDetails = dynarestDAO.getAllDynamicRestDetails();
		}
		
		String templateDirectory 		= "DynamicRest";
		String ftlCustomExtension 		= ".tgn";
		String serviceLogic 			= "serviceLogic";
		String saveQuery 				= "saveQuery-";
		String folderLocation 			= propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		folderLocation 					= folderLocation +File.separator+templateDirectory;
		
		for (JwsDynamicRestDetail currentDynamicRestDetail : dynamicRestDetails) {
			boolean isCheckSumChanged = false;
				String formName = currentDynamicRestDetail.getJwsMethodName();
				String formFolder =  folderLocation + File.separator + formName;
				if(!new File(formFolder).exists()) {
					File fileDirectory = new File(formFolder);
					fileDirectory.mkdirs();
				}
				// select
				String serviceLogicChecksum = fileUtilities.checkFileContents(serviceLogic, formFolder, currentDynamicRestDetail.getJwsServiceLogic(),currentDynamicRestDetail.getJwsServiceLogicChecksum(),ftlCustomExtension);
				if(serviceLogic!= null) {
					isCheckSumChanged = true;
					currentDynamicRestDetail.setJwsServiceLogicChecksum(serviceLogicChecksum);
				}
				
				
				//save
				for (JwsDynamicRestDaoDetail daoDetail : currentDynamicRestDetail.getJwsDynamicRestDaoDetails()) {
					String sequence = saveQuery+daoDetail.getJwsQuerySequence();
					String checksum =	fileUtilities.checkFileContents(sequence , formFolder, daoDetail.getJwsDaoQueryTemplate(),daoDetail.getChecksum(),ftlCustomExtension);
					if(checksum!= null) {
						isCheckSumChanged = true;
						daoDetail.setChecksum(checksum);
					}
				}
				// save checksum
				if(isCheckSumChanged) {
					dynarestDAO.saveJwsDynamicRestDetail(currentDynamicRestDetail);
				}	
		}
	

	}

	@Override
	public void uploadCodeToDB(String uploadFileName) throws Exception {
		
		String ftlCustomExtension 	= ".tgn";
		String templateDirectory 	= "DynamicRest";
		String serviceLogic 		= "serviceLogic";
		String saveQuery 			= "saveQuery-";
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
        	String serviceLogicCheckSum = null;
        	String currentDirectoryName = currentDirectory.getName();
        	JwsDynamicRestDetail dynamicRestDetail = dynarestDAO.getDynamicRestDetailsByName(currentDirectoryName);
        	
        	if(dynamicRestDetail ==  null) {
        		dynamicRestDetail = new JwsDynamicRestDetail();
        		dynamicRestDetail.setJwsRequestTypeId(2);
        		dynamicRestDetail.setJwsResponseProducerTypeId(7);
        		dynamicRestDetail.setJwsRbacId(1);
        		dynamicRestDetail.setJwsPlatformId(1);
        		dynamicRestDetail.setJwsDynamicRestUrl("/test");
        		dynamicRestDetail.setJwsMethodName(currentDirectoryName);
        		dynamicRestDetail.setJwsMethodDescription("Uploaded from Local Directory");
        	}
				File[] directoryFiles = currentDirectory.listFiles(textFilter);
				Integer filesPresent = directoryFiles.length;
				if(filesPresent >= 2) {
					File serviceLogicFile = new File(currentDirectory.getAbsolutePath()+File.separator+serviceLogic+ftlCustomExtension);
					if(!serviceLogicFile.exists() ) {
						throw new Exception("serviceLogic file is mandatory  for saving dynamic rest detail"+currentDirectoryName);
					}else {
						// set select
						serviceLogicCheckSum = fileUtilities.generateFileChecksum(serviceLogicFile);
						if(!serviceLogicCheckSum.equalsIgnoreCase(dynamicRestDetail.getJwsServiceLogicChecksum())) {
							
							dynamicRestDetail.setJwsServiceLogic(fileUtilities.readContentsOfFile(serviceLogicFile.getAbsolutePath()));
							dynamicRestDetail.setJwsServiceLogicChecksum(serviceLogicCheckSum);
						}
						
						// saveQuery
						dynarestDAO.saveJwsDynamicRestDetail(dynamicRestDetail);
					
						if(dynamicRestDetail.getJwsDynamicRestDaoDetails()!=null) {
							dynarestDAO.deleteDAOQueries(dynamicRestDetail.getJwsDynamicRestId());
						}
						List<JwsDynamicRestDaoDetail> dynamicRestDaoDetails = new ArrayList<>();
						int i=0;
						for (File file : directoryFiles) {
							Integer saveQueryFiles = filesPresent - 1;
							if(saveQueryFiles >= 1) {
								if(file.getName().contains(saveQuery)) {
									i++;
										JwsDynamicRestDaoDetail daoDetail = new JwsDynamicRestDaoDetail();
										File saveQueryFile = new File(currentDirectory.getAbsolutePath()+File.separator+saveQuery+i+ftlCustomExtension);
										if(saveQueryFile.exists()) {
											daoDetail.setJwsDynamicRestDetailId(dynamicRestDetail.getJwsDynamicRestId());
											daoDetail.setJwsResultVariableName("test");
											daoDetail.setChecksum(fileUtilities.generateFileChecksum(saveQueryFile));
											daoDetail.setJwsQuerySequence(i);
											daoDetail.setJwsDaoQueryTemplate(fileUtilities.readContentsOfFile(saveQueryFile.getAbsolutePath()));
											dynamicRestDaoDetails.add(daoDetail);
										}else {
											throw new Exception("saveQuery file sequence is incorrect"+currentDirectoryName);
										}
										dynarestDAO.saveJwsDynamicRestDAO(daoDetail);
								}
								
							}else {
								throw new Exception("saveQuery file is mandatory  for saving rest details"+currentDirectoryName);
							}
						
						}
					}
							
				}else {
					throw new Exception("Invalid count of files for saving dynamic rest details"+currentDirectoryName);
				}
		}

	}

}
