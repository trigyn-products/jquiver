package com.trigyn.jws.usermanagement.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.utils.Constants;

@Service
@Transactional
public class JwsUserService {
	
	@Autowired
	private JwsUserRepository jwsUserRepository = null;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveUser(String name,String email) {
		JwsUser jwsUser = new JwsUser();
		jwsUser.setFirstName(name.split(" ")[0]);
		jwsUser.setLastName(name.split(" ")[1]);
		jwsUser.setPassword(null);
		jwsUser.setSecretKey(null);
		jwsUser.setIsActive(Constants.ISACTIVE);
		jwsUser.setForcePasswordChange((Constants.INACTIVE));
		jwsUser.setEmail(email);
		
		jwsUserRepository.save(jwsUser);
//		jwsUserRepository.flush();
	}
	
	public JwsUser findUserByEmail(String  email) {
		return jwsUserRepository.findByEmailIgnoreCase(email);
	}

}
