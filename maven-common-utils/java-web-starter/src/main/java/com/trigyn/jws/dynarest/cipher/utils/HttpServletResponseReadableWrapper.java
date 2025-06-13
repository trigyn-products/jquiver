package com.trigyn.jws.dynarest.cipher.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.apache.commons.lang3.StringUtils;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

public class HttpServletResponseReadableWrapper extends HttpServletResponseWrapper {

	private HttpServletResponse		originalResponse;

	private ByteArrayOutputStream	baos;

	private ServletOutputStream		out;

	private PrintWriter				writer;

	public HttpServletResponseReadableWrapper(HttpServletResponse resp) {
		super(resp);
		this.originalResponse	= resp;
		this.baos				= new ByteArrayOutputStream();
		this.out				= new SubServletOutputStream(baos);
		this.writer				= new PrintWriter(new OutputStreamWriter(baos));
	}

	@Override
	public ServletOutputStream getOutputStream() {
		return out;
	}

	@Override
	public PrintWriter getWriter() {
		return writer;
	}

	public String getResponseBody() throws IOException {
		return this.getResponseBody(null);
	}

	public String getResponseBody(String charset) throws IOException {
		out.flush();
		writer.flush();
		return new String(baos.toByteArray(), StringUtils.isBlank(charset) ? this.getCharacterEncoding() : charset);
	}

	class SubServletOutputStream extends ServletOutputStream {
		private ByteArrayOutputStream baos;

		public SubServletOutputStream(ByteArrayOutputStream baos) {
			this.baos = baos;
		}

		@Override
		public void write(int b) {
			baos.write(b);
		}

		@Override
		public void write(byte[] b) {
			baos.write(b, 0, b.length);
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setWriteListener(WriteListener writeListener) {

		}
	}

}
