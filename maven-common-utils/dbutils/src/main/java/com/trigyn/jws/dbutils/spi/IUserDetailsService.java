package com.trigyn.jws.dbutils.spi;

import java.util.List;

import org.springframework.stereotype.Service;

import com.trigyn.jws.dbutils.vo.UserDetailsVO;

@Service
public interface IUserDetailsService {

	UserDetailsVO getUserDetails();

	List<String> getUsersRoleHavingAccessToJWS();

}
