package com.trigyn.jws.usermanagement.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidLoginException extends AuthenticationException {

	private static final long	serialVersionUID	= -8780875699654094721L;

	private String				previousEmail		= null;
	
	private String				previousAuth		= null;

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
	
	public InvalidLoginException(String msg, String a_previousEmail, String a_previousAuth) {
		super(msg);
		setPreviousEmail(a_previousEmail);
		setPreviousAuth(a_previousAuth);
	}

	public InvalidLoginException(String msg, Throwable t, String a_previousEmail, String a_previousAuth) {
		super(msg, t);
		setPreviousEmail(a_previousEmail);
		setPreviousAuth(a_previousAuth);
	}

	public String getPreviousEmail() {
		return previousEmail;
	}

	public void setPreviousEmail(String previousEmail) {
		this.previousEmail = previousEmail;
	}

	public String getPreviousAuth() {
		return previousAuth;
	}

	public void setPreviousAuth(String previousAuth) {
		this.previousAuth = previousAuth;
	}

}
