package com.shavika.websever.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve React static files from the /static directory
        registry.addResourceHandler("/web/static/**")
                .addResourceLocations("classpath:/static/static/");
    }


    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Forward all non-API and non-static requests to index.html
        registry.addViewController("/web")
                .setViewName("forward:/index.html");
        registry.addViewController("/web/")
                .setViewName("forward:/index.html"); // Handles trailing slash
        registry.addViewController("/web/{path:[^\\.]*}")
                .setViewName("forward:/index.html");
        registry.addViewController("/web/{path:[^\\.]*}/")
                .setViewName("forward:/index.html"); // Handles trailing slash for paths
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Allow all endpoints
                        .allowedOriginPatterns("*") // Allow React app
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // HTTP methods
                        .allowedHeaders("*"); // Allow all headers
            }
        };
    }
}
