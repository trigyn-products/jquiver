package com.trigyn.jws.dynarest.utils;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author: aNIRUDDHA
 * @since: 16-Mar-2023
 */

@Component
public class ViewFiler extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest a_request, HttpServletResponse a_response, FilterChain a_filterChain) throws ServletException, IOException {
		
		final String requestURL = a_request.getRequestURL().toString();
		
		a_filterChain.doFilter(a_request, new HttpServletResponseWrapper(a_response) {
			private Map<String, String> header = new HashMap<String, String>();
			
			{
				header.put("Powered-By", "JQuiver");
			}
			
			@Override
			public void setHeader(String a_name, String a_value) {
				System.out.println("Setting header : " + a_name + " : " + a_value);
				super.setHeader(a_name, a_value);
				if(a_name == null) {
					return;
				}
				if(a_value == null) {
					header.remove(a_name);
				}
				header.put(a_name, a_value);
			}
			
			@Override
			public boolean containsHeader(String a_name) {
				if(a_name == null) {
					return false;
				}
				return header.containsKey(a_name);
			}
			
			@Override
			public Collection<String> getHeaderNames() {
				System.out.println("ViewFiler.doFilterInternal(...).new HttpServletResponseWrapper() {...}.getHeaderNames()");
				return header.keySet();
			}
			
			@Override
			public String getHeader(String a_name) {
				if(a_name == null) {
					return null;
				}
				System.out.println(requestURL);
				System.out.println("ViewFiler.doFilterInternal(...).new HttpServletResponseWrapper() {...}.getHeader() " + a_name + " = " + header.get(a_name));
				if(header.get(a_name) == null) {
					return super.getHeader(a_name);
				}
				return header.get(a_name);
			}
			
			@Override
			public void addHeader(String a_name, String a_value) {
				super.addHeader(a_name, a_value);
				if(a_name == null) {
					return;
				}
				if(a_value == null) {
					header.remove(a_name);
				}
				header.put(a_name, a_value);
			}
		});		
	}

}
