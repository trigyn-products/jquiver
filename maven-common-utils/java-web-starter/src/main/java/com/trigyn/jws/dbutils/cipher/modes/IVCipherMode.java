/**
 * 
 */
package com.trigyn.jws.dbutils.cipher.modes;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.trigyn.jws.dbutils.cipher.utils.ICipherUtil;

import org.apache.commons.codec.binary.Base64;

/**
 * @author Mini.Pillai
 *
 */
public class IVCipherMode implements ICipherUtil {

	private String	fullAlgorithm;

	private String	mode;

	private String	padding;

	Integer			keyLength;

	static byte[]	initVector	= new byte[] {};

	public IVCipherMode() {
		super();
	}

	public IVCipherMode(String algorithm, String mode, String padding, Integer keyLength) {
		super();
		this.fullAlgorithm	= algorithm;
		this.mode			= mode;
		this.padding		= padding;
		this.keyLength		= keyLength;
		initVector 	=  new byte[keyLength / Byte.SIZE];
		Arrays.fill(initVector, (byte) 0x0);
	}

	public String encrypt(String strToEncrypt, String secret, String algorithm) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {

		SecretKeySpec secretKeySpec = setEncryptionKey(secret, algorithm, this.keyLength);
		fullAlgorithm += "/" + this.mode + "/" + this.padding;
		Cipher			cipher	= Cipher.getInstance(fullAlgorithm);
		//byte[]			ivBytes	= CipherUtil.generateIvBytes(cipher.getBlockSize());
		IvParameterSpec	iv		= new IvParameterSpec(initVector);
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
		String encString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
		return encString;
	}

	public String decrypt(String strToDecrypt, String secret, String algorithm)
			throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException,
			NoSuchPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
		SecretKeySpec secretKeySpec = setEncryptionKey(secret, algorithm, this.keyLength);
		fullAlgorithm += "/" + this.mode + "/" + this.padding;
		Cipher			cipher	= Cipher.getInstance(fullAlgorithm);
		//byte[]			ivBytes	= CipherUtil.generateIvBytes(cipher.getBlockSize());
		IvParameterSpec	iv		= new IvParameterSpec(initVector);
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
		String decString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt.getBytes("UTF-8"))));
		return decString;
	}
	
	
}