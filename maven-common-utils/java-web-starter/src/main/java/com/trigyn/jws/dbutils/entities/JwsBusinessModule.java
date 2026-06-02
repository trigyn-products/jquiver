package com.trigyn.jws.dbutils.entities;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.trigyn.jws.dbutils.configurations.UUIDEntityListener;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryConnection;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryDetails;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

	@Entity
	@EntityListeners(value = { UUIDEntityListener.class })
	@Table(name = "jq_business_module")
	@Cacheable
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "jwsBusinessModuleEntityRegion")
	public class JwsBusinessModule {
		@Id
		@Column(name = "business_module_id")
		private String	businessModuleId	= null;

		@Column(name = "module_name")
		private String	moduleName		= null;
		
		@Column(name = "created_by")
		private String	createdBy	= null;
		
		@Column(name = "created_date")
		@Temporal(TemporalType.TIMESTAMP)
		private Date	createdDate	= null;
		
		@Column(name = "updated_by")
		private String	updatedBy	= null;
		
		@Column(name = "updated_date")
		@Temporal(TemporalType.TIMESTAMP)
		private Date	updatedDate	= null;
		
		@OneToMany(fetch = FetchType.EAGER, mappedBy = "businessmodule")
		private List<JwsBusinessModuleEntity>	businessModuleEntity	= null;
		
		public JwsBusinessModule() {

		}

		public JwsBusinessModule(String businessModuleId, String moduleName, String createdBy, Date createdDate,
				String updatedBy, Date updatedDate) {
			this.businessModuleId	= businessModuleId;
			this.moduleName			= moduleName;
			this.createdBy			= createdBy;
			this.createdDate		= createdDate;
			this.updatedBy			= updatedBy;
			this.updatedDate		= updatedDate;
		}

		public String getBusinessModuleId() {
			return businessModuleId;
		}

		public void setBusinessModuleId(String businessModuleId) {
			this.businessModuleId = businessModuleId;
		}

		public String getModuleName() {
			return moduleName;
		}

		public void setModuleName(String moduleName) {
			this.moduleName = moduleName;
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

		public List<JwsBusinessModuleEntity> getBusinessModuleEntity() {
			return businessModuleEntity;
		}

		public void setBusinessModuleEntity(List<JwsBusinessModuleEntity> businessModuleEntity) {
			this.businessModuleEntity = businessModuleEntity;
		}
		
		public JwsBusinessModule getObject() {
			JwsBusinessModule businessModuleDetails = new JwsBusinessModule();
			
			businessModuleDetails.setBusinessModuleId(businessModuleId != null ? businessModuleId.trim() : businessModuleId);
			businessModuleDetails.setModuleName(moduleName != null ? moduleName.trim() : moduleName);
			businessModuleDetails.setCreatedBy(createdBy != null ? createdBy.trim() : createdBy);
			businessModuleDetails.setCreatedDate(createdDate);
			businessModuleDetails.setUpdatedBy(updatedBy != null ? updatedBy.trim() : updatedBy);
			businessModuleDetails.setUpdatedDate(updatedDate);

			List<JwsBusinessModuleEntity> businessModuleEntity = new ArrayList<>();
			if (this.businessModuleEntity != null && !this.businessModuleEntity.isEmpty()) {
				for (JwsBusinessModuleEntity bme : this.businessModuleEntity) {
					businessModuleEntity.add(bme.getObject());
				}
				businessModuleDetails.setBusinessModuleEntity(businessModuleEntity);
			} else {
				businessModuleDetails.setBusinessModuleEntity(null);
			}

			return businessModuleDetails;
		}


}
