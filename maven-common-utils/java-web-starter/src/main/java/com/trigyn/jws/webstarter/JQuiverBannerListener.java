package com.trigyn.jws.webstarter;

import java.io.IOException;

import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;

import com.github.lalyos.jfiglet.FigletFont;

public class JQuiverBannerListener implements ApplicationListener<ApplicationStartingEvent> {
    @Override
    public void onApplicationEvent(ApplicationStartingEvent event) {
		try {
			String banner = FigletFont.convertOneLine("JQuiver 2.x");
	        System.out.println(banner);
		} catch (IOException e) {
			
		}
    }
}