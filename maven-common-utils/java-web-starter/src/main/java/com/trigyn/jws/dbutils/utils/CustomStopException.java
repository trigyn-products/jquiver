package com.trigyn.jws.dbutils.utils;

/**
 * @author Bibhusrita.Nayak
 * 
 *         Custom Stop Exception class created for handling Stop exceptions, in
 *         all applications.
 *
 */

public class CustomStopException extends RuntimeException {

	private static final long serialVersionUID = -8432870850179064681L;
	private final int statusCode;
	private final String message;

	public CustomStopException(String a_message) {

		super(a_message);
		statusCode = Integer.parseInt(a_message.split("_")[0]);
		message = a_message.split("_")[1];
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getMessage() {
		return message;
	}

}
