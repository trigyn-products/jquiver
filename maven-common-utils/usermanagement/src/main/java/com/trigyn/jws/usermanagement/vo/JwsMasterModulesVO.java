package com.trigyn.jws.usermanagement.vo;

import com.trigyn.jws.usermanagement.entities.JwsMasterModules;

public class JwsMasterModulesVO {

	private String	moduleId		= null;

	private String	moduleName		= null;

	private Integer	isSystemModule	= null;

	private String	auxiliaryData	= null;

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

	public JwsMasterModulesVO convertEntityToVO(JwsMasterModules jwsMasterModule) {
		this.moduleId		= jwsMasterModule.getModuleId();
		this.moduleName		= jwsMasterModule.getModuleName();
		this.isSystemModule	= jwsMasterModule.getIsSystemModule();
		this.auxiliaryData	= jwsMasterModule.getAuxiliaryData();
		return this;
	}

}
