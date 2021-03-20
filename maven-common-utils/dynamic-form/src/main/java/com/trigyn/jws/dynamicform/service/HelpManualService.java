package com.trigyn.jws.dynamicform.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dynamicform.dao.HelpManualDAO;
import com.trigyn.jws.dynamicform.dao.IManualTypeRepository;
import com.trigyn.jws.dynamicform.entities.ManualEntryDetails;
import com.trigyn.jws.dynamicform.entities.ManualEntryFileAssociation;
import com.trigyn.jws.dynamicform.entities.ManualType;
import com.trigyn.jws.dynamicform.utils.Constant;

@Service
@Transactional
public class HelpManualService {

	@Autowired
	private HelpManualDAO			helpManualDAO			= null;

	@Autowired
	private IUserDetailsService		detailsService			= null;

	@Autowired
	private IManualTypeRepository	iManualTypeRepository	= null;

	public String saveManualType(String manualId, String name, String isEdit) {
		Optional<ManualType> manualTypeOptional = null;
		if (StringUtils.isBlank(manualId) == false) {
			manualTypeOptional = iManualTypeRepository.findById(manualId);
		}
		ManualType manualType = new ManualType();
		if (manualTypeOptional != null && manualTypeOptional.isPresent()) {
			manualType = manualTypeOptional.get();
		}
		manualType.setName(name);

		if (manualId != null && StringUtils.isBlank(isEdit) == false && isEdit.equals("1")) {
			manualType.setManualId(manualId);
		} else {
			manualType.setIsSystemManual(Constant.CUSTOM_MANUAL);
		}
		iManualTypeRepository.save(manualType);
		return manualType.getManualId();
	}

	public void saveFileForManualEntry(String manualEntryId, String manualId, String entryName, List<String> fileIds) {
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
		String				userName			= detailsService.getUserDetails().getUserName();
		ManualEntryDetails	manualEntryDetails	= new ManualEntryDetails(parameters, userName);
		helpManualDAO.saveManualEntry(manualEntryDetails);
		return manualEntryDetails.getManualEntryId();
	}

	public void deleteManualEntryId(String manualType, String manualEntryId, Integer sortIndex) {
		helpManualDAO.deleteManualEntryId(manualType, manualEntryId);
		helpManualDAO.updateSortIndex(manualType, sortIndex);
	}

}
