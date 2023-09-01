package com.trigyn.jws.dbutils.cipher.utils;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import  org.apache.commons.codec.binary.Base64;

public class DESCipherUtil implements ICipherUtil {

	private static final String	UNICODE_FORMAT				= "UTF8";
	public static final String	DESEDE_ENCRYPTION_SCHEME	= "DESede";
	private KeySpec				ks;
	private SecretKeyFactory	skf;
	private Cipher				cipher;
	byte[]						arrayBytes;
	private String				myEncryptionScheme;
	SecretKey					key;

	private void setKey(String secretKey) throws Exception {
		// myEncryptionKey = "ThisIsSecretKeyThisIsSecretKey";
		myEncryptionScheme	= DESEDE_ENCRYPTION_SCHEME;
		arrayBytes			= secretKey.getBytes(UNICODE_FORMAT);
		ks					= new DESedeKeySpec(arrayBytes);
		skf					= SecretKeyFactory.getInstance(myEncryptionScheme);
		cipher				= Cipher.getInstance(myEncryptionScheme);
		key					= skf.generateSecret(ks);
	}

	public String encrypt(String unencryptedString, String secretKey) throws Exception {
		String encryptedString = null;
			setKey(secretKey);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[]	plainText		= unencryptedString.getBytes(UNICODE_FORMAT);
			byte[]	encryptedText	= cipher.doFinal(plainText);
			encryptedString = new String(Base64.encodeBase64(encryptedText));
		return encryptedString;
	}

	public String decrypt(String encryptedString, String secretKey) throws Exception {
		String decryptedText = null;
			setKey(secretKey);
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[]	encryptedText	= Base64.decodeBase64(encryptedString);
			byte[]	plainText		= cipher.doFinal(encryptedText);
			decryptedText = new String(plainText);
		return decryptedText;
	}

//	public static void main(String args[]) throws Exception {
//		DESCipherUtil	td			= new DESCipherUtil();
//
//		String			target		= "imparator";
//		String			encrypted	= td.encrypt(target);
//		String			decrypted	= td.decrypt(encrypted);
//
//		System.out.println("String To Encrypt: " + target);
//		System.out.println("Encrypted String:" + encrypted);
//		System.out.println("Decrypted String:" + decrypted);
//
//	}
}
