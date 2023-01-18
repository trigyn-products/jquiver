/**
 * 
 */
package com.trigyn.jws.dbutils.cipher.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

/**
 * @author Mini.Pillai
 *
 */
public class AesCbcCipherUtil implements ICipherUtil {

	// AES encryption logic
	private static SecretKeySpec	secretKey;
	private static byte[]			key;
	static byte[] initVector = new byte[] { 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0 };
	
//	public static void main(String[] args) {
//		try {
//
//			System.out.println(encrypt("Test this string", "3sc3RLrpd17"));
//			System.out.println(encrypt("{\"data\":{\"request_data\":{\"languageID\":\"en\",\"TimeStamp\":\"234234\" }}}", "3sc3RLrpd17"));
//
//			System.out.println(decrypt("/5Bs0rBY85N722bX7UAzMXlrKGlfT9XqDSTCcFpLiC4=", "3sc3RLrpd17"));
//			System.out.println(decrypt("83yFGTmKeE9gUvII8btvL8SYTTPPdWQP7iEpt41wMHQlT1OUOW9GFDUcnOgtGw2lspcNsSaVilvrqw+6rYNQ1TPjtYEaAOgLIsLrlYiqWMQ=", "3sc3RLrpd17"));
//			
//		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException
//				| NoSuchPaddingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	private void setAESEncryptionKey(String myKey) {
		MessageDigest sha = null;
		try {
			key			= myKey.getBytes("UTF-8");
			sha			= MessageDigest.getInstance("SHA-256");
			key			= sha.digest(key);
			key			= Arrays.copyOf(key, 32);
			secretKey	= new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public String encrypt(String strToEncrypt, String secret) throws Exception {
		IvParameterSpec iv = new IvParameterSpec(initVector);
		setAESEncryptionKey(secret);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
		return Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
	}

	public String decrypt(String strToDecrypt, String secret)
			throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException,
			NoSuchPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
		IvParameterSpec iv = new IvParameterSpec(initVector);
		setAESEncryptionKey(secret);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
		return new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
	}

}