package com.czy.jforum.exceptions;

public class CacheEngineStartupException extends RuntimeException
{
	public CacheEngineStartupException(String message, Throwable t)
	{
		super(message, t);
		this.setStackTrace(t.getStackTrace());
	}
}