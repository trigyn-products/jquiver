package com.trigyn.jws.dbutils.service;

import org.springframework.stereotype.Component;

@Component
public interface DownloadUploadModule {

	void downloadCodeToLocal() throws Exception;
	
	void uploadCodeToDB() throws Exception;
}
