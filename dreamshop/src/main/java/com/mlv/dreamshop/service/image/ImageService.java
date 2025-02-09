package com.mlv.dreamshop.service.image;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mlv.dreamshop.DAO.ImageRepository;
import com.mlv.dreamshop.Model.Image;
import com.mlv.dreamshop.Model.Product;
import com.mlv.dreamshop.dto.ImageDTO;
import com.mlv.dreamshop.exceptions.ImageNotFoundException;
import com.mlv.dreamshop.service.product.IProductService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {
    private final ImageRepository imageRepository;
    private final IProductService productService;

    @Override
    public void deleteImageById(Long id) {
        // TODO Auto-generated method stub

        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete,
                    ()-> {throw new ImageNotFoundException("Image not found");});
    }

    @Override
    public Image getImageById(Long id) {
        // TODO Auto-generated method stub
        return imageRepository.findById(id).orElseThrow(() -> new ImageNotFoundException("Image not found"));
    }

    @Override
    public List<ImageDTO> saveImage(List<MultipartFile> files, Long productId) {
        // TODO Auto-generated method stub
        Product product = productService.findById(productId);
        List<ImageDTO> saveImages = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            try {
                Image image = new Image();
                image.setFileName(multipartFile.getOriginalFilename());
                image.setFileType(multipartFile.getContentType());
                image.setImage(new SerialBlob(multipartFile.getBytes()));
                image.setProduct(product);

                // hình ảnh này ch được lưu trong cơ sở dữ liệu nên id của nó sẽ được set là null
                // nên sau khi lưu thì phải thay đổi địa chỉ url bằng cách phải set lại url một lần nx
                String buildDownloadUrl = "api/v1/images/image/download/";
                String downloadUrl = buildDownloadUrl + image.getId();
                image.setDownloadUrl(downloadUrl);

                // lần save đầu tiên lấy id cụ thể của image
                Image savedImage = imageRepository.save(image);
                // sau khi luuw thì image bây h đã có id nên nên có thể gọi được hàm getId để lấy id
                savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());

                // lần save thứ hai là cập nhật lại đường url cho dữ liệu
                imageRepository.save(savedImage);   

                // trường dto chỉ hiện ra những thông tin cần thiết cho image ko in ra những thông tin quan trọng
                ImageDTO imageDTO = new ImageDTO();
                imageDTO.setImageId(savedImage.getId());
                imageDTO.setFileName(savedImage.getFileName());
                imageDTO.setDownloadUrl(savedImage.getDownloadUrl());
                saveImages.add(imageDTO);

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        return saveImages;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        // TODO Auto-generated method stub
        Image image = getImageById(imageId);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to update image", e);
        }
        
    }
    
}
