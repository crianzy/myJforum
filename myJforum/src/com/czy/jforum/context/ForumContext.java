package com.czy.jforum.context;

public interface ForumContext {

	ResponseContext getResponse();

	RequestContext getRequest();

	boolean isBot();
}
