package com.shavika.websever.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

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

        String[] origins = allowedOrigins.split(",");
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Allow all endpoints
                        .allowedOrigins(origins)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // HTTP methods
                        .allowedHeaders("*")
                        .allowCredentials(true);// Allow all headers
            }
        };
    }
}
