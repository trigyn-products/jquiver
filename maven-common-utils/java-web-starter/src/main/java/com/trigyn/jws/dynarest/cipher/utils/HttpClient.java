package com.trigyn.jws.dynarest.cipher.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.trigyn.jws.dbutils.cipher.utils.AESCipherUtil;

public class HttpClient {

	public static void main(String[] args) {
		try {
			
			String requestURL = "http://localhost:8080/api/manual-entry-sequence?manualType=07cf45ae-2987-11eb-a9be-e454e805e22f";
			

//			AESCipherUtil			cu					= new AESCipherUtil();
//			String encryptedQS = cu.encrypt(null, "secret");
//			requestURL = requestURL.concat(encryptedQS);
			
			executeRequest(null, requestURL);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static final String executeRequest(String a_requestString, String requestURL) throws Throwable {
		AESCipherUtil		cu					= new AESCipherUtil();

		URL					url					= new URL(requestURL);

		HttpURLConnection	httpUrlConnection	= (HttpURLConnection) url.openConnection();
		httpUrlConnection.setRequestMethod("POST");
		httpUrlConnection.setDoOutput(true);
		Map<String, String> headers = new HashMap<>();

		headers.put("Content-Type", "application/json");

		for (String headerKey : headers.keySet()) {
			httpUrlConnection.setRequestProperty(headerKey, headers.get(headerKey));
		}

		OutputStream os = httpUrlConnection.getOutputStream();

		if (a_requestString != null) {
			String encryptedData = cu.encrypt(a_requestString, "secret");
			os.write(encryptedData.getBytes());
		}
		os.flush();

		if (httpUrlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException("Failed : HTTP error code : " + httpUrlConnection.getResponseCode());
		}

		BufferedReader	br	= new BufferedReader(new InputStreamReader((httpUrlConnection.getInputStream())));
		StringBuilder	sb	= new StringBuilder();
		String			output;
		while ((output = br.readLine()) != null) {
			sb.append(output);
		}
		httpUrlConnection.disconnect();
		System.out.println(sb.toString());
		return sb.toString();
	}

}
