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
	public void commitChanges(String fileBinId, String fileAssociationId) throws Exception;
	
	/*
	 * This is for supporting specific temp files commit.
	 * 
	 * @author: Shrinath Halki
	 */
	public void commitChanges(String fileBinId, String fileAssociationId, String fileUploadTempId) throws Exception;

	public void clearTempFileBin(String fileBinId, String fileAssociationId) throws Exception;

	public void init();

	public String save(MultipartFile file, String fileBinId, String fileAssociationId);

	public Map<String, Object> load(String filename);

	public void deleteAll();

	public List<FileInfo> loadAll();

	public List<FileInfo> getFileDetailsByFileUploadIds(List<String> fileUploadIdList) throws Exception;

	public List<FileInfo> getFileDetailsByConfigId(String fileBinId, String fileAssociationId,
			Map<String, Object> requestParamMap) throws Exception;

	public void deleteFileByFileUploadId(String fileUploadId) throws Exception;

	public Map<String, Object> createRequestParam(HttpServletRequest httpServletRequest);

	public Integer hasPermission(String fileBinId, String fileAssociationId, String fileUploadId, Integer queryType,
			Map<String, Object> requestParams) throws Exception;

	public Map<String, Object> validateFileQueries(MultiValueMap<String, String> formData) throws Exception;

	public List<Map<String, Object>> validateFilePermission(String fileBinId, String fileAssociationId,
			String fileUploadId, Map<String, Object> parameterMap) throws Exception;

	public boolean chkAndDeleteFromTemp(String fileUploadId) throws Exception;

	public String update(MultipartFile file, String fileUploadId) throws Exception;
	
	public Boolean isExtensionSupported(String fileBinId, MultipartFile[] files);

	public Boolean isMaxFileSizeExceed(String fileBinId, MultipartFile[] files);
	
	//Fix for Session Id :  This method will delete the files from jq_file_upload_temp with fileUploadTempIds
	public void clearTempFileBin(String fileBinId, String fileAssociationId, String[] fileUploadTempIds) throws Exception;

}
