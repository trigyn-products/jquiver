package app.trigyn.common.dbutils.configurations;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import app.trigyn.common.dbutils.spi.IUserDetailsService;
import app.trigyn.common.dbutils.vo.UserDetailsVO;

@Order(1)
@Component
public class JwsAuthenticationFilter implements Filter {

	@Autowired
	private IUserDetailsService userDetailsService = null;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		UserDetailsVO userDetailsVO = userDetailsService.getUserDetails();
		if(userDetailsVO != null && Boolean.FALSE.equals(userDetailsVO.getRoleIdList().isEmpty())) {
			List<String> accessRoleList = userDetailsService.getUsersRoleHavingAccessToJWS();
			String role = userDetailsVO.getRoleIdList().get(0);
			if(Boolean.TRUE.equals(accessRoleList.contains(role))) {
				chain.doFilter(request, response);
			} else {
				HttpServletResponse httpServletResponse = (HttpServletResponse) response;
				httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have to JWS framework");
			}
		} else {
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			httpServletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have to JWS framework");
		}
	}

}
