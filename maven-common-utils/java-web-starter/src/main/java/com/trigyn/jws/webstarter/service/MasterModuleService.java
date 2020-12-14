package com.trigyn.jws.webstarter.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.webstarter.dao.IMasterModuleDAO;
import com.trigyn.jws.webstarter.entities.MasterModule;

@Service
@Transactional
public class MasterModuleService {

	@Autowired
	private IMasterModuleDAO masterModuleDAO 					= null;
	
	public List<MasterModule> getModules() {
		
		List<MasterModule> masterModules = new ArrayList<>();
		masterModules = masterModuleDAO.findAll(Sort.by(Sort.Direction.ASC, "masterModuleName"));
		
		return masterModules;
	}
}
