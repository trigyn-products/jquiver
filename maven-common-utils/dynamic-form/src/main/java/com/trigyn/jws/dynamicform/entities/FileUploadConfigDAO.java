package com.trigyn.jws.dynamicform.entities;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.repository.DBConnection;

@Repository
public class FileUploadConfigDAO extends DBConnection {

	public FileUploadConfigDAO(DataSource dataSource) {
		super(dataSource);
	}

	public List<Map<String, Object>> executeQueries(String query, Map<String, Object> parameterMap) {
		return namedParameterJdbcTemplate.queryForList(query, parameterMap);
	}

	public List<FileUpload> executeSelectQuery(String query, Map<String, Object> parameterMap) {
		return namedParameterJdbcTemplate.query(query, parameterMap,
				new BeanPropertyRowMapper<FileUpload>(FileUpload.class));
	}

	public Map<String, String> validateFileQuery(String queryString, Map<String, Object> parameterMap) {
		Map<String, String> resultSetMetadataMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		namedParameterJdbcTemplate.query(queryString, parameterMap, new ResultSetExtractor<Integer>() {

			@Override
			public Integer extractData(ResultSet resultSet) throws SQLException, DataAccessException {

				ResultSetMetaData	resultSetMetadata	= resultSet.getMetaData();
				int					columnCount			= resultSetMetadata.getColumnCount();
				for (int columnCounter = 1; columnCounter <= columnCount; columnCounter++) {
					resultSetMetadataMap.put(resultSetMetadata.getColumnLabel(columnCounter),
							resultSetMetadata.getColumnTypeName(columnCounter));
				}
				return columnCount;

			}

		});
		return resultSetMetadataMap;

	}

}
