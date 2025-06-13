
package com.trigyn.jws.formio.vo;

public class Trigger {

    private String type;
    private String javascript;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJavascript() {
        return javascript;
    }

    public void setJavascript(String javascript) {
        this.javascript = javascript;
    }

	@Override
	public String toString() {
		return "Trigger [type=" + type + ", javascript=" + javascript + "]";
	}

}
