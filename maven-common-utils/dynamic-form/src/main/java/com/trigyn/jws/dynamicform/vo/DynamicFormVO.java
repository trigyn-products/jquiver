package com.trigyn.jws.dynamicform.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class DynamicFormVO implements Serializable {

	private static final long				serialVersionUID		= -6467720176448132975L;
	private String							formId					= null;
	private String							formName				= null;
	private String							formDescription			= null;
	private String							formSelectQuery			= null;
	private String							formBody				= null;
	private Integer							formTypeId				= null;
	private String							createdBy				= null;
	private Date							createdDate				= null;
	private List<DynamicFormSaveQueryVO>	dynamicFormSaveQueries	= null;
	private Integer							selectQueryType  		= null;
	private String					variableName	= null;
	private String					datasourceDetails	= null;
	private String					queryType	= null;

	public DynamicFormVO() {

	}

	public DynamicFormVO(String formId, String formName, String formDescription, String formSelectQuery,
			String formBody, Integer formTypeId, String createdBy, Date createdDate,
			List<DynamicFormSaveQueryVO> dynamicFormSaveQueries) {
		this.formId					= formId;
		this.formName				= formName;
		this.formDescription		= formDescription;
		this.formSelectQuery		= formSelectQuery;
		this.formBody				= formBody;
		this.formTypeId				= formTypeId;
		this.createdBy				= createdBy;
		this.createdDate			= createdDate;
		this.dynamicFormSaveQueries	= dynamicFormSaveQueries;
	}

	/**
	 * @return the formId
	 */
	public String getFormId() {
		return formId;
	}

	/**
	 * @param formId the formId to set
	 */
	public void setFormId(String formId) {
		this.formId = formId;
	}

	/**
	 * @return the formName
	 */
	public String getFormName() {
		return formName;
	}

	/**
	 * @param formName the formName to set
	 */
	public void setFormName(String formName) {
		this.formName = formName;
	}

	/**
	 * @return the formDescription
	 */
	public String getFormDescription() {
		return formDescription;
	}

	/**
	 * @param formDescription the formDescription to set
	 */
	public void setFormDescription(String formDescription) {
		this.formDescription = formDescription;
	}

	/**
	 * @return the formSelectQuery
	 */
	public String getFormSelectQuery() {
		return formSelectQuery;
	}

	/**
	 * @param formSelectQuery the formSelectQuery to set
	 */
	public void setFormSelectQuery(String formSelectQuery) {
		this.formSelectQuery = formSelectQuery;
	}

	/**
	 * @return the formBody
	 */
	public String getFormBody() {
		return formBody;
	}

	/**
	 * @param formBody the formBody to set
	 */
	public void setFormBody(String formBody) {
		this.formBody = formBody;
	}

	/**
	 * @return the formTypeId
	 */
	public Integer getFormTypeId() {
		return formTypeId;
	}

	/**
	 * @param formTypeId the formTypeId to set
	 */
	public void setFormTypeId(Integer formTypeId) {
		this.formTypeId = formTypeId;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the dynamicFormSaveQueries
	 */
	public List<DynamicFormSaveQueryVO> getDynamicFormSaveQueries() {
		return dynamicFormSaveQueries;
	}

	/**
	 * @param dynamicFormSaveQueries the dynamicFormSaveQueries to set
	 */
	public void setDynamicFormSaveQueries(List<DynamicFormSaveQueryVO> dynamicFormSaveQueries) {
		this.dynamicFormSaveQueries = dynamicFormSaveQueries;
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdBy, createdDate, dynamicFormSaveQueries, formBody, formDescription, formId, formName,
				formSelectQuery, formTypeId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DynamicFormVO other = (DynamicFormVO) obj;
		return Objects.equals(createdBy, other.createdBy) && Objects.equals(createdDate, other.createdDate)
				&& Objects.equals(dynamicFormSaveQueries, other.dynamicFormSaveQueries)
				&& Objects.equals(formBody, other.formBody) && Objects.equals(formDescription, other.formDescription)
				&& Objects.equals(formId, other.formId) && Objects.equals(formName, other.formName)
				&& Objects.equals(formSelectQuery, other.formSelectQuery)
				&& Objects.equals(formTypeId, other.formTypeId);
	}

	@Override
	public String toString() {
		return "DynamicFormVO [formId=" + formId + ", formName=" + formName + ", formDescription=" + formDescription
				+ ", formSelectQuery=" + formSelectQuery + ", formBody=" + formBody + ", formTypeId=" + formTypeId
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", dynamicFormSaveQueries="
				+ dynamicFormSaveQueries + "]";
	}

	public Integer getSelectQueryType() {
		return selectQueryType;
	}

	public void setSelectQueryType(Integer selectQueryType) {
		this.selectQueryType = selectQueryType;
	}

	public String getVariableName() {
		return variableName;
	}

	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getDatasourceDetails() {
		return datasourceDetails;
	}

	public void setDatasourceDetails(String datasourceDetails) {
		this.datasourceDetails = datasourceDetails;
	}

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

}
