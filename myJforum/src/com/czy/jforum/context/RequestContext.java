package com.czy.jforum.context;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;

import javax.servlet.http.Cookie;

public interface RequestContext {

	/**
	 * ����uri
	 * 
	 * @return
	 */
	public String getRequestURI();

	/**
	 * ����url path ����� String
	 * 
	 * @return
	 */
	public String getQueryString();

	/**
	 * ���� request ��ͷ����Ϣ��û���򷵻�null
	 * 
	 * @param name
	 *            head name
	 * @return
	 */
	public String getHeader(String name);

	/**
	 * ���� cookies ����
	 * 
	 * @return
	 */
	public Cookie[] getCookies();

	/**
	 * ����ip��ַ���ͻ��ˣ������ϣ�
	 * 
	 * @return
	 */
	public String getRemoteAddr();

	/**
	 * ���� ���� request���� �����Ķ˿�
	 * 
	 * @return
	 */
	public int getServerPort();

	/**
	 * ���� Э�����ͣ�http https ����
	 * 
	 * @return
	 */
	public String getScheme();

	/**
	 * ���ط����������� ��ǰ�� ��localhost��������ip��ַ
	 * 
	 * @return
	 */
	public String getServerName();

	/**
	 * ɾ��һ�� attribute
	 * 
	 * @param name
	 */
	public void removeAttribute(String name);

	/**
	 * ���һ�� attribute
	 * 
	 * @param name
	 * @param o
	 */
	public void setAttribute(String name, Object o);

	/**
	 * ��ȡattribute
	 * 
	 * @param name
	 *            attribute name
	 * @return
	 */
	public Object getAttribute(String name);

	/**
	 * ���ñ�������
	 * 
	 * @param env
	 * @throws UnsupportedEncodingException
	 */
	public void setCharacterEncoding(String env)
			throws UnsupportedEncodingException;

	/**
	 * ��ȡsession �������ô����ڷ�
	 * 
	 * @param create
	 * @return
	 */
	public SessionContext getSessionContext(boolean create);

	/**
	 * ��ȡsession û�оʹ���һ��
	 * 
	 * @return
	 */
	public SessionContext getSessionContext();

	/**
	 * ���� contextPath
	 * 
	 * @return
	 */
	public String getContextPath();

	/**
	 * ���ص�¼��user
	 * 
	 * @return
	 */
	public String getRemoteUser();

	/**
	 * ����int ���͵Ĳ���
	 * 
	 * @param parameter
	 * @return
	 */
	public int getIntParameter(String parameter);

	/**
	 * ����String ���� ����
	 * 
	 * @param name
	 * @return
	 */
	public String[] getParameterValues(String name);

	/**
	 * ���ز�����ֵ
	 * 
	 * @param name
	 * @return
	 */
	public String getParameter(String name);

	/**
	 * ���ز����� ������
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
	 * ��Ӳ���
	 * 
	 * @param name
	 * @param value
	 */
	public void addParameter(String name, Object value);

	/**
	 * ��ӻ��滻����
	 * 
	 * @param name
	 * @param value
	 */
	public void addOrReplaceParameter(String name, Object value);

	/**
	 * ����object ����
	 * 
	 * @param parameter
	 * @return
	 */
	public Object getObjectParameter(String parameter);

	/**
	 * ���� ����� local ���� ���� I18n
	 * 
	 * @return
	 */
	public Locale getLocale();

}
