package com.trigyn.jws.dbutils.utils;

public final class Constant {

	public static final Double INITIAL_VERSION_NUMBER 		= 1.00;
	public static final Integer MAX_DECIMAL_VERSION_NUMBER 	= 99;
	public static final Integer DEFAULT_LANGUAGE_ID 		= 1;
	public static final Integer DEFAULT_SEQUENCE_NUMBER		= 1;
	public static final Integer MODULE_GROUP_ID		 		= 6;
	public static final String GROUP_MODULE_URL				= "#";
	public static final Integer HOME_PAGE_MODULE_SEQUENCE	= 9999;
	public static final String HOME_PAGE_MODULE_URL			= "home-module";
	public enum RecordStatus {
		INSERTED(0), DELETED(1);
		
		final int status;
		
		RecordStatus(int i) {
			status = i;
		}
		
		public int getStatus() {
			return status;
		}
		
	}
}
