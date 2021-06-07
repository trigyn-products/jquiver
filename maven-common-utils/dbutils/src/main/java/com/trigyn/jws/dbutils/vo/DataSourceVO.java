package com.trigyn.jws.dbutils.vo;

import java.io.Serializable;
import java.util.Objects;

public class DataSourceVO implements Serializable {

	private static final long	serialVersionUID		= 1L;

	private String				additionalDataSourceId	= null;
	private String				driverClassName			= null;
	private String				dataSourceConfiguration	= null;

	public DataSourceVO() {

	}

	public DataSourceVO(String additionalDataSourceId, String driverClassName, String dataSourceConfiguration) {
		this.additionalDataSourceId		= additionalDataSourceId;
		this.driverClassName			= driverClassName;
		this.dataSourceConfiguration	= dataSourceConfiguration;
	}

	public String getAdditionalDataSourceId() {
		return additionalDataSourceId;
	}

	public void setAdditionalDataSourceId(String additionalDataSourceId) {
		this.additionalDataSourceId = additionalDataSourceId;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getDataSourceConfiguration() {
		return dataSourceConfiguration;
	}

	public void setDataSourceConfiguration(String dataSourceConfiguration) {
		this.dataSourceConfiguration = dataSourceConfiguration;
	}

	@Override
	public int hashCode() {
		return Objects.hash(additionalDataSourceId, dataSourceConfiguration, driverClassName);
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
		DataSourceVO other = (DataSourceVO) obj;
		return Objects.equals(additionalDataSourceId, other.additionalDataSourceId)
				&& Objects.equals(dataSourceConfiguration, other.dataSourceConfiguration)
				&& Objects.equals(driverClassName, other.driverClassName);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DataSourceVO [additionalDataSourceId=").append(additionalDataSourceId).append(", driverClassName=")
				.append(driverClassName).append(", dataSourceConfiguration=").append(dataSourceConfiguration).append("]");
		return builder.toString();
	}

}
