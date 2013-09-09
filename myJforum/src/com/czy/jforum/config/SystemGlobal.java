package com.czy.jforum.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import com.czy.jforum.util.VariableExpander;
import com.czy.jforum.util.VariableStore;

public class SystemGlobal implements VariableStore{

	private static SystemGlobal systemGlobal;
	private String configFilePath;
	private static Properties systemProperties = new Properties();

	private VariableExpander expand = new VariableExpander(this,"${","}");
	private SystemGlobal() {
	}

	public static void init(String appPath, String configFilePath) {
		systemGlobal = new SystemGlobal();
		systemGlobal.buildSystemGlobal(appPath,configFilePath);
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
}
