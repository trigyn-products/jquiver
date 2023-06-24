package com.trigyn.jws.dynarest.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "jq_cluster_state")
public class JwsClusterState {

	@Id
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
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
