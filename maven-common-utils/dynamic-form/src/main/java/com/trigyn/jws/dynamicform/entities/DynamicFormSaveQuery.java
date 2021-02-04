package com.trigyn.jws.dynamicform.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "dynamic_form_save_queries")
@NamedQuery(name = "DynamicFormSaveQuery.findAll", query = "SELECT d FROM DynamicFormSaveQuery d")
public class DynamicFormSaveQuery implements Serializable {
	private static final long	serialVersionUID	= 1L;

	@Id
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
	@Column(name = "dynamic_form_query_id")
	private String				dynamicFormQueryId	= null;

	@Column(name = "dynamic_form_save_query")
	private String				formSaveQuery		= null;

	@Column(name = "dynamic_form_id")
	private String				dynamicFormId		= null;

	@Column(name = "sequence")
	private Integer				sequence			= null;

	@Column(name = "checksum")
	private String				checksum			= null;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dynamic_form_id", referencedColumnName = "form_id", updatable = false, insertable = false)
	private DynamicForm			dynamicForm			= null;

	public DynamicFormSaveQuery() {
	}

	public String getDynamicFormQueryId() {
		return this.dynamicFormQueryId;
	}

	public void setDynamicFormQueryId(String dynamicFormQueryId) {
		this.dynamicFormQueryId = dynamicFormQueryId;
	}

	public String getDynamicFormSaveQuery() {
		return this.formSaveQuery;
	}

	public void setDynamicFormSaveQuery(String dynamicFormSaveQuery) {
		this.formSaveQuery = dynamicFormSaveQuery;
	}

	public String getDynamicFormId() {
		return dynamicFormId;
	}

	public void setDynamicFormId(String dynamicFormId) {
		this.dynamicFormId = dynamicFormId;
	}

	public Integer getSequence() {
		return this.sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public DynamicForm getDynamicForm() {
		return this.dynamicForm;
	}

	public void setDynamicForm(DynamicForm dynamicForm) {
		this.dynamicForm = dynamicForm;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public DynamicFormSaveQuery getObject() {
		DynamicFormSaveQuery obj = new DynamicFormSaveQuery();
		obj.setChecksum(checksum != null ? checksum.trim() : checksum);
		obj.setDynamicFormId(dynamicFormId != null ? dynamicFormId.trim() : dynamicFormId);
		obj.setDynamicFormQueryId(dynamicFormQueryId != null ? dynamicFormQueryId.trim() : dynamicFormQueryId);
		obj.setDynamicFormSaveQuery(formSaveQuery != null ? formSaveQuery.trim() : formSaveQuery);
		obj.setSequence(sequence);

		return obj;
	}

}
