package com.czy.jforum.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.czy.jforum.JForumExecutionContext;
import com.czy.jforum.config.ConfigLoader;

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
