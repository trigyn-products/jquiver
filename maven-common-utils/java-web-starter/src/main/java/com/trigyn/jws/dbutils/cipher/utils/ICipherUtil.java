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
		byte[] keyBytes = new byte[keyLength / Byte.SIZE];
		Arrays.fill(keyBytes, (byte) 0x0);
		byte[]	encKeyBytes	= encKey.getBytes(StandardCharsets.UTF_8);
		int		length		= encKeyBytes.length < keyBytes.length ? encKeyBytes.length : keyBytes.length;
		System.arraycopy(encKeyBytes, 0, keyBytes, 0, length);
		return new SecretKeySpec(keyBytes, algorithm);
	}

}
