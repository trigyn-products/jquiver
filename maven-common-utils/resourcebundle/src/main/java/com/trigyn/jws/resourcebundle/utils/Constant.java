package com.trigyn.jws.resourcebundle.utils;

public final class Constant {

	private Constant() {

	}

	public static final Integer	DEFAULT_LANGUAGE_ID	= 1;
	public static final String	LOCALE_ID			= "userLocale";

	public enum RecordStatus {
		DELETED(1), INSERTED(0), UPDATED(0);

		final int status;

		RecordStatus(int i) {
			status = i;
		}

		public int getStatus() {
			return status;
		}
	}
}
