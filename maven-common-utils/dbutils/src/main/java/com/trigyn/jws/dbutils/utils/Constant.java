package com.trigyn.jws.dbutils.utils;

public final class Constant {

	public static final Double INITIAL_VERSION_NUMBER 		= 1.00;
	public static final Integer MAX_DECIMAL_VERSION_NUMBER 	= 99;
	public static final Integer DEFAULT_LANGUAGE_ID 		= 1;
	public static final String GROUP_MODULE_URL				= "#";
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
