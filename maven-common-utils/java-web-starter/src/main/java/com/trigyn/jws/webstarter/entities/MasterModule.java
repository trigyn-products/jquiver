package com.trigyn.jws.webstarter.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity(name="master_module")
public class MasterModule {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "master_module_id")
	private String masterModuleId = null;
	
	@Column(name = "master_module_name",unique = true)
	private String masterModuleName = null;
	
	@Column(name = "module_template_id")
	private String moduleTemplateId = null;
	
	@Column(name = "grid_details_id")
	private String gridDetailsId = null;
	
	@Column(name = "auxiliary_data")
	private String auxiliaryData = null;

	public String getMasterModuleId() {
		return masterModuleId;
	}

	public void setMasterModuleId(String masterModuleId) {
		this.masterModuleId = masterModuleId;
	}

	public String getMasterModuleName() {
		return masterModuleName;
	}

	public void setMasterModuleName(String masterModuleName) {
		this.masterModuleName = masterModuleName;
	}

	public String getModuleTemplateId() {
		return moduleTemplateId;
	}

	public void setModuleTemplateId(String moduleTemplateId) {
		this.moduleTemplateId = moduleTemplateId;
	}

	public String getGridDetailsId() {
		return gridDetailsId;
	}

	public void setGridDetailsId(String gridDetailsId) {
		this.gridDetailsId = gridDetailsId;
	}

	public String getAuxiliaryData() {
		return auxiliaryData;
	}

	public void setAuxiliaryData(String auxiliaryData) {
		this.auxiliaryData = auxiliaryData;
	}
	
	
	
	
}