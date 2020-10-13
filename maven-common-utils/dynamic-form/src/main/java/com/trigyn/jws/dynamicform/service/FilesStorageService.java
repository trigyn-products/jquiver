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

	public String save(MultipartFile file);

	public Map<String, Object> load(String filename);

	public void deleteAll();

	public List<FileInfo> loadAll();

}
