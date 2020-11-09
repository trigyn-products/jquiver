package com.trigyn.jws.usermanagement.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "jws_master_modules")
public class JwsMasterModules {

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name="module_id")
	private String moduleId = null;
	
	@Column(name="module_name")
	private String moduleName = null;
	
	@Column(name="is_system_module")
	private Integer isSystemModule = null;
	
	@Column(name="auxiliary_data")
	private String auxiliaryData = null;
	
	@Column(name="module_type_id")
	private Integer moduleTypeId = null;

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public Integer getIsSystemModule() {
		return isSystemModule;
	}

	public void setIsSystemModule(Integer isSystemModule) {
		this.isSystemModule = isSystemModule;
	}

	public String getAuxiliaryData() {
		return auxiliaryData;
	}

	public void setAuxiliaryData(String auxiliaryData) {
		this.auxiliaryData = auxiliaryData;
	}

	public JwsMasterModules getObject() {
		JwsMasterModules obj = new JwsMasterModules();
		obj.setAuxiliaryData(auxiliaryData);
		obj.setIsSystemModule(isSystemModule);
		obj.setModuleId(moduleId);
		obj.setModuleName(moduleName);
		return obj;
	}

	public Integer getModuleTypeId() {
		return moduleTypeId;
	}

	public void setModuleTypeId(Integer moduleTypeId) {
		this.moduleTypeId = moduleTypeId;
	}
}
