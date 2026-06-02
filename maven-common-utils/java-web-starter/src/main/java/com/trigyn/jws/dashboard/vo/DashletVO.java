package com.trigyn.jws.dashboard.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;

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

	private String					dataSourceId			= null;

	private Integer					showHeader				= null;

	private Integer					isActive				= null;

	private List<String>			roleIdList				= null;

	private String					resultVariableName		= null;

	private Integer					daoQueryType			= null;

	private List<DashletPropertyVO>	dashletPropertVOList	= new ArrayList<>();

	private Integer					dashletTypeId			= 1;

	private String					createdBy				= null;

	private Date					createdDate				= null;

	private String					updatedBy				= null;

	private Date					lastUpdatedTs			= null;

	private String					dashletQueryChecksum	= null;

	private String					dashletBodyChecksum		= null;

	private Integer					isCustomUpdated			= 1;

	public DashletVO() {

	}

	
	public DashletVO(String dashletId, String dashletTitle, String dashletName, String dashletBody, String dashletQuery,
			Integer xCoordinate, Integer yCoordinate, Integer width, Integer height, String dataSourceId,
			Integer showHeader, Integer isActive, List<String> roleIdList, String resultVariableName,
			Integer daoQueryType, List<DashletPropertyVO> dashletPropertVOList, Integer dashletTypeId, String createdBy,
			Date createdDate, String updatedBy, Date lastUpdatedTs, String dashletQueryChecksum,
			String dashletBodyChecksum, String datasourceId2, Integer isCustomUpdated) {
		super();
		this.dashletId				= dashletId;
		this.dashletTitle			= dashletTitle;
		this.dashletName			= dashletName;
		this.dashletBody			= dashletBody;
		this.dashletQuery			= dashletQuery;
		this.xCoordinate			= xCoordinate;
		this.yCoordinate			= yCoordinate;
		this.width					= width;
		this.height					= height;
		this.dataSourceId			= dataSourceId;
		this.showHeader				= showHeader;
		this.isActive				= isActive;
		this.roleIdList				= roleIdList;
		this.resultVariableName		= resultVariableName;
		this.daoQueryType			= daoQueryType;
		this.dashletPropertVOList	= dashletPropertVOList;
		this.dashletTypeId			= dashletTypeId;
		this.createdBy				= createdBy;
		this.createdDate			= createdDate;
		this.updatedBy				= updatedBy;
		this.lastUpdatedTs			= lastUpdatedTs;
		this.dashletQueryChecksum	= dashletQueryChecksum;
		this.dashletBodyChecksum	= dashletBodyChecksum;
		this.isCustomUpdated		= isCustomUpdated;
	}


	public DashletVO(String dashletId, String dashletTitle, String dashletName, String dashletBody, String dashletQuery,
			Integer xCoordinate, Integer yCoordinate, Integer width, Integer height, String dataSourceId,
			String resultVariableName, Integer daoQueryType, Integer showHeader, Integer isActive,
			Integer dashletTypeId) {
		this.dashletId			= dashletId;
		this.dashletTitle		= dashletTitle;
		this.dashletName		= dashletName;
		this.dashletBody		= dashletBody;
		this.dashletQuery		= dashletQuery;
		this.xCoordinate		= xCoordinate;
		this.yCoordinate		= yCoordinate;
		this.width				= width;
		this.height				= height;
		this.dataSourceId		= dataSourceId;
		this.resultVariableName	= resultVariableName;
		this.daoQueryType		= daoQueryType;
		this.showHeader			= showHeader;
		this.isActive			= isActive;
		this.dashletTypeId		= dashletTypeId;
	}

	public DashletVO(String dashletId, String dashletName) {
		this.dashletId		= dashletId;
		this.dashletName	= dashletName;
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

	public Integer getDashletTypeId() {
		return dashletTypeId;
	}

	public void setDashletTypeId(Integer dashletTypeId) {
		this.dashletTypeId = dashletTypeId;
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

	public Date getLastUpdatedTs() {
		return lastUpdatedTs;
	}

	public void setLastUpdatedTs(Date lastUpdatedTs) {
		this.lastUpdatedTs = lastUpdatedTs;
	}

	public String getDashletQueryChecksum() {
		return dashletQueryChecksum;
	}

	public void setDashletQueryChecksum(String dashletQueryChecksum) {
		this.dashletQueryChecksum = dashletQueryChecksum;
	}

	public String getDashletBodyChecksum() {
		return dashletBodyChecksum;
	}

	public void setDashletBodyChecksum(String dashletBodyChecksum) {
		this.dashletBodyChecksum = dashletBodyChecksum;
	}

	

	public Integer getIsCustomUpdated() {
		return isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
	}

	public void setxCoordinate(Integer xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public void setyCoordinate(Integer yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdBy, createdDate, daoQueryType, dashletBody, dashletBodyChecksum, dashletId,
				dashletName, dashletPropertVOList, dashletQuery, dashletQueryChecksum, dashletTitle, dashletTypeId,
				dataSourceId,  height, isActive, isCustomUpdated, lastUpdatedTs, resultVariableName,
				roleIdList, showHeader, updatedBy, width, xCoordinate, yCoordinate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DashletVO other = (DashletVO) obj;
		return Objects.equals(createdBy, other.createdBy) && Objects.equals(createdDate, other.createdDate)
				&& Objects.equals(daoQueryType, other.daoQueryType) && Objects.equals(dashletBody, other.dashletBody)
				&& Objects.equals(dashletBodyChecksum, other.dashletBodyChecksum)
				&& Objects.equals(dashletId, other.dashletId) && Objects.equals(dashletName, other.dashletName)
				&& Objects.equals(dashletPropertVOList, other.dashletPropertVOList)
				&& Objects.equals(dashletQuery, other.dashletQuery)
				&& Objects.equals(dashletQueryChecksum, other.dashletQueryChecksum)
				&& Objects.equals(dashletTitle, other.dashletTitle)
				&& Objects.equals(dashletTypeId, other.dashletTypeId)
				&& Objects.equals(dataSourceId, other.dataSourceId) 
				&& Objects.equals(height, other.height) && Objects.equals(isActive, other.isActive)
				&& Objects.equals(isCustomUpdated, other.isCustomUpdated)
				&& Objects.equals(lastUpdatedTs, other.lastUpdatedTs)
				&& Objects.equals(resultVariableName, other.resultVariableName)
				&& Objects.equals(roleIdList, other.roleIdList) && Objects.equals(showHeader, other.showHeader)
				&& Objects.equals(updatedBy, other.updatedBy) && Objects.equals(width, other.width)
				&& Objects.equals(xCoordinate, other.xCoordinate) && Objects.equals(yCoordinate, other.yCoordinate);
	}

	@Override
	public String toString() {
		return "DashletVO [dashletId=" + dashletId + ", dashletTitle=" + dashletTitle + ", dashletName=" + dashletName
				+ ", dashletBody=" + dashletBody + ", dashletQuery=" + dashletQuery + ", xCoordinate=" + xCoordinate
				+ ", yCoordinate=" + yCoordinate + ", width=" + width + ", height=" + height + ", dataSourceId="
				+ dataSourceId + ", showHeader=" + showHeader + ", isActive=" + isActive + ", roleIdList=" + roleIdList
				+ ", resultVariableName=" + resultVariableName + ", daoQueryType=" + daoQueryType
				+ ", dashletPropertVOList=" + dashletPropertVOList + ", dashletTypeId=" + dashletTypeId + ", createdBy="
				+ createdBy + ", createdDate=" + createdDate + ", updatedBy=" + updatedBy + ", lastUpdatedTs="
				+ lastUpdatedTs + ", dashletQueryChecksum=" + dashletQueryChecksum + ", dashletBodyChecksum="
				+ dashletBodyChecksum + ", isCustomUpdated=" + isCustomUpdated + "]";
	}

	
}
