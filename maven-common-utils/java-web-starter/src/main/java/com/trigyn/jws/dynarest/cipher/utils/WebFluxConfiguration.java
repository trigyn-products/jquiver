/**
 * 
 */
package com.trigyn.jws.dynarest.cipher.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

/**
 * @author Mini.Pillai
 *
 */
@Configuration
public class WebFluxConfiguration implements WebFluxConfigurer {
	
	public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
		configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024);
	}

}
