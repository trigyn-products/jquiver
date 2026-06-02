package com.trigyn.jws.dbutils.cipher.modes;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import com.trigyn.jws.dbutils.cipher.utils.ICipherUtil;
import com.trigyn.jws.dbutils.cipher.utils.CipherUtil;

public class RSACipherMode implements ICipherUtil {

    private String fullAlgorithm;

    public RSACipherMode(String algorithm, String mode, String padding, Integer keyLength) {
        this.fullAlgorithm = algorithm + "/" + mode + "/" + padding;
    }

    @Override
    public String encrypt(String dataToEncrypt, String publicKeyStr, String algorithm) throws Exception {
        PublicKey publicKey = CipherUtil.getPublicKey(publicKeyStr);
        Cipher cipher = Cipher.getInstance(fullAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(dataToEncrypt.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeBase64String(encryptedBytes);
    }

    @Override
    public String decrypt(String dataToDecrypt, String privateKeyStr, String algorithm) throws Exception {
        PrivateKey privateKey = CipherUtil.getPrivateKey(privateKeyStr);
        Cipher cipher = Cipher.getInstance(fullAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(dataToDecrypt));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}

