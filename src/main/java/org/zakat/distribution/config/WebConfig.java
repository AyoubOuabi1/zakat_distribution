package org.zakat.distribution.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String externalDir = "file:/var/www/html/aktion/";
        registry.addResourceHandler("/api/uploads/bank-details/**")
                .addResourceLocations(externalDir);
    }

}
