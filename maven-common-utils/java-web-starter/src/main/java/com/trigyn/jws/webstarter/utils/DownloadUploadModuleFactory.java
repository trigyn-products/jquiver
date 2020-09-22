package com.trigyn.jws.webstarter.utils;

import org.springframework.stereotype.Component;

@Component
public final class DownloadUploadModuleFactory {

	
	public DownloadUploadModule getModule(String moduleName){
		moduleName = moduleName.toLowerCase();
		
		if("dynarest".equalsIgnoreCase(moduleName)) {
			return new DynaRestmodule();
		}
		return null;
		
	}
}
