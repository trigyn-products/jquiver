package com.trigyn.jws.dbutils.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;

public class CustomCharacterEscapeHandler implements CharacterEscapeHandler {

	public void escape(char[] buf, int start, int len, boolean isAttValue, Writer out) throws IOException {
		StringWriter buffer = new StringWriter();

		for (int i = start; i < start + len; i++) {
			buffer.write(buf[i]);
		}

		String st = buffer.toString();

		if (!st.contains("CDATA")) {
			st = buffer.toString().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
					.replace("'", "&apos;").replace("\"", "&quot;");

		}
		out.write(st);
	}

}
