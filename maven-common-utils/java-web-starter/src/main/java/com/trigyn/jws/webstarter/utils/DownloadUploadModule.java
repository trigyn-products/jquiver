package com.trigyn.jws.webstarter.utils;

import org.springframework.stereotype.Component;

@Component
public interface DownloadUploadModule {

	void downloadCodeToLocal() throws Exception;
	
	void uploadCodeToLocal() throws Exception;
}
