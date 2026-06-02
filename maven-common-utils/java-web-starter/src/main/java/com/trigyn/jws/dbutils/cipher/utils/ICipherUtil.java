package com.trigyn.jws.dbutils.cipher.utils;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public interface ICipherUtil {

	String encrypt(String data, String key, String algorithm) throws BadPaddingException, IllegalBlockSizeException,
			InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, Exception;

	String decrypt(String data, String key, String algorithm) throws IllegalBlockSizeException, InvalidKeyException,
			BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, Exception;

	default SecretKeySpec setEncryptionKey(String encKey, String algorithm, Integer keyLength) {
		
		 int keySizeBytes;

		    switch (algorithm.toUpperCase()) {
		        case "DES":
		            keySizeBytes = 8;  // DES requires 8 bytes
		            break;
		        case "AES":
		            if (keyLength == 128) {
		                keySizeBytes = 16;
		            } else if (keyLength == 192) {
		                keySizeBytes = 24;
		            } else if (keyLength == 256) {
		                keySizeBytes = 32;
		            } else {
		                throw new IllegalArgumentException("Invalid AES key length: " + keyLength);
		            }
		            break;
		        default:
		            throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);
		    }
		byte[] keyBytes = new byte[keySizeBytes];
		    // Fill with zeros if shorter
		Arrays.fill(keyBytes, (byte) 0x0);
		byte[]	encKeyBytes	= encKey.getBytes(StandardCharsets.UTF_8);
		int		length		= encKeyBytes.length < keyBytes.length ? encKeyBytes.length : keyBytes.length;
		System.arraycopy(encKeyBytes, 0, keyBytes, 0, length);
		return new SecretKeySpec(keyBytes, algorithm);
	}

}
