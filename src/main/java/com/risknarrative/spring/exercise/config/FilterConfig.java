package com.risknarrative.spring.exercise.config;

import com.risknarrative.spring.exercise.filter.CustomRequestContextFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<CustomRequestContextFilter> customRequestContextFilter() {
        FilterRegistrationBean<CustomRequestContextFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CustomRequestContextFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}