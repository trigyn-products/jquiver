package com.trigyn.jws.dynamicform.entities;

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
	private String	manualId		= null;

	@Column(name = "name")
	private String	name			= null;

	@Column(name = "is_system_manual")
	private Integer	isSystemManual	= null;

	public ManualType() {

	}

	public ManualType(String manualId, String name, Integer isSystemManual) {
		this.manualId		= manualId;
		this.name			= name;
		this.isSystemManual	= isSystemManual;
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

	@Override
	public int hashCode() {
		return Objects.hash(isSystemManual, manualId, name);
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
				&& Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ManualType [manualId=").append(manualId).append(", name=").append(name)
				.append(", isSystemManual=").append(isSystemManual).append("]");
		return builder.toString();
	}

}
