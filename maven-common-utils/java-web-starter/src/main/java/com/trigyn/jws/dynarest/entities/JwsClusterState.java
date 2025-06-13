package com.trigyn.jws.dynarest.entities;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trigyn.jws.dbutils.configurations.UUIDEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@EntityListeners(value = { UUIDEntityListener.class })
@Table(name = "jq_cluster_state")
public class JwsClusterState {

	@Id
	@Column(name = "instance_id")
	private String	instanceId	= null;

	@Column(name = "is_active")
	private Integer	isActive	= null;

	@Column(name = "is_schedular")
	private Integer	isSchedular	= null;

	@JsonIgnore
	@Column(name = "updated_on")
	private Date	updatedOn	= null;

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getIsSchedular() {
		return isSchedular;
	}

	public void setIsSchedular(Integer isSchedular) {
		this.isSchedular = isSchedular;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}
	
}
