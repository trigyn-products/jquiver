package com.trigyn.jws.usermanagement.utils;

import java.util.UUID;
import java.util.Vector;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

@Component
public final class SessionCounter implements HttpSessionListener {

	private final static Logger		logger				= LoggerFactory.getLogger(SessionCounter.class);
	private static Vector<String>	httpSessionVector	= new Vector<String>();
	public static final String		COUNTER				= "session-counter";

	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		logger.debug("SessionCounter.sessionCreated");
		String		jqUserId	= UUID.randomUUID().toString();
		HttpSession	httpSession	= httpSessionEvent.getSession();
		httpSession.getServletContext().setAttribute("jqUserId", jqUserId);

		synchronized (httpSessionVector) {
			httpSessionVector.add(jqUserId);
		}
		logger.debug("After session created. jqUserId{}:- httpSessionVectorSize:{}- ", jqUserId, httpSessionVector.size());
	}

	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		logger.debug("SessionCounter.sessionDestroyed");
		HttpSession	httpSession	= httpSessionEvent.getSession();
		String		jqUserId	= (String) httpSession.getServletContext().getAttribute("jqUserId");
		synchronized (httpSessionVector) {
			httpSessionVector.remove(jqUserId);
		}
		logger.debug("After session destroyed. jqUserId{}:- httpSessionVectorSize:{}- ", jqUserId, httpSessionVector.size());
	}

	public int getActiveSessionCount() {
		return httpSessionVector.size();
	}
}