package com.trigyn.jws.gridutils.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class GenericGridParams {
	private String				sortIndex		= null;
	private String				sortOrder		= null;
	private int					pageIndex		= Integer.MIN_VALUE;
	private int					rowsPerPage		= Integer.MIN_VALUE;
	private int					startIndex		= Integer.MIN_VALUE;
	private FilterParams		filterParams	= null;
	private Map<String, Object>	criteriaParams	= null;

	public GenericGridParams() {
		super();
	}

	public GenericGridParams(HttpServletRequest request, Integer matchingRowCount)
			throws JsonParseException, IOException {
		this.sortIndex = request.getParameter("sidx");
		this.sortOrder = request.getParameter("sord");
		if (request.getParameter("rows") != null) {
			this.rowsPerPage = Integer.parseInt(request.getParameter("rows"));
		}
		if (matchingRowCount < rowsPerPage) {
			this.pageIndex = 1;
			this.startIndex = 0;
		} else {
			if (request.getParameter("page") != null) {
				this.pageIndex = Integer.parseInt(request.getParameter("page"));
				this.startIndex = rowsPerPage * (pageIndex - 1);
			}
		}
		String filters = request.getParameter("filters");
		if (filters != null) {
			ObjectMapper mapper = new ObjectMapper();
			this.filterParams = mapper.readValue(filters,FilterParams.class);
		}
		this.criteriaParams = request.getParameter("additionalParameters") == null ? new HashMap<String, Object>() 
				: new Gson().fromJson(request.getParameter("additionalParameters"), Map.class);
	}
	
    public GenericGridParams(HttpServletRequest request, boolean isDataGrid)
            throws JsonParseException, IOException {
	    this.sortIndex = request.getParameter("sidx");
        this.sortOrder = request.getParameter("sord");
        this.rowsPerPage = Integer.parseInt(request.getParameter("rows"));

        this.pageIndex = Integer.parseInt(request.getParameter("page"));
        this.startIndex = rowsPerPage * (pageIndex - 1);

        String filters = request.getParameter("filterRules");
        if (filters != null && "".equals(filters) == false) {
            ObjectMapper mapper = new ObjectMapper();
            final TypeReference<List<SearchFields>> typeRef = new TypeReference<List<SearchFields>>() {};
            List<SearchFields> searchFields = mapper.readValue(filters,typeRef);
            FilterParams params = new FilterParams("", searchFields);
            this.filterParams = params;
        }
        this.criteriaParams = request.getParameter("additionalParameters") == null ? new HashMap<String, Object>() 
				: new Gson().fromJson(request.getParameter("additionalParameters"), Map.class);
    }

	public GenericGridParams(String sortIndex, String sortOrder, int pageIndex, int rowsPerPage, int startIndex,
			FilterParams filterParams) {
		super();
		this.sortIndex = sortIndex;
		this.sortOrder = sortOrder;
		this.pageIndex = pageIndex;
		this.rowsPerPage = rowsPerPage;
		this.startIndex = startIndex;
		this.filterParams = filterParams;
	}

	public GenericGridParams(String sortIndex, String sortOrder, int pageIndex, int rowsPerPage, int startIndex,
			String filterParamsJson) throws JsonProcessingException, IOException {
		super();
		this.sortIndex = sortIndex;
		this.sortOrder = sortOrder;
		this.pageIndex = pageIndex;
		this.rowsPerPage = rowsPerPage;
		this.startIndex = startIndex;

		if (filterParamsJson != null) {
			ObjectMapper mapper = new ObjectMapper();
			this.filterParams = mapper.readValue(filterParamsJson,FilterParams.class);
		}

	}

	public GenericGridParams(HttpServletRequest request) throws JsonParseException, IOException {
		this.sortIndex = request.getParameter("sidx");
		this.sortOrder = request.getParameter("sord");
		this.rowsPerPage = Integer.parseInt(request.getParameter("rows"));

		this.pageIndex = Integer.parseInt(request.getParameter("page"));
		this.startIndex = rowsPerPage * (pageIndex - 1);

		String filters = request.getParameter("filters");
		if (filters != null) {
			ObjectMapper mapper = new ObjectMapper();
			this.filterParams = mapper.readValue(filters,FilterParams.class);
		}
		this.criteriaParams = request.getParameter("additionalParameters") == null ? new HashMap<String, Object>() 
				: new Gson().fromJson(request.getParameter("additionalParameters"), Map.class);
	}
	
	public GenericGridParams getPQGridDataParams(HttpServletRequest request) throws JsonParseException, JSONException, IOException {
		this.rowsPerPage = Integer.parseInt(request.getParameter("pq_rpp"));
		this.pageIndex = Integer.parseInt(request.getParameter("pq_curpage")) == 0 ? 1 : Integer.parseInt(request.getParameter("pq_curpage"));
		this.startIndex = rowsPerPage * (pageIndex - 1);
		
		//sorting functionality
		String strPQSort = request.getParameter("pq_sort");
		if(strPQSort != null)
		{
			JSONArray jsonArrSort = new JSONArray(strPQSort);
			JSONObject jsonObjSort = jsonArrSort.getJSONObject(0);
			this.sortIndex = jsonObjSort.getString("dataIndx");
			String sortDir = jsonObjSort.getString("dir");
			if(sortDir != null)
			{
				if(sortDir.equalsIgnoreCase("up"))
				{
					this.sortOrder = "ASC";
				}
				else if(sortDir.equalsIgnoreCase("down"))
				{
					this.sortOrder = "DESC";
				}
			}
		}

		String filters = request.getParameter("pq_filter");
		if (filters != null) {
			FilterParams filterParams = new FilterParams();
			List<SearchFields> lstSearch = new ArrayList<SearchFields>();
			JSONObject jsonObj = new JSONObject(filters);
			filterParams.setGroupOp(jsonObj.getString("mode"));
			
			JSONArray jsonArr = jsonObj.getJSONArray("data");
			
			for(int i = 0; i < jsonArr.length() ; i ++)
			{
				SearchFields searchField = new SearchFields();
				JSONObject dataJsonObj = jsonArr.getJSONObject(i);
				searchField.setField(dataJsonObj.getString("dataIndx"));
				searchField.setData(dataJsonObj.getString("value"));
				searchField.setOp(dataJsonObj.getString("condition"));
				
				lstSearch.add(searchField);
			}
			filterParams.setRules(lstSearch);
			this.setFilterParams(filterParams);
		}
		this.criteriaParams = request.getParameter("additionalParameters") == null ? new HashMap<String, Object>() 
				: new Gson().fromJson(request.getParameter("additionalParameters"), Map.class);
		
		return this;	
	}

	public String getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(String sortIndex) {
		this.sortIndex = sortIndex;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getRowsPerPage() {
		return rowsPerPage;
	}

	public void setRowsPerPage(int rowsPerPage) {
		this.rowsPerPage = rowsPerPage;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public FilterParams getFilterParams() {
		return filterParams;
	}

	public void setFilterParams(FilterParams filterParams) {
		this.filterParams = filterParams;
	}

	public Map<String, Object> getCriteriaParams() {
		return criteriaParams;
	}

	public void setCriteriaParams(Map<String, Object> criteriaParams) {
		this.criteriaParams = criteriaParams;
	}

}
