package com.trigyn.jws.dbutils.vo.xml;

public class Modules {

	private String				moduleID;

	private String				moduleName;

	private String				moduleType;

	private TemplateExportVO	template;

	private DashletExportVO		dashlet;

	private DynamicFormExportVO	dynamicForm;

	public String getModuleID() {
		return moduleID;
	}

	public void setModuleID(String moduleID) {
		this.moduleID = moduleID;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public TemplateExportVO getTemplate() {
		return template;
	}

	public void setTemplate(TemplateExportVO template) {
		this.template = template;
	}

	public DashletExportVO getDashlet() {
		return dashlet;
	}

	public void setDashlet(DashletExportVO dashlet) {
		this.dashlet = dashlet;
	}

	public DynamicFormExportVO getDynamicForm() {
		return dynamicForm;
	}

	public void setDynamicForm(DynamicFormExportVO dynamicForm) {
		this.dynamicForm = dynamicForm;
	}

}
