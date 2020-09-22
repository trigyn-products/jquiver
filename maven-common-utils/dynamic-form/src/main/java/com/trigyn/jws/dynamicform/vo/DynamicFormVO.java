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

	
	public String getFormId() {
		return formId;
	}

	
	public void setFormId(String formId) {
		this.formId = formId;
	}

	
	public String getFormName() {
		return formName;
	}

	
	public void setFormName(String formName) {
		this.formName = formName;
	}

	
	public String getFormDescription() {
		return formDescription;
	}

	
	public void setFormDescription(String formDescription) {
		this.formDescription = formDescription;
	}

	
	public String getFormSelectQuery() {
		return formSelectQuery;
	}

	
	public void setFormSelectQuery(String formSelectQuery) {
		this.formSelectQuery = formSelectQuery;
	}

	
	public String getFormBody() {
		return formBody;
	}

	
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
