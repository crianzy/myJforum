package com.czy.jforum;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.czy.jforum.config.ConfigKey;
import com.czy.jforum.config.ConfigLoader;
import com.czy.jforum.config.SystemGlobal;

public class JForumBaseServlet extends HttpServlet {

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String appPath = config.getServletContext().getRealPath("");
		ConfigLoader.startSystemGlobal(appPath);
		SystemGlobal.getValue(ConfigKey.TEST);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		out.print("hello"+SystemGlobal.getValue(ConfigKey.TEST_EXPAND));
	}
	
	

	

}
