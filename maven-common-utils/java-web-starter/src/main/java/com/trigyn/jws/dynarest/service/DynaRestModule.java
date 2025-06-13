package com.trigyn.jws.dynarest.service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.service.DownloadUploadModule;
import com.trigyn.jws.dbutils.service.ModuleVersionService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.CustomeFileStorageException;
import com.trigyn.jws.dbutils.utils.FileUtilities;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dbutils.vo.xml.DynaRestExportVO;
import com.trigyn.jws.dbutils.vo.xml.MetadataXMLVO;
import com.trigyn.jws.dbutils.vo.xml.Modules;
import com.trigyn.jws.dbutils.vo.xml.XMLVO;
import com.trigyn.jws.dynamicform.utils.Constant;
import com.trigyn.jws.dynarest.dao.FileUploadConfigDAO;
import com.trigyn.jws.dynarest.dao.JwsDynarestDAO;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDaoDetail;
import com.trigyn.jws.dynarest.entities.JwsDynamicRestDetail;
import com.trigyn.jws.dynarest.utils.Constants;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryConnection;
import com.trigyn.jws.usermanagement.entities.JwsEntityRoleAssociation;
import com.trigyn.jws.usermanagement.repository.JwsEntityRoleAssociationRepository;
import com.trigyn.jws.webstarter.service.ExportService;
import com.trigyn.jws.webstarter.utils.ImportExportUtility;
import com.trigyn.jws.webstarter.utils.XMLUtil;
import com.trigyn.jws.webstarter.vo.RestApiDetailsJsonVO;
import com.trigyn.jws.webstarter.xml.PermissionXMLVO;

import jakarta.servlet.http.HttpServletResponse;

@Component("dynamic-rest")
public class DynaRestModule implements DownloadUploadModule<JwsDynamicRestDetail> {

	private final static Logger logger = LoggerFactory.getLogger(ExportService.class);

	@Autowired
	private FileUtilities fileUtilities = null;

	@Autowired
	private JwsDynarestDAO jwsDynarestDAO = null;

	@Autowired
	private IUserDetailsService detailsService = null;

	@Autowired
	private PropertyMasterDAO propertyMasterDAO = null;

	private Map<String, Map<String, Object>> moduleDetailsMap = new HashMap<>();

	@Autowired
	private ModuleVersionService moduleVersionService = null;

	@Autowired
	private FileUploadConfigDAO fileUploadConfigDAO = null;

	@Autowired
	private ImportExportUtility importExportUtility = null;

	@Autowired
	private JwsEntityRoleAssociationRepository entityRoleAssociationRepository = null;

	String htmlTableJSON;
	String version = null;
	String userName = null;
	Map<String, String> moduleListMap = null;

	@Override
	public void downloadCodeToLocal(JwsDynamicRestDetail jwsDynamicRestDetail, String downloadFolderLocation)
			throws Exception {
		List<JwsDynamicRestDetail> dynaRestList = new ArrayList<>();
		ObjectMapper mapper = new ObjectMapper();
		ArrayNode jsonHtmlArr = mapper.createArrayNode();
		ArrayNode jsonPermArr = mapper.createArrayNode();
		if (jwsDynamicRestDetail != null) {
			dynaRestList.add(jwsDynamicRestDetail);
		} else {
			dynaRestList = jwsDynarestDAO.getAllDynamicRestDetails(Constant.DEFAULT_FORM_TYPE);
		}

		String templateDirectory = Constants.DYNAMIC_REST_DIRECTORY_NAME;
		String ftlCustomExtension = Constants.CUSTOM_FILE_EXTENSION;
		String selectQuery = Constants.DYNAMIC_REST_SELECT_FILE_NAME;
		String serviceLogic = Constants.DYNAMIC_REST_SERVICE_LOGIC_FILE_NAME;
		String folderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		downloadFolderLocation = downloadFolderLocation + File.separator + templateDirectory;
		MetadataXMLVO metadataXMLVO = null;
		String version = propertyMasterDAO.findPropertyMasterValue("system", "system", "version");
		UserDetailsVO detailsVO = detailsService.getUserDetails();
		String userName = detailsVO.getUserName();
		Map<String, Map<String, Object>> moduleDetailsMap = new HashMap<>();
		Set<String> existingModuleIDs = new HashSet<>();

		if (!new File(downloadFolderLocation).exists()) {
			File fileDirectory = new File(downloadFolderLocation);
			fileDirectory.mkdirs();
		} else {
			File file = new File(downloadFolderLocation + File.separator + "metadata.xml");
			if (file.exists()) {
				metadataXMLVO = (MetadataXMLVO) unMarshaling(MetadataXMLVO.class, file.getAbsolutePath());
				if (metadataXMLVO != null && metadataXMLVO.getExportModules() != null
						&& metadataXMLVO.getExportModules().getModule() != null) {
					for (Modules vo : metadataXMLVO.getExportModules().getModule()) {
						existingModuleIDs.add(vo.getModuleID());
					}
				}
			}
		}

		Map<String, XMLVO> xmlVOMap = new HashMap<>();
		String targetLocation = null;

		targetLocation = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");

		MetadataXMLVO metaXMLVO = importExportUtility.readMetaDataXML(targetLocation);
		
		xmlVOMap = importExportUtility.readFiles(targetLocation, metaXMLVO, moduleListMap);

		XMLVO xmlVO = xmlVOMap.get(Constant.PERMISSION + ".xml");

		PermissionXMLVO permissionXMLVO = (xmlVO == null) ? null : (PermissionXMLVO) xmlVO;

		Map<String, Object> moduleListMap = new HashMap<>();
		for (JwsDynamicRestDetail dynaRest : dynaRestList) {
			if (existingModuleIDs.contains(dynaRest.getJwsDynamicRestId())) {
				continue; // Skip if already present in XML
			}
			Map<String, Object> map = new HashMap<>();

			Map<String, Integer> positionMap = new HashMap<>();
			if (permissionXMLVO != null && permissionXMLVO.getJwsRoleDetails().isEmpty() == false) {
				int counter = 0;
				for (JwsEntityRoleAssociation permission : permissionXMLVO.getJwsRoleDetails()) {
					positionMap.put(permission.getEntityRoleId(), counter);
					counter = counter + 1;
				}
			}
			List<JwsEntityRoleAssociation> roles = entityRoleAssociationRepository
					.getEntityRoles(dynaRest.getJwsDynamicRestId(), Constant.DYNA_REST_MOD_ID);
			for (JwsEntityRoleAssociation role : roles) {
				ObjectNode modulePermObj = mapper.createObjectNode();
				permissionXMLVO.getJwsRoleDetails().add(role.getObject());
				modulePermObj.put("moduleType", "Permission");
				modulePermObj.put("moduleID", role.getEntityRoleId());
				modulePermObj.put("moduleName", role.getEntityName());
				modulePermObj.put("moduleVersion", "NA");
				jsonPermArr.add(modulePermObj);
			}

			boolean isCheckSumChanged = false;
			String formName = dynaRest.getJwsDynamicRestUrl();
			String formFolder = downloadFolderLocation + File.separator + formName;

			if (!new File(formFolder).exists()) {
				File fileDirectory = new File(formFolder);
				fileDirectory.mkdirs();
			}

			// htmlBody
			String seviceLogicCheckSum = fileUtilities.checkFileContents(serviceLogic, formFolder,
					dynaRest.getJwsServiceLogic(), dynaRest.getServiceLogicChecksum(), ftlCustomExtension);
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
				Integer sequenceNum = dynaRestSelectQuery.getJwsQuerySequence();
				String sequence = selectQuery + sequenceNum;
				String checksum = fileUtilities.checkFileContents(sequence, formFolder,
						dynaRestSelectQuery.getJwsDaoQueryTemplate(), dynaRestSelectQuery.getDaoQueryChecksum(),
						ftlCustomExtension);

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

			DynaRestExportVO dynamicRestExportVO = new DynaRestExportVO(dynaRest.getJwsDynamicRestId(),
					dynaRest.getJwsDynamicRestUrl(), dynaRest.getJwsMethodDescription(), dynaRest.getJwsMethodName(),
					dynaRest.getJwsPlatformId(), dynaRest.getJwsRbacId(), serviceLogic + ftlCustomExtension,
					dynaRest.getJwsRequestTypeId(), dynaRest.getJwsResponseProducerTypeId(),
					dynaRest.getJwsAllowFiles(), dynaRest.getJwsDynamicRestTypeId(), dynaRest.getJwsHeaderJson(),
					daoDetailsFileNameMap, daoDetailsVariableNameMap, daoDetailsQueryTypeMap, daoDetailsDatasourceIdMap,
					dynaRest.getLastUpdatedTs(), dynaRest.getHidedaoquery(), dynaRest.getIsSecured(),
					dynaRest.getIsCustomUpdated(), dynaRest.getScriptLibraryId());

			map.put("moduleName", formName);
			map.put("moduleObject", dynamicRestExportVO);
			moduleDetailsMap.put(dynaRest.getJwsDynamicRestId(), map);

			// Build JSON object for exportData
			ObjectNode moduleObj = mapper.createObjectNode();
			moduleObj.put("moduleType", "DynaRest");
			moduleObj.put("moduleID", dynaRest.getJwsDynamicRestId());
			moduleObj.put("moduleName", dynaRest.getJwsDynamicRestUrl());
			moduleObj.put("moduleVersion", "1.0");

			// Add object to array
			jsonHtmlArr.add(moduleObj);

			moduleListMap.put("DynaRest", jsonHtmlArr);
			moduleListMap.put("Permission", jsonPermArr);

		}
		htmlTableJSON = exportConfigData(folderLocation, null, moduleListMap, true);
		if (permissionXMLVO != null)
			XMLUtil.marshaling(permissionXMLVO, "Permission", targetLocation);

		generateMetadataXML(metadataXMLVO, moduleDetailsMap, downloadFolderLocation, version, userName);
	}

	public String exportConfigData(String downloadFolderLocation, HttpServletResponse response, Map<String, Object> out,
			boolean isExportToLocal) throws Exception {
		try {
			JSONArray finalArray = new JSONArray();
			version = propertyMasterDAO.findPropertyMasterValue("system", "system", "version");
			UserDetailsVO detailsVO = detailsService.getUserDetails();
			userName = detailsVO.getUserName();

			moduleListMap = new HashMap<>();

			Map<String, XMLVO> xmlVOMap = new HashMap<>();
			String targetLocation = null;
			JSONArray existingLocalData = new JSONArray();
			targetLocation = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");

			MetadataXMLVO metadataXMLVO = importExportUtility.readMetaDataXML(targetLocation);
			if (metadataXMLVO != null && metadataXMLVO.getInfo() != null && metadataXMLVO.getInfo() != "") {
				existingLocalData = new JSONArray(metadataXMLVO.getInfo());
			}

			xmlVOMap = importExportUtility.readFiles(targetLocation, metadataXMLVO, moduleListMap);

			XMLVO xmlVO = xmlVOMap.get(Constant.PERMISSION + ".xml");

			PermissionXMLVO permissionXMLVO = (xmlVO == null) ? null : (PermissionXMLVO) xmlVO;

			Map<String, List<String>> exportTableMap = new HashMap<>();
			Set<String> existingKeys = new HashSet<>();
			for (int i = 0; i < existingLocalData.length(); i++) {
				JSONObject existingObj = existingLocalData.getJSONObject(i);
				String key = existingObj.getString("moduleType") + ":" + existingObj.getString("moduleID");
				existingKeys.add(key);
				finalArray.put(existingObj); // Keep existing data
			}

			for (Entry<String, Object> obj : out.entrySet()) {

				String moduleType = obj.getKey();
				Object exportData = obj.getValue();

				if (exportData instanceof ArrayNode && exportData != null) {
					String jsonStr = exportData.toString();
					JSONArray jsonArray = new JSONArray(jsonStr);

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject explrObject = jsonArray.getJSONObject(i);
						String type = moduleType;

						// Track exportTableMap if needed
						List<String> list = exportTableMap.getOrDefault(type.toUpperCase(), new ArrayList<>());
						list.add(explrObject.getString("moduleID"));
						exportTableMap.put(type.toUpperCase(), list);

						// Form key for comparison
						String newKey = explrObject.getString("moduleType") + ":" + explrObject.getString("moduleID");

						if (!existingKeys.contains(newKey)) {
							finalArray.put(explrObject);
							// finalArray.put(permission);
						}
					}

				}
			}

			htmlTableJSON = StringEscapeUtils.unescapeXml("<![CDATA[" + finalArray.toString() + "]]>");

			XMLUtil.generateMetadataXML(moduleListMap, null, targetLocation, version, userName, htmlTableJSON, null);

		} catch (CustomeFileStorageException a_excep) {
			fileUtilities.customSendError(response, HttpStatus.PRECONDITION_REQUIRED.value(), a_excep.getMessage());
			return null;
		} catch (Exception a_excep) {
			logger.error("Error while exporting the configuration ", a_excep);
			if (a_excep.getMessage() != null && a_excep.getMessage().startsWith("Data mismatch while exporting")) {
				return "fail:" + a_excep.getMessage();
			} else {
				return "fail:" + "Error occurred while exporting";
			}
		}
		return htmlTableJSON;

	}

	@Override
	public void uploadCodeToDB(String dynarestTypeID, String uploadFileName) throws Exception {
		// TODO Auto-generated method stub
		String user = "admin";
		String templateDirectory = Constants.DYNAMIC_REST_DIRECTORY_NAME;

		String folderLocation = propertyMasterDAO.findPropertyMasterValue("system", "system", "template-storage-path");
		folderLocation = folderLocation + File.separator + templateDirectory;
		File directory = new File(folderLocation);
		if (!directory.exists()) {
			throw new Exception("No such directory present");
		}

		MetadataXMLVO metadataXMLVO = null;
		File metadatFile = new File(folderLocation + File.separator + "metadata.xml");
		if (metadatFile.exists() && metadatFile.isFile() && metadatFile.getName().equals("metadata.xml")) {
			metadataXMLVO = (MetadataXMLVO) unMarshaling(MetadataXMLVO.class, metadatFile.getAbsolutePath());
		}
		for (Modules module : metadataXMLVO.getExportModules().getModule()) {
			String moduleID = module.getModuleID();
			String moduleName = module.getModuleName();
			DynaRestExportVO dynaRestExportVO = module.getDynaRestExportVO();
			String serviceLogicFileName = dynaRestExportVO.getServiceLogicFileName();
			Map<Integer, String> daoDetailsFileNameMap = dynaRestExportVO.getDaoDetailsFileNameMap();
			Map<Integer, String> daoDetailsVariableNameMap = dynaRestExportVO.getDaoDetailsVariableNameMap();
			Map<Integer, Integer> daoDetailsQueryTypeMap = dynaRestExportVO.getDaoDetailsQueryTypeMap();
			Map<Integer, String> daoDetailsDatasourceIdMap = dynaRestExportVO.getDaoDetailsDatasourceIdMap();
			String ftlCustomExtension = "." + dynaRestExportVO.getServiceLogicFileName().split("\\.")[1];

			FilenameFilter textFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(ftlCustomExtension);
				}
			};

			File[] directories = directory.listFiles(new FilenameFilter() {
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
			});

			for (File currentDirectory : directories) {
				String selectCheckSum = null;
				String htmlCheckSum = null;
				String currentDirectoryName = currentDirectory.getName();
				Integer dynaRestTypeId = dynaRestExportVO.getJwsDynamicRestTypeId();
				if (Integer.parseInt(dynarestTypeID) == dynaRestTypeId && currentDirectoryName.equals(moduleName)) {
					JwsDynamicRestDetail jwsDynamicRestDetail = jwsDynarestDAO.findDynamicRestById(moduleID);

					if (jwsDynamicRestDetail == null) {
						jwsDynamicRestDetail = new JwsDynamicRestDetail();
						jwsDynamicRestDetail.setCreatedBy(user);
						jwsDynamicRestDetail.setCreatedDate(new Date());
						jwsDynamicRestDetail.setJwsDynamicRestUrl(currentDirectoryName);
						jwsDynamicRestDetail.setJwsMethodDescription("Uploaded from Local Directory");
					} else {
						jwsDynamicRestDetail.setLastUpdatedTs(new Date());
					}

					jwsDynamicRestDetail.setJwsAllowFiles(dynaRestExportVO.getJwsAllowFiles());
					jwsDynamicRestDetail.setJwsDynamicRestId(dynaRestExportVO.getJwsDynamicRestId());
					jwsDynamicRestDetail.setJwsDynamicRestTypeId(dynaRestExportVO.getJwsDynamicRestTypeId());
					jwsDynamicRestDetail.setJwsDynamicRestUrl(dynaRestExportVO.getJwsDynamicRestUrl());
					jwsDynamicRestDetail.setJwsHeaderJson(dynaRestExportVO.getJwsHeaderJson());
					jwsDynamicRestDetail.setJwsMethodDescription(dynaRestExportVO.getJwsMethodDescription());
					jwsDynamicRestDetail.setJwsMethodName(dynaRestExportVO.getJwsMethodName());
					jwsDynamicRestDetail.setJwsPlatformId(dynaRestExportVO.getJwsPlatformId());
					jwsDynamicRestDetail.setJwsRbacId(dynaRestExportVO.getJwsRbacId());
					jwsDynamicRestDetail.setJwsRequestTypeId(dynaRestExportVO.getJwsRequestTypeId());
					jwsDynamicRestDetail.setJwsResponseProducerTypeId(dynaRestExportVO.getJwsResponseProducerTypeId());
					jwsDynamicRestDetail.setLastUpdatedTs(new Date());
					jwsDynamicRestDetail.setHidedaoquery(dynaRestExportVO.getHideDaoQuery());// Added for New Column for
																								// hiding DAO Query
																								// Editor
					jwsDynamicRestDetail.setIsCustomUpdated(dynaRestExportVO.getIsCustomUpdated());
					jwsDynamicRestDetail.setIsSecured(dynaRestExportVO.getIsSecured());
					jwsDynamicRestDetail.setScriptLibraryId(dynaRestExportVO.getScriptLibraryId());
					File[] directoryFiles = currentDirectory.listFiles(textFilter);
					Integer filesPresent = directoryFiles.length;
					if (filesPresent >= 2) {
						File serviceLogicFile = new File(
								currentDirectory.getAbsolutePath() + File.separator + serviceLogicFileName);
						if (!serviceLogicFile.exists()) {
							throw new Exception("service logic  file is mandatory  for saving dynamic rest api"
									+ currentDirectoryName);
						} else {
							// set select
							selectCheckSum = fileUtilities.generateFileChecksum(serviceLogicFile);

							jwsDynamicRestDetail.setJwsServiceLogic(
									fileUtilities.readContentsOfFile(serviceLogicFile.getAbsolutePath()));
							jwsDynamicRestDetail.setServiceLogicChecksum(selectCheckSum);

							List<JwsDynamicRestDaoDetail> jwsDynamicRestDaoDetails = new ArrayList<>();

							if (daoDetailsFileNameMap != null && !daoDetailsFileNameMap.isEmpty()) {
								for (Entry<Integer, String> entry : daoDetailsFileNameMap.entrySet()) {
									Integer sequence = entry.getKey();
									String daoDetailQuery = entry.getValue();

									JwsDynamicRestDaoDetail daoDetail = new JwsDynamicRestDaoDetail();
									File daoDetailFile = new File(
											currentDirectory.getAbsolutePath() + File.separator + daoDetailQuery);
									if (daoDetailFile.exists()) {
										daoDetail.setJwsDynamicRestDetailId(jwsDynamicRestDetail.getJwsDynamicRestId());
										daoDetail
												.setDaoQueryChecksum(fileUtilities.generateFileChecksum(daoDetailFile));
										daoDetail.setJwsQuerySequence(sequence);
										daoDetail.setJwsDaoQueryTemplate(
												fileUtilities.readContentsOfFile(daoDetailFile.getAbsolutePath()));
										daoDetail.setDatasourceId(daoDetailsDatasourceIdMap.get(sequence));
										daoDetail.setJwsResultVariableName(daoDetailsVariableNameMap.get(sequence));
										daoDetail.setQueryType(daoDetailsQueryTypeMap.get(sequence));
										jwsDynamicRestDaoDetails.add(daoDetail);
									} else {
										throw new Exception(
												"saveQuery file sequence is incorrect" + currentDirectoryName);
									}
								}
							} else {
								throw new Exception(
										"saveQuery file is mandatory  for saving dynamic form" + currentDirectoryName);
							}
							jwsDynamicRestDetail.setJwsDynamicRestDaoDetails(jwsDynamicRestDaoDetails);

							jwsDynarestDAO.saveDynaRestDetail(jwsDynamicRestDetail, jwsDynamicRestDaoDetails);

							RestApiDetailsJsonVO vo = convertDynaRestEntityToVO(jwsDynamicRestDetail);
							
							moduleVersionService.saveModuleVersion(vo, null, jwsDynamicRestDetail.getJwsDynamicRestId(),
									"jq_dynamic_rest_details", Constant.UPLOAD_SOURCE_VERSION_TYPE);

							jwsDynamicRestDetail.setJwsDynamicRestDaoDetails(jwsDynamicRestDaoDetails);
						}

					} else {
						throw new Exception(
								"Invalid count of files for saving dynamic rest api" + currentDirectoryName);
					}
				}
			}
		}
	}

	private RestApiDetailsJsonVO convertDynaRestEntityToVO(JwsDynamicRestDetail dynaRest) throws Exception {
		RestApiDetailsJsonVO vo = new RestApiDetailsJsonVO();

		vo.setAllowFiles(dynaRest.getJwsAllowFiles() != null ? dynaRest.getJwsAllowFiles().toString() : "");
		JSONArray daoDetailsId = new JSONArray();
		JSONArray queryDetails = new JSONArray();
		JSONArray variableNameDetails = new JSONArray();
		JSONArray queryTypes = new JSONArray();
		if (dynaRest.getJwsDynamicRestDaoDetails() != null)
			for (JwsDynamicRestDaoDetail dao : dynaRest.getJwsDynamicRestDaoDetails()) {
				daoDetailsId.put(dao.getDatasourceId());
				queryDetails.put(dao.getJwsDaoQueryTemplate());
				variableNameDetails.put(dao.getJwsResultVariableName());
				queryTypes.put(dao.getQueryType());
			}
		vo.setDatasourceDetails(daoDetailsId.toString());
		vo.setDaoQueryDetails(queryDetails.toString());
		vo.setVariableName(variableNameDetails.toString());
		vo.setQueryType(queryTypes.toString());
		vo.setSaveUpdateQuery("");
		vo.setDynarestId(dynaRest.getJwsDynamicRestId());
		vo.setDynarestMethodDescription(dynaRest.getJwsMethodDescription());
		vo.setDynarestMethodName(dynaRest.getJwsMethodName());
		vo.setDynarestPlatformId(dynaRest.getJwsPlatformId() != null ? dynaRest.getJwsPlatformId().toString() : "");
		vo.setDynarestProdTypeId(
				dynaRest.getJwsResponseProducerTypeId() != null ? dynaRest.getJwsResponseProducerTypeId().toString()
						: "");
		vo.setDynarestRequestTypeId(
				dynaRest.getJwsRequestTypeId() != null ? dynaRest.getJwsRequestTypeId().toString() : "");
		vo.setDynarestUrl(dynaRest.getJwsDynamicRestUrl());
		vo.setEntityName("jq_dynamic_rest_details");
		vo.setFormId("8a80cb81749ab40401749ac2e7360000");
		vo.setIsEdit(dynaRest.getJwsDynamicRestId() != null ? "1" : "0");
		vo.setPrimaryKey(dynaRest.getJwsDynamicRestId());
		vo.setServiceLogic(dynaRest.getJwsServiceLogic());
		vo.setHeaderJson(dynaRest.getJwsHeaderJson());
		vo.setHidedaoquery(dynaRest.getHidedaoquery());
		vo.setDynarestSecured(dynaRest.getIsSecured());
		vo.setScriptLibId(dynaRest.getScriptLibraryId());
		return vo;
	}

	@Override
	public void exportData(Object object, String folderLocation) throws Exception {
		JwsDynamicRestDetail a_dynaRest = (JwsDynamicRestDetail) object;
		List<JwsDynamicRestDetail> dynaRestList = new ArrayList<>();
		if (a_dynaRest != null) {
			dynaRestList.add(a_dynaRest);
		} else {
			dynaRestList = jwsDynarestDAO.getAllDynamicRestDetails();
		}

		String templateDirectory = Constants.DYNAMIC_REST_DIRECTORY_NAME;
		String ftlCustomExtension = Constants.CUSTOM_FILE_EXTENSION;
		String selectQuery = Constants.DYNAMIC_REST_SELECT_FILE_NAME;
		String serviceLogic = Constants.DYNAMIC_REST_SERVICE_LOGIC_FILE_NAME;
		folderLocation = folderLocation + File.separator + templateDirectory;

		for (JwsDynamicRestDetail dynaRest : dynaRestList) {
			boolean isCheckSumChanged = false;
			String formName = dynaRest.getJwsDynamicRestUrl();
			String formFolder = folderLocation + File.separator + formName;
			if (!new File(formFolder).exists()) {
				File fileDirectory = new File(formFolder);
				fileDirectory.mkdirs();
			}

			// htmlBody
			String seviceLogicCheckSum = fileUtilities.checkFileContents(serviceLogic, formFolder,
					dynaRest.getJwsServiceLogic(), dynaRest.getServiceLogicChecksum(), ftlCustomExtension);
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
				Integer sequenceNum = dynaRestSelectQuery.getJwsQuerySequence();
				String sequence = selectQuery + sequenceNum;
				String checksum = fileUtilities.checkFileContents(sequence, formFolder,
						dynaRestSelectQuery.getJwsDaoQueryTemplate(), dynaRestSelectQuery.getDaoQueryChecksum(),
						ftlCustomExtension);
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
			List<String> scriptLibId = new ArrayList<>();
			for (int iScriptUploadCounter = 0; iScriptUploadCounter < scriptLibIdList.size(); iScriptUploadCounter++) {
				scriptLibId.add("\"" + scriptLibIdList.get(iScriptUploadCounter) + "\"");
				dynaRest.setScriptLibraryId(scriptLibId.toString());
			}
			DynaRestExportVO dynamicRestExportVO = new DynaRestExportVO(dynaRest.getJwsDynamicRestId(),
					dynaRest.getJwsDynamicRestUrl(), dynaRest.getJwsMethodDescription(), dynaRest.getJwsMethodName(),
					dynaRest.getJwsPlatformId(), dynaRest.getJwsRbacId(), serviceLogic + ftlCustomExtension,
					dynaRest.getJwsRequestTypeId(), dynaRest.getJwsResponseProducerTypeId(),
					dynaRest.getJwsAllowFiles(), dynaRest.getJwsDynamicRestTypeId(), dynaRest.getJwsHeaderJson(),
					daoDetailsFileNameMap, daoDetailsVariableNameMap, daoDetailsQueryTypeMap, daoDetailsDatasourceIdMap,
					dynaRest.getLastUpdatedTs(), dynaRest.getHidedaoquery(), dynaRest.getIsSecured(),
					dynaRest.getIsCustomUpdated(), dynaRest.getScriptLibraryId());

			Map<String, Object> map = new HashMap<>();
			map.put("moduleName", formName);
			map.put("moduleObject", dynamicRestExportVO);
			moduleDetailsMap.put(dynaRest.getJwsDynamicRestId(), map);
		}

	}

	@Override
	public Object importData(String folderLocation, String uploadFileName, String uploadID, Object importObject)
			throws Exception {
		String user = "admin";

		DynaRestExportVO dynaRestExportVO = (DynaRestExportVO) importObject;

		String serviceLogicFileName = dynaRestExportVO.getServiceLogicFileName();
		Map<Integer, String> daoDetailsFileNameMap = dynaRestExportVO.getDaoDetailsFileNameMap();
		Map<Integer, String> daoDetailsVariableNameMap = dynaRestExportVO.getDaoDetailsVariableNameMap();
		Map<Integer, Integer> daoDetailsQueryTypeMap = dynaRestExportVO.getDaoDetailsQueryTypeMap();
		Map<Integer, String> daoDetailsDatasourceIdMap = dynaRestExportVO.getDaoDetailsDatasourceIdMap();
		String ftlCustomExtension = "." + dynaRestExportVO.getServiceLogicFileName().split("\\.")[1];

		JwsDynamicRestDetail dynaRest = null;
		File directory = new File(folderLocation);
		if (!directory.exists()) {
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
			String selectCheckSum = null;
			String currentDirectoryName = currentDirectory.getName();
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
				dynaRest.setHidedaoquery(dynaRestExportVO.getHideDaoQuery());// Added for New Column for hiding DAO
																				// Query Editor
				dynaRest.setIsCustomUpdated(dynaRestExportVO.getIsCustomUpdated());
				dynaRest.setIsSecured(dynaRestExportVO.getIsSecured());
				dynaRest.setScriptLibraryId(dynaRestExportVO.getScriptLibraryId());
				File[] directoryFiles = currentDirectory.listFiles(textFilter);
				Integer filesPresent = directoryFiles.length;
				if (filesPresent >= 2) {
					File serviceLogicFile = new File(
							currentDirectory.getAbsolutePath() + File.separator + serviceLogicFileName);
					if (!serviceLogicFile.exists()) {
						throw new Exception(
								"service logic  file is mandatory  for saving dynamic rest api" + currentDirectoryName);
					} else {
						// set select
						selectCheckSum = fileUtilities.generateFileChecksum(serviceLogicFile);

						dynaRest.setJwsServiceLogic(
								fileUtilities.readContentsOfFile(serviceLogicFile.getAbsolutePath()));
						dynaRest.setServiceLogicChecksum(selectCheckSum);

						List<JwsDynamicRestDaoDetail> jwsDynamicRestDaoDetails = new ArrayList<>();

						if (daoDetailsFileNameMap != null && !daoDetailsFileNameMap.isEmpty()) {
							for (Entry<Integer, String> entry : daoDetailsFileNameMap.entrySet()) {
								Integer sequence = entry.getKey();
								String daoDetailQuery = entry.getValue();

								JwsDynamicRestDaoDetail daoDetail = new JwsDynamicRestDaoDetail();
								File daoDetailFile = new File(
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
