package com.trigyn.jws.dashboard.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class DashletVO implements Serializable{

	private static final long serialVersionUID = 6186812372333154327L;
	
	private String						dashletId				= null;

	private String						dashletTitle			= null;

	private String						dashletName				= null;

	private String						dashletBody				= null;

	private String						dashletQuery			= null;
	
	private Integer						xCoordinate				= null;

	private Integer						yCoordinate				= null;

	private Integer						width					= null;

	private Integer						height					= null;

	private String						contextId				= null;
	
	private Integer						showHeader				= null;
	
	private Integer						isActive				= null;
	
	private List<String>				roleIdList				= null;
	
	private List<DashletPropertyVO>		dashletPropertVOList	= null;
	
	public DashletVO() {
		
	}

	public DashletVO(String dashletId, String dashletTitle, String dashletName, String dashletBody, String dashletQuery
			, Integer xCoordinate, Integer yCoordinate, Integer width, Integer height, String contextId, Integer showHeader,
	        Integer isActive, List<String> roleIdList, List<DashletPropertyVO> dashletPropertVOList) {
		this.dashletId 				= dashletId;
		this.dashletTitle 			= dashletTitle;
		this.dashletName 			= dashletName;
		this.dashletBody 			= dashletBody;
		this.dashletQuery 			= dashletQuery;
		this.xCoordinate 			= xCoordinate;
		this.yCoordinate 			= yCoordinate;
		this.width 					= width;
		this.height 				= height;
		this.contextId 				= contextId;
		this.showHeader 			= showHeader;
		this.isActive 				= isActive;
		this.roleIdList 			= roleIdList;
		this.dashletPropertVOList 	= dashletPropertVOList;
	}

	public DashletVO(String dashletId, String dashletTitle, String dashletName, String dashletBody
			, String dashletQuery, Integer xCoordinate, Integer yCoordinate, Integer width
			, Integer height, String contextId, Integer showHeader, Integer isActive) {
		this.dashletId 		= dashletId;
		this.dashletTitle 	= dashletTitle;
		this.dashletName 	= dashletName;
		this.dashletBody 	= dashletBody;
		this.dashletQuery 	= dashletQuery;
		this.xCoordinate 	= xCoordinate;
		this.yCoordinate 	= yCoordinate;
		this.width 			= width;
		this.height 		= height;
		this.contextId 		= contextId;
		this.showHeader 	= showHeader;
		this.isActive 		= isActive;
	}

	/**
	 * @return the dashletId
	 */
	public String getDashletId() {
		return dashletId;
	}

	/**
	 * @param dashletId the dashletId to set
	 */
	public void setDashletId(String dashletId) {
		this.dashletId = dashletId;
	}

	/**
	 * @return the dashletTitle
	 */
	public String getDashletTitle() {
		return dashletTitle;
	}

	/**
	 * @param dashletTitle the dashletTitle to set
	 */
	public void setDashletTitle(String dashletTitle) {
		this.dashletTitle = dashletTitle;
	}

	/**
	 * @return the dashletName
	 */
	public String getDashletName() {
		return dashletName;
	}

	/**
	 * @param dashletName the dashletName to set
	 */
	public void setDashletName(String dashletName) {
		this.dashletName = dashletName;
	}

	/**
	 * @return the dashletBody
	 */
	public String getDashletBody() {
		return dashletBody;
	}

	/**
	 * @param dashletBody the dashletBody to set
	 */
	public void setDashletBody(String dashletBody) {
		this.dashletBody = dashletBody;
	}

	/**
	 * @return the dashletQuery
	 */
	public String getDashletQuery() {
		return dashletQuery;
	}

	/**
	 * @param dashletQuery the dashletQuery to set
	 */
	public void setDashletQuery(String dashletQuery) {
		this.dashletQuery = dashletQuery;
	}

	/**
	 * @return the xCoordinate
	 */
	public Integer getxCoordinate() {
		return xCoordinate;
	}

	/**
	 * @param xCoordinate the xCoordinate to set
	 */
	public void setXCoordinate(Integer xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	/**
	 * @return the yCoordinate
	 */
	public Integer getyCoordinate() {
		return yCoordinate;
	}

	/**
	 * @param yCoordinate the yCoordinate to set
	 */
	public void setYCoordinate(Integer yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	/**
	 * @return the width
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public Integer getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(Integer height) {
		this.height = height;
	}

	/**
	 * @return the contextId
	 */
	public String getContextId() {
		return contextId;
	}

	/**
	 * @param contextId the contextId to set
	 */
	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	/**
	 * @return the showHeader
	 */
	public Integer getShowHeader() {
		return showHeader;
	}

	/**
	 * @param showHeader the showHeader to set
	 */
	public void setShowHeader(Integer showHeader) {
		this.showHeader = showHeader;
	}

	/**
	 * @return the isActive
	 */
	public Integer getIsActive() {
		return isActive;
	}

	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	/**
	 * @return the roleIdList
	 */
	public List<String> getRoleIdList() {
		return roleIdList;
	}

	/**
	 * @param roleIdList the roleIdList to set
	 */
	public void setRoleIdList(List<String> roleIdList) {
		this.roleIdList = roleIdList;
	}

	/**
	 * @return the dashletPropertVOList
	 */
	public List<DashletPropertyVO> getDashletPropertVOList() {
		return dashletPropertVOList;
	}

	/**
	 * @param dashletPropertVOList the dashletPropertVOList to set
	 */
	public void setDashletPropertVOList(List<DashletPropertyVO> dashletPropertVOList) {
		this.dashletPropertVOList = dashletPropertVOList;
	}

	@Override
	public int hashCode() {
		return Objects.hash(contextId, dashletBody, dashletId, dashletName, dashletPropertVOList, dashletQuery, dashletTitle, height, roleIdList, showHeader, width, xCoordinate, yCoordinate);
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
		return Objects.equals(contextId, other.contextId) && Objects.equals(dashletBody, other.dashletBody) && Objects.equals(dashletId, other.dashletId) && Objects.equals(dashletName, other.dashletName)
		        && Objects.equals(dashletPropertVOList, other.dashletPropertVOList) && Objects.equals(dashletQuery, other.dashletQuery) && Objects.equals(dashletTitle, other.dashletTitle) && Objects.equals(height, other.height)
		        && Objects.equals(roleIdList, other.roleIdList) && Objects.equals(showHeader, other.showHeader) && Objects.equals(width, other.width) && Objects.equals(xCoordinate, other.xCoordinate)
		        && Objects.equals(yCoordinate, other.yCoordinate);
	}

	@Override
	public String toString() {
		return "DashletVO [dashletId=" + dashletId + ", dashletTitle=" + dashletTitle + ", dashletName=" + dashletName + ", dashletBody=" + dashletBody + ", dashletQuery=" + dashletQuery + ", xCoordinate=" + xCoordinate + ", yCoordinate="
		        + yCoordinate + ", width=" + width + ", height=" + height + ", contextId=" + contextId + ", showHeader=" + showHeader + ", roleIdList=" + roleIdList + ", dashletPropertVOList=" + dashletPropertVOList + "]";
	}

	
}
