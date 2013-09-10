package com.czy.jforum.context;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.http.Cookie;

public interface RequestContext {

	/**
	 * 返回uri
	 * 
	 * @return
	 */
	public String getRequestURI();

	/**
	 * 返回url path 后面的 String
	 * 
	 * @return
	 */
	public String getQueryString();

	/**
	 * 返回 request 的头部信息，没有则返回null
	 * 
	 * @param name
	 *            head name
	 * @return
	 */
	public String getHeader(String name);

	/**
	 * 返回 cookies 数组
	 * 
	 * @return
	 */
	public Cookie[] getCookies();

	/**
	 * 返回ip地址（客户端，或代理断）
	 * 
	 * @return
	 */
	public String getRemoteAddr();

	/**
	 * 返回 发送 request请求 机器的端口
	 * 
	 * @return
	 */
	public int getServerPort();

	/**
	 * 返回 协议类型，http https 。。
	 * 
	 * @return
	 */
	public String getScheme();

	/**
	 * 返回服务器名就是 ：前的 像localhost，或者是ip地址
	 * 
	 * @return
	 */
	public String getServerName();

	/**
	 * 删除一个 attribute
	 * 
	 * @param name
	 */
	public void removeAttribute(String name);

	/**
	 * 添加一个 attribute
	 * 
	 * @param name
	 * @param o
	 */
	public void setAttribute(String name, Object o);

	/**
	 * 获取attribute
	 * 
	 * @param name
	 *            attribute name
	 * @return
	 */
	public Object getAttribute(String name);

	/**
	 * 设置编码类型
	 * 
	 * @param env
	 * @throws UnsupportedEncodingException
	 */
	public void setCharacterEncoding(String env)
			throws UnsupportedEncodingException;

	/**
	 * 获取session 参数觉得创建于否
	 * 
	 * @param create
	 * @return
	 */
	public SessionContext getSessionContext(boolean create);

	/**
	 * 获取session 没有就创建一个
	 * 
	 * @return
	 */
	public SessionContext getSessionContext();

	/**
	 * 返回 contextPath
	 * 
	 * @return
	 */
	public String getContextPath();

	/**
	 * 返回登录的user
	 * 
	 * @return
	 */
	public String getRemoteUser();

	/**
	 * 返回int 类型的参数
	 * 
	 * @param parameter
	 * @return
	 */
	public int getIntParameter(String parameter);

	/**
	 * 返回String 数组 参数
	 * 
	 * @param name
	 * @return
	 */
	public String[] getParameterValues(String name);

	/**
	 * 返回参赛的值
	 * 
	 * @param name
	 * @return
	 */
	public String getParameter(String name);

	/**
	 * 返回参数的 迭代器
	 * 
	 * @return
	 */
	public Enumeration getParameterNames();

	/**
	 * http://www.host.com/webapp/servletName/groups/list action's name is
	 * "list".
	 * 
	 * @return
	 */
	public String getAction();

	/**
	 * http://www.host.com/webapp/servletName?module=groups&action=list
	 * http://www.host.com/webapp/servletName/groups/list module's name is
	 * "groups".
	 * 
	 * @return
	 */
	public String getModule();

	/**
	 * 添加参数
	 * 
	 * @param name
	 * @param value
	 */
	public void addParameter(String name, Object value);

	/**
	 * 添加或替换参数
	 * 
	 * @param name
	 * @param value
	 */
	public void addOrReplaceParameter(String name, Object value);

	/**
	 * 返回object 参数
	 * 
	 * @param parameter
	 * @return
	 */
	public Object getObjectParameter(String parameter);

	/**
	 * 返回 浏览器 local 语言 用于 I18n
	 * 
	 * @return
	 */
	public Locale getLocale();

}
