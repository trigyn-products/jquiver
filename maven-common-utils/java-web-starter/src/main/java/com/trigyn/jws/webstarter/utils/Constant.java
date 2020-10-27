package com.trigyn.jws.webstarter.utils;

public final class Constant {

    private Constant() {
        //
    }
	
    public enum TargetLookupId {
        DASHBOARD(1), DYANMICFORM(2), DYNAMICREST(3), MODELANDVIEW(4), TEMPLATE(5);

        final int lookupId;

        TargetLookupId(int i) {
            lookupId = i;
        }

        public int getTargetLookupId() {
            return lookupId;
        }
    }
    
    public static final String TEMPLATE_TYPE_LOOKUP				= "TEMPLATE_TYPE";
    public static final String DYNAMIC_FORM_TYPE_LOOKUP			= "DYNAMIC_FORM_TYPE";
    public static final Integer DYNAMIC_FORM_DEFAULT_TYPE_ID	= 1;
    public static final String DASHBOARD_TYPE_LOOKUP			= "DASHBOARD_TYPE";
    public static final String DASHLET_TYPE_LOOKUP				= "DASHLET_TYPE";
}
