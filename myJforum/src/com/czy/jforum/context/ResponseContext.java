package com.czy.jforum.context;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;

public interface ResponseContext {

	public void setContentLength(int len);

	public boolean containsHeader(String name);

	public void setHeader(String name, String value);

	public void addCookie(Cookie cookie);

	public String encodeRedirectURL(String url);

	public String getCharacterEncoding();

	public void sendRedirect(String location) throws IOException;

	public ServletOutputStream getOutputStream() throws IOException;

	public PrintWriter getWriter() throws IOException;

	public void setContentType(String type);

	public String encodeURL(String url);

	public void addHeader(String name, String value);

	public void sendError(int sc) throws IOException;

}
