package com.example.project.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedOrigins("http://trng2309-10.s3-website.us-east-2.amazonaws.com/register")
                        .allowedOrigins("http://localhost:8080/register")
                        .allowedOrigins("http://localhost:8090")
                        .allowedOrigins("http://localhost:8080/register")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowCredentials(true);
                        
            }
        };
    }
}
