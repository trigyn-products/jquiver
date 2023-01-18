package com.trigyn.jws.dbutils.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;

public class CustomRuntimeException extends RuntimeException  {

	private static final long serialVersionUID = -7661881974219233311L;

	private String errorMessage = null;
	
	private HttpStatus statusCode = null;
	
	private ErrorCode errorCode = null;
	
	private CustomRuntimeException error = null;
	
	public CustomRuntimeException() {}

	public CustomRuntimeException(CustomRuntimeException error) {
		super();
		this.error = error;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public HttpStatus getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(HttpStatus statusCode) {
		this.statusCode = statusCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	@Override
	public String toString() {
		String stacktrace = ExceptionUtils.getStackTrace(error);
		return stacktrace;
	}
	
}
