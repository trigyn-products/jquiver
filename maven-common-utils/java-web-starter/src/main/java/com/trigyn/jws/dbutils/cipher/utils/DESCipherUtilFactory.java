package com.trigyn.jws.dbutils.cipher.utils;

import com.trigyn.jws.dbutils.cipher.modes.ECBCipherMode;
import com.trigyn.jws.dbutils.cipher.modes.GCMCipherMode;
import com.trigyn.jws.dbutils.cipher.modes.IVCipherMode;

public class DESCipherUtilFactory {

	public static ICipherUtil getCipherMode(String algorithm, String mode, String padding, Integer keyLength) {
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
