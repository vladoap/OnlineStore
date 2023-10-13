package com.example.MyStore.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class CloudinaryBeanConfiguration {

    private final CloudinaryConfigurationProperties properties;

    public CloudinaryBeanConfiguration(CloudinaryConfigurationProperties properties) {
        this.properties = properties;
    }

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(Map.of(
                "cloud_name", properties.getCloudName(),
                "api_key", properties.getApiKey(),
                "api_secret", properties.getApiSecret()
        ));
    }
}
