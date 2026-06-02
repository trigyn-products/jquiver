package com.trigyn.jws.dynamicform.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trigyn.jws.dbutils.configurations.UUIDEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@EntityListeners(value = { UUIDEntityListener.class })
@Table(name = "jq_dynamic_form")
public class DynamicForm {

	@Id
	@Column(name = "form_id")
	private String						formId					= null;

	@Column(name = "form_name")
	private String						formName				= null;

	@Column(name = "form_description")
	private String						formDescription			= null;

	@Column(name = "form_select_query")
	private String						formSelectQuery			= null;

	@Column(name = "form_body")
	private String						formBody				= null;

	@Column(name = "form_type_id")
	private Integer						formTypeId				= 1;

	@Column(name = "created_by")
	private String						createdBy				= null;

	@JsonIgnore
	@Column(name = "created_date")
	private Date						createdDate				= null;

	@Column(name = "datasource_id")
	private String						datasourceId			= null;

	@Column(name = "form_select_checksum")
	private String						formSelectChecksum		= null;

	@Column(name = "form_body_checksum")
	private String						formBodyChecksum		= null;

	@Column(name = "last_updated_by")
	private String						lastUpdatedBy			= "admin@jquiver.io";

	@Column(name = "last_updated_ts")
	private Date						lastUpdatedTs			= null;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "dynamicForm")
	private List<DynamicFormSaveQuery>	dynamicFormSaveQueries	= null;

	@Column(name = "query_type")
	private Integer						selectQueryType			= null;

	@Column(name = "is_custom_updated")
	private Integer						isCustomUpdated			= 1;

	@Column(name = "is_captcha_enabled")
	private Integer						isCaptchaEnabled		= 0;

	@Column(name = "is_csrf_enabled")
	private Integer						isCsrfEnabled			= 0;

	@Column(name = "form_io_id")
	private String						formIoId				= null;

	public DynamicForm() {

	}

	public DynamicForm(String formId, String formName, String formDescription, String formSelectQuery, String formBody,
			Integer formTypeId, String createdBy, Date createdDate, String datasourceId, String formSelectChecksum,
			String formBodyChecksum, String lastUpdatedBy, Date lastUpdatedTs,
			List<DynamicFormSaveQuery> dynamicFormSaveQueries, Integer selectQueryType, String formIoId) {
		this.formId					= formId;
		this.formName				= formName;
		this.formDescription		= formDescription;
		this.formSelectQuery		= formSelectQuery;
		this.formBody				= formBody;
		this.formTypeId				= formTypeId;
		this.createdBy				= createdBy;
		this.createdDate			= createdDate;
		this.datasourceId			= datasourceId;
		this.formSelectChecksum		= formSelectChecksum;
		this.formBodyChecksum		= formBodyChecksum;
		this.lastUpdatedBy			= lastUpdatedBy;
		this.lastUpdatedTs			= lastUpdatedTs;
		this.dynamicFormSaveQueries	= dynamicFormSaveQueries;
		this.selectQueryType		= selectQueryType;
		this.formIoId				= formIoId;
	}

	public DynamicForm(String formId, String formName, String formDescription, String formSelectQuery, String formBody,
			Integer formTypeId, String createdBy, Date createdDate, String datasourceId, String formSelectChecksum,
			String formBodyChecksum, String lastUpdatedBy, Date lastUpdatedTs,
			List<DynamicFormSaveQuery> dynamicFormSaveQueries, Integer selectQueryType, Integer isCustomUpdated,
			String scriptLibraryId, Integer isCaptchaEnabled, Integer isCsrfEnabled, String formIoId) {
		this.formId					= formId;
		this.formName				= formName;
		this.formDescription		= formDescription;
		this.formSelectQuery		= formSelectQuery;
		this.formBody				= formBody;
		this.formTypeId				= formTypeId;
		this.createdBy				= createdBy;
		this.createdDate			= createdDate;
		this.datasourceId			= datasourceId;
		this.formSelectChecksum		= formSelectChecksum;
		this.formBodyChecksum		= formBodyChecksum;
		this.lastUpdatedBy			= lastUpdatedBy;
		this.lastUpdatedTs			= lastUpdatedTs;
		this.dynamicFormSaveQueries	= dynamicFormSaveQueries;
		this.selectQueryType		= selectQueryType;
		this.isCustomUpdated		= isCustomUpdated;
		this.isCaptchaEnabled		= isCaptchaEnabled;
		this.isCsrfEnabled			= isCsrfEnabled;
		this.formIoId				= formIoId;
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

	public String getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(String datasourceId) {
		this.datasourceId = datasourceId;
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

	public String getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Date getLastUpdatedTs() {
		return lastUpdatedTs;
	}

	public void setLastUpdatedTs(Date lastUpdatedTs) {
		this.lastUpdatedTs = lastUpdatedTs;
	}

	public Integer getSelectQueryType() {
		return selectQueryType;
	}

	public void setSelectQueryType(Integer selectQueryType) {
		this.selectQueryType = selectQueryType;
	}

	public Integer getIsCustomUpdated() {
		return isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
	}

	public Integer getIsCaptchaEnabled() {
		return isCaptchaEnabled;
	}

	public void setIsCaptchaEnabled(Integer isCaptchaEnabled) {
		this.isCaptchaEnabled = isCaptchaEnabled;
	}

	public Integer getIsCsrfEnabled() {
		return isCsrfEnabled;
	}

	public void setIsCsrfEnabled(Integer isCsrfEnabled) {
		this.isCsrfEnabled = isCsrfEnabled;
	}

	public String getFormIoId() {
		return formIoId;
	}

	public void setFormIoId(String formIoId) {
		this.formIoId = formIoId;
	}

	public DynamicForm getObject(Boolean isImport) {
		DynamicForm obj = new DynamicForm();
		obj.setCreatedBy(createdBy != null ? createdBy.trim() : createdBy);
		obj.setCreatedDate(createdDate);
		obj.setDatasourceId(datasourceId);
		obj.setFormBody(formBody != null ? formBody.trim() : formBody);
		obj.setFormBodyChecksum(formBodyChecksum != null ? formBodyChecksum.trim() : formBodyChecksum);
		obj.setFormDescription(formDescription != null ? formDescription.trim() : formDescription);
		obj.setFormId(formId != null ? formId.trim() : formId);
		obj.setFormName(formName != null ? formName.trim() : formName);
		obj.setFormSelectChecksum(formSelectChecksum != null ? formSelectChecksum.trim() : formSelectChecksum);
		obj.setFormSelectQuery(formSelectQuery != null ? formSelectQuery.trim() : formSelectQuery);
		obj.setFormTypeId(formTypeId);
		obj.setLastUpdatedBy(lastUpdatedBy != null ? lastUpdatedBy.trim() : lastUpdatedBy);
		obj.setLastUpdatedTs(lastUpdatedTs);
		obj.setIsCustomUpdated(isCustomUpdated);
		obj.setSelectQueryType(selectQueryType);
	//	obj.setScriptLibraryId(scriptLibraryId != null ? scriptLibraryId.trim() : scriptLibraryId);
		obj.setFormIoId(formIoId != null ? formIoId.trim() : formIoId);

		List<DynamicFormSaveQuery> dfsOtr = new ArrayList<>();
		if (dynamicFormSaveQueries != null && !dynamicFormSaveQueries.isEmpty()) {
			for (DynamicFormSaveQuery dfs : dynamicFormSaveQueries) {
				dfsOtr.add(dfs.getObject(isImport));
			}
			obj.setDynamicFormSaveQueries(dynamicFormSaveQueries);
		} else
			obj.setDynamicFormSaveQueries(null);

		return obj;
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdBy, datasourceId, dynamicFormSaveQueries, formBody, formBodyChecksum,
				formDescription, formId, formName, formSelectChecksum, formSelectQuery, formTypeId, lastUpdatedBy,
				selectQueryType, formIoId);
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
		return Objects.equals(createdBy, other.createdBy) && Objects.equals(datasourceId, other.datasourceId)
				&& Objects.equals(dynamicFormSaveQueries, other.dynamicFormSaveQueries)
				&& Objects.equals(formBody, other.formBody) && Objects.equals(formBodyChecksum, other.formBodyChecksum)
				&& Objects.equals(formDescription, other.formDescription) && Objects.equals(formId, other.formId)
				&& Objects.equals(formName, other.formName)
				&& Objects.equals(formSelectChecksum, other.formSelectChecksum)
				&& Objects.equals(formSelectQuery, other.formSelectQuery)
				&& Objects.equals(formTypeId, other.formTypeId) && Objects.equals(lastUpdatedBy, other.lastUpdatedBy)
				&& Objects.equals(selectQueryType, other.selectQueryType) && Objects.equals(formIoId, other.formIoId);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DynamicForm [formId=").append(formId).append(", formName=").append(formName)
				.append(", formDescription=").append(formDescription).append(", formSelectQuery=")
				.append(formSelectQuery).append(", formBody=").append(formBody).append(", formTypeId=")
				.append(formTypeId).append(", createdBy=").append(createdBy).append(", createdDate=")
				.append(createdDate).append(", datasourceId=").append(datasourceId).append(", formSelectChecksum=")
				.append(formSelectChecksum).append(", formBodyChecksum=").append(formBodyChecksum)
				.append(", lastUpdatedBy=").append(lastUpdatedBy).append(", lastUpdatedDate=").append(lastUpdatedTs)
				.append(", formIoId=").append(formIoId).append(", dynamicFormSaveQueries=")
				.append(dynamicFormSaveQueries).append(selectQueryType).append("]");
		return builder.toString();
	}

}
