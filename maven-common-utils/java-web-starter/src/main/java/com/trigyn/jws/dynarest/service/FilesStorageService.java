/**
 * 
 */
package com.trigyn.jws.dynarest.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.trigyn.jws.dbutils.vo.FileInfo;

public interface FilesStorageService {

	/*
	 * This will commit/save all temp files with this association id
	 */
	void commitChanges(String fileBinId, String fileAssociationId) throws Exception;
	
	/*
	 * This is for supporting specific temp files commit.
	 * 
	 * @author: Shrinath Halki
	 */
	void commitChanges(String fileBinId, String fileAssociationId, String fileUploadTempId) throws Exception;

	void clearTempFileBin(String fileBinId, String fileAssociationId) throws Exception;

	void init();

	String save(MultipartFile file, String fileBinId, String fileAssociationId);

	Map<String, Object> load(String filename) throws Exception;

	void deleteAll();

	List<FileInfo> loadAll();

	List<FileInfo> getFileDetailsByFileUploadIds(List<String> fileUploadIdList) throws Exception;

	List<FileInfo> getFileDetailsByConfigId(String fileBinId, String fileAssociationId,
			Map<String, Object> requestParamMap) throws Exception;

	void deleteFileByFileUploadId(String fileUploadId) throws Exception;

	Map<String, Object> createRequestParam(HttpServletRequest httpServletRequest);

	Integer hasPermission(String fileBinId, String fileAssociationId, String fileUploadId, Integer queryType,
			Map<String, Object> requestParams) throws Exception;

	Map<String, Object> validateFileQueries(MultiValueMap<String, String> formData) throws Exception;

	List<Map<String, Object>> validateFilePermission(String fileBinId, String fileAssociationId,
			String fileUploadId, Map<String, Object> parameterMap) throws Exception;

	boolean chkAndDeleteFromTemp(String fileUploadId) throws Exception;

	String update(MultipartFile file, String fileUploadId) throws Exception;
	
	Boolean isExtensionSupported(String fileBinId, MultipartFile[] files);

	Boolean isMaxFileSizeExceed(String fileBinId, MultipartFile[] files);
	
	//Fix for Session Id :  This method will delete the files from jq_file_upload_temp with fileUploadTempIds
	void clearTempFileBin(String fileBinId, String fileAssociationId, String[] fileUploadTempIds) throws Exception;

	boolean checkFileExistByUploadId(String fileUploadId) throws Exception;

	String updateFileUploadTemp(MultipartFile file, String fileUploadId, String fileBinId, String fileAssociationId) throws Exception;
	
}
