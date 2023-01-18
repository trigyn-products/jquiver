package com.trigyn.jws.webstarter.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dynarest.dao.JwsDynamicRestDAORepository;
import com.trigyn.jws.dynarest.dao.JwsDynamicRestDetailsRepository;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDaoDetail;
import com.trigyn.jws.dynarest.service.JwsDynamicRestDetailService;

@Service
@Transactional
public class DynarestCrudService {

	@Autowired
	private PropertyMasterDAO				propertyMasterDAO				= null;

	@Autowired
	private FileUtilities					fileUtilities					= null;

	@Autowired
	private JwsDynamicRestDetailsRepository	dynamicRestDetailsRepository	= null;

	@Autowired
	private JwsDynamicRestDAORepository		dynamicRestDAORepository		= null;

	@Autowired
	private JwsDynarestDAO					dynarestDAO						= null;

	@Autowired
	private JwsDynamicRestDetailService		dynamicRestDetailService		= null;

	@Autowired
	private ModuleVersionService			moduleVersionService			= null;

	public String getContentForDevEnvironment(String formName, String fileName) throws Exception {

		String	ftlCustomExtension	= ".tgn";
		String	templateDirectory	= "DynamicRest";
		String	folderLocation		= propertyMasterDAO.findPropertyMasterValue("system", "system",
				"template-storage-path");
		folderLocation = folderLocation + File.separator + templateDirectory + File.separator + formName;
		File directory = new File(folderLocation);
		if (!directory.exists()) {
			throw new Exception("No such directory present");
		}

		File selectFile = new File(folderLocation + File.separator + fileName + ftlCustomExtension);
		if (selectFile.exists()) {
			return fileUtilities.readContentsOfFile(selectFile.getAbsolutePath());
		} else {
			throw new Exception("Please download the forms from dynamic form  listing  " + formName);
		}
	}

	@Transactional(readOnly = false)
	public String saveDAOQueries(MultiValueMap<String, String> formData) throws Exception {
		String							dynarestUrl			= formData.getFirst("dynarestUrl");
		String							dynarestMethodName	= formData.getFirst("dynarestMethodName");
		String							daoDetailsIds		= formData.getFirst("daoDetailsIds");
		String							variableName		= formData.getFirst("variableName");
		String							queryType			= formData.getFirst("queryType");
		String							daoQueryDetails		= formData.getFirst("daoQueryDetails");
		String							datasourceDetails   = formData.getFirst("datasourceDetails");

		String							dynamicRestId		= dynamicRestDetailsRepository
				.findByJwsDynamicRestId(dynarestUrl, dynarestMethodName);

		ObjectMapper					objectMapper		= new ObjectMapper();
		TypeReference<List<Integer>>	listOfInteger		= new TypeReference<List<Integer>>() {
															};

		List<Integer>					daoDetailsIdList	= new ArrayList<>();
		List<String>					variableNameList	= objectMapper.readValue(variableName, List.class);
		List<Integer>					queryTypeList		= objectMapper.readValue(queryType, listOfInteger);
		List<String>					daoQueryDetailsList	= objectMapper.readValue(daoQueryDetails, List.class);
		List<String>					datasourceDetailsList	= objectMapper.readValue(datasourceDetails, List.class);
		if (!StringUtils.isBlank(daoDetailsIds)) {
			daoDetailsIdList = new ObjectMapper().readValue(daoDetailsIds, listOfInteger);
		}

		List<JwsDynamicRestDaoDetail> dynamicRestDaoDetailsList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(daoQueryDetailsList)) {

			for (int counter = 0; counter < variableNameList.size(); counter++) {
				JwsDynamicRestDaoDetail dynamicRestDaoDetail = new JwsDynamicRestDaoDetail();
				if (!CollectionUtils.isEmpty(daoDetailsIdList) && daoDetailsIdList.size() > counter) {
					dynamicRestDaoDetail.setJwsDaoDetailsId(daoDetailsIdList.get(counter));
				}
				dynamicRestDaoDetail.setJwsDynamicRestDetailId(dynamicRestId);
				dynamicRestDaoDetail.setJwsResultVariableName(variableNameList.get(counter));
				dynamicRestDaoDetail.setQueryType(queryTypeList.get(counter));
				dynamicRestDaoDetail.setJwsDaoQueryTemplate(daoQueryDetailsList.get(counter));
				dynamicRestDaoDetail.setDatasourceId(datasourceDetailsList.get(counter));
				dynamicRestDaoDetail.setJwsQuerySequence(counter + 1);
				dynamicRestDaoDetailsList.add(dynamicRestDaoDetail);

			}
			dynamicRestDAORepository.saveAll(dynamicRestDaoDetailsList);
		}

		return dynamicRestId;

	}

	@Transactional(readOnly = false)
	public void deleteDAOQueries(MultiValueMap<String, String> formData) throws Exception {
		String							dynarestUrl			= formData.getFirst("dynarestUrl");
		String							dynarestMethodName	= formData.getFirst("dynarestMethodName");
		String							daoDetailsIds		= formData.getFirst("daoDetailsIds");
		ObjectMapper					objectMapper		= new ObjectMapper();
		TypeReference<List<Integer>>	listOfInteger		= new TypeReference<List<Integer>>() {
															};
		List<Integer>					daoDetailsIdList	= new ArrayList<>();
		if (!StringUtils.isBlank(daoDetailsIds)) {
			daoDetailsIdList = objectMapper.readValue(daoDetailsIds, listOfInteger);
		}

		String dynamicRestId = dynamicRestDetailsRepository.findByJwsDynamicRestId(dynarestUrl, dynarestMethodName);
		dynarestDAO.deleteDAOQueriesById(dynamicRestId, daoDetailsIdList);
	}

}
