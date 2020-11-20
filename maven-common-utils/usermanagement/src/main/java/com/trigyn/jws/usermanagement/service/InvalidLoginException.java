package com.trigyn.jws.usermanagement.service;

import org.springframework.security.core.AuthenticationException;

public class InvalidLoginException  extends AuthenticationException{
	
	public InvalidLoginException(String msg) {
		super(msg);
	}

	public InvalidLoginException(String msg, Throwable t) {
		super(msg, t);
	}
	
}
