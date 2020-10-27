/**
 * 
 */
package com.trigyn.jws.dynamicform.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.trigyn.jws.dbutils.vo.FileInfo;

public interface FilesStorageService {

	public void init();

	public String save(MultipartFile file, String fileConfigId);

	public Map<String, Object> load(String filename);

	public void deleteAll();

	public List<FileInfo> loadAll();

	public List<FileInfo> getFileDetailsByIds(List<String> fileIdList);

	public void deleteFileById(String fileId) throws Exception;

}
