package com.czy.jforum.context.web;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringUtils;

import com.czy.jforum.UrlPattern;
import com.czy.jforum.UrlPatternCollection;
import com.czy.jforum.config.ConfigKey;
import com.czy.jforum.config.SystemGlobal;
import com.czy.jforum.context.RequestContext;
import com.czy.jforum.context.SessionContext;

public class WebRequestContext extends HttpServletRequestWrapper implements
		RequestContext {
	private Map query;

	/**
	 * Default constructor.
	 * 
	 * @param superRequest
	 *            Original <code>HttpServletRequest</code> instance
	 * @throws IOException
	 */
	public WebRequestContext(HttpServletRequest superRequest)
			throws IOException {
		super(superRequest);

		this.query = new HashMap();
		boolean isMultipart = false;

		String requestType = superRequest.getMethod().toUpperCase();
		String contextPath = superRequest.getContextPath();
		String requestUri = this.extractRequestUri(
				superRequest.getRequestURI(), contextPath);
		String encoding = SystemGlobal.getValue(ConfigKey.ENCODING);
		String servletExtension = SystemGlobal
				.getValue(ConfigKey.SERVLET_EXTENSION);

		boolean isPost = "POST".equals(requestType);
		boolean isGet = !isPost;

		boolean isQueryStringEmpty = (superRequest.getQueryString() == null || superRequest
				.getQueryString().length() == 0);

		if (isGet && isQueryStringEmpty
				&& requestUri.endsWith(servletExtension)) {
			superRequest.setCharacterEncoding(encoding);
			this.parseFriendlyURL(requestUri, servletExtension);
		} else if (isPost) {
			// isMultipart = ServletFileUpload
			// .isMultipartContent(new ServletRequestContext(superRequest));

			if (isMultipart) {
				this.handleMultipart(superRequest, encoding);
			}
		}

		if (!isMultipart) {
			boolean isAjax = "XMLHttpRequest".equals(superRequest
					.getHeader("X-Requested-With"));

			if (!isAjax) {
				superRequest.setCharacterEncoding(encoding);
			} else {
				// Ajax requests are *usually* sent using
				// application/x-www-form-urlencoded; charset=UTF-8.
				// In JForum, we assume this as always true.
				superRequest.setCharacterEncoding("UTF-8");
			}

			String containerEncoding = SystemGlobal
					.getValue(ConfigKey.DEFAULT_CONTAINER_ENCODING);

			if (isPost) {
				containerEncoding = encoding;
			}

			for (Enumeration e = superRequest.getParameterNames(); e
					.hasMoreElements();) {
				String name = (String) e.nextElement();

				String[] values = superRequest.getParameterValues(name);

				if (values != null && values.length > 1) {
					for (int i = 0; i < values.length; i++) {
						this.addParameter(
								name,
								new String(values[i]
										.getBytes(containerEncoding), encoding));
					}
				} else {
					this.addParameter(name, new String(superRequest
							.getParameter(name).getBytes(containerEncoding),
							encoding));
				}
			}

			if (this.getModule() == null && this.getAction() == null) {
				int index = requestUri.indexOf('?');

				if (index > -1) {
					requestUri = requestUri.substring(0, index);
				}

				this.parseFriendlyURL(requestUri, servletExtension);
			}
		}
	}

	/**
	 * @param requestUri
	 * @param servletExtension
	 */
	private void parseFriendlyURL(String requestUri, String servletExtension) {
		requestUri = requestUri.substring(0, requestUri.length()
				- servletExtension.length());
		String[] urlModel = requestUri.split("/");

		int moduleIndex = 1;
		int actionIndex = 2;
		int baseLen = 3;

		UrlPattern url = null;

		if (urlModel.length >= baseLen) {
			// <moduleName>.<actionName>.<numberOfParameters>
			StringBuffer sb = new StringBuffer(64)
					.append(urlModel[moduleIndex]).append('.')
					.append(urlModel[actionIndex]).append('.')
					.append(urlModel.length - baseLen);

			url = UrlPatternCollection.findPattern(sb.toString());
		}

		if (url != null) {
			if (url.getSize() >= urlModel.length - baseLen) {
				for (int i = 0; i < url.getSize(); i++) {
					this.addParameter(url.getVars()[i], urlModel[i + baseLen]);
				}
			}

			this.addOrReplaceParameter("module", urlModel[moduleIndex]);
			this.addParameter("action", urlModel[actionIndex]);
		} else {
			this.addOrReplaceParameter("module", null);
			this.addParameter("action", null);
		}
	}

	public SessionContext getSessionContext(boolean create) {
		return new WebSessionContext(this.getSession(true));
	}

	public SessionContext getSessionContext() {
		return new WebSessionContext(this.getSession());
	}

	/**
	 * @param superRequest
	 *            HttpServletRequest
	 * @param encoding
	 *            String
	 * @throws UnsupportedEncodingException
	 */
	private void handleMultipart(HttpServletRequest superRequest,
			String encoding) throws UnsupportedEncodingException {
		String tmpPath = new StringBuffer(256)
				.append(SystemGlobal.getApplicationPath()).append('/')
				.append(SystemGlobal.getValue(ConfigKey.TMP_DIR)).toString();

		File tmpDir = new File(tmpPath);
		boolean success = false;

		try {
			if (!tmpDir.exists()) {
				tmpDir.mkdirs();
				success = true;
			}
		} catch (Exception e) {
			// We won't log it because the directory
			// creation failed for some reason - a SecurityException
			// or something else. We don't care about it, as the
			// code below tries to use java.io.tmpdir
		}

		if (!success) {
			tmpPath = System.getProperty("java.io.tmpdir");
			tmpDir = new File(tmpPath);
		}

		/*
		 * ServletFileUpload upload = new ServletFileUpload( new
		 * DiskFileItemFactory(100 * 1024, tmpDir));
		 * upload.setHeaderEncoding(encoding);
		 * 
		 * try { List items = upload.parseRequest(superRequest);
		 * 
		 * for (Iterator iter = items.iterator(); iter.hasNext();) { FileItem
		 * item = (FileItem) iter.next();
		 * 
		 * if (item.isFormField()) { this.addParameter(item.getFieldName(),
		 * item.getString(encoding)); } else { if (item.getSize() > 0) { // We
		 * really don't want to call addParameter(), as // there should not be
		 * possible to have multiple // values for a InputStream data
		 * this.query.put(item.getFieldName(), item); } } } } catch
		 * (FileUploadException e) { throw new MultipartHandlingException(
		 * "Error while processing multipart content: " + e); }
		 */
	}

	public String[] getParameterValues(String name) {
		Object value = this.getObjectParameter(name);

		if (value instanceof String) {
			return new String[] { (String) value };
		}

		List l = (List) value;

		return l == null ? super.getParameterValues(name) : (String[]) l
				.toArray(new String[0]);
	}

	private String extractRequestUri(String requestUri, String contextPath) {
		// First, remove the context path from the requestUri,
		// so we can work only with the important stuff
		if (contextPath != null && contextPath.length() > 0) {
			requestUri = requestUri.substring(contextPath.length(),
					requestUri.length());
		}

		// Remove the "jsessionid" (or similar) from the URI
		// Probably this is not the right way to go, since we're
		// discarding the value...
		int index = requestUri.indexOf(';');

		if (index > -1) {
			int lastIndex = requestUri.indexOf('?', index);

			if (lastIndex == -1) {
				lastIndex = requestUri.indexOf('&', index);
			}

			if (lastIndex == -1) {
				requestUri = requestUri.substring(0, index);
			} else {
				String part1 = requestUri.substring(0, index);
				requestUri = part1 + requestUri.substring(lastIndex);
			}
		}

		return requestUri;
	}

	public String getParameter(String parameter) {
		return (String) this.query.get(parameter);
	}

	public int getIntParameter(String parameter) {
		return Integer.parseInt(this.getParameter(parameter));
	}

	public Object getObjectParameter(String parameter) {
		return this.query.get(parameter);
	}

	public void addParameter(String name, Object value) {
		if (!this.query.containsKey(name)) {
			this.query.put(name, value);
		} else {
			Object currentValue = this.getObjectParameter(name);
			List l;

			if (!(currentValue instanceof List)) {
				l = new ArrayList();
				l.add(currentValue);
			} else {
				l = (List) currentValue;
			}

			l.add(value);
			this.query.put(name, l);
		}
	}

	public void addOrReplaceParameter(String name, Object value) {
		this.query.put(name, value);
	}

	public String getAction() {
		return this.getParameter("action");
	}

	public void changeAction(String newAction) {
		if (this.query.containsKey("action")) {
			this.query.remove("action");
			this.query.put("action", newAction);
		} else {
			this.addParameter("action", newAction);
		}
	}

	public String getModule() {
		return this.getParameter("module");
	}

	public Object getObjectRequestParameter(String parameter) {
		return this.query.get(parameter);
	}

	public String getContextPath() {
		String contextPath = super.getContextPath();
		String proxiedContextPath = SystemGlobal
				.getValue(ConfigKey.PROXIED_CONTEXT_PATH);

		if (!StringUtils.isEmpty(proxiedContextPath)) {
			contextPath = proxiedContextPath;
		}

		return contextPath;
	}

	public String getRemoteAddr() {
		// We look if the request is forwarded
		// If it is not call the older function.
		String ip = super.getHeader("x-forwarded-for");

		if (ip == null) {
			ip = super.getRemoteAddr();
		} else {
			// Process the IP to keep the last IP (real ip of the computer on
			// the net)
			StringTokenizer tokenizer = new StringTokenizer(ip, ",");

			// Ignore all tokens, except the last one
			for (int i = 0; i < tokenizer.countTokens() - 1; i++) {
				tokenizer.nextElement();
			}

			ip = tokenizer.nextToken().trim();

			if (ip.equals("")) {
				ip = null;
			}
		}

		// If the ip is still null, we put 0.0.0.0 to avoid null values
		if (ip == null) {
			ip = "0.0.0.0";
		}

		return ip;
	}

}
