package com.czy.jforum.util;

import java.util.HashMap;
import java.util.Map;

/**
 * ���������ļ��� ${xxx} �ص�����
 * @author chen9_000
 *
 */
public class VariableExpander {

	private String pre;
	private String end;
	private VariableStore variable;

	// �洢֮ǰ�����������ݣ������ٴν���
	private Map<String, String> cache;

	public VariableExpander(VariableStore variable, String pre, String end) {
		this.variable = variable;
		this.pre = pre;
		this.end = end;
		cache = new HashMap<String, String>();
	}

	public void cleanCache() {
		cache.clear();
	}

	public String expandVariable(String source) {
		String result = cache.get(source);
		if (result != null || source == null) {
			return result;
		}
		int fIndex = source.indexOf(this.pre);
		if (fIndex == -1) {
			return source;
		}

		StringBuffer sb = new StringBuffer(source);

		while (fIndex > -1) {
			int rIndex = sb.indexOf(this.end);
			int start = fIndex + this.pre.length();
			String varName = sb.substring(start, rIndex);
			sb.replace(fIndex, rIndex + 1,
					this.variable.getVariableValue(varName));
			fIndex = sb.indexOf(this.pre);
		}
		result = sb.toString();
		this.cache.put(source, result);
		return result;

	}

}
