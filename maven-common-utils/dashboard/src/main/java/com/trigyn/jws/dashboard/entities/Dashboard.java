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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "dashboard")
public class Dashboard implements Serializable {

	private static final long serialVersionUID 							= 1L;

	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "dashboard_id")
	private String								dashboardId				= null;

	@Column(name = "dashboard_name")
	private String								dashboardName			= null;

	@Column(name = "context_id")
	private String								contextId				= null;

	@Column(name = "dashboard_type")
	private Integer								dashboardType			= 1;

	@Column(name = "created_by")
	private String								createdBy				= null;
	
	@JsonIgnore
	@Column(name = "created_date")
	private Date								createdDate				= null;

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_updated_date")
	private Date								lastUpdatedDate			= null;

	@Column(name = "is_deleted")
	private Integer								isDeleted				= 0;

	@Column(name = "is_draggable")
	private Integer								isDraggable				= 0;

	@Column(name = "is_exportable")
	private Integer								isExportable			= 0;

	@OneToMany(mappedBy = "dashboard", fetch = FetchType.EAGER)
	private List<DashboardRoleAssociation>		dashboardRoles			= new ArrayList<>();

	@OneToMany(mappedBy = "dashboard", fetch = FetchType.LAZY)
	private List<DashboardDashletAssociation>	dashboardDashlets		= new ArrayList<>();
	
	@OneToMany(mappedBy = "dashboard", fetch = FetchType.LAZY)
	private List<DashboardRoleAssociation> dashboardRoleAssociations	= new ArrayList<>();


	public Dashboard() {
		
	}

	public Dashboard(String dashboardId, String dashboardName, String contextId, Integer dashboardType
			, String createdBy, Date createdDate, Date lastUpdatedDate, Integer isDeleted
			, Integer isDraggable, Integer isExportable, List<DashboardRoleAssociation> dashboardRoles
			, List<DashboardDashletAssociation> dashboardDashlets) {
		this.dashboardId 		= dashboardId;
		this.dashboardName 		= dashboardName;
		this.contextId 			= contextId;
		this.dashboardType 		= dashboardType;
		this.createdBy 			= createdBy;
		this.createdDate 		= createdDate;
		this.lastUpdatedDate 	= lastUpdatedDate;
		this.isDeleted 			= isDeleted;
		this.isDraggable 		= isDraggable;
		this.isExportable 		= isExportable;
		this.dashboardRoles 	= dashboardRoles;
		this.dashboardDashlets 	= dashboardDashlets;
	}

	public String getDashboardId() {
		return this.dashboardId;
	}

	public void setDashboardId(String dashboardId) {
		this.dashboardId = dashboardId;
	}

	public String getDashboardName() {
		return this.dashboardName;
	}

	public void setDashboardName(String dashboardName) {
		this.dashboardName = dashboardName;
	}

	public String getContextId() {
		return this.contextId;
	}

	public void setContextId(String contextId) {
		this.contextId = contextId;
	}

	public Integer getDashboardType() {
		return this.dashboardType;
	}

	public void setDashboardType(Integer dashboardType) {
		this.dashboardType = dashboardType;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastUpdatedDate() {
		return this.lastUpdatedDate;
	}

	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	public Integer getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getIsDraggable() {
		return this.isDraggable;
	}

	public void setIsDraggable(Integer isDraggable) {
		this.isDraggable = isDraggable;
	}

	public Integer getIsExportable() {
		return this.isExportable;
	}

	public void setIsExportable(Integer isExportable) {
		this.isExportable = isExportable;
	}

	public List<DashboardRoleAssociation> getDashboardRoles() {
		return this.dashboardRoles;
	}

	public void setDashboardRoles(List<DashboardRoleAssociation> dashboardRoles) {
		this.dashboardRoles = dashboardRoles;
	}

	public List<DashboardDashletAssociation> getDashboardDashlets() {
		return this.dashboardDashlets;
	}

	public void setDashboardDashlets(List<DashboardDashletAssociation> dashboardDashlets) {
		this.dashboardDashlets = dashboardDashlets;
	}

	public Dashboard dashboardId(String dashboardId) {
		this.dashboardId = dashboardId;
		return this;
	}

	public Dashboard dashboardName(String dashboardName) {
		this.dashboardName = dashboardName;
		return this;
	}



	@Override
	public int hashCode() {
		return Objects.hash(contextId, createdBy, createdDate, dashboardDashlets, dashboardId, dashboardName,
				dashboardRoleAssociations, dashboardRoles, dashboardType, isDeleted, isDraggable, isExportable,
				lastUpdatedDate);
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
		Dashboard other = (Dashboard) obj;
		return Objects.equals(contextId, other.contextId) && Objects.equals(createdBy, other.createdBy)
				&& Objects.equals(createdDate, other.createdDate)
				&& Objects.equals(dashboardDashlets, other.dashboardDashlets)
				&& Objects.equals(dashboardId, other.dashboardId) && Objects.equals(dashboardName, other.dashboardName)
				&& Objects.equals(dashboardRoleAssociations, other.dashboardRoleAssociations)
				&& Objects.equals(dashboardRoles, other.dashboardRoles)
				&& Objects.equals(dashboardType, other.dashboardType) && Objects.equals(isDeleted, other.isDeleted)
				&& Objects.equals(isDraggable, other.isDraggable) && Objects.equals(isExportable, other.isExportable)
				&& Objects.equals(lastUpdatedDate, other.lastUpdatedDate);
	}

	
	@Override
	public String toString() {
		return "{" +
			" dashboardId='" + getDashboardId() + "'" +
			", dashboardName='" + getDashboardName() + "'" +
			", contextId='" + getContextId() + "'" +
			", dashboardType='" + getDashboardType() + "'" +
			", createdBy='" + getCreatedBy() + "'" +
			", createdDate='" + getCreatedDate() + "'" +
			", lastUpdatedDate='" + getLastUpdatedDate() + "'" +
			", isDeleted='" + getIsDeleted() + "'" +
			", isDraggable='" + getIsDraggable() + "'" +
			", isExportable='" + getIsExportable() + "'" +
			", dashboardRoles='" + getDashboardRoles() + "'" +
			", dashboardDashlets='" + getDashboardDashlets() + "'" +
			", dashboardRoleAssociations='" + getDashboardRoles() + "'" +
			"}";
	}
	

}
