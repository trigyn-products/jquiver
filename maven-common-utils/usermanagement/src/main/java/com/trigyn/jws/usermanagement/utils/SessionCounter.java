package com.trigyn.jws.usermanagement.utils;

import java.util.UUID;
import java.util.Vector;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public final class SessionCounter implements HttpSessionListener {

	private final static Logger		logger				= LogManager.getLogger(SessionCounter.class);
	private static Vector<String>	httpSessionVector	= new Vector<String>();
	public static final String		COUNTER				= "session-counter";

	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		logger.info("SessionCounter.sessionCreated");
		String		jqUserId	= UUID.randomUUID().toString();
		HttpSession	httpSession	= httpSessionEvent.getSession();
		httpSession.getServletContext().setAttribute("jqUserId", jqUserId);

		synchronized (httpSessionVector) {
			httpSessionVector.add(jqUserId);
		}
		logger.info("After session created. jqUserId{}:- httpSessionVectorSize:{}- ", jqUserId, httpSessionVector.size());
	}

	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		logger.info("SessionCounter.sessionDestroyed");
		HttpSession	httpSession	= httpSessionEvent.getSession();
		String		jqUserId	= (String) httpSession.getServletContext().getAttribute("jqUserId");
		synchronized (httpSessionVector) {
			httpSessionVector.remove(jqUserId);
		}
		logger.info("After session destroyed. jqUserId{}:- httpSessionVectorSize:{}- ", jqUserId, httpSessionVector.size());
	}

	public int getActiveSessionCount() {
		return httpSessionVector.size();
	}
}