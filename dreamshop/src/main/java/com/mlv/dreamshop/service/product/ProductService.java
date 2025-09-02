package com.mlv.dreamshop.service.product;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.mlv.dreamshop.DAO.CategoryRepository;
import com.mlv.dreamshop.DAO.ImageRepository;
import com.mlv.dreamshop.DAO.ProductRepository;
import com.mlv.dreamshop.Model.Category;
import com.mlv.dreamshop.Model.Image;
import com.mlv.dreamshop.Model.Product;
import com.mlv.dreamshop.dto.ImageDTO;
import com.mlv.dreamshop.dto.ProductDTO;
import com.mlv.dreamshop.exceptions.AlreadyExistsException;
import com.mlv.dreamshop.exceptions.ProductNotFoundException;
import com.mlv.dreamshop.request.AddProductRequest;
import com.mlv.dreamshop.request.UpdateProductRequest;

import lombok.RequiredArgsConstructor;

// import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
// @RequiredArgsConstructor // những field được đánh dấu là final và @nonnull sẽ được tự động thêm vào constructor
public class ProductService implements IProductService {
    // define the productrepo
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    
    
    

    @Override
    public void deleteProductById(Long id) {
        // TODO Auto-generated method stub
        productRepository.findById(id).ifPresentOrElse(productRepository::delete, () -> {throw new ProductNotFoundException("Product not found");});

        
    }

    @Override
    public Product findById(Long id) {
        // TODO Auto-generated method stub
        Optional<Product> product = productRepository.findById(id);
        return product.orElseThrow(() -> new ProductNotFoundException("Product not found"));
    }

    @Override
    public List<Product> getAllProducts() {
        // TODO Auto-generated method stub
        List<Product> list = productRepository.findAll();
        return list;
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long productId) {
        // TODO Auto-generated method stub
        // private Long id;
        // private String name;
        // private String brand;
        // private BigDecimal price;
        // private int inventory;
        // private String description;
        // private Category category;

       return productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(request, existingProduct))
                .map(productRepository::save)
                .orElseThrow(() -> new ProductNotFoundException("Product Not found"));
    }

    public Product updateExistingProduct(UpdateProductRequest request, Product existingProduct) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return null;
    }


    

    @Override
    public List<Product> getProductsByBrand(String brand) {
        // TODO Auto-generated method stub
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        // TODO Auto-generated method stub
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String brand, String category) {
        // TODO Auto-generated method stub
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public Long countProductsByBrandAndName(String name, String brand) {
        // TODO Auto-generated method stub
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String name, String brand) {
        // TODO Auto-generated method stub
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        // TODO Auto-generated method stub
        return productRepository.findByName(name);
    }

    @Override
    public Product addProuct(AddProductRequest request) {
        // TODO Auto-generated method stub
        // before adding the productk, check the category
        // check if the category found in the Db
        // if yes set it  as the new product
        // if no  save it as a new category

        if (productExist(request.getName(), request.getBrand())  ) {
            throw new AlreadyExistsException(request.getName() + " " + request.getBrand() + " already exists, u should update the product");
        }

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                            .orElseGet(() -> {
                                Category newCategory = new Category(request.getCategory().getName());
                                return categoryRepository.save(newCategory);
                            });


        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
        
    }

    // check product already in the db
    private boolean productExist(String name, String brand) {
        return productRepository.existsByNameAndBrand(name, brand);
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
            request.getName(), 
            request.getBrand(), 
            request.getPrice(),
            request.getInventory(),
            request.getDescription(), 
            category
        );
    }

    @Override
    public List<ProductDTO> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }

    
    // convert to dto
    @Override
    public ProductDTO convertToDto(Product product) {
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        
        // Manual mapping để đảm bảo downloadUrl được map đúng
        List<ImageDTO> imageDTOs = images.stream().map(image -> {
            ImageDTO imageDTO = new ImageDTO();
            imageDTO.setImageId(image.getId());
            imageDTO.setFileName(image.getFileName());
            imageDTO.setFileType(image.getFileType());
            
            // Fix: Ưu tiên MinIO URL, fallback về image controller
            String downloadUrl = image.getDownloadUrl();
            if (downloadUrl != null && downloadUrl.startsWith("http://localhost:9000/")) {
                // Đây là MinIO URL - sử dụng trực tiếp
                imageDTO.setDownloadUrl(downloadUrl);
            } else if (image.getMinioFileName() != null && !image.getMinioFileName().isEmpty()) {
            // Có MinIO filename - tạo MinIO URL
                String minioUrl = String.format("http://127.0.0.1:9000/dreamshop-images/%s", 
                                            image.getMinioFileName());
                imageDTO.setDownloadUrl(minioUrl);
            } else {
                // Fix: Đảm bảo không duplicate URL
                if (downloadUrl != null && downloadUrl.startsWith("/api/v1/images/")) {
                    // URL đã đúng format - dùng luôn
                    imageDTO.setDownloadUrl(downloadUrl);
                } else {
                    // Tạo mới controller endpoint
                    imageDTO.setDownloadUrl("/api/v1/images/image/download/" + image.getId());
                }
            }
            // else {
            //     // Fallback về controller endpoint
            //     imageDTO.setDownloadUrl("/api/v1/images/image/download/" + image.getId());
            // }
            
            return imageDTO;
        }).toList();
        
        productDTO.setImages(imageDTOs);
        return productDTO;
    }
    

    

    
    
}
