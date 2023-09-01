package com.trigyn.jws.dbutils.vo.xml;

import java.util.ArrayList;
import java.util.List;

import com.trigyn.jws.dashboard.entities.DashletProperties;

public class DashletExportVO {

	private String	dashletId			= null;

	private String	dashletName			= null;

	private String	dashletTitle		= null;

	private Integer	xCoordinate			= null;

	private Integer	yCoordinate			= null;

	private Integer	width				= null;

	private Integer	height				= null;

	private Integer	showHeader			= 1;

	private Integer	isActive			= null;

	private Integer	dashletTypeId		= 1;

	private String	selectQueryFileName	= null;

	private String	htmlBodyFileName	= null;
	
	private List<DashletProperties>	properties	= new ArrayList<>();
	
	private String	resultVariableName		= null;
	
	private Integer	daoQueryType			= null;
	
	public DashletExportVO() {

	}

	public DashletExportVO(String dashletId, String dashletName, String dashletTitle, Integer xCoordinate,
			Integer yCoordinate, Integer width, Integer height,Integer showHeader, Integer isActive,
			Integer dashletTypeId, String selectQueryFileName, String htmlBodyFileName,List<DashletProperties>	properties,Integer	daoQueryType,String	resultVariableName) {
		this.dashletId				= dashletId;
		this.dashletName			= dashletName;
		this.dashletTitle			= dashletTitle;
		this.xCoordinate			= xCoordinate; 
		this.yCoordinate			= yCoordinate; 
		this.width					= width;
		this.height					= height;
		this.showHeader				= showHeader;
		this.isActive				= isActive;
		this.dashletTypeId			= dashletTypeId;
		this.selectQueryFileName	= selectQueryFileName;
		this.htmlBodyFileName		= htmlBodyFileName;
		this.properties				= properties;
		this.daoQueryType           = daoQueryType;
		this.resultVariableName     = resultVariableName;
	}

	public String getDashletId() {
		return dashletId;
	}

	public void setDashletId(String dashletId) {
		this.dashletId = dashletId;
	}

	public String getDashletName() {
		return dashletName;
	}

	public void setDashletName(String dashletName) {
		this.dashletName = dashletName;
	}

	public String getDashletTitle() {
		return dashletTitle;
	}

	public void setDashletTitle(String dashletTitle) {
		this.dashletTitle = dashletTitle;
	}

	public Integer getxCoordinate() {
		return xCoordinate;
	}

	public void setxCoordinate(Integer xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public Integer getyCoordinate() {
		return yCoordinate;
	}

	public void setyCoordinate(Integer yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getShowHeader() {
		return showHeader;
	}

	public void setShowHeader(Integer showHeader) {
		this.showHeader = showHeader;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Integer getDashletTypeId() {
		return dashletTypeId;
	}

	public void setDashletTypeId(Integer dashletTypeId) {
		this.dashletTypeId = dashletTypeId;
	}

	public String getSelectQueryFileName() {
		return selectQueryFileName;
	}

	public void setSelectQueryFileName(String selectQueryFileName) {
		this.selectQueryFileName = selectQueryFileName;
	}

	public String getHtmlBodyFileName() {
		return htmlBodyFileName;
	}

	public void setHtmlBodyFileName(String htmlBodyFileName) {
		this.htmlBodyFileName = htmlBodyFileName;
	}

	public List<DashletProperties> getProperties() {
		return properties;
	}

	public void setProperties(List<DashletProperties> properties) {
		this.properties = properties;
	}

	public String getResultVariableName() {
		return resultVariableName;
	}

	public void setResultVariableName(String resultVariableName) {
		this.resultVariableName = resultVariableName;
	}

	public Integer getDaoQueryType() {
		return daoQueryType;
	}

	public void setDaoQueryType(Integer daoQueryType) {
		this.daoQueryType = daoQueryType;
	}
}
