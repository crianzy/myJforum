package com.czy.jforum.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.czy.jforum.UrlPatternCollection;

public class ConfigLoader {

	public static void startSystemGlobal(String appPath) {
		SystemGlobal.init(appPath, appPath
				+ "/WEB-INF/config/SystemGlobal.properties");
	}

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
}
