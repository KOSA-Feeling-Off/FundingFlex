package com.fundingflex.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/images/fundings/**")
        .addResourceLocations("file:src/main/resources/static/images/fundings/");
		
		registry.addResourceHandler("/images/qa/**")
        .addResourceLocations("file:src/main/resources/static/images/qa/");
	}
	
}
