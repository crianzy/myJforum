package com.czy.jforum;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.jforum.ForumStartup;

import org.apache.log4j.Logger;

import com.czy.jforum.config.ConfigKey;
import com.czy.jforum.config.SystemGlobal;
import com.czy.jforum.context.JForumContext;
import com.czy.jforum.context.RequestContext;
import com.czy.jforum.context.ResponseContext;
import com.czy.jforum.context.web.WebRequestContext;
import com.czy.jforum.context.web.WebResponseContext;
import com.czy.jforum.repository.ModulesRepository;

import freemarker.template.SimpleHash;
import freemarker.template.Template;

public class JForum extends JForumBaseServlet {

	private static boolean isDatabaseUp;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		super.startApplication();
		//TODO 数据库是否准备好
		isDatabaseUp = false;
		//TODO 数据库的相关配置
		
		JForumExecutionContext ex = JForumExecutionContext.get();
		//在ex 中加入 conn
		//ex.setConnection(conn);
		JForumExecutionContext.set(ex);
		
		//初始化基本的 仓库
		
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		Writer out = null;
		JForumContext forumContext = null;
		RequestContext request = null;
		ResponseContext response = null;
		String encoding = SystemGlobal.getValue(ConfigKey.ENCODING);

		try {
			// 初始啊 execute context
			JForumExecutionContext ex = JForumExecutionContext.get();
			
			request = new WebRequestContext(req);
			response = new WebResponseContext(res);
			//TODO 检查数据库是否准备好
			forumContext = new JForumContext(request.getContextPath(),
					SystemGlobal.getValue(ConfigKey.SERVLET_EXTENSION),
					request, response);
			ex.setForumContext(forumContext);
			JForumExecutionContext.set(ex);

			SimpleHash context = JForumExecutionContext.getTemplateContext();

			ControllerUtils utils = new ControllerUtils();
			utils.prepareTemplateContext(context, forumContext);

			String module = request.getModule();
			String moduleClass = module != null ? ModulesRepository
					.getModuleClass(module) : null;

			context.put("request", req);
			context.put("response", response);

			out = this.processCommand(out, request, response, encoding,
					context, moduleClass);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Writer processCommand(Writer out, RequestContext request,
			ResponseContext response, String encoding, SimpleHash context,
			String moduleClass) throws Exception {
		Command c = this.retrieveCommand(moduleClass);
		Template template = c.process(request, response, context);
		if (JForumExecutionContext.getRedirectTo() == null) {
			String contentType = JForumExecutionContext.getContentType();

			if (contentType == null) {
				contentType = "text/html; charset=" + encoding;
			}
			response.setContentType(contentType);
			if (!JForumExecutionContext.isCustomContent()) {
				out = new BufferedWriter(new OutputStreamWriter(
						response.getOutputStream(), encoding));
				template.process(JForumExecutionContext.getTemplateContext(),
						out);
				out.flush();
			}
		}
		return out;
	}

	private com.czy.jforum.Command retrieveCommand(String moduleClass)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return (Command) Class.forName(moduleClass).newInstance();
	}

}
