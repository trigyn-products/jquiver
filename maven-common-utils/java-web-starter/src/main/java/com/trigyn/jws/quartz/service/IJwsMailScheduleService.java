package com.trigyn.jws.quartz.service;

import com.trigyn.jws.quartz.models.entities.request.EmailSchedulerRequestVo;

public interface IJwsMailScheduleService {
	
	public boolean sendMail(EmailSchedulerRequestVo emailVo) throws Exception;
	
}
