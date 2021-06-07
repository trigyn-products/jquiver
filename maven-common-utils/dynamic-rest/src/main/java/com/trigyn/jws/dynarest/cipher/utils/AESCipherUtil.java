package com.trigyn.jws.dynarest.cipher.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AESCipherUtil implements ICipherUtil {

	// AES encryption logic
	private static SecretKeySpec	secretKey;
	private static byte[]			key;

	public static void main(String[] args) {
//		System.out.println(decrypt("wMVQRPRdFB3E5IahmCyNkata5ETjczS47bFYICeH7V6rQ2QT46AvKSEjPmSbut8FuEKrevJtgFKtOnLj5+G47ut773KkKFsG94cYwF9lctI=","secret"));
	}
	private void setAESEncryptionKey(String myKey) {
		MessageDigest sha = null;
		try {
			key			= myKey.getBytes("UTF-8");
			sha			= MessageDigest.getInstance("SHA-1");
			key			= sha.digest(key);
			key			= Arrays.copyOf(key, 16);
			secretKey	= new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public String encrypt(String strToEncrypt, String secret) throws Exception {
			setAESEncryptionKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
	}

	public String decrypt(String strToDecrypt, String secret) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
			setAESEncryptionKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
	}
}
