package com.echo.echo_backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // URL: http://localhost:8080/uploads/파일명
        // 실제 파일 위치: C:/uploads/파일명
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadDir);
    }
}
