package com.trigyn.jws.dynamicform.entities;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "jq_manual_type")
public class ManualType {

	@Id
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
	@Column(name = "manual_id")
	private String manualId = null;

	@Column(name = "name")
	private String name = null;

	@Column(name = "is_system_manual")
	private Integer isSystemManual = null;

	@Column(name = "created_by")
	private String createdBy = null;

	@Column(name = "created_date")
	private Date createdDate = null;

	@Column(name = "last_updated_by")
	private String lastUpdatedBy = null;

	@Column(name = "last_updated_ts")
	private Date lastUpdatedTs = null;

	public ManualType() {

	}

//	public ManualType(String manualId, String name, Integer isSystemManual) {
//		this.manualId = manualId;
//		this.name = name;
//		this.isSystemManual = isSystemManual;
//	}

	public ManualType(String manualId, String name, Integer isSystemManual,String createdBy,Date createdDate,String	lastUpdatedBy,Date	lastUpdatedTs) {
		this.manualId		= manualId;
		this.name			= name;
		this.isSystemManual	= isSystemManual;
		this.createdBy=createdBy;
		this.createdDate=createdDate;
		this.lastUpdatedBy=lastUpdatedBy;
		this.lastUpdatedTs=lastUpdatedTs;
	}

	public String getManualId() {
		return manualId;
	}

	public void setManualId(String manualId) {
		this.manualId = manualId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getIsSystemManual() {
		return isSystemManual;
	}

	public void setIsSystemManual(Integer isSystemManual) {
		this.isSystemManual = isSystemManual;
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

	@Override
	public int hashCode() {
		return Objects.hash(isSystemManual, manualId, name, createdBy, createdDate, lastUpdatedBy, lastUpdatedTs);
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
		ManualType other = (ManualType) obj;
		return Objects.equals(isSystemManual, other.isSystemManual) && Objects.equals(manualId, other.manualId)
				&& Objects.equals(name, other.name) && Objects.equals(createdBy, other.createdBy)
				&& Objects.equals(createdDate, other.createdDate) && Objects.equals(lastUpdatedBy, other.lastUpdatedBy)
				&& Objects.equals(lastUpdatedTs, other.lastUpdatedTs);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ManualType [manualId=").append(manualId).append(", name=").append(name)
				.append(", isSystemManual=").append(isSystemManual).append(", createdBy=").append(createdBy)
				.append(", createdDate=").append(createdDate).append(", lastUpdatedBy=").append(lastUpdatedBy)
				.append(", lastUpdatedTs=").append(lastUpdatedTs).append("]");
		return builder.toString();
	}

}
