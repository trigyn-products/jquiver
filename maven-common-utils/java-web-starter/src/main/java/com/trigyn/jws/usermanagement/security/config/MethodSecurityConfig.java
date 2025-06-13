package com.trigyn.jws.usermanagement.security.config;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;

//@Configuration
//@EnableMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig {//  extends DefaultMethodSecurityExpressionHandler {

//	@Autowired
//	private PermissionEvaluator customPermissionEvalutor = null;

//	@Override
	protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
//		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
////		expressionHandler.setPermissionEvaluator(customPermissionEvalutor);
//		return expressionHandler;
		
//		MethodSecurityExpressionRoot expressionHandler = new MethodSecurityExpressionRoot(authentication);
//	return expressionHandler;
		return null;
	}
}