package com.trigyn.jws.dbutils.cipher.modes;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

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
		this.mode			= mode;
		this.padding		= padding;
		this.keyLength		= keyLength;
		this.fullAlgorithm	= algorithm + "/" + mode + "/" + padding;
	}
	
	private byte[] padNoPadding(String input, int blockSize) throws UnsupportedEncodingException {
        byte[] bytes = input.getBytes("UTF-8");
        int paddedLength = ((bytes.length + blockSize - 1) / blockSize) * blockSize;
        byte[] padded = new byte[paddedLength];
        System.arraycopy(bytes, 0, padded, 0, bytes.length);
        return padded;
    }

    private String trimNoPadding(byte[] decrypted) throws UnsupportedEncodingException {
        int i = decrypted.length;
        while (i > 0 && decrypted[i - 1] == 0x00) {
            i--;
        }
        return new String(Arrays.copyOf(decrypted, i), "UTF-8");
    }

	@Override
	public String encrypt(String dataToEncrypt, String secretKey, String algorithm)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException {
		SecretKeySpec secretKeySpec = setEncryptionKey(secretKey, algorithm, this.keyLength);
		//fullAlgorithm += "/" + this.mode + "/" + this.padding;
		Cipher cipher = Cipher.getInstance(this.fullAlgorithm);
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		 byte[] inputBytes;
	        if ("NoPadding".equalsIgnoreCase(this.padding)) {
	            inputBytes = padNoPadding(dataToEncrypt, cipher.getBlockSize());
	        } else {
	            inputBytes = dataToEncrypt.getBytes("UTF-8");
	        }

	        return Base64.encodeBase64String(cipher.doFinal(inputBytes));
	}

	@Override
	public String decrypt(String dataToDecrypt, String secretKey, String algorithm) throws IllegalBlockSizeException,
			NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException,UnsupportedEncodingException {
		SecretKeySpec secretKeySpec = setEncryptionKey(secretKey, algorithm, this.keyLength);
		//fullAlgorithm += "/" + this.mode + "/" + this.padding;
		Cipher cipher = Cipher.getInstance(this.fullAlgorithm);
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
		  byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(dataToDecrypt));

	        if ("NoPadding".equalsIgnoreCase(this.padding)) {
	            return trimNoPadding(decryptedBytes);
	        } else {
	            return new String(decryptedBytes, "UTF-8");
	        }
	    }
	

}
