package kganesh1795_hackerpolls.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kganesh1795_hackerpolls.config.security.SecurityPrincipleInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login");
	}
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new SecurityPrincipleInterceptor()).addPathPatterns("/admin/**", "/user/**");
	}
}