/*
 * Copyright 2000-2014 Namics AG. All rights reserved.
 */

package com.namics.oss.spring.support.batch.web.config;

import com.namics.oss.spring.support.batch.service.JobService;
import com.namics.oss.spring.support.batch.web.controller.JobDataController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * override Namics' class...
 */
@Configuration
public class SpringBatchSupportWebServletConfig extends WebMvcConfigurationSupport {
	@Override
	protected void addViewControllers(ViewControllerRegistry registry) {
		super.addViewControllers(registry);
		registry.addViewController("/").setViewName("redirect:overview.html");
	}

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		super.addResourceHandlers(registry);
		registry.addResourceHandler("/*.html").addResourceLocations("classpath:/META-INF/spring-batch/terrific/assets/");
		registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/META-INF/spring-batch/terrific/assets/font/");
		registry.addResourceHandler("/**/*.html").addResourceLocations("classpath:/META-INF/spring-batch/terrific/");
		registry.addResourceHandler("/**/*.css", "/**/*.js").addResourceLocations("classpath:/META-INF/spring-batch/terrific/");
	}

	@Override
	protected void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable("defaultServlet");
	}

	@Bean
	public JobDataController jobDataController(JobService jobService) {
		return new JobDataController(jobService);
	}


}
