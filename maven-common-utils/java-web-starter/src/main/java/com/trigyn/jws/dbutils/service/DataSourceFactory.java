package com.trigyn.jws.dbutils.service;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.trigyn.jws.dbutils.vo.DataSourceVO;

public final class DataSourceFactory {

	private DataSourceFactory() {
		throw new RuntimeException("Do not try to instantiate this class");
	}

	private static final Logger logger = LoggerFactory.getLogger(DataSourceFactory.class);

	private final static Map<String, BasicDataSource> dataSourceMap = new HashMap<>();

	public static BasicDataSource getDataSource(DataSourceVO a_dataSourceVO) {
		return getOrInitDataSource(a_dataSourceVO, false);
	}

	public static BasicDataSource getOrInitDataSource(DataSourceVO a_dataSourceVO, boolean forceUpdate) {
		if (CollectionUtils.isEmpty(dataSourceMap) == true
				|| dataSourceMap.containsKey(a_dataSourceVO.getAdditionalDataSourceId()) == false
				|| forceUpdate == Boolean.TRUE.booleanValue()) {
			Gson gson = new Gson();
			Map<String, String> dsConfig = gson.fromJson(a_dataSourceVO.getDataSourceConfiguration(), Map.class);
			BasicDataSource datasource = new BasicDataSource();
			datasource.setDriverClassName(a_dataSourceVO.getDriverClassName());
			datasource.setUrl(dsConfig.get("url"));
			datasource.setUsername(dsConfig.get("userName"));
			datasource.setPassword(dsConfig.get("password"));
			datasource.setTestOnBorrow(true);
//			datasource.setValidationQuery("/* ping */ SELECT 1");
			datasource.setMaxIdle(650000);

			// Apply custom datasource properties
//			DatasourcePropertyValidationResult validationResult =	DatasourcePropertyValidator.validate(
//
//			        datasource,
//			        a_dataSourceVO.getDatasourceProperties(),
//			        a_dataSourceVO.getConnectionProperties()
//
//			);

			applyDatasourceProperties(datasource, a_dataSourceVO.getDatasourceProperties());

			dataSourceMap.put(a_dataSourceVO.getAdditionalDataSourceId(), datasource);
		}
		return dataSourceMap.get(a_dataSourceVO.getAdditionalDataSourceId());
	}

	public static void clearDataSource(String a_dataSourceID) throws SQLException {
		if (dataSourceMap != null) {
			BasicDataSource ds = dataSourceMap.remove(a_dataSourceID);

			if (ds != null) {
				ds.close();
			}
		}

	}

	public static void applyDatasourceProperties(BasicDataSource datasource, String datasourcePropertiesJson) {

		if (StringUtils.isBlank(datasourcePropertiesJson)) {
			return;
		}

		Type type = new TypeToken<Map<String, String>>() {
		}.getType();

		Map<String, String> properties = new Gson().fromJson(datasourcePropertiesJson, type);

		if (properties == null) {
			return;
		}

		BeanWrapper wrapper = new BeanWrapperImpl(datasource);

		for (Map.Entry<String, String> entry : properties.entrySet()) {

			if (wrapper.isWritableProperty(entry.getKey())) {
				wrapper.setPropertyValue(entry.getKey(), entry.getValue());
			} else {
				datasource.addConnectionProperty(entry.getKey(), entry.getValue());
			}
		}
	}
}
