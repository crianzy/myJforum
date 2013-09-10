package com.czy.jforum;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.czy.jforum.config.ConfigKey;
import com.czy.jforum.config.SystemGlobal;
import com.czy.jforum.context.JForumContext;
import com.czy.jforum.context.RequestContext;
import com.czy.jforum.context.ResponseContext;
import com.czy.jforum.context.web.WebRequestContext;
import com.czy.jforum.context.web.WebResponseContext;

public class JForum extends JForumBaseServlet {
	private static Logger logger = Logger.getLogger(JForum.class);

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		JForumContext forumContext = null;
		RequestContext request = null;
		ResponseContext response = null;
		String encoding = SystemGlobal.getValue(ConfigKey.ENCODING);
		try {
			// Initializes the execution context
			// JForumExecutionContext ex = JForumExecutionContext.get();
			request = new WebRequestContext(req);
			response = new WebResponseContext(res);
			forumContext = new JForumContext(request.getContextPath(),
					SystemGlobal.getValue(ConfigKey.SERVLET_EXTENSION),
					request, response);
			// ex.setForumContext(forumContext);
			// JForumExecutionContext.set(ex);
			out.println(request.getAction() );
			out.println(request.getModule());
			out.println(request.getContextPath());
			out.println(request.getLocale());
			out.println(request.getRemoteAddr());
			out.println(request.getScheme());
			out.println(request.getParameter("parm"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
