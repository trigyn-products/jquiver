package com.trigyn.jws.dbutils.service;

import org.springframework.stereotype.Component;

@Component
public interface DownloadUploadModule<T> {

	void downloadCodeToLocal(T object) throws Exception;
	
	void uploadCodeToDB(String uploadFileName) throws Exception;
	
}
