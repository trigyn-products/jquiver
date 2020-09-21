package app.trigyn.common.resourcebundle.utils;

public class ResourceBundleUtils {
 
     /** 
     * @param a_source
     * @return String
     */
    public static String getUnicode(String a_source) {
		StringBuilder sb = new StringBuilder();
		for (char c : a_source.toCharArray()) {
			sb.append(unicodeEscaped(c));
		}
		return sb.toString();
	}

	
    /** 
     * @param ch
     * @return String
     */
    public static String unicodeEscaped(char ch) {
		return "&#" + (int) ch + ";";
	}

	
    /** 
     * @param ch
     * @return String
     */
    public static String unicodeEscaped(Character ch) {
		if (ch == null) {
			return null;
		}
		return unicodeEscaped(ch.charValue());
    }
    
}