package com.czy.jforum.context;

import java.util.Enumeration;

public interface SessionContext {

	public void setAttribute(String name, Object value);

	public void removeAttribute(String name);

	public Object getAttribute(String name);

	public String getId();

	public Enumeration getAttributeNames();

	public void invalidate();
}
