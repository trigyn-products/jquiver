package com.trigyn.jws.dbutils.cipher.modes;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.trigyn.jws.dbutils.cipher.utils.ICipherUtil;

import org.apache.commons.codec.binary.Base64;

public class ECBCipherMode implements ICipherUtil {

	private String	fullAlgorithm;

	private String	mode;

	private String	padding;

	private Integer	keyLength;

	public ECBCipherMode() {
		super();
	}

	public ECBCipherMode(String algorithm, String mode, String padding, Integer keyLength) {
		super();
		this.fullAlgorithm	= algorithm;
		this.mode			= mode;
		this.padding		= padding;
		this.keyLength		= keyLength;
	}

	@Override
	public String encrypt(String dataToEncrypt, String secretKey, String algorithm)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {
		SecretKeySpec secretKeySpec = setEncryptionKey(secretKey, algorithm, this.keyLength);
		fullAlgorithm += "/" + this.mode + "/" + this.padding;
		Cipher cipher = Cipher.getInstance(fullAlgorithm);
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		return Base64.encodeBase64String(cipher.doFinal(dataToEncrypt.getBytes("UTF-8")));
	}

	@Override
	public String decrypt(String dataToDecrypt, String secretKey, String algorithm) throws IllegalBlockSizeException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException {
		SecretKeySpec secretKeySpec = setEncryptionKey(secretKey, algorithm, this.keyLength);
		fullAlgorithm += "/" + this.mode + "/" + this.padding;
		Cipher cipher = Cipher.getInstance(fullAlgorithm);
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
		return new String(cipher.doFinal(Base64.decodeBase64(dataToDecrypt)));
	}

}
