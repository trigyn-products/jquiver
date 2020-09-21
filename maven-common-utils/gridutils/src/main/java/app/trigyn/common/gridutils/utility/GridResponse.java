package app.trigyn.common.gridutils.utility;

import java.util.List;
import java.util.Map;

public class GridResponse {

	private List<Map<String, Object>> list = null;

	private Integer matchingRowCount = null;

	private GenericGridParams gridParams = null;

	private Map<String, String> userData = null;

	public GridResponse(List<Map<String, Object>> list, Integer matchingRowCount, GenericGridParams gridParams) {
		super();
		this.list = list;
		this.matchingRowCount = matchingRowCount;
		this.gridParams = gridParams;
	}

	public CustomGridsResponse getResponse() throws Exception {

		Double rowCount = matchingRowCount.doubleValue();
		Double totalPages = Math.ceil(rowCount / gridParams.getRowsPerPage());

		if (gridParams.getStartIndex() > matchingRowCount) {
			gridParams.setPageIndex(1);
			gridParams.setStartIndex(0);
		}

		CustomGridsResponse response = new CustomGridsResponse();
		response.setRows(list);
		response.setRecords(String.valueOf(matchingRowCount));
		response.setPage(String.valueOf(gridParams.getPageIndex()));
		response.setTotal(String.valueOf(totalPages.intValue()));

		if (userData != null) {
			response.setUserdata(userData);
		}

		return response;
	}

}
