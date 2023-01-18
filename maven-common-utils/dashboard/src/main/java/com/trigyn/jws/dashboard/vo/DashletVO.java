package com.trigyn.jws.dashboard.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DashletVO implements Serializable {

	private static final long		serialVersionUID		= 6186812372333154327L;

	private String					dashletId				= null;

	private String					dashletTitle			= null;

	private String					dashletName				= null;

	private String					dashletBody				= null;

	private String					dashletQuery			= null;

	private Integer					xCoordinate				= null;

	private Integer					yCoordinate				= null;

	private Integer					width					= null;

	private Integer					height					= null;

	private String					contextId				= null;

	private String					dataSourceId			= null;

	private Integer					showHeader				= null;

	private Integer					isActive				= null;

	private List<String>			roleIdList				= null;

	private String					resultVariableName		= null;

	private Integer					daoQueryType			= null;

	private List<DashletPropertyVO>	dashletPropertVOList	= new ArrayList<>();

	public DashletVO() {

	}

	public DashletVO(String dashletId, String dashletTitle, String dashletName, String dashletBody, String dashletQuery,
			Integer xCoordinate, Integer yCoordinate, Integer width, Integer height, String contextId,
			String dataSourceId, String resultVariableName, Integer daoQueryType, Integer showHeader,
			Integer isActive) {
		this.dashletId			= dashletId;
		this.dashletTitle		= dashletTitle;
		this.dashletName		= dashletName;
		this.dashletBody		= dashletBody;
		this.dashletQuery		= dashletQuery;
		this.xCoordinate		= xCoordinate;
		this.yCoordinate		= yCoordinate;
		this.width				= width;
		this.height				= height;
		this.contextId			= contextId;
		this.dataSourceId		= dataSourceId;
		this.resultVariableName	= resultVariableName;
		this.daoQueryType		= daoQueryType;
		this.showHeader			= showHeader;
		this.isActive			= isActive;
	}

	public String getDashletId() {
		return dashletId;
	}

	public void setDashletId(String dashletId) {
		this.dashletId = dashletId;
	}

	public String getDashletTitle() {
		return dashletTitle;
	}

	public void setDashletTitle(String dashletTitle) {
		this.dashletTitle = dashletTitle;
	}

	public String getDashletName() {
		return dashletName;
	}

	public void setDashletName(String dashletName) {
		this.dashletName = dashletName;
	}

	public String getDashletBody() {
		return dashletBody;
	}

	public void setDashletBody(String dashletBody) {
		this.dashletBody = dashletBody;
	}

	public String getDashletQuery() {
		return dashletQuery;
	}

	public void setDashletQuery(String dashletQuery) {
		this.dashletQuery = dashletQuery;
	}

	public Integer getxCoordinate() {
		return xCoordinate;
	}

	public void setXCoordinate(Integer xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public Integer getyCoordinate() {
		return yCoordinate;
	}

	public void setYCoordinate(Integer yCoordinate) {
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

	public String getContextId() {
		return contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public String getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
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

	public List<String> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}

	public List<DashletPropertyVO> getDashletPropertVOList() {
		return dashletPropertVOList;
	}

	public void setDashletPropertVOList(List<DashletPropertyVO> dashletPropertVOList) {
		this.dashletPropertVOList = dashletPropertVOList;
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

	@Override
	public int hashCode() {
		return Objects.hash(contextId, dashletBody, dashletId, dashletName, dashletPropertVOList, dashletQuery,
				dashletTitle, dataSourceId, height, isActive, roleIdList, showHeader, width, xCoordinate, yCoordinate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DashletVO other = (DashletVO) obj;
		return Objects.equals(contextId, other.contextId) && Objects.equals(dashletBody, other.dashletBody)
				&& Objects.equals(dashletId, other.dashletId) && Objects.equals(dashletName, other.dashletName)
				&& Objects.equals(dashletPropertVOList, other.dashletPropertVOList)
				&& Objects.equals(dashletQuery, other.dashletQuery) && Objects.equals(dashletTitle, other.dashletTitle)
				&& Objects.equals(dataSourceId, other.dataSourceId) && Objects.equals(height, other.height)
				&& Objects.equals(isActive, other.isActive) && Objects.equals(roleIdList, other.roleIdList)
				&& Objects.equals(showHeader, other.showHeader) && Objects.equals(width, other.width)
				&& Objects.equals(xCoordinate, other.xCoordinate) && Objects.equals(yCoordinate, other.yCoordinate);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DashletVO [dashletId=").append(dashletId).append(", dashletTitle=").append(dashletTitle)
				.append(", dashletName=").append(dashletName).append(", dashletBody=").append(dashletBody)
				.append(", dashletQuery=").append(dashletQuery).append(", xCoordinate=").append(xCoordinate)
				.append(", yCoordinate=").append(yCoordinate).append(", width=").append(width).append(", height=")
				.append(height).append(", contextId=").append(contextId).append(", dataSourceId=").append(dataSourceId)
				.append(", showHeader=").append(showHeader).append(", isActive=").append(isActive)
				.append(", roleIdList=").append(roleIdList).append(", dashletPropertVOList=")
				.append(dashletPropertVOList).append("]");
		return builder.toString();
	}

}
