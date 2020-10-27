package com.trigyn.jws.webstarter.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.webstarter.dao.HelpManualDAO;
import com.trigyn.jws.webstarter.entities.ManualEntryDetails;
import com.trigyn.jws.webstarter.entities.ManualEntryFileAssociation;

@Service
@Transactional
public class HelpManualService {

	@Autowired
	private HelpManualDAO helpManualDAO = null;

	@Autowired
	private IUserDetailsService detailsService = null;

	public void saveManualType(String manualId, String name) {
		if (manualId != null) {
			helpManualDAO.updateManualDetails(manualId, name);
		} else {
			helpManualDAO.insertManualDetails(manualId, name);
		}
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

	public void saveManualEntryDetails(Map<String, Object> parameters) {
		String userName = detailsService.getUserDetails().getUserName();
		ManualEntryDetails manualEntryDetails = new ManualEntryDetails(parameters, userName);
		helpManualDAO.saveManualEntry(manualEntryDetails);
	}

}
