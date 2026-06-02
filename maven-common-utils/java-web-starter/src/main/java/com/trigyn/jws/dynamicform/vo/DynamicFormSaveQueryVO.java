
package com.trigyn.jws.dynamicform.vo;

import java.util.Objects;

public class DynamicFormSaveQueryVO {

	private String	dynamicFormId		= null;
	private String	formSaveQuery		= null;
	private Integer	sequence			= null;
	private String	resultVariableName	= null;
	private Integer	daoQueryType		= null;
	private String	datasourceId		= null;
	private String	dynamicFormQueryId	= null;

	public DynamicFormSaveQueryVO() {

	}

	public DynamicFormSaveQueryVO(String dynamicFormId, String formSaveQuery, Integer sequence,
			String resultVariableName, Integer daoQueryType, String datasourceId,String dynamicFormQueryId) {
		this.dynamicFormId		= dynamicFormId;
		this.formSaveQuery		= formSaveQuery;
		this.sequence			= sequence;
		this.resultVariableName	= resultVariableName;
		this.daoQueryType		= daoQueryType;
		this.datasourceId		= datasourceId;
		this.dynamicFormQueryId	= dynamicFormQueryId;
	}

	/**
	 * @return the dynamicFormId
	 */
	public String getDynamicFormId() {
		return dynamicFormId;
	}

	/**
	 * @param dynamicFormId the dynamicFormId to set
	 */
	public void setDynamicFormId(String dynamicFormId) {
		this.dynamicFormId = dynamicFormId;
	}

	/**
	 * @return the formSaveQuery
	 */
	public String getFormSaveQuery() {
		return formSaveQuery;
	}

	/**
	 * @param formSaveQuery the formSaveQuery to set
	 */
	public void setFormSaveQuery(String formSaveQuery) {
		this.formSaveQuery = formSaveQuery;
	}

	/**
	 * @return the sequence
	 */
	public Integer getSequence() {
		return sequence;
	}

	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getResultVariableName() {
		return resultVariableName;
	}

	public void setResultVariableName(String resultVariableName) {
		this.resultVariableName = resultVariableName;
	}

	public Integer getDaoQueryType() {
		return daoQueryType;
	}

	public void setDaoQueryType(Integer daoQueryType) {
		this.daoQueryType = daoQueryType;
	}

	public String getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(String datasourceId) {
		this.datasourceId = datasourceId;
	}

	public String getDynamicFormQueryId() {
		return dynamicFormQueryId;
	}

	public void setDynamicFormQueryId(String dynamicFormQueryId) {
		this.dynamicFormQueryId = dynamicFormQueryId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(daoQueryType, datasourceId, dynamicFormId, dynamicFormQueryId, formSaveQuery,
				resultVariableName, sequence);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DynamicFormSaveQueryVO other = (DynamicFormSaveQueryVO) obj;
		return Objects.equals(daoQueryType, other.daoQueryType) && Objects.equals(datasourceId, other.datasourceId)
				&& Objects.equals(dynamicFormId, other.dynamicFormId)
				&& Objects.equals(dynamicFormQueryId, other.dynamicFormQueryId)
				&& Objects.equals(formSaveQuery, other.formSaveQuery)
				&& Objects.equals(resultVariableName, other.resultVariableName)
				&& Objects.equals(sequence, other.sequence);
	}

	@Override
	public String toString() {
		return "DynamicFormSaveQueryVO [dynamicFormId=" + dynamicFormId + ", formSaveQuery=" + formSaveQuery
				+ ", sequence=" + sequence + ", resultVariableName=" + resultVariableName + ", daoQueryType="
				+ daoQueryType + ", datasourceId=" + datasourceId + ", dynamicFormQueryId=" + dynamicFormQueryId + "]";
	}

}
