package com.trigyn.jws.gridutils.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.gridutils.service.GenericUtilsService;
import com.trigyn.jws.gridutils.utility.CustomGridsResponse;
import com.trigyn.jws.gridutils.utility.DataGridResponse;
import com.trigyn.jws.gridutils.utility.GenericGridParams;
import com.trigyn.jws.gridutils.utility.GridResponse;
import com.trigyn.jws.usermanagement.security.config.Authorized;
import com.trigyn.jws.usermanagement.utils.Constants;

@RestController
@RequestMapping(value = "/cf")
public class GridUtilsController {

	private final static Logger	logger				= LogManager.getLogger(GridUtilsController.class);

	@Autowired
	private GenericUtilsService	genericGridService	= null;

	@Autowired
	private IUserDetailsService		detailsService			= null;

	@RequestMapping(value = "/grid-data", produces = MediaType.APPLICATION_JSON_VALUE)
	@Authorized(moduleName = Constants.GRIDUTILS)
	public CustomGridsResponse loadGridData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String						gridId				= request.getParameter("gridId");
		GenericGridParams			gridParams			= new GenericGridParams(request, detailsService);
		Integer						matchingRowCount	= genericGridService.findCount(gridId, gridParams);
		List<Map<String, Object>>	list				= genericGridService.findAllRecords(gridId, gridParams);
		GridResponse				gridResponse		= new GridResponse(list, matchingRowCount, gridParams);
		return gridResponse.getResponse();
	}

	@RequestMapping(value = "/pq-grid-data", produces = MediaType.APPLICATION_JSON_VALUE)
	@Authorized(moduleName = Constants.GRIDUTILS)
	public DataGridResponse loadPQGridWithData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String				gridId		= request.getParameter("gridId");
		GenericGridParams	gridParams	= new GenericGridParams();
		gridParams.getPQGridDataParams(request);
		Integer						matchingRowCount	= genericGridService.findCount(gridId, gridParams);
		List<Map<String, Object>>	list				= genericGridService.findAllRecords(gridId, gridParams);
		DataGridResponse			gridResponse		= new DataGridResponse(list, matchingRowCount, gridParams.getPageIndex());
		List<DataGridResponse>		lstDataGridResponse	= new ArrayList<DataGridResponse>();
		lstDataGridResponse.add(gridResponse);
		return gridResponse;
	}

}