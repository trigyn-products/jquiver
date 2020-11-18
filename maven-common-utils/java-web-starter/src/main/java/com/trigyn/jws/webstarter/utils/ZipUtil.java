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
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

	public static String zipDirectory(String zipSourceFolder, String zipDestinationFolder) {

    	File dir = new File(zipSourceFolder);
    	
    	Date date = new Date();  
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");  
        String strDate= formatter.format(date); 
    	String zipFileName = strDate + ".zip";
    	
    	String zipFilePath = zipDestinationFolder + File.separator + zipFileName;

		try {
			zipFolder(dir.toPath(), zipFileName);

			File directory = new File(zipSourceFolder);
			if (directory.exists()) {
				FileUtil.deleteFolder(directory);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return zipFilePath;
    }
	
	private static String zipFolder(Path source,String zipFileName ) throws IOException {
		
		try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFileName))) {
			Files.walkFileTree(source, new SimpleFileVisitor<>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {

					if (attributes.isSymbolicLink()) {
						return FileVisitResult.CONTINUE;
					}

					try (FileInputStream fis = new FileInputStream(file.toFile())) {

						Path targetFile = source.relativize(file);
						zos.putNextEntry(new ZipEntry(targetFile.toString()));

						byte[] buffer = new byte[1024];
						int len;
						while ((len = fis.read(buffer)) > 0) {
							zos.write(buffer, 0, len);
						}
						zos.closeEntry();

					} catch (IOException e) {
						e.printStackTrace();
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
		return source.toString() + File.separator + zipFileName;
	}

	public static void unzip(InputStream fis, String destDir) {
        File dir = new File(destDir);
        if(!dir.exists()) 
        	dir.mkdirs();
        byte[] buffer = new byte[1024];
        try {
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				String fileName = ze.getName();
				File newFile = new File(destDir + File.separator + fileName);
				// create directories for sub directories in zip
				new File(newFile.getParent()).mkdirs();
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				// close this ZipEntry
				zis.closeEntry();
				ze = zis.getNextEntry();
			}
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
