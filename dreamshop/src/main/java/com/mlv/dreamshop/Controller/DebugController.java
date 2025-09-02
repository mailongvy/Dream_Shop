package com.mlv.dreamshop.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mlv.dreamshop.DAO.ImageRepository;
import com.mlv.dreamshop.DAO.ProductRepository;
import com.mlv.dreamshop.Model.Image;
import com.mlv.dreamshop.Model.Product;
import com.mlv.dreamshop.Response.ApiResponse;
import com.mlv.dreamshop.service.minio.MinioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/debug")
public class DebugController {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final MinioService minioService;

    /**
     * Debug database contents
     */
    @GetMapping("/database")
    public ResponseEntity<ApiResponse> debugDatabase() {
        try {
            long productCount = productRepository.count();
            long imageCount = imageRepository.count();
            
            List<Product> products = productRepository.findAll();
            List<Image> images = imageRepository.findAll();
            
            String debugInfo = String.format(
                "Products: %d, Images: %d\nFirst product: %s\nFirst image: %s",
                productCount, 
                imageCount,
                products.isEmpty() ? "None" : products.get(0).getName(),
                images.isEmpty() ? "None" : images.get(0).getFileName()
            );
            
            return ResponseEntity.ok(new ApiResponse(debugInfo, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse("Database error: " + e.getMessage(), null));
        }
    }

    /**
     * Debug MinIO connection
     */
    @GetMapping("/minio")
    public ResponseEntity<ApiResponse> debugMinIO() {
        try {
            boolean connected = minioService.testConnection();
            boolean bucketExists = minioService.checkBucketExists();
            
            String debugInfo = String.format(
                "MinIO Connected: %s, Bucket exists: %s",
                connected, bucketExists
            );
            
            return ResponseEntity.ok(new ApiResponse(debugInfo, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse("MinIO error: " + e.getMessage(), null));
        }
    }

    /**
     * Fix duplicate URLs in database
     */
    @GetMapping("/fix-urls")
    public ResponseEntity<ApiResponse> fixDuplicateUrls() {
        try {
            List<Image> images = imageRepository.findAll();
            int fixedCount = 0;
            
            for (Image image : images) {
                String downloadUrl = image.getDownloadUrl();
                if (downloadUrl != null && downloadUrl.contains("/api/v1/images/image/download/api/v1/images/image/download/")) {
                    // Fix duplicate URL
                    String imageId = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1);
                    String correctUrl = "/api/v1/images/image/download/" + imageId;
                    image.setDownloadUrl(correctUrl);
                    imageRepository.save(image);
                    fixedCount++;
                }
            }
            
            return ResponseEntity.ok(new ApiResponse("Fixed " + fixedCount + " duplicate URLs", null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse("Error fixing URLs: " + e.getMessage(), null));
        }
    }

    /**
     * Fix URLs missing leading slash
     */
    @PostMapping("/fix-missing-slash")
    public ResponseEntity<ApiResponse> fixMissingSlash() {
        try {
            List<Image> images = imageRepository.findAll();
            int fixedCount = 0;
            
            for (Image image : images) {
                String downloadUrl = image.getDownloadUrl();
                if (downloadUrl != null && downloadUrl.startsWith("api/v1/") && !downloadUrl.startsWith("/api/v1/")) {
                    // Fix missing leading slash
                    String correctUrl = "/" + downloadUrl;
                    image.setDownloadUrl(correctUrl);
                    imageRepository.save(image);
                    fixedCount++;
                }
            }
            
            return ResponseEntity.ok(new ApiResponse("Fixed " + fixedCount + " URLs missing leading slash", null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse("Error fixing URLs: " + e.getMessage(), null));
        }
    }

    /**
     * Debug image URLs
     */
    @GetMapping("/image-urls")
    public ResponseEntity<ApiResponse> debugImageUrls() {
        try {
            List<Image> images = imageRepository.findAll();
            
            if (images.isEmpty()) {
                return ResponseEntity.ok(new ApiResponse("No images found in database", null));
            }
            
            StringBuilder debugInfo = new StringBuilder();
            for (Image image : images.subList(0, Math.min(5, images.size()))) {
                debugInfo.append(String.format(
                    "Image ID: %d, File: %s, MinIO File: %s, URL: %s\n",
                    image.getId(),
                    image.getFileName(),
                    image.getMinioFileName(),
                    image.getDownloadUrl()
                ));
            }
            
            return ResponseEntity.ok(new ApiResponse(debugInfo.toString(), null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ApiResponse("Image URL debug error: " + e.getMessage(), null));
        }
    }
}
