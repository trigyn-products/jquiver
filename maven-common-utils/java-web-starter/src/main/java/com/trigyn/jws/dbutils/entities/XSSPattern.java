package com.trigyn.jws.dbutils.entities;

import java.util.regex.Pattern;

public class XSSPattern {

	    private String name;
	    private String regex;
	    private String patterns;

	    // Getters and setters
	    public String getName() {
	        return name;
	    }
	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getRegex() {
	        return regex;
	    }
	    public void setRegex(String regex) {
	        this.regex = regex;
	    }	    
	    
		public String getPatterns() {
			return patterns;
		}
		public void setPatterns(String patterns) {
			this.patterns = patterns;
		}
		
		@Override
		public String toString() {
			return "XSSPattern [name=" + name + ", regex=" + regex + ", patterns=" + patterns + "]";
		}
		
//		public static int getPatternFlags(String patternCodes) {
//	        int flags = 0;
//	        for (String code : patternCodes.split(",")) {
//	            switch (code.trim()) {
//	                case "0x40" -> flags |= Pattern.CASE_INSENSITIVE;
//	                case "0x41" -> flags |= Pattern.DOTALL;
//	                case "0x42" -> flags |= Pattern.MULTILINE;
//	                // Add more codes and flag mappings as needed
//	            }
//	        }
//	        return flags;
//	    }
}
