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

	byte[]	initVector	= new byte[] {};

	public IVCipherMode() {
		super();
	}

	public IVCipherMode(String algorithm, String mode, String padding, Integer keyLength) throws Exception {
		//this.fullAlgorithm	= algorithm;
		this.mode			= mode;
		this.padding		= padding;
		this.keyLength		= keyLength;
		this.fullAlgorithm = algorithm + "/" + mode + "/" + padding;
		Cipher cipher = Cipher.getInstance(this.fullAlgorithm);
		this.initVector = new byte[cipher.getBlockSize()];
		//initVector 	=  new byte[keyLength / Byte.SIZE];
		Arrays.fill(this.initVector, (byte) 0x0);
	}
	
	// Manual padding if NoPadding
	private byte[] padNoPadding(String input, int blockSize) throws UnsupportedEncodingException {
	    byte[] bytes = input.getBytes("UTF-8");
	    int paddedLength = ((bytes.length + blockSize - 1) / blockSize) * blockSize;
	    byte[] padded = new byte[paddedLength];
	    System.arraycopy(bytes, 0, padded, 0, bytes.length);
	    // remaining bytes are 0x00
	    return padded;
	}

	// Remove 0x00 padding after decrypt (for NoPadding only)
    private String trimNoPadding(byte[] decrypted) throws UnsupportedEncodingException {
        int i = decrypted.length;
        while (i > 0 && decrypted[i - 1] == 0x00) {
            i--;
        }
        return new String(Arrays.copyOf(decrypted, i), "UTF-8");
    }
    
	public String encrypt(String strToEncrypt, String secret, String algorithm) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {

		SecretKeySpec secretKeySpec = setEncryptionKey(secret, algorithm, this.keyLength);
		//fullAlgorithm += "/" + this.mode + "/" + this.padding;
		Cipher			cipher	= Cipher.getInstance(this.fullAlgorithm);
		//byte[]			ivBytes	= CipherUtil.generateIvBytes(cipher.getBlockSize());
		IvParameterSpec	iv		= new IvParameterSpec(this.initVector);
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
		
		byte[] inputBytes;
        if ("NoPadding".equalsIgnoreCase(this.padding)) {
            inputBytes = padNoPadding(strToEncrypt, cipher.getBlockSize());
        } else {
            inputBytes = strToEncrypt.getBytes("UTF-8");
        }
        
		String encString = Base64.encodeBase64String(cipher.doFinal(inputBytes));
		return encString;
	}

	public String decrypt(String strToDecrypt, String secret, String algorithm)
			throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException,
			NoSuchPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
		SecretKeySpec secretKeySpec = setEncryptionKey(secret, algorithm, this.keyLength);
		//fullAlgorithm += "/" + this.mode + "/" + this.padding;
		Cipher			cipher	= Cipher.getInstance(this.fullAlgorithm);
		//byte[]			ivBytes	= CipherUtil.generateIvBytes(cipher.getBlockSize());
		IvParameterSpec	iv		= new IvParameterSpec(this.initVector);
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
		//String decString = new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt.getBytes("UTF-8"))));
		byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(strToDecrypt.getBytes("UTF-8")));
		  if ("NoPadding".equalsIgnoreCase(this.padding)) {
	            return trimNoPadding(decryptedBytes);
	        } else {
	            return new String(decryptedBytes, "UTF-8");
	        }
		
	}
	
	
}