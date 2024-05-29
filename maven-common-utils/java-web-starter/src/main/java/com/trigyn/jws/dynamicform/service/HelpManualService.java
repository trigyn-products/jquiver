package com.trigyn.jws.dynamicform.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynamicform.dao.HelpManualDAO;
import com.trigyn.jws.dynamicform.dao.IManualEntryDetailsRepository;
import com.trigyn.jws.dynamicform.dao.IManualTypeRepository;
import com.trigyn.jws.dynamicform.entities.ManualEntryDetails;
import com.trigyn.jws.dynamicform.entities.ManualEntryFileAssociation;
import com.trigyn.jws.dynamicform.entities.ManualType;
import com.trigyn.jws.dynamicform.utils.Constant;

@Service
@Transactional
public class HelpManualService {

	private static final Logger logger = LogManager.getLogger(HelpManualService.class);

	@Autowired
	private HelpManualDAO helpManualDAO = null;

	@Autowired
	private IUserDetailsService detailsService = null;

	@Autowired
	private IManualTypeRepository iManualTypeRepository = null;

	@Autowired
	private IManualEntryDetailsRepository iManualEntryDetailsRepository = null;

	@Autowired
	private IUserDetailsService userDetailsService = null;

	public boolean manualTypeExist(String name) {
		return iManualTypeRepository.existsByName(name);
		// return helpManualDAO.getManualTypeByName(name);
	}

	public String saveManualType(String manualId, String name, String isEdit) {
		logger.debug("Inside HelpManualService.saveManualType(manualId: {}, name: {}, isEdit: {})", manualId, name,
				isEdit);
		UserDetailsVO userDetailsVO = userDetailsService.getUserDetails();
		Date date = new Date();
		Optional<ManualType> manualTypeOptional = null;
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

		String userName = detailsService.getUserDetails().getUserName();
		ManualEntryDetails manualEntryDetails = new ManualEntryDetails(parameters, userName);
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

	public List<ManualEntryDetails> fetchByNodeId(String nodeId, String manualId) {
		return iManualEntryDetailsRepository.fetchByNodeId(nodeId, manualId);

	}

	public Boolean hasChild(String nodeId) {
		boolean result = false;
		Integer count = iManualEntryDetailsRepository.hasChild(nodeId);
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

}
