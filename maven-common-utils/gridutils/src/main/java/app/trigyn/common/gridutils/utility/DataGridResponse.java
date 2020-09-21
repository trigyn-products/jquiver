package app.trigyn.common.gridutils.utility;

import java.util.List;
import java.util.Map;


public class DataGridResponse
{
    private String total = null;
    private String curPage = null;
    private List<Map<String, Object>> rows = null;

    
    public DataGridResponse(List<Map<String, Object>> list, Integer total, Integer currentPage)
    {
        this.rows = list;
        this.total = total + "";
        this.curPage = currentPage + "";
    }

    public DataGridResponse(List<Map<String, Object>> list, Integer total, Map<String, String> userData)
    {
        this.rows = list;
        this.total = total + "";
    }

    /**
	 * @return the total
	 */
	public String getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(String total) {
		this.total = total;
	}

	/**
	 * @return the rows
	 */
	public List<Map<String, Object>> getRows() {
		return rows;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(List<Map<String, Object>> rows) {
		this.rows = rows;
	}

	/**
	 * @return the curPage
	 */
	public String getCurPage() {
		return curPage;
	}

	/**
	 * @param curPage the curPage to set
	 */
	public void setCurPage(String curPage) {
		this.curPage = curPage;
	}

}
