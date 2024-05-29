package com.trigyn.jws.dbutils.cipher.modes;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.trigyn.jws.dbutils.cipher.utils.CipherUtil;
import com.trigyn.jws.dbutils.cipher.utils.ICipherUtil;

import org.apache.commons.codec.binary.Base64;

public class GCMCipherMode implements ICipherUtil {

	private String	fullAlgorithm;

	private String	mode;

	private String	padding;

	Integer			keyLength;

	public GCMCipherMode(String fullAlgorithm, String mode, String padding, Integer keyLength) {
		super();
		this.fullAlgorithm	= fullAlgorithm;
		this.mode			= mode;
		this.padding		= padding;
		this.keyLength		= keyLength;
	}

	public GCMCipherMode() {
		super();
	}

	public String encrypt(String strToEncrypt, String secret, String algorithm) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {

		SecretKeySpec secretKeySpec = setEncryptionKey(secret, algorithm, this.keyLength);
		fullAlgorithm += "/" + this.mode + "/" + this.padding;
		Cipher	cipher	= Cipher.getInstance(fullAlgorithm);
		byte[]	ivBytes	= CipherUtil.generateIvBytes(cipher.getBlockSize());
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new GCMParameterSpec(this.keyLength * 8, ivBytes));
		return Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
	}

	public String decrypt(String strToDecrypt, String secret, String algorithm)
			throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException,
			NoSuchPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
		SecretKeySpec secretKeySpec = setEncryptionKey(secret, algorithm, this.keyLength);
		this.fullAlgorithm += "/" + this.mode + "/" + this.padding;
		Cipher	cipher	= Cipher.getInstance(fullAlgorithm);
		byte[]	ivBytes	= CipherUtil.generateIvBytes(cipher.getBlockSize());
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new GCMParameterSpec(this.keyLength * 8, ivBytes));
		return new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt)));
	}

}
