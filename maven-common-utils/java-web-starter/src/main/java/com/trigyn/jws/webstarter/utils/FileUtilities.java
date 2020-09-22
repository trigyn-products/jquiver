package com.trigyn.jws.webstarter.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileUtilities {

	private final static Logger logger = LogManager.getLogger(FileUtilities.class);
	
    
    private static final String SHA_256 = "SHA-256";

    public static String generateFileChecksum(File file) throws NoSuchAlgorithmException {
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
        } catch (Exception e) {
            return null;
        }
    }

}
