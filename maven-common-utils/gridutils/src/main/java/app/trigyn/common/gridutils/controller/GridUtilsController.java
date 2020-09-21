package app.trigyn.common.gridutils.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.trigyn.common.gridutils.service.GenericUtilsService;
import app.trigyn.common.gridutils.utility.CustomGridsResponse;
import app.trigyn.common.gridutils.utility.DataGridResponse;
import app.trigyn.common.gridutils.utility.GenericGridParams;
import app.trigyn.common.gridutils.utility.GridResponse;

@RestController
@RequestMapping(value = "/cf")
public class GridUtilsController {
	
	private final static Logger logger = LogManager.getLogger(GridUtilsController.class);
     
    @Autowired
    private GenericUtilsService genericGridService = null;

	@PostMapping(value="/grid-data", produces = MediaType.APPLICATION_JSON_VALUE)
	public CustomGridsResponse loadGridData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String gridId=request.getParameter("gridId");
		GenericGridParams gridParams = new GenericGridParams(request);
    	Integer matchingRowCount = genericGridService.findCount(gridId,gridParams);
        List<Map<String,Object>> list = genericGridService.findAllRecords(gridId, gridParams);
        GridResponse gridResponse = new GridResponse(list, matchingRowCount, gridParams);
        return gridResponse.getResponse();
	}
	

	
	@PostMapping(value="/pq-grid-data", produces = MediaType.APPLICATION_JSON_VALUE)
    public DataGridResponse loadPQGridWithData(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String gridId=request.getParameter("gridId");
        GenericGridParams gridParams = new GenericGridParams();
        gridParams.getPQGridDataParams(request);
        Integer matchingRowCount = genericGridService.findCount(gridId,gridParams);
        List<Map<String,Object>> list = genericGridService.findAllRecords(gridId, gridParams);
        DataGridResponse gridResponse = new DataGridResponse(list, matchingRowCount, gridParams.getPageIndex());
        List<DataGridResponse> lstDataGridResponse = new ArrayList<DataGridResponse>();
        lstDataGridResponse.add(gridResponse);
        return gridResponse;
    }





}