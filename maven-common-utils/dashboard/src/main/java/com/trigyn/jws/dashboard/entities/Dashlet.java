package com.trigyn.jws.dashboard.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "dashlet")
public class Dashlet implements Serializable {

	/**
	 * 
	 */
	private static final long			serialVersionUID		= 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "dashlet_id", nullable = false)
	private String						dashletId				= null;

	@Column(name = "dashlet_name")
	private String						dashletName				= null;

	@Column(name = "dashlet_title")
	private String						dashletTitle			= null;

	@Column(name = "x_coordinate")
	private Integer						xCoordinate				= null;

	@Column(name = "y_coordinate")
	private Integer						yCoordinate				= null;
	
	@Column(name = "dashlet_width")
	private Integer						width					= null;

	@Column(name = "dashlet_height")
	private Integer						height					= null;
	
	@Column(name = "context_id", nullable = false)
	private String						contextId				= null;
	
	@Column(name = "show_header", nullable = false)
	private Integer						showHeader				= 1;
	
	@Column(name = "dashlet_query")
	private String						dashletQuery			= null;
	
	@Column(name = "dashlet_body")
	private String						dashletBody				= null;

	@Column(name = "created_by")
	private String						createdBy				= null;

	@Column(name = "created_date")
	private Date						createdDate				= null;

	@Column(name = "updated_by")
	private String						updatedBy				= null;

	@Column(name = "updated_date")
	private Date						updatedDate				= null;
	
	@Column(name = "is_active")
	private Integer						isActive				= null;
	
	@Column(name = "dashlet_query_checksum")
	private String				dashletQueryChecksum			= null;
	
	@Column(name = "dashlet_body_checksum")
	private String				dashletBodyChecksum				= null;

	@OneToMany(mappedBy = "dashlet", fetch = FetchType.EAGER)
	private List<DashletProperties>				properties				= new ArrayList<>();

	@OneToMany(mappedBy = "dashlet", fetch = FetchType.LAZY)
	private List<DashletRoleAssociation>		roleAssociation			= new ArrayList<>();

	@OneToMany(mappedBy = "dashlet", fetch = FetchType.LAZY)
	private List<DashboardDashletAssociation>	dashboardAssociation	= new ArrayList<>();
	
	@OneToMany(mappedBy = "dashlet", fetch = FetchType.LAZY)
	private List<DashletRoleAssociation> dashletRoleAssociations		= new ArrayList<>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "context_id", referencedColumnName = "context_id", nullable = false, insertable = false, updatable = false)
	private ContextMaster				contextMaster					= null;


	public Dashlet() {
	}


	public Dashlet(String dashletId, String dashletName, String dashletTitle, Integer xCoordinate, Integer yCoordinate, Integer width, Integer height, String contextId, Integer showHeader, String dashletQuery, String dashletBody,
	        String createdBy, Date createdDate, String updatedBy, Date updatedDate, Integer isActive, List<DashletProperties> properties, List<DashletRoleAssociation> roleAssociation, List<DashboardDashletAssociation> dashboardAssociation,
	        ContextMaster contextMaster,String dashletQueryChecksum,String dashletBodyChecksum) {
		this.dashletId 					= dashletId;
		this.dashletName 				= dashletName;
		this.dashletTitle 				= dashletTitle;
		this.xCoordinate 				= xCoordinate;
		this.yCoordinate 				= yCoordinate;
		this.width 						= width;
		this.height 					= height;
		this.contextId 					= contextId;
		this.showHeader 				= showHeader;
		this.dashletQuery 				= dashletQuery;
		this.dashletBody 				= dashletBody;
		this.createdBy 					= createdBy;
		this.createdDate 				= createdDate;
		this.updatedBy 					= updatedBy;
		this.updatedDate 				= updatedDate;
		this.isActive 					= isActive;
		this.properties 				= properties;
		this.roleAssociation 			= roleAssociation;
		this.dashboardAssociation 		= dashboardAssociation;
		this.contextMaster 				= contextMaster;
		this.dashletQueryChecksum       = dashletQueryChecksum;
		this.dashletBodyChecksum 		= dashletBodyChecksum;
	}


	public Dashlet(String dashletId, String dashletName, String dashletTitle, Integer xCoordinate, Integer yCoordinate, Integer width, Integer height, String contextId, Integer showHeader, String dashletQuery, String dashletBody,
	        String createdBy, Date createdDate, String updatedBy, Date updatedDate, Integer isActive) {
		this.dashletId 					= dashletId;
		this.dashletName 				= dashletName;
		this.dashletTitle 				= dashletTitle;
		this.xCoordinate 				= xCoordinate;
		this.yCoordinate 				= yCoordinate;
		this.width 						= width;
		this.height 					= height;
		this.contextId 					= contextId;
		this.showHeader 				= showHeader;
		this.dashletQuery 				= dashletQuery;
		this.dashletBody 				= dashletBody;
		this.createdBy 					= createdBy;
		this.createdDate 				= createdDate;
		this.updatedBy 					= updatedBy;
		this.updatedDate 				= updatedDate;
		this.isActive 					= isActive;
		this.dashletQueryChecksum       = dashletQueryChecksum;
		this.dashletBodyChecksum 		= dashletBodyChecksum;
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
	 * @return the xCoordinate
	 */
	public Integer getXCoordinate() {
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
	public Integer getYCoordinate() {
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
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}


	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}


	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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
	 * @return the updatedBy
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}


	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}


	/**
	 * @return the updatedDate
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}


	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}


	/**
	 * @return the properties
	 */
	public List<DashletProperties> getProperties() {
		return properties;
	}


	/**
	 * @param properties the properties to set
	 */
	public void setProperties(List<DashletProperties> properties) {
		this.properties = properties;
	}


	/**
	 * @return the roleAssociation
	 */
	public List<DashletRoleAssociation> getRoleAssociation() {
		return roleAssociation;
	}


	/**
	 * @param roleAssociation the roleAssociation to set
	 */
	public void setRoleAssociation(List<DashletRoleAssociation> roleAssociation) {
		this.roleAssociation = roleAssociation;
	}


	/**
	 * @return the dashboardAssociation
	 */
	public List<DashboardDashletAssociation> getDashboardAssociation() {
		return dashboardAssociation;
	}


	/**
	 * @param dashboardAssociation the dashboardAssociation to set
	 */
	public void setDashboardAssociation(List<DashboardDashletAssociation> dashboardAssociation) {
		this.dashboardAssociation = dashboardAssociation;
	}


	/**
	 * @return the dashletRoleAssociations
	 */
	public List<DashletRoleAssociation> getDashletRoleAssociations() {
		return dashletRoleAssociations;
	}


	/**
	 * @param dashletRoleAssociations the dashletRoleAssociations to set
	 */
	public void setDashletRoleAssociations(List<DashletRoleAssociation> dashletRoleAssociations) {
		this.dashletRoleAssociations = dashletRoleAssociations;
	}
	
	
	/**
	 * @return the contextMaster
	 */
	public ContextMaster getContextMaster() {
		return contextMaster;
	}


	/**
	 * @param contextMaster the contextMaster to set
	 */
	public void setContextMaster(ContextMaster contextMaster) {
		this.contextMaster = contextMaster;
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


	@Override
	public int hashCode() {
		return Objects.hash(contextId, contextMaster, createdBy, createdDate, dashboardAssociation, dashletBody,
				dashletBodyChecksum, dashletId, dashletName, dashletQuery, dashletQueryChecksum,
				dashletRoleAssociations, dashletTitle, height, isActive, properties, roleAssociation, showHeader,
				updatedBy, updatedDate, width, xCoordinate, yCoordinate);
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
		Dashlet other = (Dashlet) obj;
		return Objects.equals(contextId, other.contextId) && Objects.equals(contextMaster, other.contextMaster)
				&& Objects.equals(createdBy, other.createdBy) && Objects.equals(createdDate, other.createdDate)
				&& Objects.equals(dashboardAssociation, other.dashboardAssociation)
				&& Objects.equals(dashletBody, other.dashletBody)
				&& Objects.equals(dashletBodyChecksum, other.dashletBodyChecksum)
				&& Objects.equals(dashletId, other.dashletId) && Objects.equals(dashletName, other.dashletName)
				&& Objects.equals(dashletQuery, other.dashletQuery)
				&& Objects.equals(dashletQueryChecksum, other.dashletQueryChecksum)
				&& Objects.equals(dashletRoleAssociations, other.dashletRoleAssociations)
				&& Objects.equals(dashletTitle, other.dashletTitle) && Objects.equals(height, other.height)
				&& Objects.equals(isActive, other.isActive) && Objects.equals(properties, other.properties)
				&& Objects.equals(roleAssociation, other.roleAssociation)
				&& Objects.equals(showHeader, other.showHeader) && Objects.equals(updatedBy, other.updatedBy)
				&& Objects.equals(updatedDate, other.updatedDate) && Objects.equals(width, other.width)
				&& Objects.equals(xCoordinate, other.xCoordinate) && Objects.equals(yCoordinate, other.yCoordinate);
	}


	@Override
	public String toString() {
		return "Dashlet [dashletId=" + dashletId + ", dashletName=" + dashletName + ", dashletTitle=" + dashletTitle
				+ ", xCoordinate=" + xCoordinate + ", yCoordinate=" + yCoordinate + ", width=" + width + ", height="
				+ height + ", contextId=" + contextId + ", showHeader=" + showHeader + ", dashletQuery=" + dashletQuery
				+ ", dashletBody=" + dashletBody + ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate + ", isActive=" + isActive
				+ ", dashletQueryChecksum=" + dashletQueryChecksum + ", dashletBodyChecksum=" + dashletBodyChecksum
				+ ", properties=" + properties + ", roleAssociation=" + roleAssociation + ", dashboardAssociation="
				+ dashboardAssociation + ", dashletRoleAssociations=" + dashletRoleAssociations + ", contextMaster="
				+ contextMaster + "]";
	}


}
