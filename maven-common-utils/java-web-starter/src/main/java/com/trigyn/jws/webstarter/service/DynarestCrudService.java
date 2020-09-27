package com.trigyn.jws.webstarter.service;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dynarest.dao.JwsDynamicRestDAORepository;
import com.trigyn.jws.dynarest.dao.JwsDynamicRestDetailsRepository;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDaoDetail;
import com.trigyn.jws.dynarest.service.JwsDynamicRestDetailService;
import com.trigyn.jws.dynarest.utils.Constants;
import com.trigyn.jws.webstarter.utils.DownloadUploadModule;

@Service
@Transactional
public class DynarestCrudService {
	
	@Autowired
	private PropertyMasterDAO propertyMasterDAO 							= null;
	
	@Autowired
	private FileUtilities fileUtilities  									= null;
	
	@Autowired
	@Qualifier("dynamic-rest")
	private DownloadUploadModule downloadUploadModule 						= null;
	
	@Autowired
	private JwsDynamicRestDetailsRepository dynamicRestDetailsRepository	= null ;
	
	@Autowired
	private JwsDynamicRestDAORepository dynamicRestDAORepository 			= null ;
	
	@Autowired
	private JwsDynarestDAO dynarestDAO 										= null;
	
	@Autowired
	private JwsDynamicRestDetailService dynamicRestDetailService 			= null;
	
	public void downloadDynamicRestCode() throws Exception {
		downloadUploadModule.downloadCodeToLocal();
	}

	public void uploadDynamicRestCode() throws Exception {
		downloadUploadModule.uploadCodeToDB();
	}

	public  String getContentForDevEnvironment(String formName, String fileName) throws Exception {
		
		String ftlCustomExtension = ".tgn";
		String templateDirectory = "DynamicRest";
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
	
	
	@Transactional(readOnly = false)
	public Boolean saveDAOQueries(MultiValueMap<String, String> formData) throws Exception {
		String dynarestUrl 			= formData.getFirst("dynarestUrl");
		String dynarestMethodName 	= formData.getFirst("dynarestMethodName");
		String variableName 		= formData.getFirst("variableName");
		String daoQueryDetails 		= formData.getFirst("daoQueryDetails");
		
		Integer dynamicRestId		= dynamicRestDetailsRepository.findByJwsDynamicRestId(dynarestUrl, dynarestMethodName);
		
		List<String> variableNameList 		= new ObjectMapper().readValue(variableName, List.class);
		List<String> daoQueryDetailsList 	= new ObjectMapper().readValue(daoQueryDetails, List.class);
		
		List<JwsDynamicRestDaoDetail> dynamicRestDaoDetailsList = new ArrayList<>();
		if(!CollectionUtils.isEmpty(daoQueryDetailsList)) {
			
			for(int counter = 0; counter < variableNameList.size() ; counter++) {
				JwsDynamicRestDaoDetail dynamicRestDaoDetail = new JwsDynamicRestDaoDetail();
				dynamicRestDaoDetail.setJwsDynamicRestDetailId(dynamicRestId);
				dynamicRestDaoDetail.setJwsResultVariableName(variableNameList.get(counter));
				dynamicRestDaoDetail.setJwsDaoQueryTemplate(daoQueryDetailsList.get(counter));
				dynamicRestDaoDetail.setJwsQuerySequence(counter+1);
				dynamicRestDaoDetailsList.add(dynamicRestDaoDetail);
				dynamicRestDAORepository.saveAndFlush(dynamicRestDaoDetail);
			}
		}
		updateLocalClassFiles();
		return true;
	}
	
	private void updateLocalClassFiles() throws Exception {
		String path = propertyMasterDAO.findPropertyMasterValue("system","system", Constants.DYNAREST_CLASS_FILE_PATH);
		File file = Paths.get(path).toFile();
		if (file.exists()) {
			file.delete();
		}
		File sourceFile = File.createTempFile(Constants.SERVICE_CLASS_NAME, ".java");
        String className = sourceFile.getName().replaceAll(".java", "");
		dynamicRestDetailService.precompileClassAndGetFileLocation(className, sourceFile);
	}

	@Transactional(readOnly = false)
	public void deleteDAOQueries(MultiValueMap<String, String> formData) throws Exception {
		String dynarestUrl 			= formData.getFirst("dynarestUrl");
		String dynarestMethodName 	= formData.getFirst("dynarestMethodName");
		
		Integer dynamicRestId		= dynamicRestDetailsRepository.findByJwsDynamicRestId(dynarestUrl, dynarestMethodName);
		dynarestDAO.deleteDAOQueries(dynamicRestId);
	}
	
	
}
