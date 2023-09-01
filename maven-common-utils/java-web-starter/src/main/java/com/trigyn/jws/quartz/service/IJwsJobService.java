package com.trigyn.jws.quartz.service;

import org.springframework.stereotype.Service;


@Service
public interface IJwsJobService {
	
	void executeSchedular(String id);

}
