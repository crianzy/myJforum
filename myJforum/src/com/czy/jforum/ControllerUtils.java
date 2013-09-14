package com.czy.jforum;

import sun.security.util.SecurityConstants;

import com.czy.jforum.config.ConfigKey;
import com.czy.jforum.config.SystemGlobal;
import com.czy.jforum.context.ForumContext;
import com.czy.jforum.context.RequestContext;
import com.sun.org.apache.xml.internal.security.utils.I18n;

import freemarker.template.SimpleHash;

/**
 * Common methods used by the controller.
 * 
 * @author chen9_000
 */
public class ControllerUtils {

	public void prepareTemplateContext(SimpleHash context,
			ForumContext jforumContext) {
		RequestContext request = JForumExecutionContext.getRequest();

		context.put("contextPath", request.getContextPath());
		context.put("serverName", request.getServerName());
		context.put("extension",
				SystemGlobal.getValue(ConfigKey.SERVLET_EXTENSION));
		context.put("serverPort", Integer.toString(request.getServerPort()));
		context.put("forumTitle",
				SystemGlobal.getValue(ConfigKey.FORUM_PAGE_TITLE));
		context.put("pageTitle",
				SystemGlobal.getValue(ConfigKey.FORUM_PAGE_TITLE));
		context.put("metaKeywords",
				SystemGlobal.getValue(ConfigKey.FORUM_PAGE_METATAG_KEYWORDS));
		context.put("metaDescription",
				SystemGlobal.getValue(ConfigKey.FORUM_PAGE_METATAG_DESCRIPTION));
		context.put("forumLink", SystemGlobal.getValue(ConfigKey.FORUM_LINK));
		context.put("homepageLink",
				SystemGlobal.getValue(ConfigKey.HOMEPAGE_LINK));
		context.put("encoding", SystemGlobal.getValue(ConfigKey.ENCODING));
		context.put("JForumContext", jforumContext);
		context.put("timestamp", new Long(System.currentTimeMillis()));
	}
}
