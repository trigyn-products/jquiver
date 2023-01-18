package com.trigyn.jws.dynarest.cipher.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.NoSuchElementException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.StringUtils;

public class HttpServletRequestWritableWrapper extends HttpServletRequestWrapper {

	private HttpServletRequest	orginalRequest;

	private String				modifyRequestBody;

	private String				contentType;

	public HttpServletRequestWritableWrapper(HttpServletRequest orginalRequest, String modifyRequestBody) {
		this(orginalRequest, modifyRequestBody, null);
	}

	public HttpServletRequestWritableWrapper(HttpServletRequest orginalRequest, String modifyRequestBody,
			String contentType) {
		super(orginalRequest);
		this.modifyRequestBody	= modifyRequestBody;
		this.orginalRequest		= orginalRequest;
		this.contentType		= contentType;
	}

	@Override
	public ServletInputStream getInputStream() throws UnsupportedEncodingException {
		return new ServletInputStream() {
			private InputStream in = new ByteArrayInputStream((orginalRequest.getCharacterEncoding() != null) ? 
					modifyRequestBody.getBytes(orginalRequest.getCharacterEncoding()) : modifyRequestBody.getBytes());

			@Override
			public int read() throws IOException {
				return in.read();
			}

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {

			}
		};
	}

	@Override
	public int getContentLength() {
		try {
			return modifyRequestBody.getBytes(orginalRequest.getCharacterEncoding()).length;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public long getContentLengthLong() {
		try {
			return modifyRequestBody.getBytes(orginalRequest.getCharacterEncoding()).length;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public String getContentType() {
		return StringUtils.isBlank(contentType) ? orginalRequest.getContentType() : contentType;
	}

	@Override
	public Enumeration<String> getHeaders(String name) {
		if (null != name && name.replace("-", "").toLowerCase().equals("contenttype")
				&& !StringUtils.isBlank(contentType)) {
			return new Enumeration<String>() {
				private boolean hasGetted = false;

				@Override
				public boolean hasMoreElements() {
					return !hasGetted;
				}

				@Override
				public String nextElement() {
					if (hasGetted) {
						throw new NoSuchElementException();
					} else {
						hasGetted = true;
						return contentType;
					}
				}
			};
		}
		return super.getHeaders(name);
	}
	
	 public BufferedReader getReader() throws IOException {
		 return new BufferedReader(new InputStreamReader(this.getInputStream()));
		 
	 }

	public String getModifyRequestBody() {
		return modifyRequestBody;
	}
}
