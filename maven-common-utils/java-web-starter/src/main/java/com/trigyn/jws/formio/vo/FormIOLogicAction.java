package com.trigyn.jws.formio.vo;

import java.util.Arrays;
import java.util.List;

public class FormIOLogicAction {

	private String			name;
	private Trigger			trigger;
	private String[]		actions;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}

	public String[] getActions() {
		return actions;
	}

	public void setActions(String[] actions) {
		this.actions = actions;
	}

	@Override
	public String toString() {
		return "FormIOLogicAction [name=" + name + ", trigger=" + trigger + ", actions=" + Arrays.toString(actions)
				+ "]";
	}

}
