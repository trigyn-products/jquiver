package com.trigyn.jws.dynamicform.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CryptoUtils {
	private static final Logger	logger			= LogManager.getLogger(CryptoUtils.class);

	private static final String	ALGORITHM		= "AES";
	private static final String	TRANSFORMATION	= "AES";

	public static void encrypt(String key, File inputFile, File outputFile) throws CryptoException {
		doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
	}

	public static byte[] decrypt(String key, File inputFile, File outputFile) throws CryptoException {
		if (outputFile != null) {
			doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
			return null;
		} else {
			return doCrypto(Cipher.DECRYPT_MODE, key, inputFile);
		}
	}

	public static byte[] decrypt(String key, byte[] fileByteArray, File outputFile) throws CryptoException {
		return doCrypto(Cipher.DECRYPT_MODE, key, fileByteArray);
	}

	private static byte[] doCrypto(int cipherMode, String key, File inputFile) throws CryptoException {
		try {
			Key		secretKey	= new SecretKeySpec(key.getBytes(), ALGORITHM);
			Cipher	cipher		= Cipher.getInstance(TRANSFORMATION);
			cipher.init(cipherMode, secretKey);

			FileInputStream	inputStream	= new FileInputStream(inputFile);
			byte[]			inputBytes	= new byte[(int) inputFile.length()];
			inputStream.read(inputBytes);

			byte[] outputBytes = cipher.doFinal(inputBytes);
			inputStream.close();
			return outputBytes;

		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | IOException a_ex) {
			logger.error("Error encrypting/decrypting file", a_ex);
			throw new CryptoException("Error encrypting/decrypting file", a_ex);
		}
	}

	private static void doCrypto(int cipherMode, String key, File inputFile, File outputFile) throws CryptoException {
		try {
			Key		secretKey	= new SecretKeySpec(key.getBytes(), ALGORITHM);
			Cipher	cipher		= Cipher.getInstance(TRANSFORMATION);
			cipher.init(cipherMode, secretKey);

			FileInputStream	inputStream	= new FileInputStream(inputFile);
			byte[]			inputBytes	= new byte[(int) inputFile.length()];
			inputStream.read(inputBytes);

			byte[]				outputBytes		= cipher.doFinal(inputBytes);

			FileOutputStream	outputStream	= new FileOutputStream(outputFile);
			outputStream.write(outputBytes);

			inputStream.close();
			outputStream.close();

		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | IOException a_ex) {
			logger.error("Error encrypting/decrypting file", a_ex);
			throw new CryptoException("Error encrypting/decrypting file", a_ex);
		}
	}

	private static byte[] doCrypto(int cipherMode, String key, byte[] fileByteArray) throws CryptoException {
		try {
			Key		secretKey	= new SecretKeySpec(key.getBytes(), ALGORITHM);
			Cipher	cipher		= Cipher.getInstance(TRANSFORMATION);
			cipher.init(cipherMode, secretKey);

			byte[] outputBytes = cipher.doFinal(fileByteArray);
			return outputBytes;

		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException a_ex) {
			logger.error("Error encrypting/decrypting file", a_ex);
			throw new CryptoException("Error encrypting/decrypting file", a_ex);
		}
	}
	
	public static String decrypt(String key, String encryptedString) throws CryptoException {
		try {
			Key		secretKey	= new SecretKeySpec(key.getBytes(), ALGORITHM);
			Cipher	cipher		= Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] encryptedText = Base64.decodeBase64(encryptedString);
			byte[] outputBytes = cipher.doFinal(encryptedText);
			return new String(outputBytes);

		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException a_ex) {
			logger.error("Error encrypting/decrypting text", a_ex);
			throw new CryptoException("Error encrypting/decrypting text", a_ex);
		}
	}
	
	public static String encrypt(String key, String text) throws CryptoException {
		try {
			String encryptedString = null;
			Key		secretKey	= new SecretKeySpec(key.getBytes(), ALGORITHM);
			Cipher	cipher		= Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

			byte[] outputBytes = cipher.doFinal(text.getBytes());
			 encryptedString = new String(Base64.encodeBase64(outputBytes));
			return encryptedString;

		} catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException a_ex) {
			logger.error("Error encrypting/decrypting text", a_ex);
			throw new CryptoException("Error encrypting/decrypting text", a_ex);
		}
	}

}
