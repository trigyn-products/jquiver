/**
 * 
 */
package com.trigyn.jws.dynamicform.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.trigyn.jws.dbutils.vo.FileInfo;

public interface FilesStorageService {

	public void init();

	public String save(MultipartFile file, String fileBinId, String fileAssociationId);

	public Map<String, Object> load(String filename);

	public void deleteAll();

	public List<FileInfo> loadAll();

	public List<FileInfo> getFileDetailsByFileUploadIds(List<String> fileUploadIdList);

	public List<FileInfo> getFileDetailsByConfigId(String fileBinId, String fileAssociationId,
			Map<String, Object> requestParamMap) throws Exception;

	public void deleteFileByFileUploadId(String fileUploadId) throws Exception;

	public Map<String, Object> createRequestParam(HttpServletRequest httpServletRequest);

	public Integer hasPermission(String fileBinId, String fileAssociationId, String fileUploadId,
			Integer queryType, HttpServletRequest httpServletRequest) throws Exception;

	public Map<String, Object> validateFileQueries(MultiValueMap<String, String> formData) throws Exception;

	public List<Map<String, Object>> validateFilePermission(String fileBinId, String fileAssociationId,
			String fileUploadId, Map<String, Object> parameterMap) throws Exception;

}
