package com.trigyn.jws.dynarest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.dynarest.repository.JqschedulerRepository;

@Service
@Transactional
public class SchedulerService {

	@Autowired
	private JqschedulerRepository	jqschedulerRepository	= null;

	public void deleteScheduler(String schedulerID) {
		jqschedulerRepository.deleteById(schedulerID);
	}
}
