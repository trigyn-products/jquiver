package com.trigyn.jws.usermanagement.security.config;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.trigyn.jws.usermanagement.entities.JwsUser;
import com.trigyn.jws.usermanagement.repository.JwsUserRepository;
import com.trigyn.jws.usermanagement.repository.JwsUserRoleAssociationRepository;
import com.trigyn.jws.usermanagement.utils.Constants;
import com.trigyn.jws.usermanagement.vo.JwsRoleVO;

public class DefaultUserDetailsServiceImpl implements UserDetailsService {

	private JwsUserRepository userRepository = null;

	private JwsUserRoleAssociationRepository userRoleAssociationRepository = null;
	

	public DefaultUserDetailsServiceImpl(JwsUserRepository userRepository, JwsUserRoleAssociationRepository userRoleAssociationRepository) {
		this.userRepository = userRepository;
		this.userRoleAssociationRepository = userRoleAssociationRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		JwsUser user = userRepository.findByEmailIgnoreCase(email);
		if (user == null ) {
			throw new UsernameNotFoundException("Not found!");
		}
		
		List<JwsRoleVO> rolesVOs =  userRoleAssociationRepository.getUserRoles(Constants.ISACTIVE, user.getUserId());
		return new UserInformation(user, rolesVOs);
	}

}
