package com.czy.jforum;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.czy.jforum.config.ConfigKey;
import com.czy.jforum.config.ConfigLoader;
import com.czy.jforum.config.SystemGlobal;
import com.czy.jforum.repository.ModulesRepository;
import com.czy.jforum.repository.Tpl;

public class JForumBaseServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(JForumBaseServlet.class);

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String appPath = config.getServletContext().getRealPath("");
		ConfigLoader.startSystemGlobal(appPath);
		SystemGlobal.getValue(ConfigKey.TEST);

		ModulesRepository.init(SystemGlobal.getValue(ConfigKey.CONFIG_DIR));
		this.loadConfigStuff();

	}

	protected void loadConfigStuff() {
		ConfigLoader.loadUrlPatterns();
		Tpl.load(SystemGlobal.getValue(ConfigKey.TEMPLATES_MAPPING));
		// I18n.load();
		// Tpl.load(SystemGlobals.getValue(ConfigKeys.TEMPLATES_MAPPING));
		// BB Code
		// BBCodeRepository.setBBCollection(new BBCodeHandler().parse());
	}
	// @Override
	// protected void service(HttpServletRequest req, HttpServletResponse res)
	// throws ServletException, IOException {
	// PrintWriter out = res.getWriter();
	// out.print("hello"+SystemGlobal.getValue(ConfigKey.TEST_EXPAND));
	// }

}
