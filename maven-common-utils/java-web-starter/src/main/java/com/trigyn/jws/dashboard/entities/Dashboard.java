package com.trigyn.jws.dashboard.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trigyn.jws.dbutils.configurations.UUIDEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@EntityListeners(value = { UUIDEntityListener.class })
@Table(name = "jq_dashboard")
public class Dashboard implements Serializable {

	private static final long					serialVersionUID			= 1L;

	@Id
	@Column(name = "dashboard_id")
	private String								dashboardId					= null;

	@Column(name = "dashboard_name")
	private String								dashboardName				= null;

	@Column(name = "dashboard_type")
	private Integer								dashboardType				= 1;

	@Column(name = "created_by")
	private String								createdBy					= null;

	@JsonIgnore
	@Column(name = "created_date")
	private Date								createdDate					= null;

	@JsonIgnore
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_updated_ts")
	private Date								lastUpdatedTs				= null;

	@Column(name = "is_deleted")
	private Integer								isDeleted					= 0;

	@Column(name = "is_draggable")
	private Integer								isDraggable					= 0;

	@Column(name = "is_exportable")
	private Integer								isExportable				= 0;

	@Column(name = "is_custom_updated")
	private Integer								isCustomUpdated				= 1;
	
	@Column(name = "dashboard_body")
	private String								dashboardBody				= null;

	@OneToMany(mappedBy = "dashboard", fetch = FetchType.EAGER)
	private List<DashboardRoleAssociation>		dashboardRoles				= new ArrayList<>();

	@JsonIgnore
	@OneToMany(mappedBy = "dashboard", fetch = FetchType.LAZY)
	private List<DashboardDashletAssociation>	dashboardDashlets			= new ArrayList<>();

	@OneToMany(mappedBy = "dashboard", fetch = FetchType.LAZY)
	private List<DashboardRoleAssociation>		dashboardRoleAssociations	= new ArrayList<>();

	public Dashboard() {

	}

	public Dashboard(String dashboardId, String dashboardName, Integer dashboardType,
			String createdBy, Date createdDate, Date lastUpdatedTs, Integer isDeleted, Integer isDraggable,
			Integer isExportable, List<DashboardRoleAssociation> dashboardRoles,
			List<DashboardDashletAssociation> dashboardDashlets,String dashboardBody) {
		this.dashboardId		= dashboardId;
		this.dashboardName		= dashboardName;
		this.dashboardType		= dashboardType;
		this.createdBy			= createdBy;
		this.createdDate		= createdDate;
		this.lastUpdatedTs		= lastUpdatedTs;
		this.isDeleted			= isDeleted;
		this.isDraggable		= isDraggable;
		this.isExportable		= isExportable;
		this.dashboardRoles		= dashboardRoles;
		this.dashboardDashlets	= dashboardDashlets;
		this.dashboardBody		= dashboardBody;
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

	public Date getLastUpdatedTs() {
		return this.lastUpdatedTs;
	}

	public void setLastUpdatedTs(Date lastUpdatedTs) {
		this.lastUpdatedTs = lastUpdatedTs;
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

	public List<DashboardRoleAssociation> getDashboardRoleAssociations() {
		return dashboardRoleAssociations;
	}

	public void setDashboardRoleAssociations(List<DashboardRoleAssociation> dashboardRoleAssociations) {
		this.dashboardRoleAssociations = dashboardRoleAssociations;
	}

	public Integer getIsCustomUpdated() {
		return isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
	}

	public String getDashboardBody() {
		return dashboardBody;
	}

	public void setDashboardBody(String dashboardBody) {
		this.dashboardBody = dashboardBody;
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
		return Objects.hash(createdBy, createdDate,dashboardId, dashboardName,
				dashboardRoleAssociations, dashboardRoles, dashboardType, isDeleted, isDraggable, isExportable,
				lastUpdatedTs,dashboardBody);
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
		return Objects.equals(createdBy, other.createdBy)
				&& Objects.equals(createdDate, other.createdDate)
				&& Objects.equals(dashboardDashlets, other.dashboardDashlets)
				&& Objects.equals(dashboardId, other.dashboardId) && Objects.equals(dashboardName, other.dashboardName)
				&& Objects.equals(dashboardRoleAssociations, other.dashboardRoleAssociations)
				&& Objects.equals(dashboardRoles, other.dashboardRoles)
				&& Objects.equals(dashboardType, other.dashboardType) && Objects.equals(isDeleted, other.isDeleted)
				&& Objects.equals(isDraggable, other.isDraggable) && Objects.equals(isExportable, other.isExportable)
				&& Objects.equals(lastUpdatedTs, other.lastUpdatedTs);
	}

	@Override
	public String toString() {
		return "{" + " dashboardId='" + getDashboardId() + "'" + ", dashboardName='" + getDashboardName() + "'"
				+ ", dashboardType='" + getDashboardType() + "'"
				+ ", createdBy='" + getCreatedBy() + "'" + ", createdDate='" + getCreatedDate() + "'"
				+ ", lastUpdatedTs='" + getLastUpdatedTs() + "'" + ", isDeleted='" + getIsDeleted() + "'"
				+ ", isDraggable='" + getIsDraggable() + "'" + ", isExportable='" + getIsExportable() + "'"
				+ "'" + ", dashboardRoleAssociations='" + getDashboardRoles() + "'"
				+ "'" + ", dashboardBody='" + getDashboardBody() + "'" + "}";
	}

	public Dashboard getObject() {
		Dashboard dashboard = new Dashboard();
		dashboard.setCreatedBy(createdBy != null ? createdBy.trim() : createdBy);
		dashboard.setCreatedDate(createdDate);
		dashboard.setDashboardId(dashboardId != null ? dashboardId.trim() : dashboardId);
		dashboard.setDashboardName(dashboardName != null ? dashboardName.trim() : dashboardName);
		dashboard.setDashboardType(dashboardType);
		dashboard.setIsDeleted(isDeleted);
		dashboard.setIsDraggable(isDraggable);
		dashboard.setIsExportable(isExportable);
		dashboard.setLastUpdatedTs(lastUpdatedTs);
		dashboard.setDashboardBody(dashboardBody);

		List<DashboardDashletAssociation> dashboardDashletsOtr = new ArrayList<>();
		if (dashboardDashlets != null && !dashboardDashlets.isEmpty()) {
			for (DashboardDashletAssociation obj : dashboardDashlets) {
				dashboardDashletsOtr.add(obj.getObject());
			}
			dashboard.setDashboardDashlets(dashboardDashletsOtr);
		} else {
			dashboard.setDashboardDashlets(null);
		}

		List<DashboardRoleAssociation> dashboardRoleAssociationsOtr = new ArrayList<>();
		if (dashboardRoleAssociations != null && !dashboardRoleAssociations.isEmpty()) {
			for (DashboardRoleAssociation obj : dashboardRoleAssociations) {
				dashboardRoleAssociationsOtr.add(obj.getObject());
			}
			dashboard.setDashboardRoleAssociations(dashboardRoleAssociationsOtr);
		} else {
			dashboard.setDashboardRoleAssociations(null);
		}

		List<DashboardRoleAssociation> dashboardRolesOtr = new ArrayList<>();
		if (dashboardRoles != null && !dashboardRoles.isEmpty()) {
			for (DashboardRoleAssociation obj : dashboardRoles) {
				dashboardRolesOtr.add(obj.getObject());
			}
			dashboard.setDashboardRoles(dashboardRolesOtr);
		} else {
			dashboard.setDashboardRoles(null);
		}

		return dashboard;
	}

}
