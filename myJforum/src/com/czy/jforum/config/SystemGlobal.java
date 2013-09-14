package com.czy.jforum.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.czy.jforum.util.VariableExpander;
import com.czy.jforum.util.VariableStore;

public class SystemGlobal implements VariableStore {
	public static Logger logger = Logger.getLogger(SystemGlobal.class);

	/**
	 * 单粒自己本身
	 */
	private static SystemGlobal systemGlobal = new SystemGlobal();

	/**
	 * 配置文件的路径
	 */
	private String configFilePath;
	/**
	 * 主配置文件 与 systemGolbal.properties 相对应的配置文件
	 */
	private static Properties systemProperties = new Properties();

	/**
	 * 数据库相关的配置文件
	 */
	private static Properties queries = new Properties();

	/**
	 * 安装相关的配置文件
	 */
	private Properties installation = new Properties();

	/**
	 * 添加过的配置文件 的列表
	 */
	private static List additionalDefaultsList = new ArrayList();

	/**
	 * 解析相关 配置文件中 键值队 的对象
	 */
	private VariableExpander expand = new VariableExpander(this, "${", "}");

	private SystemGlobal() {
	}

	public static void init(String appPath, String configFilePath) {
		systemGlobal = new SystemGlobal();
		systemGlobal.buildSystemGlobal(appPath, configFilePath);
	}

	/**
	 * 构建 SystemGlobals 读取配置文件，并进行相关成员变量的赋值
	 * 
	 * @param appPath
	 *            application 的path like localhost:8080/jforum
	 * @param configFilePath
	 *            SystemGlobal.properties 的path
	 *            localhost:8080/jfprum/WEB-INF/config/SystemGlobal.properties
	 */
	private void buildSystemGlobal(String appPath, String configFilePath) {
		if (configFilePath == null) {
			throw new RuntimeException("configFilePath can not be null");
		}
		this.configFilePath = configFilePath;
		systemProperties.put(ConfigKey.APP_PATH, appPath);
		systemProperties.put(ConfigKey.SYSTEM_GLOBAL_PATH, configFilePath);
		loadConfigFilePath();

	}

	/**
	 * 读取配置文件中的内容
	 */
	public void loadConfigFilePath() {
		try {
			FileInputStream fin = new FileInputStream(
					systemGlobal.configFilePath);
			systemProperties.load(fin);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getValue(String key) {
		return systemGlobal.getVariableValue(key);
	}

	@Override
	public String getVariableValue(String key) {
		String preExpention = systemGlobal.systemProperties.getProperty(key);
		return expand.expandVariable(preExpention);
	}

	public static Object getApplicationPath() {
		return getValue(ConfigKey.APP_PATH);
	}

	public static boolean getBoolValue(String field) {
		return "true".equals(getValue(field));
	}

	/**
	 * 读取相关文件，并放入 queries.load(fis);
	 * 
	 * @param queryFile
	 *            文件名
	 */
	public static void loadQueries(String queryFile) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(queryFile);
			queries.load(fis);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void loadAdditionalDefaults(String filename) {
		if (!new File(filename).exists()) {
			logger.info("cannot find file " + filename + ".will ingore it");
		}
		try {
			FileInputStream input = new FileInputStream(filename);
			systemGlobal.installation.load(input);
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!additionalDefaultsList.contains(filename)) {
			additionalDefaultsList.add(filename);
		}
	}
}
