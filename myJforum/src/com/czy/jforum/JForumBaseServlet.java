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
			// ��ȡ���ݿ���ص�����
			SystemGlobal.loadQueries(SystemGlobal
					.getValue(ConfigKey.SQL_QUERIES_GENERIC));
			SystemGlobal.loadQueries(SystemGlobal
					.getValue(ConfigKey.SQL_QUERIES_DRIVER));
			// ��ȡ quartz-jforum.preperties ���ļ���
			String filename = SystemGlobal.getValue(ConfigKey.QUARTZ_CONFIG);
			// ��ȡ quartz-jforum.preperties
			SystemGlobal.loadAdditionalDefaults(filename);
			//TODO  configLoader ���
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
			// ��ȡ�����ļ��ľ���·���������Ӳ�̵�G��//.....
			String appPath = config.getServletContext().getRealPath("");
			debug = "true".equals(config.getInitParameter("development"));
			logger.info("Starting Jforum Debug mode is " + debug);

			// ��ʼ��ȫ�������ļ�
			ConfigLoader.startSystemGlobal(appPath);
			// ����ģ������
			Configuration templateCfg = new Configuration();
			templateCfg.setTemplateUpdateDelay(2);
			templateCfg.setSetting("number_format", "#");
			templateCfg.setSharedVariable("startupTime",
					new Long(new Date().getTime()));
			// ���� Ĭ�ϵ�ģ�� loader
			String defaultPath = SystemGlobal.getApplicationPath()
					+ "/templates";
			FileTemplateLoader defaultLoader = new FileTemplateLoader(new File(
					defaultPath));
			// TODO �˴���ʱֻ����Ĭ�ϵ�ģ�档
			templateCfg.setTemplateLoader(defaultLoader);

			// ��ʼ�� ģ��ֿ�
			ModulesRepository.init(SystemGlobal.getValue(ConfigKey.CONFIG_DIR));
			// ��ȡ������һЩ�������
			this.loadConfigStuff();
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * ��ȡ������������� ��ȡ url mapping
	 * 
	 */
	protected void loadConfigStuff() {
		// ��ȡurl ������Ϣ�������ļ�
		ConfigLoader.loadUrlPatterns();
		// ��ȡģ�����������Ϣ���Ǹ�ģ�������Ӧ��صľ���html�ļ�
		// ֮���Ѿ����ع�Ĭ�ϵ� ģ�����棨���������ģ��Ŀ¼����������ֻҪ��Ӧ�������ļ���
		Tpl.load(SystemGlobal.getValue(ConfigKey.TEMPLATES_MAPPING));
	}

}
