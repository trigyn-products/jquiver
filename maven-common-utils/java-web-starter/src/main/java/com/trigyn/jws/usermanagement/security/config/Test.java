package com.trigyn.jws.usermanagement.security.config;

public class Test {

	public static void main(String[] args) {
		System.out.println(getUnicode(" "));
	}

	private static String getUnicode(String a_source) {
		StringBuilder sb = new StringBuilder();
		for (char c : a_source.toCharArray()) {
			sb.append(unicodeEscaped(c));
		}
		return sb.toString();
	}

	private static String unicodeEscaped(char ch) {
		return "&#" + (int) ch + ";";
	}

}
