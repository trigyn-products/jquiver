package com.trigyn.jws.gridutils.utility;

import java.util.List;

public class FilterParams {
    
    private String groupOp = null;
    
    private List<SearchFields> rules = null;

    public FilterParams(String groupOp, List<SearchFields> rules) {
	super();
	this.groupOp = groupOp;
	this.rules = rules;
    }

    public FilterParams() {
	super();
    }

    public String getGroupOp() {
        return groupOp;
    }

    public void setGroupOp(String groupOp) {
        this.groupOp = groupOp;
    }

    public List<SearchFields> getRules() {
        return rules;
    }

    public void setRules(List<SearchFields> rules) {
        this.rules = rules;
    }

    
    
}
