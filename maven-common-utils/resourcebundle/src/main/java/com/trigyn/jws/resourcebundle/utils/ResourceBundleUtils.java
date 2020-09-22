package com.trigyn.jws.resourcebundle.utils;

public class ResourceBundleUtils {
 
     
    public static String getUnicode(String a_source) {
		StringBuilder sb = new StringBuilder();
		for (char c : a_source.toCharArray()) {
			sb.append(unicodeEscaped(c));
		}
		return sb.toString();
	}

	
    
    public static String unicodeEscaped(char ch) {
		return "&#" + (int) ch + ";";
	}

	
    
    public static String unicodeEscaped(Character ch) {
		if (ch == null) {
			return null;
		}
		return unicodeEscaped(ch.charValue());
    }
    
}