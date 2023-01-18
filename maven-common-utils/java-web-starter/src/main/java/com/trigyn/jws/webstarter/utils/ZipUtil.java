package com.trigyn.jws.webstarter.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ZipUtil {

	private final static Logger logger = LogManager.getLogger(ZipUtil.class);

	public static String zipDirectory(String zipSourceFolder, String zipDestinationFolder) {

		File				dir			= new File(zipSourceFolder);

		Date				date		= new Date();
		SimpleDateFormat	formatter	= new SimpleDateFormat("dd-MMM-yyyy");
		String				strDate		= formatter.format(date);
		String				zipFileName	= strDate + ".zip";

		String				zipFilePath	= zipDestinationFolder + File.separator + zipFileName;

		try {
			zipFolder(dir.toPath(), zipDestinationFolder, zipFileName);

			File directory = new File(zipSourceFolder);
			if (directory.exists()) {
				FileUtil.deleteFolder(directory);
			}

		} catch (IOException a_ioe) {
			logger.error("Error ocurred while zipping the folder.", a_ioe);
		}
		return zipFilePath;
	}
	
	public static String unzip(InputStream fis, String destDir) throws IOException {

		String targetFolder = System.getProperty("java.io.tmpdir");
		if (targetFolder.endsWith(File.separator) == false) {
			targetFolder += File.separator;
		}
		targetFolder += UUID.randomUUID().toString() + File.separator;
		new File(targetFolder).mkdirs();
//		FileInputStream	fis	= new FileInputStream(destPath.toString() + File.separator + zipFileName);
		ZipInputStream	zis	= new ZipInputStream(fis);
		ZipEntry		ze	= zis.getNextEntry();
		byte[] buffer = new byte[1024];
		while (ze != null) {
			String fileName = ze.getName();

			if (ze.isDirectory()) {
				new File(targetFolder, fileName).mkdirs();
			} else {
				
				File	newFile;
				if(fileName.contains("/")) {
					newFile = new File(targetFolder + fileName.substring(0, fileName.lastIndexOf("/")), fileName.substring(fileName.lastIndexOf("/")));
					newFile.getParentFile().mkdirs();
				} else {
					newFile		= new File(targetFolder + fileName);
				}
				FileOutputStream	fos	= new FileOutputStream(newFile);
				int					len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
			}
			zis.closeEntry();
			ze = zis.getNextEntry();
		}

		return targetFolder;
	}

	private static String zipFolder(Path source, String destPath, String zipFileName) throws IOException {

		try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destPath.toString() + File.separator + zipFileName))) {
			Files.walkFileTree(source, new SimpleFileVisitor<>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {

					if (attributes.isSymbolicLink()) {
						return FileVisitResult.CONTINUE;
					}

					try (FileInputStream fis = new FileInputStream(file.toFile())) {

						Path targetFile = source.relativize(file);
						zos.putNextEntry(new ZipEntry(targetFile.toString()));

						byte[]	buffer	= new byte[1024];
						int		len;
						while ((len = fis.read(buffer)) > 0) {
							zos.write(buffer, 0, len);
						}
						zos.closeEntry();

					} catch (IOException a_ioe) {
						logger.error("Error ocurred while zipping the folder.", a_ioe);
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) {
					System.err.printf("Unable to zip : %s%n%s%n", file, exc);
					return FileVisitResult.CONTINUE;
				}
			});
		}
		return destPath.toString() + File.separator + zipFileName;
	}

//	public static void unzip(InputStream fis, String destDir) {
//		File dir = new File(destDir);
//		if (dir.exists() == false) {
//			dir.mkdirs();
//		}
//		
//		byte[] buffer = new byte[1024];
//		try {
//			ZipInputStream	zis	= new ZipInputStream(fis);
//			ZipEntry		ze	= zis.getNextEntry();
//			while (ze != null) {
//				String parentFolder = destDir;
//				String	fileName	= ze.getName();
//				
//				//check if it's part of another folder then chage the parent folder and
//				//update the file name
//				if(File.separator == "/") {
//					if(fileName.indexOf("\\") >= 0) {
//						parentFolder += File.separator + fileName.split("\\\\")[0];
//						fileName = fileName.split("\\\\")[1];
//					}
//				} else {
//					if(fileName.indexOf("/") >= 0) {
//						String[] folders = fileName.split("/");
//						for(int i = 0; i < folders.length - 1; i++) {
//							parentFolder += File.separator + folders[i];
//						}
//						fileName = folders[folders.length-1];
//					}
//				}
//				
//				
////				//create folders in case it's not created
//				new File(parentFolder).mkdirs();
//				System.out.println("fileName :: " + fileName);
//				File	newFile		= new File(parentFolder + File.separator + fileName);
//				// create directories for sub directories in zip
//				FileOutputStream	fos	= null;
//				if(ze.isDirectory()) {
////					new File(newFile.getParent()).mkdirs();
////					fos	= new FileOutputStream(newFile.getParent());
//					newFile.mkdirs();
//				} else {
//					fos	= new FileOutputStream(newFile);
//
//					int					len;
//					while ((len = zis.read(buffer)) > 0) {
//						fos.write(buffer, 0, len);
//					}
//					fos.close();
//				}
//				
//				// close this ZipEntry
//				zis.closeEntry();
//				ze = zis.getNextEntry();
//			}
//			// close last ZipEntry
//			zis.closeEntry();
//			zis.close();
//			fis.close();
//		} catch (IOException a_ioe) {
//			logger.error("Error ocurred while zipping the folder.", a_ioe);
//		}
//
//	}
}
