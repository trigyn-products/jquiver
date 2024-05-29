package com.trigyn.jws.usermanagement.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidLoginException extends AuthenticationException {
	
	private String previousEmail = null;

	public InvalidLoginException(String msg) {
		super(msg);
	}

	public InvalidLoginException(String msg, Throwable t) {
		super(msg, t);
	}
	
	public InvalidLoginException(String msg, String a_previousEmail) {
		super(msg);
		setPreviousEmail(a_previousEmail);
	}

	public InvalidLoginException(String msg, Throwable t, String a_previousEmail) {
		super(msg, t);
		setPreviousEmail(a_previousEmail);
	}
	
	public String getPreviousEmail() {
		return previousEmail;
	}
	
	public void setPreviousEmail(String previousEmail) {
		this.previousEmail = previousEmail;
	}

}
