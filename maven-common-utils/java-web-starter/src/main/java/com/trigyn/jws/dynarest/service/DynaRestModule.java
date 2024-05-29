package com.trigyn.jws.dynarest.service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.trigyn.jws.dbutils.service.DownloadUploadModule;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dbutils.vo.xml.DynaRestExportVO;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDaoDetail;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;
import com.trigyn.jws.dynarest.utils.Constants;

@Component("dynamic-rest")
public class DynaRestModule implements DownloadUploadModule<DynaRest> {

	@Autowired
	private FileUtilities fileUtilities = null;

	@Autowired
	private JwsDynarestDAO jwsDynarestDAO = null;
	
	private Map<String, Map<String, Object>>	moduleDetailsMap				= new HashMap<>();

	@Override
	public void downloadCodeToLocal(DynaRest object, String folderLocation) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void uploadCodeToDB(String uploadFileName) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void exportData(Object object, String folderLocation) throws Exception {
		JwsDynamicRestDetail			a_dynaRest	= (JwsDynamicRestDetail) object;
		List<JwsDynamicRestDetail>	dynaRestList		= new ArrayList<>();
		if (a_dynaRest != null) {
			dynaRestList.add(a_dynaRest);
		} else {
			dynaRestList = jwsDynarestDAO.getAllDynamicRestDetails();
		}

		String	templateDirectory	= Constants.DYNAMIC_REST_DIRECTORY_NAME;
		String	ftlCustomExtension	= Constants.CUSTOM_FILE_EXTENSION;
		String	selectQuery			= Constants.DYNAMIC_REST_SELECT_FILE_NAME;
		String	serviceLogic			= Constants.DYNAMIC_REST_SERVICE_LOGIC_FILE_NAME;
		// String folderLocation = propertyMasterDAO.findPropertyMasterValue("system",
		// "system", "template-storage-path");
		folderLocation = folderLocation + File.separator + templateDirectory;

		for (JwsDynamicRestDetail dynaRest : dynaRestList) {
			boolean	isCheckSumChanged	= false;
			String	formName			= dynaRest.getJwsDynamicRestUrl();
			String	formFolder			= folderLocation + File.separator + formName;
			if (!new File(formFolder).exists()) {
				File fileDirectory = new File(formFolder);
				fileDirectory.mkdirs();
			}

			// htmlBody
			String seviceLogicCheckSum = fileUtilities.checkFileContents(serviceLogic, formFolder, dynaRest.getJwsServiceLogic(),
					dynaRest.getServiceLogicChecksum(), ftlCustomExtension);
			if (seviceLogicCheckSum != null) {
				isCheckSumChanged = true;
				dynaRest.setServiceLogicChecksum(seviceLogicCheckSum);
			}

			// save
			Map<Integer, String> daoDetailsFileNameMap = new HashMap<>();
			Map<Integer, String> daoDetailsVariableNameMap = new HashMap<>();
			Map<Integer, Integer> daoDetailsQueryTypeMap = new HashMap<>();
			Map<Integer, String> daoDetailsDatasourceIdMap = new HashMap<>();
			for (JwsDynamicRestDaoDetail dynaRestSelectQuery : dynaRest.getJwsDynamicRestDaoDetails()) {
				Integer	sequenceNum	= dynaRestSelectQuery.getJwsQuerySequence();
				String	sequence	= selectQuery + sequenceNum;
				String	checksum	= fileUtilities.checkFileContents(sequence, formFolder, dynaRestSelectQuery.getJwsDaoQueryTemplate(),
						dynaRestSelectQuery.getDaoQueryChecksum(), ftlCustomExtension);
				daoDetailsFileNameMap.put(sequenceNum, sequence + ftlCustomExtension);
				daoDetailsVariableNameMap.put(sequenceNum, dynaRestSelectQuery.getJwsResultVariableName());
				daoDetailsQueryTypeMap.put(sequenceNum, dynaRestSelectQuery.getQueryType());
				daoDetailsDatasourceIdMap.put(sequenceNum, dynaRestSelectQuery.getDatasourceId());
				if (checksum != null) {
					isCheckSumChanged = true;
					dynaRestSelectQuery.setDaoQueryChecksum(checksum);
				}
			}

			// save checksum
			if (isCheckSumChanged) {
				jwsDynarestDAO.saveJwsDynamicRestDetail(dynaRest);
			}
			List<String> scriptLibIdList = jwsDynarestDAO.getscriptLibId(dynaRest.getJwsDynamicRestId());
			List<String>					scriptLibId		= new ArrayList<>();
			for(int iScriptUploadCounter = 0 ; iScriptUploadCounter<scriptLibIdList.size(); iScriptUploadCounter++) {
				scriptLibId.add("\"" +scriptLibIdList.get(iScriptUploadCounter)+ "\"");
				dynaRest.setScriptLibraryId(scriptLibId.toString());
			}
			DynaRestExportVO	dynamicRestExportVO	= new DynaRestExportVO(dynaRest.getJwsDynamicRestId(), dynaRest.getJwsDynamicRestUrl(), 
					dynaRest.getJwsMethodDescription(), dynaRest.getJwsMethodName(), dynaRest.getJwsPlatformId(), dynaRest.getJwsRbacId(), 
					serviceLogic + ftlCustomExtension, dynaRest.getJwsRequestTypeId(), dynaRest.getJwsResponseProducerTypeId(), 
					dynaRest.getJwsAllowFiles(), dynaRest.getJwsDynamicRestTypeId(), dynaRest.getJwsHeaderJson(), daoDetailsFileNameMap, 
					daoDetailsVariableNameMap, daoDetailsQueryTypeMap, daoDetailsDatasourceIdMap, dynaRest.getLastUpdatedTs(),dynaRest.getHidedaoquery()
					,dynaRest.getIsSecured(), dynaRest.getIsCustomUpdated(),dynaRest.getScriptLibraryId());

			Map<String, Object>	map					= new HashMap<>();
			map.put("moduleName", formName);
			map.put("moduleObject", dynamicRestExportVO);
			moduleDetailsMap.put(dynaRest.getJwsDynamicRestId(), map);
		}

	}

	@Override
	public Object importData(String folderLocation, String uploadFileName, String uploadID, Object importObject)
			throws Exception {
		String					user				= "admin";

		DynaRestExportVO		dynaRestExportVO	= (DynaRestExportVO) importObject;

		String					serviceLogicFileName			= dynaRestExportVO.getServiceLogicFileName();
		Map<Integer, String> daoDetailsFileNameMap = dynaRestExportVO.getDaoDetailsFileNameMap();
		Map<Integer, String> daoDetailsVariableNameMap =dynaRestExportVO.getDaoDetailsVariableNameMap();
		Map<Integer, Integer> daoDetailsQueryTypeMap = dynaRestExportVO.getDaoDetailsQueryTypeMap();
		Map<Integer, String> daoDetailsDatasourceIdMap = dynaRestExportVO.getDaoDetailsDatasourceIdMap();
		String					ftlCustomExtension	= "." + dynaRestExportVO.getServiceLogicFileName().split("\\.")[1];

		JwsDynamicRestDetail				dynaRest			= null;
		File					directory			= new File(folderLocation);
		if (!directory.exists()) {
			throw new Exception("No such directory present");
		}
		FilenameFilter	textFilter	= new FilenameFilter() {
										public boolean accept(File dir, String name) {
											return name.toLowerCase().endsWith(ftlCustomExtension);
										}
									};

		File[]			directories	= directory.listFiles((new FilenameFilter() {
										@Override
										public boolean accept(File current, String name) {
											if (!StringUtils.isBlank(uploadFileName)) {
												if (name.equalsIgnoreCase(uploadFileName)) {
													return new File(current, name).isDirectory();
												}
											} else {
												return new File(current, name).isDirectory();
											}
											return false;
										}
									}));
		for (File currentDirectory : directories) {
			String	selectCheckSum			= null;
			String	currentDirectoryName	= currentDirectory.getName();
			if (currentDirectoryName.equals(uploadFileName)) {
				JwsDynamicRestDetail dynamicRestEntity = jwsDynarestDAO.findDynamicRestById(uploadID);
				dynaRest = new JwsDynamicRestDetail();
				if (dynamicRestEntity != null) {
					dynaRest = dynamicRestEntity.getObject();
				} else {
					dynaRest.setCreatedBy(user);
					dynaRest.setCreatedDate(new Date());
				}

				dynaRest.setJwsAllowFiles(dynaRestExportVO.getJwsAllowFiles());
				dynaRest.setJwsDynamicRestId(dynaRestExportVO.getJwsDynamicRestId());
				dynaRest.setJwsDynamicRestTypeId(dynaRestExportVO.getJwsDynamicRestTypeId());
				dynaRest.setJwsDynamicRestUrl(dynaRestExportVO.getJwsDynamicRestUrl());
				dynaRest.setJwsHeaderJson(dynaRestExportVO.getJwsHeaderJson());
				dynaRest.setJwsMethodDescription(dynaRestExportVO.getJwsMethodDescription());
				dynaRest.setJwsMethodName(dynaRestExportVO.getJwsMethodName());
				dynaRest.setJwsPlatformId(dynaRestExportVO.getJwsPlatformId());
				dynaRest.setJwsRbacId(dynaRestExportVO.getJwsRbacId());
				dynaRest.setJwsRequestTypeId(dynaRestExportVO.getJwsRequestTypeId());
				dynaRest.setJwsResponseProducerTypeId(dynaRestExportVO.getJwsResponseProducerTypeId());
				dynaRest.setLastUpdatedTs(new Date());
				dynaRest.setHidedaoquery(dynaRestExportVO.getHideDaoQuery());//Added for New Column for hiding DAO Query Editor
				dynaRest.setIsCustomUpdated(dynaRestExportVO.getIsCustomUpdated());
				dynaRest.setIsSecured(dynaRestExportVO.getIsSecured());
				dynaRest.setScriptLibraryId(dynaRestExportVO.getScriptLibraryId());
				File[]	directoryFiles	= currentDirectory.listFiles(textFilter);
				Integer	filesPresent	= directoryFiles.length;
				if (filesPresent >= 2) {
					File	serviceLogicFile		= new File(
							currentDirectory.getAbsolutePath() + File.separator + serviceLogicFileName); 
					if (!serviceLogicFile.exists()) {
						throw new Exception(
								"service logic  file is mandatory  for saving dynamic rest api"
										+ currentDirectoryName);
					} else {
						// set select
						selectCheckSum = fileUtilities.generateFileChecksum(serviceLogicFile);

						dynaRest.setJwsServiceLogic(fileUtilities.readContentsOfFile(serviceLogicFile.getAbsolutePath()));
						dynaRest.setServiceLogicChecksum(selectCheckSum);

				
						List<JwsDynamicRestDaoDetail> jwsDynamicRestDaoDetails = new ArrayList<>();

						if (daoDetailsFileNameMap != null && !daoDetailsFileNameMap.isEmpty()) {
							for (Entry<Integer, String> entry : daoDetailsFileNameMap.entrySet()) {
								Integer					sequence		= entry.getKey();
								String					daoDetailQuery		= entry.getValue();

								JwsDynamicRestDaoDetail	daoDetail	= new JwsDynamicRestDaoDetail();
								File					daoDetailFile	= new File(
										currentDirectory.getAbsolutePath() + File.separator + daoDetailQuery);
								if (daoDetailFile.exists()) {
									daoDetail.setJwsDynamicRestDetailId(dynaRest.getJwsDynamicRestId());
									daoDetail.setDaoQueryChecksum(fileUtilities.generateFileChecksum(daoDetailFile));
									daoDetail.setJwsQuerySequence(sequence);
									daoDetail.setJwsDaoQueryTemplate(
											fileUtilities.readContentsOfFile(daoDetailFile.getAbsolutePath()));
									daoDetail.setDatasourceId(daoDetailsDatasourceIdMap.get(sequence));
									daoDetail.setJwsResultVariableName(daoDetailsVariableNameMap.get(sequence));
									daoDetail.setQueryType(daoDetailsQueryTypeMap.get(sequence));
									jwsDynamicRestDaoDetails.add(daoDetail);
								} else {
									throw new Exception("saveQuery file sequence is incorrect" + currentDirectoryName);
								}
							}
						} else {
							throw new Exception(
									"saveQuery file is mandatory  for saving dynamic form" + currentDirectoryName);
						}

						dynaRest.setJwsDynamicRestDaoDetails(jwsDynamicRestDaoDetails);
					}

				} else {
					throw new Exception("Invalid count of files for saving dynamic rest api" + currentDirectoryName);
				}
			}
		}
		return dynaRest;
	}

	public Map<String, Map<String, Object>> getModuleDetailsMap() {
		return moduleDetailsMap;
	}

	public void setModuleDetailsMap(Map<String, Map<String, Object>> moduleDetailsMap) {
		this.moduleDetailsMap = moduleDetailsMap;
	}

}
