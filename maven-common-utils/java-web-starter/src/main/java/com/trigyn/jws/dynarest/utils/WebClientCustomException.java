package com.trigyn.jws.dynarest.utils;

import org.springframework.http.HttpStatus;

/**
 * @author Bibhusrita.Nayak
 * 
 * Custom Exception class created for handling
 * exceptions, in Rest Client.
 *
 */

public class WebClientCustomException extends Exception {

	private static final long	serialVersionUID	= -8511603979550526831L;

	private final HttpStatus	statusCode;

	private final String		statusText;

	public WebClientCustomException(String a_message, HttpStatus a_statusCode, String a_statusText) {

		super(a_message);

		statusCode = a_statusCode;
		statusText = a_statusText;

	}

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public String getStatusText() {
		return statusText;
	}
}