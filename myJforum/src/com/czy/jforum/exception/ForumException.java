package com.czy.jforum.exception;

public class ForumException extends RuntimeException{
	public ForumException(String message) {
		super(message);
	}

	public ForumException(Throwable t) {
		this(t.toString(), t);
	}

	public ForumException(String message, Throwable t) {
		super(message, t);
		this.setStackTrace(t.getStackTrace());
	}
}
