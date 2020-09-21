package app.trigyn.common.dynamicform.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "dynamic_form")
public class DynamicForm {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
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

	@Column(name = "created_by")
	private String createdBy = null;

	@Column(name = "created_date")
	private Date createdDate = null;
	
	@Column(name = "form_select_checksum")
	private String formSelectChecksum = null;

	@Column(name = "form_body_checksum")
	private String formBodyChecksum = null;

	@OneToMany(mappedBy = "dynamicForm")
	private List<DynamicFormSaveQuery> dynamicFormSaveQueries = null;

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
	
	
}
