package com.czy.jforum;

import java.io.IOException;

import com.czy.jforum.config.ConfigKey;
import com.czy.jforum.config.SystemGlobal;
import com.czy.jforum.context.RequestContext;
import com.czy.jforum.context.ResponseContext;

import freemarker.template.SimpleHash;
import freemarker.template.Template;

public abstract class Command {

	private static Class[] NO_ARGS_CLASS = new Class[0];
	private static Object[] NO_ARGS_OBJECT = new Object[0];

	private boolean ignoreAction;
	protected String templateName;
	protected RequestContext request;
	protected ResponseContext response;
	protected SimpleHash context;

	protected void setTemplateName(String templateName) {
		// this.templateName = Tpl.name(templateName);
	}

	protected void ignoreAction() {
		this.ignoreAction = true;
	}

	public abstract void list();

	public Template process(RequestContext request, ResponseContext response,
			SimpleHash context) {
		this.request = request;
		this.response = response;
		this.context = context;
		String action = this.request.getAction();
		if (!this.ignoreAction) {
			try {
				this.getClass().getMethod(action, NO_ARGS_CLASS)
						.invoke(this, NO_ARGS_OBJECT);
			} catch (NoSuchMethodException e) {
				this.list();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		// if (JForumExecutionContext.getRedirectTo() != null) {
		// this.setTemplateName(TemplateKey.EMPTY);
		// }
		if (request.getAttribute("template") != null) {
			this.setTemplateName((String) request.getAttribute("template"));
		}
		try {
			return JForumExecutionContext.templateConfig().getTemplate(
					new StringBuffer(SystemGlobal
							.getValue(ConfigKey.TEMPLATE_DIR)).append('/')
							.append(this.templateName).toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}
