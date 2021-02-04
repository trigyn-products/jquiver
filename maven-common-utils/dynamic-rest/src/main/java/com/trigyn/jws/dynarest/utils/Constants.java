package com.trigyn.jws.dynarest.utils;

public final class Constants {

	private Constants() {

	}

	public static final String	SERVICE_CLASS_NAME			= "ServiceLogic";
	public static final String	DYNAREST_CLASS_FILE_PATH	= "dynarest-class-file-path";

	public enum Platforms {
		JAVA(1), FTL(2), JAVASCRIPT(3);

		final int platform;

		Platforms(int platformId) {
			platform = platformId;
		}

		public int getPlatform() {
			return platform;
		}
	}

	public enum QueryType {
		DML(2), SELECT(1), SP(3);

		final int queryType;

		QueryType(int queryType) {
			this.queryType = queryType;
		}

		public int getQueryType() {
			return queryType;
		}
	}
}
