package com.mlv.dreamshop.proporties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "minio")
@Component
public class MinioProperties {
    private String accessKey;    // Bỏ @Value
    private String secretKey;    // Bỏ @Value  
    private String endpoint;     // Bỏ @Value
    // private String bucketName;   // Thêm bucketName
}
