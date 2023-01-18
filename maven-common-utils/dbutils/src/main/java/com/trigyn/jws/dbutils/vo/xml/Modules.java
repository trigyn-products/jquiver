package com.trigyn.jws.dbutils.vo.xml;

public class Modules {

	private String				moduleID;

	private String				moduleName;

	private String				moduleType;

	private TemplateExportVO	template;

	private DashletExportVO		dashlet;

	private DynamicFormExportVO	dynamicForm;

	private HelpManualTypeExportVO	helpManual;
	
	private FileUploadConfigExportVO fileBin;
	
	private DynaRestExportVO dynaRestExportVO;

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

	public HelpManualTypeExportVO getHelpManual() {
		return helpManual;
	}

	public void setHelpManual(HelpManualTypeExportVO helpManual) {
		this.helpManual = helpManual;
	}

	public FileUploadConfigExportVO getFileBin() {
		return fileBin;
	}

	public void setFileBin(FileUploadConfigExportVO fileBin) {
		this.fileBin = fileBin;
	}

	public DynaRestExportVO getDynaRestExportVO() {
		return dynaRestExportVO;
	}

	public void setDynaRestExportVO(DynaRestExportVO dynaRestExportVO) {
		this.dynaRestExportVO = dynaRestExportVO;
	}

}
