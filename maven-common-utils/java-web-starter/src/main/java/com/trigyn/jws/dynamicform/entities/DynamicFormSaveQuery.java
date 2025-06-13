package com.trigyn.jws.dynamicform.entities;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trigyn.jws.dbutils.configurations.UUIDEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@EntityListeners(value = { UUIDEntityListener.class })
@Table(name = "jq_dynamic_form_save_queries")
@NamedQuery(name = "DynamicFormSaveQuery.findAll", query = "SELECT d FROM DynamicFormSaveQuery d")
public class DynamicFormSaveQuery implements Serializable {
	private final static Logger	logger				= LoggerFactory.getLogger(DynamicFormSaveQuery.class);
	private static final long	serialVersionUID	= 1L;

	@Id
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

	@Column(name = "result_variable_name ")
	private String				resultVariableName	= null;

	@Column(name = "dao_query_type ")
	private Integer				daoQueryType		= null;

	@Column(name = "datasource_id  ")
	private String				datasourceId		= null;
	
	@Transient
	private String				scriptLibraryId		= null;
	
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

	public DynamicFormSaveQuery getObject(Boolean isImport) {
		DynamicFormSaveQuery	obj	= new DynamicFormSaveQuery();
		obj.setChecksum(checksum != null ? checksum.trim() : checksum);
		obj.setDynamicFormId(dynamicFormId != null ? dynamicFormId.trim() : dynamicFormId);
		obj.setDynamicFormQueryId(dynamicFormQueryId != null ? dynamicFormQueryId.trim() : dynamicFormQueryId);
		obj.setDynamicFormSaveQuery(formSaveQuery != null ? formSaveQuery.trim() : formSaveQuery);
		obj.setSequence(sequence);
		obj.setDaoQueryType(daoQueryType);
		obj.setDatasourceId(datasourceId != null ? datasourceId.trim() : datasourceId);
		obj.setResultVariableName(resultVariableName != null ? resultVariableName.trim() : resultVariableName);
		obj.setScriptLibraryId(scriptLibraryId != null ? scriptLibraryId.trim() : scriptLibraryId);

		return obj;
	}

	public String getFormSaveQuery() {
		return formSaveQuery;
	}

	public void setFormSaveQuery(String formSaveQuery) {
		this.formSaveQuery = formSaveQuery;
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

	public String getScriptLibraryId() {
		return scriptLibraryId;
	}

	public void setScriptLibraryId(String scriptLibraryId) {
		this.scriptLibraryId = scriptLibraryId;
	}

}
