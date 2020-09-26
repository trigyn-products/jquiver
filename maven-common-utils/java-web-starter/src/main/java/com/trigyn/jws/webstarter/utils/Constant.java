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
}
