package com.trigyn.jws.gridutils.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trigyn.jws.gridutils.dao.GridUtilsDAO;
import com.trigyn.jws.gridutils.entities.GridDetails;
import com.trigyn.jws.gridutils.utility.GenericGridParams;

@Service
public class GenericUtilsService {

	private final static Logger	logger			= LoggerFactory.getLogger(GenericUtilsService.class);

	@Autowired
	private GridUtilsDAO		genericUtilsDAO	= null;

	public Integer findCount(String gridId, GenericGridParams gridParams, Map<String, Object> requestParam) throws Exception {
		GridDetails gridDetails = getGridDetails(gridId);
		return genericUtilsDAO.findCount(gridDetails, gridParams, requestParam);
	}

	public List<Map<String, Object>> findAllRecords(String gridId, GenericGridParams gridParams, Map<String, Object> requestParam) throws Exception {
		GridDetails					gridDetails	= getGridDetails(gridId);
		List<Map<String, Object>>	allRecords	= genericUtilsDAO.findAllRecords(gridDetails, gridParams, requestParam);
		return allRecords;
	}

	private GridDetails getGridDetails(String gridId) {
		return genericUtilsDAO.getGridDetails(gridId);
	}
}
