package com.trigyn.jws.webstarter.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.webstarter.utils.Constant;
import com.trigyn.jws.webstarter.utils.FileUtil;
import com.trigyn.jws.webstarter.utils.ZipUtil;

@Service
@Transactional(readOnly = false)
public class ImportService {

	public void importConfig(String fileLocation) throws Exception {
		String unZipFilePath = FileUtil.generateTemporaryFilePath(Constant.IMPORTTEMPPATH);
		ZipUtil.unzip(fileLocation, unZipFilePath);
		
		
	}
	
	private void readMetaDataXML(String filePath) {
		
	}
}
