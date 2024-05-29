package com.trigyn.jws.dbutils.cipher.utils;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base64;

public class CipherUtil {
	
	public static PublicKey getPublicKey(String base64PublicKey) {
		PublicKey publicKey = null;
		try {
			X509EncodedKeySpec	keySpec		= new X509EncodedKeySpec(Base64.decodeBase64(base64PublicKey.getBytes()));
			KeyFactory			keyFactory	= KeyFactory.getInstance("RSA");
			publicKey = keyFactory.generatePublic(keySpec);
			return publicKey;
		} catch (NoSuchAlgorithmException exec) {
			exec.printStackTrace();
		} catch (InvalidKeySpecException exec) {
			exec.printStackTrace();
		}
		return publicKey;
	}

	public static PrivateKey getPrivateKey(String base64PrivateKey) {
		PrivateKey			privateKey	= null;
		PKCS8EncodedKeySpec	keySpec		= new PKCS8EncodedKeySpec(Base64.decodeBase64(base64PrivateKey.getBytes()));
		KeyFactory			keyFactory	= null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			privateKey = keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException exec) {
			exec.printStackTrace();
		} catch (InvalidKeySpecException exec) {
			// TODO Auto-generated catch block
			exec.printStackTrace();
		}
		
		return privateKey;
	}
	
	public static byte[] generateSecretKeyBytes(String algorithm, Integer keyLengh) throws Exception {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
		keyGenerator.init(keyLengh);
		SecretKey secretKey = keyGenerator.generateKey();
		return secretKey.getEncoded();
	}
	

	public static byte[] generateIvBytes(int blockSize) {
		byte[]			ivBytes			= new byte[blockSize];
		SecureRandom	secureRandom	= new SecureRandom();
		secureRandom.nextBytes(ivBytes);
		return ivBytes;
	}

	

}
