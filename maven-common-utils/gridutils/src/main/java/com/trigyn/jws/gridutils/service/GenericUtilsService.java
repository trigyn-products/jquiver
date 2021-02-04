package com.trigyn.jws.gridutils.service;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trigyn.jws.gridutils.dao.GridUtilsDAO;
import com.trigyn.jws.gridutils.entities.GridDetails;
import com.trigyn.jws.gridutils.utility.GenericGridParams;

@Service
public class GenericUtilsService {

	private final static Logger	logger			= LogManager.getLogger(GenericUtilsService.class);

	@Autowired
	private GridUtilsDAO		genericUtilsDAO	= null;

	public Integer findCount(String gridId, GenericGridParams gridParams) {
		GridDetails gridDetails = getGridDetails(gridId);
		return genericUtilsDAO.findCount(gridDetails, gridParams);
	}

	public List<Map<String, Object>> findAllRecords(String gridId, GenericGridParams gridParams) {
		GridDetails					gridDetails	= getGridDetails(gridId);
		List<Map<String, Object>>	allRecords	= genericUtilsDAO.findAllRecords(gridDetails, gridParams);
		return allRecords;
	}

	private GridDetails getGridDetails(String gridId) {
		return genericUtilsDAO.getGridDetails(gridId);
	}
}
