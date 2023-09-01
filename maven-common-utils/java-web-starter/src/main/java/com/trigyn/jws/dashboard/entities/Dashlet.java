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

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "jq_dashlet")
public class Dashlet implements Serializable {

	private static final long					serialVersionUID		= 1L;

	@Id
	@GeneratedValue(generator = "inquisitive-uuid")
	@GenericGenerator(name = "inquisitive-uuid", strategy = "com.trigyn.jws.dbutils.configurations.CustomUUIDGenerator")
	@Column(name = "dashlet_id", nullable = false)
	private String								dashletId				= null;

	@Column(name = "dashlet_name")
	private String								dashletName				= null;

	@Column(name = "dashlet_title")
	private String								dashletTitle			= null;

	@Column(name = "x_coordinate")
	private Integer								xCoordinate				= null;

	@Column(name = "y_coordinate")
	private Integer								yCoordinate				= null;

	@Column(name = "dashlet_width")
	private Integer								width					= null;

	@Column(name = "dashlet_height")
	private Integer								height					= null;

	@Column(name = "show_header", nullable = false)
	private Integer								showHeader				= 1;

	@Column(name = "dashlet_query")
	private String								dashletQuery			= null;

	@Column(name = "dashlet_body")
	private String								dashletBody				= null;

	@Column(name = "created_by")
	private String								createdBy				= null;

	@JsonIgnore
	@Column(name = "created_date")
	private Date								createdDate				= null;

	@Column(name = "updated_by")
	private String								updatedBy				= null;

	@JsonIgnore
	@Column(name = "last_updated_ts")
	private Date								lastUpdatedTs			= null;

	@Column(name = "is_active")
	private Integer								isActive				= null;

	@Column(name = "dashlet_query_checksum")
	private String								dashletQueryChecksum	= null;

	@Column(name = "dashlet_body_checksum")
	private String								dashletBodyChecksum		= null;

	@Column(name = "dashlet_type_id")
	private Integer								dashletTypeId			= 1;

	@Column(name = "datasource_id")
	private String								datasourceId			= null;

	@Column(name = "result_variable_name")
	private String								resultVariableName		= null;

	@Column(name = "dao_query_type")
	private Integer								daoQueryType			= null;

	@Column(name = "is_custom_updated")
	private Integer								isCustomUpdated			= 1;

	@OneToMany(mappedBy = "dashlet", fetch = FetchType.EAGER)
	private List<DashletProperties>				properties				= new ArrayList<>();

	@OneToMany(mappedBy = "dashlet", fetch = FetchType.LAZY)
	private List<DashboardDashletAssociation>	dashboardAssociation	= new ArrayList<>();

	public Dashlet() {
	}

	public Dashlet(String dashletId, String dashletName, String dashletTitle, Integer xCoordinate, Integer yCoordinate,
			Integer width, Integer height, Integer showHeader, String dashletQuery,
			String dashletBody, String createdBy, Date createdDate, String updatedBy, Date lastUpdatedTs,
			Integer isActive, String dashletQueryChecksum, String dashletBodyChecksum, Integer dashletTypeId,
			String datasourceId, String resultVariableName, Integer daoQueryType) {
		this.dashletId				= dashletId;
		this.dashletName			= dashletName;
		this.dashletTitle			= dashletTitle;
		this.xCoordinate			= xCoordinate;
		this.yCoordinate			= yCoordinate;
		this.width					= width;
		this.height					= height;
		this.showHeader				= showHeader;
		this.dashletQuery			= dashletQuery;
		this.dashletBody			= dashletBody;
		this.createdBy				= createdBy;
		this.createdDate			= createdDate;
		this.updatedBy				= updatedBy;
		this.lastUpdatedTs			= lastUpdatedTs;
		this.isActive				= isActive;
		this.dashletQueryChecksum	= dashletQueryChecksum;
		this.dashletBodyChecksum	= dashletBodyChecksum;
		this.dashletTypeId			= dashletTypeId;
		this.datasourceId			= datasourceId;
		this.resultVariableName		= resultVariableName;
		this.daoQueryType			= daoQueryType;
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

	public Integer getXCoordinate() {
		return xCoordinate;
	}

	public void setXCoordinate(Integer xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public Integer getYCoordinate() {
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

	public Integer getShowHeader() {
		return showHeader;
	}

	public void setShowHeader(Integer showHeader) {
		this.showHeader = showHeader;
	}

	public String getDashletQuery() {
		return dashletQuery;
	}

	public void setDashletQuery(String dashletQuery) {
		this.dashletQuery = dashletQuery;
	}

	public String getDashletBody() {
		return dashletBody;
	}

	public void setDashletBody(String dashletBody) {
		this.dashletBody = dashletBody;
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

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
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

	public List<DashletProperties> getProperties() {
		return properties;
	}

	public void setProperties(List<DashletProperties> properties) {
		this.properties = properties;
	}

	public List<DashboardDashletAssociation> getDashboardAssociation() {
		return dashboardAssociation;
	}

	public void setDashboardAssociation(List<DashboardDashletAssociation> dashboardAssociation) {
		this.dashboardAssociation = dashboardAssociation;
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

	public Integer getDashletTypeId() {
		return dashletTypeId;
	}

	public void setDashletTypeId(Integer dashletTypeId) {
		this.dashletTypeId = dashletTypeId;
	}

	public String getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(String datasourceId) {
		this.datasourceId = datasourceId;
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

	public Integer getIsCustomUpdated() {
		return isCustomUpdated;
	}

	public void setIsCustomUpdated(Integer isCustomUpdated) {
		this.isCustomUpdated = isCustomUpdated;
	}

	
	@Override
	public int hashCode() {
		return Objects.hash(createdBy, createdDate, daoQueryType, dashboardAssociation, dashletBody,
				dashletBodyChecksum, dashletId, dashletName, dashletQuery, dashletQueryChecksum, dashletTitle,
				dashletTypeId, datasourceId, height, isActive, isCustomUpdated, lastUpdatedTs, properties,
				resultVariableName, showHeader, updatedBy, width, xCoordinate, yCoordinate);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Dashlet [dashletId=").append(dashletId).append(", dashletName=").append(dashletName)
				.append(", dashletTitle=").append(dashletTitle).append(", xCoordinate=").append(xCoordinate)
				.append(", yCoordinate=").append(yCoordinate).append(", width=").append(width).append(", height=")
				.append(height).append(", showHeader=").append(showHeader)
				.append(", dashletQuery=").append(dashletQuery).append(", dashletBody=").append(dashletBody)
				.append(", createdBy=").append(createdBy).append(", createdDate=").append(createdDate)
				.append(", updatedBy=").append(updatedBy).append(", lastUpdatedTs=").append(lastUpdatedTs)
				.append(", isActive=").append(isActive).append(", dashletQueryChecksum=").append(dashletQueryChecksum)
				.append(", dashletBodyChecksum=").append(dashletBodyChecksum).append(", dashletTypeId=")
				.append(dashletTypeId).append(", dashboardAssociation=")
				.append(dashboardAssociation).append("]");
		return builder.toString();
	}

	public Dashlet getObject() {
		Dashlet dashlet = new Dashlet();
		dashlet.setCreatedBy(createdBy != null ? createdBy.trim() : createdBy);
		dashlet.setCreatedDate(createdDate);
		dashlet.setDatasourceId(datasourceId);

		List<DashboardDashletAssociation> ddaOtr = new ArrayList<>();
		if (dashboardAssociation != null && !dashboardAssociation.isEmpty()) {
			for (DashboardDashletAssociation obj : dashboardAssociation) {
				ddaOtr.add(obj.getObject());
			}
			dashlet.setDashboardAssociation(ddaOtr);
		} else
			dashlet.setDashboardAssociation(null);

		dashlet.setDashletBody(dashletBody != null ? dashletBody.trim() : dashletBody);
		dashlet.setDashletBodyChecksum(dashletBodyChecksum != null ? dashletBodyChecksum.trim() : dashletBodyChecksum);
		dashlet.setDashletId(dashletId != null ? dashletId.trim() : dashletId);
		dashlet.setDashletName(dashletName != null ? dashletName.trim() : dashletName);
		dashlet.setDashletQuery(dashletQuery != null ? dashletQuery.trim() : dashletQuery);
		dashlet.setDashletQueryChecksum(
				dashletQueryChecksum != null ? dashletQueryChecksum.trim() : dashletQueryChecksum);

		dashlet.setDashletTitle(dashletTitle != null ? dashletTitle.trim() : dashletTitle);
		dashlet.setDashletTypeId(dashletTypeId);
		dashlet.setHeight(height);
		dashlet.setIsActive(isActive);
		dashlet.setProperties(properties);

		List<DashletProperties> dpOtr = new ArrayList<>();
		if (properties != null && !properties.isEmpty()) {
			for (DashletProperties obj : properties) {
				dpOtr.add(obj.getObj());
			}
			dashlet.setProperties(dpOtr);
		} else
			dashlet.setProperties(null);

		dashlet.setShowHeader(showHeader);
		dashlet.setUpdatedBy(updatedBy != null ? updatedBy.trim() : updatedBy);
		dashlet.setLastUpdatedTs(lastUpdatedTs);
		dashlet.setWidth(width);
		dashlet.setXCoordinate(xCoordinate);
		dashlet.setYCoordinate(yCoordinate);
		dashlet.setDaoQueryType(daoQueryType);
		dashlet.setResultVariableName(resultVariableName);
		return dashlet;
	}

}
