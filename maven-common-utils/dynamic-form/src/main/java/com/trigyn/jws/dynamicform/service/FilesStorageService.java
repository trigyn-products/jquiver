/**
 * 
 */
package com.trigyn.jws.dynamicform.service;

import java.io.File;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {

	public void init();

	public String save(MultipartFile file);

	public File load(String filename);

	public void deleteAll();

	public Stream<Path> loadAll();

}
