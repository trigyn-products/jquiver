package com.trigyn.jws.dbutils.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class FileUtilities {

	private final static Logger	logger	= LogManager.getLogger(FileUtilities.class);

	private static final String	SHA_256	= "SHA-256";

	public String generateFileChecksum(File file) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance(SHA_256);
		try (DigestInputStream stream = new DigestInputStream(new FileInputStream(file), messageDigest)) {
			// empty loop to clear the data
			while (stream.read() != -1)
				;
			messageDigest = stream.getMessageDigest();
			StringBuilder result = new StringBuilder();
			for (byte bytes : messageDigest.digest()) {
				result.append(String.format("%02x", bytes));
			}
			return result.toString();
		} catch (Exception a_exc) {
			logger.error(a_exc);
			return null;
		}
	}

	public String readContentsOfFile(String filePath) throws Exception {
		StringBuilder contentBuilder = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
			stream.forEach(s -> contentBuilder.append(s).append(System.lineSeparator()));
		} catch (IOException a_excep) {
			logger.error("Error while reading contents of file ", a_excep);
			throw new Exception("Error while reading contents of file ", a_excep);
		}
		return contentBuilder.toString();
	}

	public String checkFileContents(String queryName, String formFolder, String content, String existingCheckSum,
			String ftlCustomExtension) throws Exception {
		String	newGeneratedCheckSum	= null;
		File	file					= new File(formFolder + File.separator + queryName + ftlCustomExtension);

		if (file.exists()) {
			String generatedFileCheckSum = generateFileChecksum(file);
			if (!generatedFileCheckSum.equalsIgnoreCase(existingCheckSum)) {
				file.delete();
				newGeneratedCheckSum = writeFileContents(content, file);
			}
		} else {
			newGeneratedCheckSum = writeFileContents(content, file);
		}
		return newGeneratedCheckSum;
	}

	public String writeFileContents(String content, File file) throws Exception {

		String generateFileCheckSum = null;
		file.createNewFile();
		try (FileWriter fileWriter = new FileWriter(file)) {
			fileWriter.write(content);
			fileWriter.flush();
			generateFileCheckSum = generateFileChecksum(file);
		} catch (IOException a_excep) {
			logger.error("Error while writing contents to file ", a_excep);
			throw new Exception("Error while writing contents to file ", a_excep);
		}
		return generateFileCheckSum;

	}

	public String generateChecksum(Object content) throws Exception {
		MessageDigest	messageDigest	= MessageDigest.getInstance(SHA_256);
		String			stringContent	= content.toString();

		messageDigest.update(stringContent.getBytes());
		StringBuilder checksum = new StringBuilder();
		for (byte bytes : messageDigest.digest()) {
			checksum.append(String.format("%02x", bytes));
		}
		return checksum.toString();
	}

}
