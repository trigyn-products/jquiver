package com.trigyn.jws.dbutils.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.trigyn.jws.dbutils.configurations.UUIDEntityListener;
import com.trigyn.jws.dbutils.vo.DatasourceLookUpVO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@EntityListeners(value = { UUIDEntityListener.class })
@Table(name = "jq_datasource_lookup")
@NamedQuery(name = "DatasourceLookUp.findAll", query = "SELECT j FROM DatasourceLookUp j")
public class DatasourceLookUp implements Serializable {
	private static final long			serialVersionUID		= 1L;

	@Id
	@Column(name = "datasource_lookup_id")
	private String						datasourceLookupId		= null;

	@Column(name = "database_product_name")
	private String						databaseProductName		= null;

	@Column(name = "driver_class_name")
	private String						driverClassName			= null;

	@Column(name = "datasource_supported_version ")
	private Double						datasourceSupportedVersion 			= null;

	@Column(name = "is_deleted")
	private Integer						isDeleted				= null;

	@OneToMany(mappedBy = "datasourceLookup")
	private List<AdditionalDatasource>	additionalDatasource	= null;

	@Column(name = "db_product_display_name")
	private String						databaseDisplayProductName		= null;
	
	@Column(name = "connection_url_pattern")
	private String connectionUrlPattern = null;

	public DatasourceLookUp() {

	}

	public DatasourceLookUp(String datasourceLookupId, String databaseProductName, String driverClassName,
			Double datasourceSupportedVersion, Integer isDeleted, List<AdditionalDatasource> additionalDatasource,
			String databaseDisplayProductName, String connectionUrlPattern) {
		super();
		this.datasourceLookupId			= datasourceLookupId;
		this.databaseProductName		= databaseProductName;
		this.driverClassName			= driverClassName;
		this.datasourceSupportedVersion	= datasourceSupportedVersion;
		this.isDeleted					= isDeleted;
		this.additionalDatasource		= additionalDatasource;
		this.databaseDisplayProductName	= databaseDisplayProductName;
		this.connectionUrlPattern		= connectionUrlPattern;
	}



	public String getDatasourceLookupId() {
		return this.datasourceLookupId;
	}

	public void setDatasourceLookupId(String datasourceLookupId) {
		this.datasourceLookupId = datasourceLookupId;
	}

	public String getDatabaseProductName() {
		return this.databaseProductName;
	}

	public void setDatabaseProductName(String databaseProductName) {
		this.databaseProductName = databaseProductName;
	}

	public String getDriverClassName() {
		return this.driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public Integer getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public List<AdditionalDatasource> getAdditionalDatasource() {
		return this.additionalDatasource;
	}

	public void setAdditionalDatasource(List<AdditionalDatasource> additionalDatasource) {
		this.additionalDatasource = additionalDatasource;
	}

	public Double getDatasourceSupportedVersion() {
		return datasourceSupportedVersion;
	}

	public void setDatasourceSupportedVersion(Double datasourceSupportedVersion) {
		this.datasourceSupportedVersion = datasourceSupportedVersion;
	}

	public String getDatabaseDisplayProductName() {
		return databaseDisplayProductName;
	}

	public void setDatabaseDisplayProductName(String databaseDisplayProductName) {
		this.databaseDisplayProductName = databaseDisplayProductName;
	}
	
	public String getConnectionUrlPattern() {
		return connectionUrlPattern;
	}

	public void setConnectionUrlPattern(String connectionUrlPattern) {
		this.connectionUrlPattern = connectionUrlPattern;
	}


	public AdditionalDatasource addAdditionalDatasource(AdditionalDatasource additionalDatasource) {
		getAdditionalDatasource().add(additionalDatasource);
		additionalDatasource.setDatasourceLookUp(this);

		return additionalDatasource;
	}

	public AdditionalDatasource removeAdditionalDatasource(AdditionalDatasource additionalDatasource) {
		getAdditionalDatasource().remove(additionalDatasource);
		additionalDatasource.setDatasourceLookUp(null);

		return additionalDatasource;
	}

	@Override
	public int hashCode() {
		return Objects.hash(databaseProductName, datasourceLookupId, driverClassName, isDeleted, additionalDatasource, connectionUrlPattern);
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
		DatasourceLookUp other = (DatasourceLookUp) obj;
		return Objects.equals(databaseProductName, other.databaseProductName)
				&& Objects.equals(datasourceLookupId, other.datasourceLookupId) && Objects.equals(driverClassName, other.driverClassName)
				&& Objects.equals(isDeleted, other.isDeleted) && Objects.equals(additionalDatasource, other.additionalDatasource)
				&& Objects.equals(connectionUrlPattern, other.connectionUrlPattern);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DatasourceLookUp [datasourceLookupId=").append(datasourceLookupId).append(", databaseProductName=")
				.append(databaseProductName).append(", driverClassName=").append(driverClassName).append(", isDeleted=").append(isDeleted)
				.append(", additionalDatasource=").append(additionalDatasource)
				.append(", connectionUrlPattern=").append(connectionUrlPattern).append("]");
		return builder.toString();
	}


	public DatasourceLookUp getObject() {
		DatasourceLookUp datasourceLookUp = new DatasourceLookUp();

		datasourceLookUp.setDatabaseProductName(databaseProductName);
		datasourceLookUp.setDatasourceLookupId(datasourceLookupId);
		datasourceLookUp.setDriverClassName(driverClassName);
		datasourceLookUp.setIsDeleted(isDeleted);
		datasourceLookUp.setDatasourceSupportedVersion(datasourceSupportedVersion);
		datasourceLookUp.setDatabaseDisplayProductName(databaseDisplayProductName);
		datasourceLookUp.setConnectionUrlPattern(connectionUrlPattern);
		
		return datasourceLookUp;
	}

	public DatasourceLookUpVO convertEntityToVO(DatasourceLookUp ds) {

		DatasourceLookUpVO datasourceLookUpVO = new DatasourceLookUpVO();
		datasourceLookUpVO.setDatasourceName(ds.getDatabaseProductName());
		datasourceLookUpVO.setDriverClassAvailable(false);
		try {
			Class.forName(ds.getDriverClassName());
			datasourceLookUpVO.setDriverClassAvailable(true);
		} catch (ClassNotFoundException exception) {
		}
		datasourceLookUpVO.setDriverClassName(ds.getDriverClassName());
		datasourceLookUpVO.setDatabaseDisplayProductName(ds.getDatabaseDisplayProductName());
		datasourceLookUpVO.setDatasourceSupportedVersion(ds.getDatasourceSupportedVersion());
		
		return datasourceLookUpVO;
	}

}