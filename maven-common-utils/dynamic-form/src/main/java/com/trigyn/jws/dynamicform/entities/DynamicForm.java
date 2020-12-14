package com.trigyn.jws.dynamicform.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "dynamic_form")
public class DynamicForm {

	@Id
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
	@Column(name = "form_id")
	private String formId = null;

	@Column(name = "form_name")
	private String formName = null;

	@Column(name = "form_description")
	private String formDescription = null;

	@Column(name = "form_select_query")
	private String formSelectQuery = null;

	@Column(name = "form_body")
	private String formBody = null;
	
	@Column(name = "form_type_id")
	private Integer formTypeId = 1;

	@Column(name = "created_by")
	private String createdBy = null;

	@JsonIgnore
	@Column(name = "created_date")
	private Date createdDate = null;
	
	@Column(name = "form_select_checksum")
	private String formSelectChecksum = null;

	@Column(name = "form_body_checksum")
	private String formBodyChecksum = null;

	@OneToMany(mappedBy = "dynamicForm")
	private List<DynamicFormSaveQuery> dynamicFormSaveQueries = null;

	public DynamicForm() {
		
	}

	public DynamicForm(String formId, String formName, String formDescription, String formSelectQuery, String formBody,
			Integer formTypeId, String createdBy, Date createdDate, String formSelectChecksum, String formBodyChecksum,
			List<DynamicFormSaveQuery> dynamicFormSaveQueries) {
		this.formId = formId;
		this.formName = formName;
		this.formDescription = formDescription;
		this.formSelectQuery = formSelectQuery;
		this.formBody = formBody;
		this.formTypeId = formTypeId;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.formSelectChecksum = formSelectChecksum;
		this.formBodyChecksum = formBodyChecksum;
		this.dynamicFormSaveQueries = dynamicFormSaveQueries;
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

	public Integer getFormTypeId() {
		return formTypeId;
	}

	public void setFormTypeId(Integer formTypeId) {
		this.formTypeId = formTypeId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public List<DynamicFormSaveQuery> getDynamicFormSaveQueries() {
		return dynamicFormSaveQueries;
	}

	public void setDynamicFormSaveQueries(List<DynamicFormSaveQuery> dynamicFormSaveQueries) {
		this.dynamicFormSaveQueries = dynamicFormSaveQueries;
	}

	public String getFormSelectChecksum() {
		return formSelectChecksum;
	}

	public void setFormSelectChecksum(String formSelectChecksum) {
		this.formSelectChecksum = formSelectChecksum;
	}

	public String getFormBodyChecksum() {
		return formBodyChecksum;
	}

	public void setFormBodyChecksum(String formBodyChecksum) {
		this.formBodyChecksum = formBodyChecksum;
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdBy, createdDate, dynamicFormSaveQueries, formBody, formBodyChecksum, formDescription,
				formId, formName, formSelectChecksum, formSelectQuery, formTypeId);
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
		DynamicForm other = (DynamicForm) obj;
		return Objects.equals(createdBy, other.createdBy) && Objects.equals(createdDate, other.createdDate)
				&& Objects.equals(dynamicFormSaveQueries, other.dynamicFormSaveQueries)
				&& Objects.equals(formBody, other.formBody) && Objects.equals(formBodyChecksum, other.formBodyChecksum)
				&& Objects.equals(formDescription, other.formDescription) && Objects.equals(formId, other.formId)
				&& Objects.equals(formName, other.formName)
				&& Objects.equals(formSelectChecksum, other.formSelectChecksum)
				&& Objects.equals(formSelectQuery, other.formSelectQuery)
				&& Objects.equals(formTypeId, other.formTypeId);
	}

	@Override
	public String toString() {
		return "DynamicForm [formId=" + formId + ", formName=" + formName + ", formDescription=" + formDescription
				+ ", formSelectQuery=" + formSelectQuery + ", formBody=" + formBody + ", formTypeId=" + formTypeId
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", formSelectChecksum="
				+ formSelectChecksum + ", formBodyChecksum=" + formBodyChecksum + ", dynamicFormSaveQueries="
				+ dynamicFormSaveQueries + "]";
	}
	  
    public DynamicForm getObject() {
    	DynamicForm obj = new DynamicForm();
    	obj.setCreatedBy(createdBy!=null?createdBy.trim():createdBy);
    	obj.setCreatedDate(createdDate);
    	obj.setFormBody(formBody!=null?formBody.trim():formBody);
    	obj.setFormBodyChecksum(formBodyChecksum!=null?formBodyChecksum.trim():formBodyChecksum);
    	obj.setFormDescription(formDescription!=null?formDescription.trim():formDescription);
    	obj.setFormId(formId!=null?formId.trim():formId);
    	obj.setFormName(formName!=null?formName.trim():formName);
    	obj.setFormSelectChecksum(formSelectChecksum!=null?formSelectChecksum.trim():formSelectChecksum);
    	obj.setFormSelectQuery(formSelectQuery!=null?formSelectQuery.trim():formSelectQuery);
    	obj.setFormTypeId(formTypeId);
    	

		List<DynamicFormSaveQuery>	dfsOtr		= new ArrayList<>();
		if(dynamicFormSaveQueries != null && !dynamicFormSaveQueries.isEmpty()) {
			for(DynamicFormSaveQuery dfs : dynamicFormSaveQueries) {
				dfsOtr.add(dfs.getObject());
			}
			obj.setDynamicFormSaveQueries(dynamicFormSaveQueries);
		} else obj.setDynamicFormSaveQueries(null);
		
		return obj;
	}

	
}
