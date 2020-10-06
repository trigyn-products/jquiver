package com.trigyn.jws.dbutils.service;

import org.springframework.stereotype.Component;

@Component
public interface DownloadUploadModule {

	void downloadCodeToLocal(Object object) throws Exception;
	
	void uploadCodeToDB(String uploadFileName) throws Exception;
	
}
