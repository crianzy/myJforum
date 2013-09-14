package com.czy.jforum;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;

import com.czy.jforum.config.ConfigKey;
import com.czy.jforum.config.ConfigLoader;
import com.czy.jforum.config.SystemGlobal;
import com.czy.jforum.repository.ModulesRepository;
import com.czy.jforum.repository.Tpl;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class JForumBaseServlet extends HttpServlet {

	private static final long serialVersionUID = -1723358586540418904L;

	private static Logger logger = Logger.getLogger(JForumBaseServlet.class);
	protected boolean debug;

	protected void startApplication() {
		try {
			// 读取数据库相关的配置
			SystemGlobal.loadQueries(SystemGlobal
					.getValue(ConfigKey.SQL_QUERIES_GENERIC));
			SystemGlobal.loadQueries(SystemGlobal
					.getValue(ConfigKey.SQL_QUERIES_DRIVER));
			// 获取 quartz-jforum.preperties 的文件名
			String filename = SystemGlobal.getValue(ConfigKey.QUARTZ_CONFIG);
			// 读取 quartz-jforum.preperties
			SystemGlobal.loadAdditionalDefaults(filename);
			//TODO  configLoader 相关
			// ConfigLoader.createLoginAuthenticator();
			ConfigLoader.loadDaoImplementation();
			// ConfigLoader.listenForChanges();
			// ConfigLoader.startSearchIndexer();
			// ConfigLoader.startSummaryJob();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		try {
			// 获取配置文件的绝对路径。相对于硬盘的G：//.....
			String appPath = config.getServletContext().getRealPath("");
			debug = "true".equals(config.getInitParameter("development"));
			logger.info("Starting Jforum Debug mode is " + debug);

			// 初始化全局配置文件
			ConfigLoader.startSystemGlobal(appPath);
			// 配置模版引擎
			Configuration templateCfg = new Configuration();
			templateCfg.setTemplateUpdateDelay(2);
			templateCfg.setSetting("number_format", "#");
			templateCfg.setSharedVariable("startupTime",
					new Long(new Date().getTime()));
			// 创建 默认的模版 loader
			String defaultPath = SystemGlobal.getApplicationPath()
					+ "/templates";
			FileTemplateLoader defaultLoader = new FileTemplateLoader(new File(
					defaultPath));
			// TODO 此处暂时只设置默认的模版。
			templateCfg.setTemplateLoader(defaultLoader);

			// 初始化 模块仓库
			ModulesRepository.init(SystemGlobal.getValue(ConfigKey.CONFIG_DIR));
			// 读取其他的一些相关配置
			this.loadConfigStuff();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 读取其他的相关配置 读取 url mapping
	 * 
	 */
	protected void loadConfigStuff() {
		// 读取url 解析信息的配置文件
		ConfigLoader.loadUrlPatterns();
		// 读取模版配置相关信息。那个模版参数对应相关的具体html文件
		// 之间已经加载过默认的 模版引擎（里面包含有模版目录）所以这里只要对应的配置文件名
		Tpl.load(SystemGlobal.getValue(ConfigKey.TEMPLATES_MAPPING));
	}

}
