package com.trigyn.jws.dbutils.entities;

import java.util.Date;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.trigyn.jws.dbutils.configurations.UUIDEntityListener;
import com.trigyn.jws.usermanagement.entities.JwsRole;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

	@Entity
	@EntityListeners(value = { UUIDEntityListener.class })
	@Table(name = "jq_business_module_entity_details")
	@Cacheable
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "jwsBusinessModuleEntityRegion")
	public class JwsBusinessModuleEntity {

		@Id
		@Column(name = "business_module_entity_details_id")
		private String	businessModuleEntityDetailsId	= null;

		@Column(name = "business_module_id")
		private String	businessModuleId		= null;
		
		@ManyToOne
		@JoinColumn(name = "business_module_id", referencedColumnName = "business_module_id", insertable = false, updatable = false)
		private JwsBusinessModule	businessmodule			= null;

		@Column(name = "module_id")
		private String	moduleId		= null;

		@Column(name = "entity_id")
		private String	entityId		= null;


		@Column(name = "created_date")
		@Temporal(TemporalType.TIMESTAMP)
		private Date	createdDate	= null;

		@Column(name = "created_by")
		private String	createdBy	= null;
		
		public String getBusinessModuleEntityDetailsId() {
			return businessModuleEntityDetailsId;
		}

		public void setBusinessModuleEntityDetailsId(String businessModuleEntityDetailsId) {
			this.businessModuleEntityDetailsId = businessModuleEntityDetailsId;
		}

		public String getBusinessModuleId() {
			return businessModuleId;
		}

		public void setBusinessModuleId(String businessModuleId) {
			this.businessModuleId = businessModuleId;
		}

		public JwsBusinessModule getBusinessmodule() {
			return businessmodule;
		}

		public void setBusinessmodule(JwsBusinessModule businessmodule) {
			this.businessmodule = businessmodule;
		}

		public String getModuleId() {
			return moduleId;
		}

		public void setModuleId(String moduleId) {
			this.moduleId = moduleId;
		}

		public String getEntityId() {
			return entityId;
		}

		public void setEntityId(String entityId) {
			this.entityId = entityId;
		}

		public Date getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
		}

		public String getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(String createdBy) {
			this.createdBy = createdBy;
		}

		public JwsBusinessModuleEntity getObject() {
			JwsBusinessModuleEntity module = new JwsBusinessModuleEntity();
			module.setBusinessModuleEntityDetailsId (businessModuleEntityDetailsId != null ? businessModuleEntityDetailsId.trim() : businessModuleEntityDetailsId);
			module.setBusinessModuleId(businessModuleId != null ? businessModuleId.trim() : businessModuleId);
			module.setModuleId(moduleId != null ? moduleId.trim() : moduleId);
			module.setEntityId(entityId != null ? entityId.trim() : entityId);
			module.setCreatedDate(createdDate);
			module.setCreatedBy(createdBy != null ? createdBy.trim() : createdBy);
			// role.setJwsRole(jwsRole.getObject());
			return module;
		}
	}

