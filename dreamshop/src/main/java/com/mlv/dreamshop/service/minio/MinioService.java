package com.mlv.dreamshop.service.minio;



import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.SetBucketPolicyArgs;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MinioService {
    private final MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    @PostConstruct
    private void init() {
        log.info("Initializing MinioService...");
        log.info("MinIO Endpoint: {}", minioClient.toString());
        log.info("Bucket Name: {}", bucketName);
        
        try {
            createBucket(bucketName);
            log.info("MinioService initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize MinioService: {}", e.getMessage(), e);
            throw e;
        }
    }

    // @SneakyThrows // bỏ qua các khai báo các exception checked thủ công
    public void createBucket(final String name) {
        // check xem bucket đã được tạo ch
        try {
            boolean found = minioClient.bucketExists(
                BucketExistsArgs.builder()
                                    .bucket(name)
                                    .build()
            );

            // nếu ch tồn tịa thì tạo
            if (!found) {
                minioClient.makeBucket(
                    MakeBucketArgs.builder()
                                        .bucket(name)
                                        .build()
                );

                // ghi log xem bucket được tạo thành công hay ch
                log.info("Bucket {} created successfully", name);

                // mặc định thì minio set repo là private 
                // chuyển qua public
                String policy = """
                {
                  "Version": "2012-10-17",
                  "Statement": [
                    {
                      "Effect": "Allow",
                      "Principal": "*",
                      "Action": "s3:GetObject",
                      "Resource": "arn:aws:s3:::%s/*"
                    }
                  ]
                }
                """.formatted(name);

                // set lại policy cho bucket
                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                    .bucket(name)
                    .config(policy)
                    .build());

                
            }

            else {
                log.info("Bucket {} is already existed", name);
            }
        } catch (Exception e) {
            log.error("Could not create bucket: {}", e.getMessage());
            throw new RuntimeException("Could not create bucket", e);
        }
        
        
    }

    // tạo một tên file dựa vào tên file gốc sử dụng UUID (định danh duy nhất)
    private String generateFileName(String originalFilename) {
        // extension là phần mở rộng của tệp
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }

    // trả về tên file đã upload
    // tên file sau khi đã được định danh duy nhất
    public String uploadFile(MultipartFile file) {
        try {
            String filename = generateFileName(file.getOriginalFilename());

            // upload tệp
            minioClient.putObject(
                PutObjectArgs.builder()   // xây dựng các tham số để upload tệp
                    .bucket(bucketName)   // chỉ định bucket để upload tệp
                    .object(filename)     // tên tệp sẽ được đưa vào bucket 
                    .stream(file.getInputStream(), file.getSize(), -1) // Bỏ qua kiểm tra part size (thường để tối ưu hóa upload lớn).
                    .contentType(file.getContentType())         // set kiểu nội dung của file jpeg, mp4 ...
                    .build()
            );

            // ghi log đã upload file thành công
            log.info("File {} uploaded successfully", filename);
            return filename;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error uploading file: {}", e.getMessage());
            throw new RuntimeException("Could not upload file: {}", e);
        } 
    }

    // trả về url tạm thời để truy cập tệp
    public String getFileUrl(String filename) {
        try {
            String presignedUrl = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET) // chỉ định url chỉ được lấy và xem 
                    .bucket(bucketName) // chỉ định bucket nơi chứa tệp
                    .object(filename)  // file cần được lấy (url)
                    .expiry(60 * 60 * 24) // chỉ định trong 24 tiếng
                    .build()


            );

            log.info("Get File Url Successfully : {}", filename);
            return presignedUrl;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // log lỗi 
            log.error("Error getting file URL: {}", e.getMessage());
            return null;
        } 
    }


    // delete file 
    public void deleteFile(String filename) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName) // chỉ định bucket chứa file
                    .object(filename)   // chỉ định file được xoá
                    .build()
            );
            log.info("File {} deleted successfully", filename);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Error deleting file: {}", e.getMessage());
            throw new RuntimeException("Could not delete file", e);
        } 
    }

    public String getPublicUrl(String fileName) {
        return String.format("http://127.0.0.1:9000/%s/%s", bucketName, fileName);
    }

    // Debug method to test MinIO connectivity
    public boolean testConnection() {
        try {
            log.info("Testing MinIO connection...");
            minioClient.listBuckets();
            log.info("MinIO connection test successful");
            return true;
        } catch (Exception e) {
            log.error("MinIO connection test failed: {}", e.getMessage());
            return false;
        }
    }

    // Debug method to check if bucket exists
    public boolean checkBucketExists() {
        try {
            boolean exists = minioClient.bucketExists(
                BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build()
            );
            log.info("Bucket {} exists: {}", bucketName, exists);
            return exists;
        } catch (Exception e) {
            log.error("Error checking bucket existence: {}", e.getMessage());
            return false;
        }
    }
}
