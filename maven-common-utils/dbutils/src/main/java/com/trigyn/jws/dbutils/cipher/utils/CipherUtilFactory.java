package com.trigyn.jws.dbutils.cipher.utils;

public class CipherUtilFactory {

	public static ICipherUtil getCipherUtil(String encryptionType) {
		if("AES/ECB".equalsIgnoreCase(encryptionType)) {
			return new AESCipherUtil();
		} else if("AES/CBC".equalsIgnoreCase(encryptionType)) {
			return new AesCbcCipherUtil();
		} else if("DES".equalsIgnoreCase(encryptionType)) {
			return new DESCipherUtil();
		} else if("RSA".equalsIgnoreCase(encryptionType)) {
			return new RSACipherUtil();
		} else {
			return null;
		}
	}
}
