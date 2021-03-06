
package com.trigyn.jws.dynamicform.vo;

import java.util.Objects;

public class DynamicFormSaveQueryVO {

	private String	dynamicFormId	= null;
	private String	formSaveQuery	= null;
	private Integer	sequence		= null;

	public DynamicFormSaveQueryVO() {

	}

	public DynamicFormSaveQueryVO(String dynamicFormId, String formSaveQuery, Integer sequence) {
		this.dynamicFormId	= dynamicFormId;
		this.formSaveQuery	= formSaveQuery;
		this.sequence		= sequence;
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

	@Override
	public int hashCode() {
		return Objects.hash(dynamicFormId, formSaveQuery, sequence);
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
		DynamicFormSaveQueryVO other = (DynamicFormSaveQueryVO) obj;
		return Objects.equals(dynamicFormId, other.dynamicFormId) && Objects.equals(formSaveQuery, other.formSaveQuery)
				&& Objects.equals(sequence, other.sequence);
	}

	@Override
	public String toString() {
		return "DynamicFormSaveQueryVO [dynamicFormId=" + dynamicFormId + ", formSaveQuery=" + formSaveQuery
				+ ", sequence=" + sequence + "]";
	}

}
