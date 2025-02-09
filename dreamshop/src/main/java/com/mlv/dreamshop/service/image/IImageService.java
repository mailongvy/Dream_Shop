package com.mlv.dreamshop.service.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mlv.dreamshop.Model.Image;
import com.mlv.dreamshop.dto.ImageDTO;

public interface IImageService  {
    // get image by id
    Image getImageById(Long id);

    // delete image by id
    void deleteImageById(Long id);

    // save image 
    List<ImageDTO> saveImage(List<MultipartFile> files, Long productId);

    //void update the image
    void updateImage(MultipartFile file, Long imageId);
}
