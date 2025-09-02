package com.mlv.dreamshop.dto;

import lombok.Data;

//dto chỉ chứa các trường dữ liệu mang dữ liệu ko mang tính chất truy vấn từ database
@Data
public class ImageDTO {
    private Long imageId;
    private String fileName;
    private String downloadUrl;
    private String fileType;
}
