package com.czy.jforum.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.czy.jforum.JForumExecutionContext;
import com.czy.jforum.config.ConfigLoader;

/**
 * 根据传过来的 module name来 通过配置文件和自己的缓存，获取相对应的 Class Name
 * @author chen9_000
 *
 */
public class ModulesRepository {
	private static final Logger logger = Logger
			.getLogger(ModulesRepository.class);

	private static Map cache = new HashMap();
	private static final String ENTRIES = "entries";

	public static void init(String baseDir) {
		cache.put(ENTRIES, ConfigLoader.loadModulesMapping(baseDir));
	}

	public static int size() {
		return cache.size();
	}

	public static String getModuleClass(String moduleName) {
		Properties p = (Properties) cache.get(ENTRIES);

		if (p == null) {
			logger.error("Null modules. Askes moduleName: " + moduleName
					+ ", url="
					+ JForumExecutionContext.getRequest().getQueryString());
			return null;
		}

		return p.getProperty(moduleName);
	}
}
