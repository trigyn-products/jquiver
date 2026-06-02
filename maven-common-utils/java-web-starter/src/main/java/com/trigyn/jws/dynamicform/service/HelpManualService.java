package com.trigyn.jws.dynamicform.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.trigyn.jws.dbutils.service.PropertyMasterService;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.HelpManualCHMExport;
import com.trigyn.jws.dbutils.utils.HelpManualWordImport;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dbutils.vo.xml.FileUploadConfigExportVO;
import com.trigyn.jws.dbutils.vo.xml.FileUploadExportVO;
import com.trigyn.jws.dbutils.vo.xml.HelpManualTypeExportVO;
import com.trigyn.jws.dbutils.vo.xml.ManualEntryDetailsExportVO;
import com.trigyn.jws.dynamicform.dao.HelpManualDAO;
import com.trigyn.jws.dynamicform.dao.IManualEntryDetailsRepository;
import com.trigyn.jws.dynamicform.dao.IManualTypeRepository;
import com.trigyn.jws.dynamicform.entities.ManualEntryDetails;
import com.trigyn.jws.dynamicform.entities.ManualEntryFileAssociation;
import com.trigyn.jws.dynamicform.entities.ManualType;
import com.trigyn.jws.dynamicform.utils.Constant;
import com.trigyn.jws.dynarest.entities.FileUpload;
import com.trigyn.jws.dynarest.entities.FileUploadConfig;
import com.trigyn.jws.dynarest.repository.FileUploadRepository;
import com.trigyn.jws.dynarest.repository.IFileUploadConfigRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@Service
@Transactional
public class HelpManualService {

	private static final Logger				logger							= LoggerFactory
			.getLogger(HelpManualService.class);

	@Autowired
	private HelpManualDAO					helpManualDAO					= null;

	@Autowired
	private IUserDetailsService				detailsService					= null;

	@Autowired
	private IManualTypeRepository			iManualTypeRepository			= null;

	@Autowired
	private IManualEntryDetailsRepository	iManualEntryDetailsRepository	= null;

	@Autowired
	private IUserDetailsService				userDetailsService				= null;

	@Autowired
	private PropertyMasterService			propertyMasterService			= null;

	@Autowired
	private FileUploadRepository			fileUploadRepository			= null;

	@Autowired
	private IFileUploadConfigRepository		iFileUploadConfigRepository		= null;
	
	@Autowired
	private HelpManualWordImport			helpManualWordImport			= null;
	
	@Autowired
	private HelpManualCHMExport			helpManualWordIExport			= null;
	
	public boolean manualTypeExist(String name) {
		return iManualTypeRepository.existsByName(name);
		// return helpManualDAO.getManualTypeByName(name);
	}

	public String saveManualType(String manualId, String name, String isEdit, String headerTemplate, Integer editorName) {
		logger.debug("Inside HelpManualService.saveManualType(manualId: {}, name: {}, isEdit: {})", manualId, name,
				isEdit);
		UserDetailsVO			userDetailsVO		= userDetailsService.getUserDetails();
		Date					date				= new Date();
		Optional<ManualType>	manualTypeOptional	= null;
		if (StringUtils.isBlank(manualId) == false) {
			manualTypeOptional = iManualTypeRepository.findById(manualId);
		}
		ManualType manualType = new ManualType();
		if (manualTypeOptional != null && manualTypeOptional.isPresent()) {
			manualType = manualTypeOptional.get();
			manualType.setLastUpdatedBy(userDetailsVO.getUserName()); 
			manualType.setLastUpdatedTs(date);
		}
		manualType.setName(name);
		manualType.setHeaderTemplate(headerTemplate);
		manualType.setEditorName(editorName);
		if (manualId != null && StringUtils.isBlank(isEdit) == false && isEdit.equals("1")) {
			manualType.setManualId(manualId);

		} else {
			manualType.setIsSystemManual(Constant.CUSTOM_MANUAL);
			manualType.setCreatedBy(userDetailsVO.getUserName());
			manualType.setCreatedDate(date);
		}
		iManualTypeRepository.save(manualType);
		return manualType.getManualId();
	}

	public void saveFileForManualEntry(String manualEntryId, String manualId, String entryName, List<String> fileIds) {
		logger.debug(
				"Inside HelpManualService.saveFileForManualEntry(manualEntryId: {}, manualId: {}, entryName: {}, fileIds: {})",
				manualEntryId, manualId, entryName, fileIds);

		if (manualEntryId == null) {
			manualEntryId = helpManualDAO.getManualDetailByIdAndName(manualId, entryName);
		}
		helpManualDAO.deleteFilesByManualEntryId(manualEntryId);
		for (String fileId : fileIds) {
			ManualEntryFileAssociation association = new ManualEntryFileAssociation();
			association.setFileUploadId(fileId);
			association.setManualEntryId(manualEntryId);
			helpManualDAO.saveFileAssociation(association);
		}

	}

	public String saveManualEntryDetails(Map<String, Object> parameters) {
		logger.debug("Inside HelpManualService.saveManualEntryDetails(parameters: {})", parameters);

		String				userName			= detailsService.getUserDetails().getUserName();
		ManualEntryDetails	manualEntryDetails	= new ManualEntryDetails(parameters, userName);
		helpManualDAO.saveManualEntry(manualEntryDetails);
		return manualEntryDetails.getManualEntryId();
	}

	public String saveManualEntryDetails(ManualEntryDetails manualEntryDetails) {
		logger.debug("Inside HelpManualService.saveManualEntryDetails(parameters: {})", manualEntryDetails);

		String userName = detailsService.getUserDetails().getUserName();
		manualEntryDetails.setLastUpdatedBy(userName);
		helpManualDAO.saveManualEntry(manualEntryDetails);
		return manualEntryDetails.getManualEntryId();
	}

	public void deleteManualEntryId(String manualType, String manualEntryId, Integer sortIndex) {
		logger.debug("Inside HelpManualService.deleteManualEntryId(manualType: {}, manualEntryId: {}, sortIndex: {})",
				manualType, manualEntryId, sortIndex);

		helpManualDAO.deleteManualEntryId(manualType, manualEntryId);
		helpManualDAO.updateSortIndex(manualType, sortIndex);
	}

	public void deleteHelpManualEntryId(String manualType, String manualEntryId) {
		logger.debug("Inside HelpManualService.deleteManualEntryId(manualType: {}, manualEntryId: {}, sortIndex: {})",
				manualType, manualEntryId);

		helpManualDAO.deleteHelpManualEntryId(manualType, manualEntryId);
	}

	public void deleteHelpManualIdEntries(String manualType) {
		logger.debug(
				"Inside HelpManualService.deleteHelpManualIdEntries(manualType: {}, manualEntryId: {}, sortIndex: {})",
				manualType);

		helpManualDAO.deleteHelpManualIdEntries(manualType);
	}

	public void updateHelpManualEntryId(String manualEntryId, String entryName, String entryContent, String manualId) {
		logger.debug(
				"Inside HelpManualService.deleteManualEntryId(manualType: {}, manualEntryId: {}, entryName: {},entryContent{})",
				manualEntryId, manualEntryId);

		helpManualDAO.updateHelpManualDetails(manualEntryId, entryName, entryContent, manualId);
	}

	public List<ManualEntryDetails> fetchParentNodes(String manualId) {
		List<ManualEntryDetails> helpManualEntries = iManualEntryDetailsRepository.fetchParentNodes(manualId);
		return helpManualEntries;

	}

	public List<ManualEntryDetails> fetchAll() {
		return iManualEntryDetailsRepository.findAll();

	}

	public List<ManualEntryDetails> fetchByParentAndManualId(String parentId, String manualId) {
		return iManualEntryDetailsRepository.fetchByParentAndManualId(parentId, manualId);

	}

	public Boolean hasChild(String nodeId) {
		boolean	result	= false;
		Integer	count	= iManualEntryDetailsRepository.hasChild(nodeId);
		if (count > 0)
			result = true;
		return result;
	}

	public String fetchContent(String nodeId) {
		return iManualEntryDetailsRepository.fetchContent(nodeId);

	}

	public String findlastSortIndex(String mt) {
		return helpManualDAO.getSortIndex(mt);
	}

	public List<ManualEntryDetails> searchText(String searchText, String manualId) {
		List<ManualEntryDetails> helpManualEntries = iManualEntryDetailsRepository.searchNode(searchText, manualId);
		return helpManualEntries;
	}

	public Optional<ManualEntryDetails> fetchByManualEntryId(String manualEntryId) {
		return iManualEntryDetailsRepository.findById(manualEntryId);

	}

	public String getManualUploadJson(String entityId) throws Exception {
		ManualType							a_manualType				= helpManualDAO.getManualType(entityId);
		List<ManualEntryDetails>			helpManualEntries			= iManualEntryDetailsRepository
				.findAllByManualType(entityId);
		String								fileUploadConfigId			= null;
		List<ManualEntryDetailsExportVO>	manualEntryDetailsVOList	= new ArrayList<>();
		List<FileUpload>					fileUploads					= fileUploadRepository
				.findAllByFileAssociationId(entityId);
		List<FileUploadExportVO>			fileUploadExportVOList		= new ArrayList<>();
		for (FileUpload fu : fileUploads) {
			fileUploadExportVOList.add(new FileUploadExportVO(fu.getFileUploadId(), fu.getPhysicalFileName(),
					fu.getOriginalFileName(), fu.getFilePath(), fu.getUpdatedBy(), fu.getLastUpdatedTs(),
					fu.getFileBinId(), fu.getFileAssociationId()));
		}

		String jsonString = "";

		if (helpManualEntries != null) {
			for (ManualEntryDetails med : helpManualEntries) {
				manualEntryDetailsVOList.add(new ManualEntryDetailsExportVO(med.getManualEntryId(), med.getManualId(),
						med.getEntryName(), med.getEntryContent().trim(), med.getSortIndex(), med.getLastUpdatedBy(),
						med.getLastModifiedOn(), med.getCreatedBy(), med.getCreatedDate(), med.getParentId()));

				if (fileUploads != null && fileUploads.isEmpty() == false)
					fileUploadConfigId = fileUploads.get(0).getFileBinId();
			}

			FileUploadConfigExportVO fileUploadConfigExportVO = null;
			if (fileUploadConfigId != null) {
				FileUploadConfig fileUploadConfig = iFileUploadConfigRepository.findById(fileUploadConfigId)
						.orElseThrow(() -> new Exception("file not found with id : "));
				fileUploadConfigExportVO = new FileUploadConfigExportVO(fileUploadConfig.getFileBinId(),
						fileUploadConfig.getFileTypSupported(), fileUploadConfig.getMaxFileSize(),
						fileUploadConfig.getNoOfFiles(),
						StringEscapeUtils
								.unescapeXml("<![CDATA[" + fileUploadConfig.getUploadQueryContent().trim() + "]]>"),
						StringEscapeUtils
								.unescapeXml("<![CDATA[" + fileUploadConfig.getViewQueryContent().trim() + "]]>"),
						StringEscapeUtils
								.unescapeXml("<![CDATA[" + fileUploadConfig.getDeleteQueryContent().trim() + "]]>"),
						fileUploadConfig.getIsDeleted(), fileUploadConfig.getLastUpdatedBy(),
						fileUploadConfig.getLastUpdatedTs());
			}

			HelpManualTypeExportVO	helpManualTypeVO	= new HelpManualTypeExportVO(a_manualType.getManualId(),
					a_manualType.getName(), a_manualType.getIsSystemManual(), a_manualType.getHeaderTemplate(),a_manualType.getEditorName(),
					manualEntryDetailsVOList, a_manualType.getCreatedBy(), a_manualType.getCreatedDate(),
					a_manualType.getLastUpdatedBy(), a_manualType.getLastUpdatedTs(), fileUploadConfigExportVO,
					fileUploadExportVOList);

			Gson					gson				= new Gson();
			ObjectMapper			objectMapper		= new ObjectMapper();
			String					dbDateFormat		= propertyMasterService.getDateFormatByName(
					com.trigyn.jws.dbutils.utils.Constant.PROPERTY_MASTER_OWNER_TYPE,
					com.trigyn.jws.dbutils.utils.Constant.PROPERTY_MASTER_OWNER_ID,
					com.trigyn.jws.dbutils.utils.Constant.JWS_DATE_FORMAT_PROPERTY_NAME,
					com.trigyn.jws.dbutils.utils.Constant.JWS_JAVA_DATE_FORMAT_PROPERTY_NAME);
			DateFormat				dateFormat			= new SimpleDateFormat(dbDateFormat);
			objectMapper.setDateFormat(dateFormat);
			Map<String, Object> objectMap = objectMapper.convertValue(helpManualTypeVO, TreeMap.class);
			jsonString = gson.toJson(objectMap);
		}
		return jsonString;
	}

	public String importHelpManualData(HttpServletRequest request, HttpServletResponse response, Part filePart,
			Integer sourceTypeId) throws Exception {

		return helpManualWordImport.importHelpManualData(request, response, filePart, sourceTypeId);
	}
	
	public void exportHelpManualData(HttpServletRequest request, HttpServletResponse response) throws Exception {

		helpManualWordIExport.exportHelpManualToCHM(request);
	}


}
