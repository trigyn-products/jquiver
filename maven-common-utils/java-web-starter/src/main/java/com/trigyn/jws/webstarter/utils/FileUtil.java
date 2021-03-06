package com.trigyn.jws.webstarter.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;

public class FileUtil {

	public static String generateTemporaryFilePath(String tempFolderName) {

		String	systemPath		= System.getProperty("user.dir");
		String	tempFilePath	= systemPath + File.separator + tempFolderName;

		File	f				= new File(tempFilePath);
		if (f.exists() && f.isDirectory()) {
			f.delete();
		}
		new File(tempFilePath).mkdir();

		return tempFilePath;
	}

	public static void downloadFile(HttpServletRequest request, HttpServletResponse response, String zipFilePath)
			throws IOException {
		File		zipFile		= new File(zipFilePath);
		InputStream	inputStream	= new FileInputStream(zipFile);
		response.setContentType("application/force-download");
		response.setHeader("Content-disposition", "attachment; filename=" + zipFile.getName());
		IOUtils.copy(inputStream, response.getOutputStream());
		response.flushBuffer();
		inputStream.close();

		File directory = new File(zipFilePath);
		if (directory.exists()) {
			deleteFolder(directory);
		}

	}

	public static void deleteFolder(File file) {
		if (file.list() == null) {
			file.delete();
		} else {
			String files[] = file.list();
			for (String temp : files) {
				File fileDelete = new File(file, temp);
				deleteFolder(fileDelete);
			}
			if (file.list().length == 0) {
				file.delete();
			}
		}
	}

}
