package com.trigyn.jws.dbutils.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;
import com.trigyn.jws.dbutils.vo.DataSourceVO;

public final class DataSourceFactory {

	private DataSourceFactory() {
		throw new RuntimeException("Do not try to instantiate this class");
	}

	private final static Map<String, BasicDataSource> dataSourceMap = new HashMap<>();

	public static BasicDataSource getDataSource(DataSourceVO a_dataSourceVO) {
		return getOrInitDataSource(a_dataSourceVO, false);
	}

	public static BasicDataSource getOrInitDataSource(DataSourceVO a_dataSourceVO, boolean forceUpdate) {
		if (CollectionUtils.isEmpty(dataSourceMap) == true || dataSourceMap.containsKey(a_dataSourceVO.getAdditionalDataSourceId()) == false
				|| forceUpdate == Boolean.TRUE.booleanValue()) {
			Gson				gson		= new Gson();
			Map<String, String>	dsConfig	= gson.fromJson(a_dataSourceVO.getDataSourceConfiguration(), Map.class);
			BasicDataSource		datasource	= new BasicDataSource();
			datasource.setDriverClassName(a_dataSourceVO.getDriverClassName());
			datasource.setUrl(dsConfig.get("url"));
			datasource.setUsername(dsConfig.get("userName"));
			datasource.setPassword(dsConfig.get("password"));
			dataSourceMap.put(a_dataSourceVO.getAdditionalDataSourceId(), datasource);
		}
		return dataSourceMap.get(a_dataSourceVO.getAdditionalDataSourceId());
	}

}
