package app.trigyn.core.dynarest.utils;

public final class Constants {
    
    private Constants() {

    }

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
