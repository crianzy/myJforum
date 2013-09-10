package com.czy.jforum;

/**
 * http://localhost:8080/webappName/someDir/myServlet/news/view/3.page
 * We only want the part
 * <i>news/view/3.page</i> ( where .page is the servlet extension ).
 * news.view.2 = page, news_id
 * Here, <i>news.view.1</i> is the pattern's name, and <i>news_id</i> is
 * the patterns itself. <br>
 * 
 * news.view.2 = page, news_id
 * 有两个值则放进数组中
 * 
 * @author chen9_000
 *
 */
public class UrlPattern {

	private String name;
	private String value;
	private int size;
	private String[] vars;

	public UrlPattern(String name, String value) {
		this.name = name;
		this.value = value;

		this.processPattern();
	}

	private void processPattern() {
		String[] p = this.value.split(",");

		this.vars = new String[p.length];
		this.size = ((((p[0]).trim()).equals("")) ? 0 : p.length);

		for (int i = 0; i < this.size; i++) {
			this.vars[i] = (p[i]).trim();
		}
	}

	public String getName() {
		return this.name;
	}

	public int getSize() {
		return this.size;
	}

	public String[] getVars() {
		return this.vars;
	}
}
