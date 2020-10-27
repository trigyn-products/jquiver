package com.trigyn.jws.templating.utils;

import java.util.Locale;

import org.springframework.context.MessageSource;

public final class MessageSourceUtils {

    private MessageSource	messageSource	= null;
	private Locale			locale			= null;

	private MessageSourceUtils(MessageSource a_messageSource, Locale a_local) {
		messageSource = a_messageSource;
		locale = a_local;
	}

	public static MessageSourceUtils getMessageSource(MessageSource a_messageSource, Locale a_local) {
		return new MessageSourceUtils(a_messageSource, a_local);
	}

	public String getMessage(String a_strKey) {
		return messageSource.getMessage(a_strKey, null, locale);
	}

	public String getMessage(String a_strKey, Object args[]) {
		return messageSource.getMessage(a_strKey, args, locale);
	}

	public String getMessageWithDefault(String a_strKey, String defaultMessage) {
		String message = messageSource.getMessage(a_strKey, null, locale);
		StringBuilder keyNotFound = new StringBuilder().append("???").append(a_strKey).append("???");
		if(message != null && message.equals(keyNotFound.toString())) {
			return defaultMessage;
		}
		return message;
	}
	
	public String getMessages(Object... args) {
		if (args == null) {
			return "???null???";
		} else if (args.length == 1 && args[0] != null) {
			return messageSource.getMessage(args[0].toString(), null, locale);
		} else {
			Object l_args[] = new Object[args.length - 1];
			System.arraycopy(args, 1, l_args, 0, l_args.length);
			return messageSource.getMessage(args[0].toString(), l_args, locale);
		}
	}
    
}
