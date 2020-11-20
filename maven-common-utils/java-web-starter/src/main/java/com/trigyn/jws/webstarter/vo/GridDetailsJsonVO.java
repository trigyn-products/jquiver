package com.trigyn.jws.webstarter.vo;

import java.io.Serializable;

public class GridDetailsJsonVO  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String entityName					= null;

	private String formId					= null;

    private String gridColumnName               = null;

    private String gridDescription               = null;

	private String gridId                        = null;

    private String gridName                      = null;

    private String gridTableName                 = null;

    private String primaryKey                   = null;

	private String queryType                    = null;

    public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getGridColumnName() {
		return gridColumnName;
	}

	public void setGridColumnName(String gridColumnName) {
		this.gridColumnName = gridColumnName;
	}

	public String getGridDescription() {
		return gridDescription;
	}

	public void setGridDescription(String gridDescription) {
		this.gridDescription = gridDescription;
	}

	public String getGridId() {
		return gridId;
	}

	public void setGridId(String gridId) {
		this.gridId = gridId;
	}

	public String getGridName() {
		return gridName;
	}

	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	public String getGridTableName() {
		return gridTableName;
	}

	public void setGridTableName(String gridTableName) {
		this.gridTableName = gridTableName;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

    
}
