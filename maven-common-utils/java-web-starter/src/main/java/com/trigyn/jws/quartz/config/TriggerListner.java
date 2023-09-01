package com.trigyn.jws.quartz.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;
import org.springframework.stereotype.Component;

@Component
public class TriggerListner implements TriggerListener {
	
	private final static Logger LOGGER = LogManager.getLogger(TriggerListner.class);

    @Override
    public String getName() {
        return "globalTrigger";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
    	LOGGER.info("TriggerListner.triggerFired()");
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
    	LOGGER.info("TriggerListner.vetoJobExecution()");
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
    	LOGGER.info("TriggerListner.triggerMisfired()");
        String jobName = trigger.getJobKey().getName();
        LOGGER.info("Job name: " + jobName + " is misfired");
        
    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context, CompletedExecutionInstruction triggerInstructionCode) {
        LOGGER.info("TriggerListner.triggerComplete()");
    }
}
