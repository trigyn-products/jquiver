package com.trigyn.jws.dynamicform.vo;

import java.io.Serializable;
import java.util.Objects;

public class DynamicFormVO  implements Serializable{

	private static final long serialVersionUID 	= -6467720176448132975L;
	private String formId 						= null;
	private String formName 					= null;
	private String formDescription 				= null;
	private String formSelectQuery 				= null;
	private String formBody 					= null;
	private String formSelectCheckSum   		= null;
	private String formBodyCheckSum   			= null;
	
	public DynamicFormVO() {

	}

	public DynamicFormVO(String formId, String formName, String formDescription, String formSelectQuery,
			String formBody, String formSelectCheckSum,String formBodyCheckSum) {
		this.formId 				= formId;
		this.formName 				= formName;
		this.formDescription 		= formDescription;
		this.formSelectQuery 		= formSelectQuery;
		this.formBody 				= formBody;
		this.formSelectCheckSum 	= formSelectCheckSum;
		this.formBodyCheckSum       = formBodyCheckSum;
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
	
	

	public String getFormSelectCheckSum() {
		return formSelectCheckSum;
	}

	public void setFormSelectCheckSum(String formSelectCheckSum) {
		this.formSelectCheckSum = formSelectCheckSum;
	}

	public String getFormBodyCheckSum() {
		return formBodyCheckSum;
	}

	public void setFormBodyCheckSum(String formBodyCheckSum) {
		this.formBodyCheckSum = formBodyCheckSum;
	}

	@Override
	public int hashCode() {
		return Objects.hash(formBody, formDescription, formId, formName, formSelectCheckSum,formBodyCheckSum, formSelectQuery);
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
		return Objects.equals(formBody, other.formBody) && Objects.equals(formDescription, other.formDescription)
				&& Objects.equals(formId, other.formId) && Objects.equals(formName, other.formName)
				&& Objects.equals(formSelectCheckSum, other.formSelectCheckSum)
				&& Objects.equals(formBodyCheckSum, other.formBodyCheckSum)
				&& Objects.equals(formSelectQuery, other.formSelectQuery);
	}

	@Override
	public String toString() {
		return "DynamicFormVO [formId=" + formId + ", formName=" + formName + ", formDescription=" + formDescription
				+ ", formSelectQuery=" + formSelectQuery + ", formBody=" + formBody 
				+ ", formSelectCheckSum=" + formSelectCheckSum + ", formBodyCheckSum=" + formBodyCheckSum 
				+ "]";
	}

	
}
