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
	 * �����Լ�����
	 */
	private static SystemGlobal systemGlobal = new SystemGlobal();

	/**
	 * �����ļ���·��
	 */
	private String configFilePath;
	/**
	 * �������ļ� �� systemGolbal.properties ���Ӧ�������ļ�
	 */
	private static Properties systemProperties = new Properties();

	/**
	 * ���ݿ���ص������ļ�
	 */
	private static Properties queries = new Properties();

	/**
	 * ��װ��ص������ļ�
	 */
	private Properties installation = new Properties();

	/**
	 * ��ӹ��������ļ� ���б�
	 */
	private static List additionalDefaultsList = new ArrayList();

	/**
	 * ������� �����ļ��� ��ֵ�� �Ķ���
	 */
	private VariableExpander expand = new VariableExpander(this, "${", "}");

	private SystemGlobal() {
	}

	public static void init(String appPath, String configFilePath) {
		systemGlobal = new SystemGlobal();
		systemGlobal.buildSystemGlobal(appPath, configFilePath);
	}

	/**
	 * ���� SystemGlobals ��ȡ�����ļ�����������س�Ա�����ĸ�ֵ
	 * 
	 * @param appPath
	 *            application ��path like localhost:8080/jforum
	 * @param configFilePath
	 *            SystemGlobal.properties ��path
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
	 * ��ȡ�����ļ��е�����
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
	 * ��ȡ����ļ��������� queries.load(fis);
	 * 
	 * @param queryFile
	 *            �ļ���
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
