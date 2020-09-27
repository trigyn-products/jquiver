package com.trigyn.jws.dynarest.utils;

public final class Constants {
    
    private Constants() {

    }

    public static final String SERVICE_CLASS_NAME 							= "ServiceLogic";
    public static final String DYNAREST_CLASS_FILE_PATH 					= "dynarest-class-file-path";
	
    public enum Platforms {
        JAVA(1), FTL(2);

        final int platform;

        Platforms(int platformId) {
            platform = platformId;
        }

        public int getPlatform() {
            return platform;
        }
    }
}
