package app.trigyn.common.dbutils.spi;

import java.util.List;

import org.springframework.stereotype.Service;

import app.trigyn.common.dbutils.vo.UserDetailsVO;

@Service
public interface IUserDetailsService {

	UserDetailsVO getUserDetails();
	
	List<String> getUsersRoleHavingAccessToJWS();

}
