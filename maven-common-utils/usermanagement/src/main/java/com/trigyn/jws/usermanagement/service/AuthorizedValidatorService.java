package com.trigyn.jws.usermanagement.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.usermanagement.repository.AuthorizedValidatorDAO;

@Service
@Transactional
public class AuthorizedValidatorService {

	@Autowired
	private AuthorizedValidatorDAO authorizedValidatorDAO = null;

	public boolean  hasAccessToCurrentDynamicForm(String formId, List<String> roleNames) {
		boolean hasAccess = false;
		Long count = authorizedValidatorDAO.hasAccessToCurrentDynamicForm(formId,roleNames);
		if(count > 0) {
			hasAccess = true;
		}
		return hasAccess;
		
	}

	
	
}
