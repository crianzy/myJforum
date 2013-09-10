package com.czy.jforum.context.web;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;

import com.czy.jforum.context.SessionContext;

public class WebSessionContext implements SessionContext {

	private HttpSession httpSession;

	public WebSessionContext(HttpSession httpSession) {
		this.httpSession = httpSession;
	}

	public void setAttribute(String name, Object value) {
		httpSession.setAttribute(name, value);
	}

	public void removeAttribute(String name) {
		httpSession.removeAttribute(name);
	}

	public Object getAttribute(String name) {
		return httpSession.getAttribute(name);
	}

	public String getId() {
		return httpSession.getId();
	}

	public Enumeration getAttributeNames() {
		return httpSession.getAttributeNames();
	}

	public void invalidate() {
		httpSession.invalidate();
	}

}
