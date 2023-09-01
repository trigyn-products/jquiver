package com.trigyn.jws.gridutils.controller;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.vo.FileInfo;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.gridutils.service.GenericUtilsService;
import com.trigyn.jws.gridutils.utility.CustomGridsResponse;
import com.trigyn.jws.gridutils.utility.DataGridResponse;
import com.trigyn.jws.gridutils.utility.GenericGridParams;
import com.trigyn.jws.gridutils.utility.GridResponse;
import com.trigyn.jws.usermanagement.repository.AuthorizedValidatorDAO;
import com.trigyn.jws.usermanagement.security.config.Authorized;
import com.trigyn.jws.usermanagement.utils.Constants;

@RestController
@RequestMapping(value = "/cf")
public class GridUtilsController {

	private final static Logger	logger				= LogManager.getLogger(GridUtilsController.class);

	@Autowired
	private GenericUtilsService	genericGridService	= null;

	@Autowired
	private IUserDetailsService	detailsService		= null;

	@Autowired
	private AuthorizedValidatorDAO				authorizedValidatorDAO			= null;

	private String								moduleId						= "07067149-098d-11eb-9a16-f48e38ab9348";

	@RequestMapping(value = "/grid-data", produces = MediaType.APPLICATION_JSON_VALUE)
	@Authorized(moduleName = Constants.GRIDUTILS)
	public CustomGridsResponse loadGridData(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws Exception {
		logger.debug("Inside GridUtilsController.loadGridData(gridId: {})", httpServletRequest.getParameter("gridId"));
		String gridId = httpServletRequest.getParameter("gridId");
		try {
			UserDetailsVO		detailsVO		= detailsService.getUserDetails();
			Map<String, Object>	requestParam	= new HashMap<>();
			requestParam.put("httpServletRequest", httpServletRequest);
			requestParam.put("session", httpServletRequest.getSession());
			requestParam.put("userObject", detailsVO);
			
			GenericGridParams			gridParams			= new GenericGridParams(httpServletRequest, detailsService);
			Integer						matchingRowCount	= genericGridService.findCount(gridId, gridParams,
					requestParam);
			List<Map<String, Object>>	list				= genericGridService.findAllRecords(gridId, gridParams,
					requestParam);
			GridResponse				gridResponse		= new GridResponse(list, matchingRowCount, gridParams);
			
			return gridResponse.getResponse();
		} catch (Exception exception) {
			logger.error("Error occured while fetching grid response(gridId: {})", gridId, exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
	}

	@RequestMapping(value = "/pq-grid-data", produces = MediaType.APPLICATION_JSON_VALUE)
	@Authorized(moduleName = Constants.GRIDUTILS)
	public DataGridResponse loadPQGridWithData(HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse) throws Exception {
		logger.debug("Inside GridUtilsController.loadPQGridWithData(gridId: {})",
				httpServletRequest.getParameter("gridId"));
		String gridId = httpServletRequest.getParameter("gridId");
		
		try {
			UserDetailsVO		detailsVO		= detailsService.getUserDetails();
			String strPQSort = httpServletRequest.getParameter("pq_sort");
			String filters = httpServletRequest.getParameter("pq_filter");
			DataGridResponse	gridResponse	= getDataGridResponse(httpServletRequest, gridId, detailsVO, strPQSort, filters);
			return gridResponse;
		} catch (Exception exception) {
			logger.error("Error occured while fetching PQ grid response(gridId: {})", gridId, exception);
			if (httpServletResponse.getStatus() == HttpStatus.FORBIDDEN.value()) {
				return null;
			}
			httpServletResponse.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
			return null;
		}
	}

	private DataGridResponse getDataGridResponse(HttpServletRequest httpServletRequest, String gridId,
			UserDetailsVO detailsVO, String strPQSort, String filters) throws Exception {
		Map<String, Object> requestParam = new HashMap<>();
		requestParam.put("httpServletRequest", httpServletRequest);
		requestParam.put("session", httpServletRequest.getSession());
		requestParam.put("userObject", detailsVO);
		
		GenericGridParams gridParams = new GenericGridParams();
		gridParams.getPQGridDataParams(httpServletRequest, detailsService, strPQSort, filters);
		Integer						matchingRowCount	= genericGridService.findCount(gridId, gridParams,
				requestParam);
		List<Map<String, Object>>	list				= genericGridService.findAllRecords(gridId, gridParams,
				requestParam);
		DataGridResponse			gridResponse		= new DataGridResponse(list, matchingRowCount,
				gridParams.getPageIndex());
		return gridResponse;
	}

	public Map<String, Object> loadPQGridWithData(HttpServletRequest a_httpServletRequest, Map<String, FileInfo> files,
			Map<String, Object> dAOparameters, UserDetailsVO userDetails) {
		Map<String, Object> responseMap = new HashMap<>();
		
		logger.debug("Inside GridUtilsController.loadPQGridWithData(gridId: {})",
				a_httpServletRequest.getParameter("gridId"));
		String gridId = a_httpServletRequest.getParameter("gridId");
		boolean	hasAccess	= false;
		Long	count		= authorizedValidatorDAO.hasAccessToGridUtils(gridId, userDetails.getRoleIdList(), moduleId);
		if (count > 0) {
			hasAccess = true;
		}
		
		if(hasAccess) {
			try {
				String strPQSort = null;
				if(a_httpServletRequest.getParameter("pq_sort") != null) {
					strPQSort = java.net.URLDecoder.decode(a_httpServletRequest.getParameter("pq_sort"), StandardCharsets.UTF_8.name());
				}
				
				String filters = null;
				if(a_httpServletRequest.getParameter("pq_filter") != null) {
					filters = java.net.URLDecoder.decode(a_httpServletRequest.getParameter("pq_filter"), StandardCharsets.UTF_8.name());
				}
				
				DataGridResponse gridResponse = getDataGridResponse(a_httpServletRequest, gridId, userDetails, strPQSort, filters);
				responseMap.put("pq-grid-data", gridResponse);

			} catch (Exception exception) {
				logger.error("Error occured while fetching PQ grid response(gridId: {})", gridId, exception);
				return null;
			}
		} else {
			logger.error("You do not have enough privilege to access: "+gridId, gridId);
			return null;
		}

		return responseMap;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.genericGridService	= applicationContext.getBean(GenericUtilsService.class);
		this.detailsService		= applicationContext.getBean(IUserDetailsService.class);
		this.authorizedValidatorDAO		= applicationContext.getBean(AuthorizedValidatorDAO.class);
	}

}