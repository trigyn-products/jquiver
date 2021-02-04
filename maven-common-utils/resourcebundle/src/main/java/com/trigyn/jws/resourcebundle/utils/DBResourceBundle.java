package com.trigyn.jws.resourcebundle.utils;

import java.text.MessageFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Component;

import com.trigyn.jws.resourcebundle.repository.interfaces.IResourceBundleRepository;

@Component("messageSource")
public class DBResourceBundle extends AbstractMessageSource {

	@Autowired
	private IResourceBundleRepository	iResourceBundleRepository	= null;

	private static final Locale			DEFAULT_LOCALE_CODE			= Locale.US;

	@Override
	protected MessageFormat resolveCode(String a_key, Locale a_locale) {

		String			message		= null;
		MessageFormat	mfReturn	= null;
		if (a_locale == null) {
			a_locale = DEFAULT_LOCALE_CODE;
		}

		message = iResourceBundleRepository.findByKeyAndLanguageCode(a_key, a_locale.toString(),
				DEFAULT_LOCALE_CODE.toString(), Constant.RecordStatus.INSERTED.getStatus());
		if (message != null) {
			mfReturn = new MessageFormat(message, a_locale);
		} else {
			mfReturn = new MessageFormat("???" + a_key + "???");
		}

		return mfReturn;
	}

}