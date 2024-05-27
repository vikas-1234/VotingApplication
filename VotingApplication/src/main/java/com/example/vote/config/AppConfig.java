package com.example.vote.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class AppConfig {

    @Bean
    public FilterRegistrationBean<CorsConfig> corsFilter() {
        FilterRegistrationBean<CorsConfig> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new CorsConfig());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}