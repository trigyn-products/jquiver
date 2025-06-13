package com.trigyn.jws.quartz.config;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TriggerListner implements TriggerListener {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(TriggerListner.class);

    @Override
    public String getName() {
        return "globalTrigger";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
    	LOGGER.debug("TriggerListner.triggerFired()");
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
    	LOGGER.debug("TriggerListner.vetoJobExecution()");
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
    	LOGGER.debug("TriggerListner.triggerMisfired()");
        String jobName = trigger.getJobKey().getName();
        LOGGER.debug("Job name: " + jobName + " is misfired");
        
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, CompletedExecutionInstruction triggerInstructionCode) {
        LOGGER.debug("TriggerListner.triggerComplete()");
    }
}
