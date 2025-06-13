package com.trigyn.jws.dynarest.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dbutils.repository.DBConnection;
import com.trigyn.jws.dbutils.repository.PropertyMasterDAO;
import com.trigyn.jws.dbutils.spi.IUserDetailsService;
import com.trigyn.jws.dbutils.utils.CustomeFileStorageException;
import com.trigyn.jws.dbutils.vo.UserDetailsVO;
import com.trigyn.jws.dynamicform.utils.Constant;
import com.trigyn.jws.dynarest.entities.FileUpload;
import com.trigyn.jws.dynarest.entities.FileUploadConfig;
import com.trigyn.jws.dynarest.utils.Constants;
import com.trigyn.jws.sciptlibrary.entities.ScriptLibraryConnection;
import com.trigyn.jws.webstarter.utils.ImportExportUtility;

@Repository
public class FileUploadConfigDAO extends DBConnection {
	
	@Autowired
	private IUserDetailsService	detailsService			= null;

	@Autowired
	private  JwsDynarestDAO 	dynarestDAO 			= null;
	
	@Autowired
	private ImportExportUtility importExportUtility 			= null;
	
	@Autowired
	private PropertyMasterDAO	propertyMasterDAO	= null;

	public FileUploadConfigDAO(DataSource dataSource) {
		super(dataSource);
	}

	public List<Map<String, Object>> executeQueries(String dataSourceId, String query,
			Map<String, Object> parameterMap) {
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = updateNamedParameterJdbcTemplateDataSource(
				dataSourceId);
		return namedParameterJdbcTemplate.queryForList(query, parameterMap);
	}

	public List<FileUpload> executeSelectQuery(String dataSourceId, String query, Map<String, Object> parameterMap) {
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = updateNamedParameterJdbcTemplateDataSource(
				dataSourceId);
		return namedParameterJdbcTemplate.query(query, parameterMap,
				new BeanPropertyRowMapper<FileUpload>(FileUpload.class));
	}

	public Map<String, String> validateFileQuery(String dataSourceId, String queryString,
			Map<String, Object> parameterMap) {
		Map<String, String>			resultSetMetadataMap		= new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
		NamedParameterJdbcTemplate	namedParameterJdbcTemplate	= updateNamedParameterJdbcTemplateDataSource(
				dataSourceId);
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

	public FileUploadConfig getFileUploadConfig(String fileUploadConfigId) {
		FileUploadConfig fileUploadConfig = getCurrentSession().get(FileUploadConfig.class, fileUploadConfigId);
		if (fileUploadConfig != null)
			getCurrentSession().evict(fileUploadConfig);
		return fileUploadConfig;
	}

	@Transactional(readOnly = false)
	public void saveFileUploadConfig(FileUploadConfig fileUploadConfig) {
		if (fileUploadConfig.getFileBinId() == null || getFileUploadConfig(fileUploadConfig.getFileBinId()) == null) {
			getCurrentSession().persist(fileUploadConfig);
		} else {
			getCurrentSession().merge(fileUploadConfig);
		}
	}

	public List<String> getAllTempDeletedFileUploadId(String fileBinId, String fileAssociationId) {
		Query deleteTempFileBinQuery = getCurrentSession().createNativeQuery(
				"SELECT file_upload_id FROM jq_file_upload_temp WHERE file_association_id = :fileAssociationId AND file_bin_id=:fileBinId AND action = -1", String.class);
		deleteTempFileBinQuery.setParameter("fileAssociationId", fileAssociationId);
		deleteTempFileBinQuery.setParameter("fileBinId", fileBinId);
		return deleteTempFileBinQuery.list();
	}

	public int getAllTempFileUploadIdForInsert(String fileBinId, String fileAssociationId, String fileUploadTempId) {
		Query insertTempFileBinQuery = getCurrentSession().createNativeQuery(
				"SELECT file_upload_id FROM jq_file_upload_temp WHERE file_association_id = :fileAssociationId AND file_bin_id=:fileBinId "
				+ " AND file_upload_temp_id=:fileUploadTempId AND action = 1", String.class);
		insertTempFileBinQuery.setParameter("fileAssociationId", fileAssociationId);
		insertTempFileBinQuery.setParameter("fileBinId", fileBinId);
		insertTempFileBinQuery.setParameter("fileUploadTempId", fileUploadTempId);
		if (null !=insertTempFileBinQuery.list())
			return insertTempFileBinQuery.list().size();
		else
			return 0;
	}

	
	public Map<String, List<Object[]>> commitChanges(String fileBinId, String fileAssociationId,
            String fileUploadTempId)throws Exception {
		
		FileUploadConfig fileUploadConfig = getFileUploadConfig(fileBinId);
        /**
         * This section is to handle the files which are not to be updated in
         * file_upload table. That is, if we have uploaded anything into temporary table
         * and deleted it from that table, before saving.
         */
        Query deleteTempFileBinQuery = getCurrentSession()
                .createNativeQuery("DELETE FROM jq_file_upload_temp WHERE file_upload_temp_id IN "
                        + " (SELECT file_upload_temp_id FROM jq_file_upload_temp WHERE file_association_id = :fileAssociationId "
                        + " AND file_bin_id=:fileBinId AND action = -1)" + " AND action = 1", Map.class);
        deleteTempFileBinQuery.setParameter("fileAssociationId", fileAssociationId);
        deleteTempFileBinQuery.setParameter("fileBinId", fileBinId);
        deleteTempFileBinQuery.executeUpdate();

        /*
         * written the query for getting the action as DELETE for implementing Activity
         * Log
         */
        Query selectTempFileBinQuery = getCurrentSession().createNativeQuery("SELECT file_bin_id,original_file_name "
                + " FROM jq_file_upload_temp WHERE file_association_id = :fileAssociationId  AND file_bin_id=:fileBinId AND action = -1", Map.class);
        selectTempFileBinQuery.setParameter("fileAssociationId", fileAssociationId);
        selectTempFileBinQuery.setParameter("fileBinId", fileBinId);
        Map<String, List<Object[]>> returnResult = new HashMap<>();
        List<Object[]> delResult = (List<Object[]>) selectTempFileBinQuery.getResultList();
        returnResult.put("DELETE", delResult);

        /*
         * written the query for getting the action as UPLOAD for implementing Activity
         * Log
         */
        Query selectFileBinQuery = getCurrentSession().createNativeQuery("SELECT file_bin_id,original_file_name "
                + " FROM jq_file_upload_temp WHERE file_association_id = :fileAssociationId AND file_bin_id=:fileBinId AND action = 1", Object[].class);
        selectFileBinQuery.setParameter("fileAssociationId", fileAssociationId);
        selectFileBinQuery.setParameter("fileBinId", fileBinId);
        List<Object[]> insResult = (List<Object[]>) selectFileBinQuery.getResultList();
        returnResult.put("INSERT", insResult);

        /**
         * This section is to handle the files which are to be deleted from remote
         * location
         */
		List<String> counts = getAllTempDeletedFileUploadId(fileBinId, fileAssociationId);
		if (null != counts && 1 <= counts.size()) {
			if (("1").equalsIgnoreCase(fileUploadConfig.getIsFileStorageEnable().toString()) == true) {
				Class<?> serviceClass = Class.forName(fileUploadConfig.getCustomFileStorageClass(), Boolean.TRUE,
						this.getClass().getClassLoader());
				Object classInstance = serviceClass.getDeclaredConstructor().newInstance();
				try {
					((ICustomFileStorage) classInstance).deleteFileById(fileUploadTempId);
				} catch (Exception e) {
					throw new CustomeFileStorageException(e.getMessage());
				}
			}
		}
        /**
         * This section is to handle the files which are to be deleted from file_upload
         * table
         */
        String deleteQuery ="DELETE FROM jq_file_upload WHERE file_upload_id IN "
                + " (SELECT file_upload_id FROM jq_file_upload_temp WHERE file_association_id = :fileAssociationId "
                + " AND file_bin_id=:fileBinId AND action = -1";
        if(fileUploadTempId != null) {
            deleteQuery += " AND file_upload_id=:fileUploadTempId ";
        }
        deleteQuery += " ) ";
        MutationQuery deleteFileBinQuery = getCurrentSession().createNativeMutationQuery(deleteQuery);
        deleteFileBinQuery.setParameter("fileAssociationId", fileAssociationId);
        deleteFileBinQuery.setParameter("fileBinId", fileBinId);
        if(fileUploadTempId != null) {
            deleteFileBinQuery.setParameter("fileUploadTempId", fileUploadTempId);
        }
        deleteFileBinQuery.executeUpdate();

        String filePath = "";
		String insertQuery = "";
		int insertCount=getAllTempFileUploadIdForInsert(fileBinId, fileAssociationId, fileUploadTempId);
		
		if (0<insertCount &&("1").equalsIgnoreCase(fileUploadConfig.getIsFileStorageEnable().toString()) == true) {
			Class<?> serviceClass = Class.forName(fileUploadConfig.getCustomFileStorageClass(), Boolean.TRUE,
					this.getClass().getClassLoader());
			Object classInstance = serviceClass.getDeclaredConstructor().newInstance();
			try {
				filePath = ((ICustomFileStorage) classInstance).uploadFile(fileBinId, fileAssociationId,
						fileUploadTempId);
			} catch (Exception e) {
				throw new CustomeFileStorageException(e.getMessage());
			}
			 /**
             * This section is to handle the files which are to be inserted from file_upload
             * table
             */
			insertQuery = 
		                "INSERT INTO jq_file_upload (file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) "
		                        + " (SELECT file_upload_id, file_bin_id, '"+filePath+"', original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id"
		                        + " FROM jq_file_upload_temp"
		                        + " WHERE file_association_id = :fileAssociationId AND file_bin_id=:fileBinId";
			
        }
        else {
        	 /**
             * This section is to handle the files which are to be inserted from file_upload
             * table
             */
             insertQuery = 
                    "INSERT INTO jq_file_upload (file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id) "
                            + " (SELECT file_upload_id, file_bin_id, file_path, original_file_name, physical_file_name, updated_by, last_update_ts, file_association_id"
                            + " FROM jq_file_upload_temp"
                            + " WHERE file_association_id = :fileAssociationId AND file_bin_id=:fileBinId";
        }
        
        if(fileUploadTempId != null) {
            insertQuery += " AND file_upload_temp_id=:fileUploadTempId ";
        }
        insertQuery += " AND action = 1) ";
        MutationQuery addFileBinQuery = getCurrentSession().createNativeMutationQuery(insertQuery);
        addFileBinQuery.setParameter("fileAssociationId", fileAssociationId);
        addFileBinQuery.setParameter("fileBinId", fileBinId);
        if(fileUploadTempId != null) {
            addFileBinQuery.setParameter("fileUploadTempId", fileUploadTempId);
        }
        addFileBinQuery.executeUpdate();

        return returnResult;
    }

	public void clearTempFileBin(String fileBinId, String fileAssociationId, String fileUploadId) {
		String queryStr = "DELETE FROM jq_file_upload_temp WHERE file_upload_temp_id IN "
				+ " (SELECT file_upload_temp_id FROM jq_file_upload_temp WHERE file_association_id = :fileAssociationId AND file_bin_id=:fileBinId ";

		if(fileUploadId != null) {
			queryStr += " AND (file_upload_id = :fileUploadId OR file_upload_temp_id = :fileUploadId )";
		}
		queryStr += " )";
		MutationQuery query = getCurrentSession().createNativeMutationQuery(queryStr);
				
		query.setParameter("fileAssociationId", fileAssociationId);
		query.setParameter("fileBinId", fileBinId);
		if(fileUploadId != null) {
			query.setParameter("fileUploadId", fileUploadId);
		}
		query.executeUpdate();

	}
	
	public final List<Object> scriptLibExecution(String fileBinId) {
		Query	scriptLibQuery	= getCurrentSession().createQuery("SELECT jqsl.`template_id` "
				+ "FROM `jq_script_lib_connect` jqs LEFT JOIN `jq_file_upload_config` jqf "
				+ "ON jqf.`file_bin_id` = SUBSTRING_INDEX(jqs.entity_id , '_',-1) "
				+ "LEFT JOIN `jq_script_lib_details` jqsl "
				+ "ON jqs.`script_lib_id` = jqsl.`script_lib_id` "
				+ "WHERE jqs.`module_type_id` = :moduleId AND jqs.entity_id = :fileBinId", Object[].class);
		
		scriptLibQuery.setParameter("moduleId", Constants.FILE_BIN_MOD_ID);
		scriptLibQuery.setParameter("fileBinId", fileBinId);
		
		List<Object[]> scriptLibList = scriptLibQuery.list();
		List<Object>		resultMap	= new ArrayList<>();
		
		for(int iCounter=0;iCounter<scriptLibList.size();iCounter++){
			Query roleQuery = getCurrentSession().createQuery("SELECT COUNT(*) FROM `jq_entity_role_association` WHERE `role_id` = :roleId"
					+ " AND `is_active` = :isActive AND entity_id = :entityId", Object[].class);
			roleQuery.setParameter("roleId",   Constant.ANONYMOUS_ROLE_ID);
			roleQuery.setParameter("isActive", Constant.IS_ACTIVE);
			roleQuery.setParameter("entityId", scriptLibList.get(iCounter));
			List<Object[]> roleCount = roleQuery.list();
			if(roleQuery.list().get(0).toString().equalsIgnoreCase("0")) {
				Query templateQuery = getCurrentSession().createQuery("SELECT template FROM jq_template_master WHERE template_id = :templateId ", Object[].class);
				templateQuery.setParameter("templateId", scriptLibList.get(iCounter));
				List<Object[]> listTemplate = templateQuery.list();
				resultMap.add(listTemplate.get(0));
			}
		}
		return resultMap;
	}
	
	public List<String> getFileBinScriptLibId(String entityId) {
		Query querySQL = getCurrentSession().createNativeQuery("SELECT script_lib_id FROM jq_script_lib_connect WHERE entity_id = :entityId", String.class);
		querySQL.setParameter("entityId", entityId);
		List<String> scriptLibIdList = querySQL.list();
		return scriptLibIdList;
	}
	
	public void scriptLibSave(List<String> formSaveQueryIdList,List<String> scriptLibInsertList,List<ScriptLibraryConnection>	scriptLibInsert,String moduleId,Integer sourceTypeId) throws Exception { 
		UserDetailsVO detailsVO = detailsService.getUserDetails();
		if(null != scriptLibInsertList && scriptLibInsertList.size() != 0 && scriptLibInsertList.isEmpty() == false) {
			for(int iScrInsertCounter=0; iScrInsertCounter<scriptLibInsertList.size(); iScrInsertCounter++) {
				String[] scriptLibId = scriptLibInsertList.get(iScrInsertCounter).split(",");
				if(null != scriptLibId && scriptLibId.length != 0) {
					for(int iscrLibIdCount = 0;iscrLibIdCount<scriptLibId.length;iscrLibIdCount++) {
						ScriptLibraryConnection scriptlibrary = new ScriptLibraryConnection();
						String scriptLibID = scriptLibId[iscrLibIdCount];
						if(sourceTypeId.equals(Constant.IMPORT_SOURCE_VERSION_TYPE)){// == Constant.IMPORT_SOURCE_VERSION_TYPE) {
							dynarestDAO.scriptLibDeleteById(formSaveQueryIdList.get(0));
						}
						if(scriptLibID.isEmpty() == false) {
							
					//		scriptlibrary.setScriptlibconnId(UUID.randomUUID().toString());
							scriptlibrary.setScriptLibId(scriptLibID);
							scriptlibrary.setModuletypeId(moduleId);
							scriptlibrary.setEntityId(formSaveQueryIdList.get(0));
							scriptlibrary.setCreatedBy(detailsVO.getUserName());
							scriptlibrary.setUpdatedBy(detailsVO.getUserName());
							scriptlibrary.setUpdatedDate(new Date());
							scriptlibrary.setIsCustomUpdated(1);
							scriptLibInsert.add(scriptlibrary);
							getCurrentSession().merge(scriptlibrary);
						}
					}
				}
			}
		}
	}
	
}
