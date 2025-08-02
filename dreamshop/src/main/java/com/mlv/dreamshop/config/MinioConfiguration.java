package com.mlv.dreamshop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mlv.dreamshop.proporties.MinioProperties;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MinioConfiguration {
    // @Autowired
    private final MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioProperties.getEndpoint())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }
}
