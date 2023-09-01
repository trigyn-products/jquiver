package com.trigyn.jws.usermanagement.utils;

import java.util.Hashtable;

import javax.naming.Context;

import org.springframework.ldap.core.support.LdapContextSource;

/**
 * 
 * 
 * {@summary}
 * @author Shrinath.Halki
 * @since  26-OCT-2022
 *
 */
public class SSLLdapContextSource extends LdapContextSource {
	
	public Hashtable<String, Object> getAnonymousEnv(){
        System.setProperty("com.sun.jndi.ldap.object.disableEndpointIdentification", "true");
        Hashtable<String, Object> anonymousEnv = super.getAnonymousEnv();
        anonymousEnv.put("java.naming.security.protocol", "ssl");
        anonymousEnv.put("java.naming.ldap.factory.socket", LDAPCustomSSLSocketFactory.class.getName());
        anonymousEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        return anonymousEnv;
    }

}
