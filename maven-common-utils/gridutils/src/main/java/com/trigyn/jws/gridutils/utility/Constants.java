package com.trigyn.jws.gridutils.utility;

public final class Constants {

	public static final String DEFAULT_GRID_TEMPLATE_NAME = "default-grid-template";

	public enum queryImplementationType {
		VIEW(1), STORED_PROCEDURE(2);

		final int type;

		queryImplementationType(int i) {
			type = i;
		}

		public int getType() {
			return type;
		}
	}
}