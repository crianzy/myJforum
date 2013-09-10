package com.czy.jforum;

import java.util.HashMap;
import java.util.Map;

public class UrlPatternCollection {

	private static Map patternsMap = new HashMap();

	public static UrlPattern findPattern(String name) {
		return (UrlPattern) UrlPatternCollection.patternsMap.get(name);
	}

	public static void addPattern(String name, String value) {
		UrlPatternCollection.patternsMap.put(name, new UrlPattern(name, value));
	}
}
