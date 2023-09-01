package com.trigyn.jws.usermanagement.security.config;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import de.taimos.totp.TOTP;

@Component
public class TwoFactorGoogleUtil {

	private final static Logger logger = LogManager.getLogger(TwoFactorGoogleUtil.class);

	// private static final String SecretKey = "XJZI7XO2YD2PEZJQU3RLS4TBDTPL47AJ";

	// public static void main(String[] args) {
	// System.out.println("Hello World!");
	// System.out.println("Secret Key : " + SecretKey /* + generateSecretKey() */);
	// //System.out.println(getGoogleAuthenticatorBarCode("a.d.chowdhury@gmail.com",
	// "My Company"));
	// //createQRCode(getGoogleAuthenticatorBarCode("ravi.gowda@trigyn.com",
	// "Trigyn"), "D://" + UUID.randomUUID().toString() + ".png", 300, 300);
	// validateOTP();
	// }

	public void validateOTP(String enteredCode, String secretKey) {
		if (enteredCode.equals(getTOTPCode(secretKey))) {
			System.out.println("Logged in successfully");
		} else {
			System.out.println("Invalid 2FA Code");
		}
	}

	public void createQRCode(String barCodeData, OutputStream outputStream, int height, int width) {
		try {
			BitMatrix matrix = new MultiFormatWriter().encode(barCodeData, BarcodeFormat.QR_CODE, width, height);
			// FileOutputStream out = new FileOutputStream(filePath);
			MatrixToImageWriter.writeToStream(matrix, "png", outputStream);
		} catch (Throwable a_th) {
			logger.error("Error ocurred.", a_th);
		}
	}

	public String getGoogleAuthenticatorBarCode(String account, String issuer, String secretKey) {
		try {
			return "otpauth://totp/" + URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20") + "?secret="
					+ URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20") + "&issuer="
					+ URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");
		} catch (UnsupportedEncodingException a_unSupEncExc) {
			logger.error("Error ocurred.", a_unSupEncExc);
			throw new IllegalStateException(a_unSupEncExc);
		}
	}

	public String generateSecretKey() {
		SecureRandom	random	= new SecureRandom();
		byte[]			bytes	= new byte[20];
		random.nextBytes(bytes);
		Base32 base32 = new Base32();
		return base32.encodeToString(bytes);
	}

	public String getTOTPCode(String secretKey) {
		Base32	base32	= new Base32();
		byte[]	bytes	= base32.decode(secretKey);
		String	hexKey	= Hex.encodeHexString(bytes);
		return TOTP.getOTP(hexKey);
	}

}
