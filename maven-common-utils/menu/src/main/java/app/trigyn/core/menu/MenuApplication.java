package app.trigyn.core.menu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
public class MenuApplication {

	public static void main(String[] args) {
		SpringApplication.run(MenuApplication.class, args);
	}
	
	@Bean
	public DispatcherServlet dispatcherServlet () {
	    DispatcherServlet dispatcherServlet = new DispatcherServlet();
	    dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
	    return dispatcherServlet;
	}

}
