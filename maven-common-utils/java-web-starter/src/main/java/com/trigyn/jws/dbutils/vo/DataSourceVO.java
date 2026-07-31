package com.trigyn.jws.dbutils.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class DataSourceVO implements Serializable {

	private static final long	serialVersionUID		= 1L;

	private String additionalDataSourceId = null;
	private String driverClassName = null;
	private String dataSourceConfiguration = null;
	private String datasourceProperties = null;
	private List<ConnectionPropertyVO> connectionProperties;

	public DataSourceVO() {

	}

	public DataSourceVO(String additionalDataSourceId, String driverClassName, String dataSourceConfiguration,String datasourceProperties) {
		this.additionalDataSourceId		= additionalDataSourceId;
		this.driverClassName			= driverClassName;
		this.dataSourceConfiguration	= dataSourceConfiguration;
		this.datasourceProperties = datasourceProperties;
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
	
	

	public String getDatasourceProperties() {
		return datasourceProperties;
	}

	public void setDatasourceProperties(String datasourceProperties) {
		this.datasourceProperties = datasourceProperties;
	}

	
	public List<ConnectionPropertyVO> getConnectionProperties() {
		return connectionProperties;
	}

	public void setConnectionProperties(List<ConnectionPropertyVO> connectionProperties) {
		this.connectionProperties = connectionProperties;
	}

	@Override
	public int hashCode() {
		return Objects.hash(additionalDataSourceId, dataSourceConfiguration, driverClassName,datasourceProperties);
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
				&& Objects.equals(driverClassName, other.driverClassName)
				&& Objects.equals(datasourceProperties, other.datasourceProperties);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DataSourceVO [additionalDataSourceId=").append(additionalDataSourceId).append(", driverClassName=")
				.append(driverClassName).append(", dataSourceConfiguration=").append(dataSourceConfiguration).append(", datasourceProperties=").append(datasourceProperties)
				.append("]");
		return builder.toString();
	}

}
