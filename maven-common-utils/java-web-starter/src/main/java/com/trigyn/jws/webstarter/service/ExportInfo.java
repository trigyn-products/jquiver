package com.trigyn.jws.webstarter.service;
public class ExportInfo {

    private String moduleId;
    private String moduleName;
    private String moduleVersion="1.0";

    public ExportInfo(String moduleId, String moduleName,String moduleVersion) {
        this.moduleId = moduleId;
        this.moduleName = moduleName;
        this.moduleVersion = moduleVersion;
    }

    public String getModuleId() {
        return moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

	public String getModuleVersion() {
		return moduleVersion;
	}
    
    
}