package com.trigyn.jws.dbutils.cipher.utils;

public class CipherUtilFactory {

	public static ICipherUtil getCipherUtil(String algorithm, String mode, String padding, Integer keyLength)
			throws Exception {

		if (algorithm != null && algorithm.isEmpty() == false) {
			switch (algorithm) {
				case "AES":
					return AESCipherUtilFactory.getCipherMode(algorithm, mode, padding, keyLength);
				case "DES":
					return DESCipherUtilFactory.getCipherMode(algorithm, mode, padding, keyLength);
				case "RSA":
					return RSACipherUtilFactory.getCipherMode(algorithm, mode, padding, keyLength);
				default:
					throw new Exception("Algorithm Not Supported");
			}
		}
		return null;
	}
}
