package com.czy.jforum.context;

public class JForumContext implements ForumContext {

	private String contextPath;
	private String servletExtension;

	private RequestContext request;
	private ResponseContext response;

	private boolean isEncodingDisabled;
	private boolean isBot;

	public JForumContext(String contextPath, String servletExtension,
			RequestContext request, ResponseContext response) {

		this.contextPath = contextPath;
		this.servletExtension = servletExtension;
		this.request = request;
		this.response = response;
		this.isBot = false;
		this.isEncodingDisabled = isBot;
	}

	public JForumContext(String contextPath, String servletExtension,
			RequestContext request, ResponseContext response,
			boolean isEncodingDisabled) {
		this.contextPath = contextPath;
		this.servletExtension = servletExtension;
		this.request = request;
		this.response = response;
		this.isEncodingDisabled = isEncodingDisabled;

		Boolean isBotObject = false;
		this.isBot = (isBotObject != null && isBotObject.booleanValue());
	}

	public String encodeURL(String url) {
		return this.encodeURL(url, servletExtension);
	}

	public String encodeURL(String url, String extension) {
		String ucomplete = contextPath + url + extension;

		if (isEncodingDisabled()) {
			return ucomplete;
		}

		return response.encodeURL(ucomplete);
	}

	@Override
	public ResponseContext getResponse() {
		return this.response;
	}

	@Override
	public RequestContext getRequest() {
		return this.request;
	}

	@Override
	public boolean isBot() {
		return this.isBot;
	}

	public boolean isEncodingDisabled()
	{
		return this.isEncodingDisabled;
	}
	
}
