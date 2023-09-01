package com.trigyn.jws.dbutils.spi;

public class DynarestApiDetails {

	private String	compiledClassPath	= null;

	private String	compiledClassName	= null;

	public String getCompiledClassPath() {
		return compiledClassPath;
	}

	public String getCompiledClassName() {
		return compiledClassName;
	}

	public void setCompiledClassPath(String compiledClassPath) {
		this.compiledClassPath = compiledClassPath;
	}

	public void setCompiledClassName(String compiledClassName) {
		this.compiledClassName = compiledClassName;
	}

}
