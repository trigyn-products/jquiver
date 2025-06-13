package com.trigyn.jws.formio.exceptions;

public class FormValidationException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1049380404543187867L;

	public FormValidationException(Exception cause) {
		super("Error while form validation.", cause);
	}
}
