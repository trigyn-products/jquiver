package com.trigyn.jws.usermanagement.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "jq_master_modules")
public class JwsMasterModules {

	@Id
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
	@Column(name = "module_id")
	private String	moduleId				= null;

	@Column(name = "module_name")
	private String	moduleName				= null;

	@Column(name = "is_system_module")
	private Integer	isSystemModule			= null;

	@Column(name = "auxiliary_data")
	private String	auxiliaryData			= null;

	@Column(name = "module_type_id")
	private Integer	moduleTypeId			= null;

	@Column(name = "sequence")
	private Integer	sequence				= null;

	@Column(name = "grid_details_id")
	private String	gridDetailsId			= null;

	@Column(name = "module_type")
	private String	moduleType				= null;

	@Column(name = "is_perm_supported")
	private Integer	isPermSupported			= null;

	@Column(name = "is_entity_perm_supported")
	private Integer	isEntityPermSupported	= null;

	@Column(name = "is_imp_exp_supported")
	private Integer	isImpExpSupported		= null;

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

	public Integer getModuleTypeId() {
		return moduleTypeId;
	}

	public void setModuleTypeId(Integer moduleTypeId) {
		this.moduleTypeId = moduleTypeId;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getGridDetailsId() {
		return gridDetailsId;
	}

	public void setGridDetailsId(String gridDetailsId) {
		this.gridDetailsId = gridDetailsId;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public Integer getIsPermSupported() {
		return isPermSupported;
	}

	public void setIsPermSupported(Integer isPermSupported) {
		this.isPermSupported = isPermSupported;
	}

	public Integer getIsImpExpSupported() {
		return isImpExpSupported;
	}

	public void setIsImpExpSupported(Integer isImpExpSupported) {
		this.isImpExpSupported = isImpExpSupported;
	}

	public JwsMasterModules getObject() {
		JwsMasterModules obj = new JwsMasterModules();
		obj.setAuxiliaryData(auxiliaryData != null ? auxiliaryData.trim() : auxiliaryData);
		obj.setIsSystemModule(isSystemModule);
		obj.setModuleId(moduleId != null ? moduleId.trim() : moduleId);
		obj.setModuleName(moduleName != null ? moduleName.trim() : moduleName);
		obj.setModuleTypeId(moduleTypeId);
		obj.setSequence(sequence);
		obj.setGridDetailsId(gridDetailsId != null ? gridDetailsId.trim() : gridDetailsId);
		obj.setModuleType(moduleType != null ? moduleType.trim() : moduleType);
		obj.setIsPermSupported(isPermSupported);
		obj.setIsImpExpSupported(isImpExpSupported);
		return obj;
	}

}
