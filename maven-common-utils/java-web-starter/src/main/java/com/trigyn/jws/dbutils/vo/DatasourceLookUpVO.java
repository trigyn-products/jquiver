package com.trigyn.jws.dbutils.vo;

import java.io.Serializable;
import java.util.Objects;

public class DatasourceLookUpVO implements Serializable {

	private static final long	serialVersionUID			= 2957394044383347260L;

	private String				datasourceName				= null;
	private String				driverClassName				= null;
	private Boolean				driverClassAvailable		= false;

	private String				databaseDisplayProductName	= null;

	private Double				datasourceSupportedVersion	= null;
	
	private String 				connectionUrlPattern  = null;

	public DatasourceLookUpVO() {

	}

	public DatasourceLookUpVO(String datasourceName, String driverClassName, Boolean driverClassAvailable, String connectionUrlString) {
		this.datasourceName			= datasourceName;
		this.driverClassName		= driverClassName;
		this.driverClassAvailable	= driverClassAvailable;
		this.connectionUrlPattern 	= connectionUrlString;
	}

	public String getDatasourceName() {
		return datasourceName;
	}

	public void setDatasourceName(String datasourceName) {
		this.datasourceName = datasourceName;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public Boolean getDriverClassAvailable() {
		return driverClassAvailable;
	}

	public void setDriverClassAvailable(Boolean driverClassAvailable) {
		this.driverClassAvailable = driverClassAvailable;
	}

	public String getDatabaseDisplayProductName() {
		return databaseDisplayProductName;
	}

	public void setDatabaseDisplayProductName(String databaseDisplayProductName) {
		this.databaseDisplayProductName = databaseDisplayProductName;
	}

	public Double getDatasourceSupportedVersion() {
		return datasourceSupportedVersion;
	}

	public void setDatasourceSupportedVersion(Double datasourceSupportedVersion) {
		this.datasourceSupportedVersion = datasourceSupportedVersion;
	}

	public String getConnectionUrlPattern() {
		return connectionUrlPattern;
	}

	public void setConnectionUrlPattern(String connectionUrlPattern) {
		this.connectionUrlPattern = connectionUrlPattern;
	}

	@Override
	public int hashCode() {
		return Objects.hash(datasourceName, driverClassAvailable, driverClassName, connectionUrlPattern);
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
		DatasourceLookUpVO other = (DatasourceLookUpVO) obj;
		return Objects.equals(datasourceName, other.datasourceName)
				&& Objects.equals(driverClassAvailable, other.driverClassAvailable)
				&& Objects.equals(driverClassName, other.driverClassName)
				&& Objects.equals(connectionUrlPattern, other.connectionUrlPattern);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DatasourceLookUpVO [datasourceName=").append(datasourceName).append(", driverClassName=")
				.append(driverClassName).append(", driverClassAvailable=").append(driverClassAvailable)
				.append(", connectionUrlPattern=").append(connectionUrlPattern)
				.append("]");
		return builder.toString();
	}

}
