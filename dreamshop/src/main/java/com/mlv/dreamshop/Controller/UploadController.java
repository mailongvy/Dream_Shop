package com.mlv.dreamshop.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mlv.dreamshop.Response.ApiResponse;
import com.mlv.dreamshop.dto.ImageDTO;
import com.mlv.dreamshop.service.image.IImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/upload")
public class UploadController {

    private final IImageService imageService;

    /**
     * Upload multiple images for a product
     * POST /api/v1/upload/images/{productId}
     */
    @PostMapping("/images/{productId}")
    public ResponseEntity<ApiResponse> uploadImages(
            @PathVariable Long productId,
            @RequestParam(value="images") List<MultipartFile> files) {
        
        try {
            // Validate files
            if (files == null || files.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("Please select at least one image file", null));
            }

            // Validate file types
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse("Please select non-empty files", null));
                }
                
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse("Only image files are allowed", null));
                }
                
                // Check file size (max 5MB)
                if (file.getSize() > 5 * 1024 * 1024) {
                    return ResponseEntity.badRequest()
                            .body(new ApiResponse("File size should be less than 5MB", null));
                }
            }

            // Upload to MinIO and save to database
            List<ImageDTO> uploadedImages = imageService.saveImage(files, productId);
            
            return ResponseEntity.ok(new ApiResponse(
                    "Successfully uploaded " + uploadedImages.size() + " images", 
                    uploadedImages));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to upload images: " + e.getMessage(), null));
        }
    }

    /**
     * Upload single image for a product
     * POST /api/v1/upload/image/{productId}
     */
    @PostMapping("/image/{productId}")
    public ResponseEntity<ApiResponse> uploadSingleImage(
            @PathVariable Long productId,
            @RequestParam("image") MultipartFile file) {
        
        try {
            // Validate file
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("Please select an image file", null));
            }

            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("Only image files are allowed", null));
            }

            // Check file size (max 5MB)
            if (file.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("File size should be less than 5MB", null));
            }

            // Upload to MinIO and save to database
            List<ImageDTO> uploadedImages = imageService.saveImage(List.of(file), productId);
            
            return ResponseEntity.ok(new ApiResponse(
                    "Image uploaded successfully", 
                    uploadedImages.get(0)));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to upload image: " + e.getMessage(), null));
        }
    }

    

    /**
     * Get upload status and file info
     * GET /api/v1/upload/status
     */
    @GetMapping("/status")
    public ResponseEntity<ApiResponse> getUploadStatus() {
        try {
            return ResponseEntity.ok(new ApiResponse("Upload service is ready", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Upload service error: " + e.getMessage(), null));
        }
    }
}
