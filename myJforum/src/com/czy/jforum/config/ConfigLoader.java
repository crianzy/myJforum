package com.czy.jforum.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.czy.jforum.UrlPatternCollection;
import com.czy.jforum.exception.ForumException;

/**
 * �����ļ���ȡ��
 * 
 * @author chen9_000
 * 
 */
public class ConfigLoader {
	public static Logger logger = Logger.getLogger(ConfigLoader.class);

	/**
	 * ��ȡסȫ�������ļ�
	 * 
	 * @param appPath
	 */
	public static void startSystemGlobal(String appPath) {
		SystemGlobal.init(appPath, appPath
				+ "/WEB-INF/config/SystemGlobal.properties");
	}

	/**
	 * ��ȡ url���� module action ���� ��ι滮����������ļ�
	 */
	public static void loadUrlPatterns() {
		FileInputStream fis = null;
		try {
			Properties p = new Properties();
			fis = new FileInputStream(
					SystemGlobal.getValue(ConfigKey.CONFIG_DIR)
							+ "/urlPattern.properties");
			p.load(fis);

			for (Iterator iter = p.entrySet().iterator(); iter.hasNext();) {
				Map.Entry entry = (Map.Entry) iter.next();
				UrlPatternCollection.addPattern((String) entry.getKey(),
						(String) entry.getValue());
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if (fis != null) {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
	}

	public static Properties loadModulesMapping(String baseConfigDir) {
		FileInputStream fis = null;

		try {
			Properties modulesMapping = new Properties();
			fis = new FileInputStream(baseConfigDir
					+ "/modulesMapping.properties");
			modulesMapping.load(fis);

			return modulesMapping;
		} catch (IOException e) {
			throw new RuntimeException();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
				}
			}
		}
	}

	public static void loadDaoImplementation() {
		String driver = SystemGlobal.getValue(ConfigKey.DAO_DRIVER);
		logger.info("Loading JDBC driver " + driver);
		try {
			Class c = Class.forName(driver);
			//DataAccessDriver d = (DataAccessDriver) c.newInstance();
			//DataAccessDriver.init(d);
		} catch (Exception e) {
			throw new ForumException(e);
		}
	}

}
