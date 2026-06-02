package com.trigyn.jws.dbutils.cipher.utils;

import com.trigyn.jws.dbutils.cipher.modes.ECBCipherMode;
import com.trigyn.jws.dbutils.cipher.modes.GCMCipherMode;
import com.trigyn.jws.dbutils.cipher.modes.IVCipherMode;
import com.trigyn.jws.dbutils.cipher.modes.RSACipherMode;

public class RSACipherUtilFactory {
	
	public static ICipherUtil getCipherMode(String algorithm, String mode, String padding, Integer keyLength) throws Exception {
		if ("RSA".equalsIgnoreCase(algorithm)) {
            return new RSACipherMode(algorithm, mode, padding, keyLength);
        }
		if (mode != null && mode.isEmpty() == false) {
			switch (mode) {
				case "ECB":
					return new ECBCipherMode(algorithm, mode, padding, keyLength);
				case "GCM":
					return new GCMCipherMode(algorithm, mode, padding, keyLength);
				default:
					return new IVCipherMode(algorithm, mode, padding, keyLength);
			}
		}
		return null;
	}
}
