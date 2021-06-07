package com.trigyn.jws.dynarest.cipher.utils;

public class CipherUtilFactory {

	public static ICipherUtil getCipherUtil(String encryptionType) {
		if("AES".equalsIgnoreCase(encryptionType)) {
			return new AESCipherUtil();
		} else if("DES".equalsIgnoreCase(encryptionType)) {
			return new DESCipherUtil();
		} else if("RSA".equalsIgnoreCase(encryptionType)) {
			return new RSACipherUtil();
		} else {
			return null;
		}
	}
}
