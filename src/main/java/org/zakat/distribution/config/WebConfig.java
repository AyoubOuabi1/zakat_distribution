package org.zakat.distribution.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String resourcesDir = null;
        try {
            resourcesDir = new ClassPathResource("uploads/bank-details/").getURI().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        registry.addResourceHandler("/api/uploads/bank-details/**")
                .addResourceLocations(resourcesDir);
    }

}
