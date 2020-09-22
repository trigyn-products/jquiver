package com.trigyn.jws.resourcebundle.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.resourcebundle.vo.LanguageVO;
import com.trigyn.jws.resourcebundle.vo.ResourceBundleVO;

@Repository
public class ResourceBundleDAO extends DBConnection {

    @Autowired
    public ResourceBundleDAO(DataSource dataSource) {
        super(dataSource);
    }

    
    
    public void deleteResourceEntry(ResourceBundleVO dbresource, LanguageVO languageVO) throws Exception {
        
        List<LanguageVO> languageVOList = getLanguageIdByLanguageName(languageVO);
		for (LanguageVO language : languageVOList) {
			dbresource.setLanguageId(language.getLanguageId());
		}
		jdbcTemplate.update(QueryStore.SQL_QUERY_TO_DELETE_RESOURCE_BUNDLE,
				new Object[] { dbresource.getText(), dbresource.getLanguageId(), dbresource.getResourceKey() });
	}

	
    
    
    public void saveOrUpdateRecord(ResourceBundleVO dbresource)throws Exception {
		
		List<ResourceBundleVO> listDbresource = jdbcTemplate.query(QueryStore.SQL_QUERY_TO_GET_MESSAGE_DETAILS,
				new Object[] { dbresource.getLanguageId(), dbresource.getResourceKey() },
				new RowMapper<ResourceBundleVO>() {
					public ResourceBundleVO mapRow(ResultSet rs, int rowNum) throws SQLException {
						ResourceBundleVO resource = new ResourceBundleVO();
						resource.setLanguageId(rs.getInt("languageId"));
						resource.setResourceKey(rs.getString("resourceKey"));
						resource.setText(rs.getString("text"));
						return resource;
					}
				});
		if (listDbresource.isEmpty()) {
			jdbcTemplate.update(QueryStore.SQL_QUERY_TO_INSERT_RESOURCE_BUNDLE, dbresource.getLanguageId(), dbresource.getResourceKey(),
					dbresource.getText());
		} else {
			jdbcTemplate.update(QueryStore.SQL_QUERY_TO_UPDATE_RESOURCE_BUNDLE,
					new Object[] { dbresource.getText(), dbresource.getResourceKey(), dbresource.getLanguageId() });
		}

	}

	
    
    
    private List<LanguageVO> getLanguageIdByLanguageName(LanguageVO languageVO)throws Exception {

		List<LanguageVO> listDbresource = jdbcTemplate.query(QueryStore.SQL_QUERY_TO_GET_LANGAUGE_ID_BY_NAME, new Object[] { languageVO.getLanguageName() },
				new RowMapper<LanguageVO>() {
					public LanguageVO mapRow(ResultSet rs, int rowNum) throws SQLException {
						LanguageVO resource = new LanguageVO();
						resource.setLanguageId(rs.getInt("languageId"));
						return resource;
					}
				});
		return listDbresource;
	}

	
	
    
    public List<ResourceBundleVO> checkResourceData(String resKey,String langId) throws Exception {
			
		List<ResourceBundleVO> listDbresource = jdbcTemplate.query(QueryStore.SQL_QUERY_TO_GET_MESSAGE_DETAILS,
			new Object[] { resKey,langId },
			new RowMapper<ResourceBundleVO>() {
				public ResourceBundleVO mapRow(ResultSet rs, int rowNum) throws SQLException {
					ResourceBundleVO resource = new ResourceBundleVO();
					resource.setResourceKey(rs.getString("resourceKey"));
					resource.setLanguageId(rs.getInt("languageId"));
					return resource;
				}
            });
		
		return  listDbresource;
		
	}

    
    
    public List<ResourceBundleVO> getLanguageIdAndText(String resourceKey) throws Exception {
        Map<String,String> namedParameters = new HashMap<String, String>();
        namedParameters.put("resourceKey", resourceKey);
        List<ResourceBundleVO> dataList = namedParameterJdbcTemplate.query(QueryStore.SQL_QUERY_TO_GET_MESSAGE_DETAILS_BY_RESOURCE_KEY, namedParameters,new BeanPropertyRowMapper<ResourceBundleVO>(ResourceBundleVO.class));
        return dataList;
	
    }
}