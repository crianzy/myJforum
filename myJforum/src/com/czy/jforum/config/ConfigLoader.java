package com.czy.jforum.config;

public class ConfigLoader {
	
	public static void startSystemGlobal(String appPath){
		SystemGlobal.init(appPath,appPath+"/WEB-INF/config/SystemGlobal.properties");
	}

}
