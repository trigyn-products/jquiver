package com.trigyn.jws.dbutils.service;

import org.springframework.stereotype.Component;

@Component
public interface DownloadUploadModule<T> {

	void downloadCodeToLocal(T object, String folderLocation) throws Exception;
	
	void uploadCodeToDB(String uploadFileName) throws Exception;

	Object importData(String folderLocation, String uploadFileName, String uploadID) throws Exception;
}
