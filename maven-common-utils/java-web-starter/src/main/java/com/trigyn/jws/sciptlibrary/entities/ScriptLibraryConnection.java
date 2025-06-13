package com.trigyn.jws.sciptlibrary.entities;

import java.util.Date;
import java.util.Objects;

import com.trigyn.jws.dbutils.configurations.UUIDEntityListener;
import com.trigyn.jws.dynamicform.entities.DynamicForm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlTransient;

@Entity
@EntityListeners(value = { UUIDEntityListener.class })
@Table(name = "jq_script_lib_connect")
public class ScriptLibraryConnection {

	@Id
	@Column(name = "script_lib_conn_id")
	private String						scriptlibconnId		= null;

	@Column(name = "script_lib_id")
	private String						scriptLibId			= null;

	@Column(name = "module_type_id")
	private String						moduletypeId		= null;

	@Column(name = "entity_id")
	private String						entityId			= null;

	@Column(name = "created_by")
	private String						createdBy			= null;

	@Column(name = "updated_by")
	private String						updatedBy			= null;

	@Column(name = "updated_date")
	private Date						updatedDate			= null;

	@Column(name = "is_custom_updated")
	private Integer						isCustomUpdated		= 1;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "script_lib_id", referencedColumnName = "script_lib_id", updatable = false, insertable = false)
	private ScriptLibraryDetails		scriptLibraryDetails			= null;

	public ScriptLibraryConnection() {

	}

	public ScriptLibraryConnection(String scriptlibconnId, String scriptLibId, String moduletypeId, String entityId,
			String createdBy, String updatedBy, Date updatedDate, Integer isCustomUpdated) {
		this.scriptlibconnId = scriptlibconnId;
		this.scriptLibId = scriptLibId;
		this.moduletypeId = moduletypeId;
		this.entityId = entityId;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
		this.isCustomUpdated = isCustomUpdated;
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdBy, entityId, isCustomUpdated, moduletypeId, scriptLibId, scriptlibconnId, updatedBy,
				updatedDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ScriptLibraryConnection other = (ScriptLibraryConnection) obj;
		return Objects.equals(createdBy, other.createdBy) && Objects.equals(entityId, other.entityId)
				&& Objects.equals(isCustomUpdated, other.isCustomUpdated)
				&& Objects.equals(moduletypeId, other.moduletypeId) && Objects.equals(scriptLibId, other.scriptLibId)
				&& Objects.equals(scriptlibconnId, other.scriptlibconnId) && Objects.equals(updatedBy, other.updatedBy)
				&& Objects.equals(updatedDate, other.updatedDate);
	}

	@Override
	public String toString() {
		return "ScriptLibrary [scriptlibconnId=" + scriptlibconnId + ", scriptLibId=" + scriptLibId + ", moduletypeId="
				+ moduletypeId + ", entityId=" + entityId + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy
				+ ", updatedDate=" + updatedDate + ", isCustomUpdated=" + isCustomUpdated + "]";
	}

	public String getScriptlibconnId() {
		return scriptlibconnId;
	}

	public void setScriptlibconnId(String scriptlibconnId) {
		this.scriptlibconnId = scriptlibconnId;
	}

	public String getScriptLibId() {
		return scriptLibId;
	}

	public void setScriptLibId(String scriptLibId) {
		this.scriptLibId = scriptLibId;
	}

	public String getModuletypeId() {
		return moduletypeId;
	}

	public void setModuletypeId(String moduletypeId) {
		this.moduletypeId = moduletypeId;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getIsCustomUpdated() {
		return isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
	}
	
	@XmlTransient
	public ScriptLibraryDetails getScriptLibraryDetails() {
		return scriptLibraryDetails;
	}

	public void setScriptLibraryDetails(ScriptLibraryDetails scriptLibraryDetails) {
		this.scriptLibraryDetails = scriptLibraryDetails;
	}

	public ScriptLibraryConnection getObject() {
		ScriptLibraryConnection scriptLibraryConn = new ScriptLibraryConnection();
		scriptLibraryConn.setCreatedBy(createdBy != null ? createdBy.trim() : createdBy);
		scriptLibraryConn.setEntityId(entityId != null ? entityId.trim() : entityId);
		scriptLibraryConn.setModuletypeId(moduletypeId != null ? moduletypeId.trim() : moduletypeId);
		scriptLibraryConn.setScriptlibconnId(scriptlibconnId != null ? scriptlibconnId.trim() : scriptlibconnId);
		scriptLibraryConn.setScriptLibId(scriptLibId != null ? scriptLibId.trim() : scriptLibId);
		scriptLibraryConn.setUpdatedBy(updatedBy != null ? updatedBy.trim() : updatedBy);
		scriptLibraryConn.setUpdatedDate(updatedDate);
		scriptLibraryConn.setIsCustomUpdated(isCustomUpdated);

		return scriptLibraryConn;
	}
	
}
