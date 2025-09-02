package com.mlv.dreamshop.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mlv.dreamshop.Model.Product;
import com.mlv.dreamshop.Response.ApiResponse;
import com.mlv.dreamshop.dto.ProductDTO;
import com.mlv.dreamshop.exceptions.AlreadyExistsException;
import com.mlv.dreamshop.exceptions.ProductNotFoundException;
import com.mlv.dreamshop.request.AddProductRequest;
import com.mlv.dreamshop.request.UpdateProductRequest;
import com.mlv.dreamshop.service.product.ProductService;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RestController
@RequestMapping("${apiPrefix}/products")
public class    ProductController {
    private final ProductService productService;

    //get all products
    @GetMapping("/product/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDTO> convertedProducts = productService.getConvertedProducts(products);
        return ResponseEntity.ok(new ApiResponse("Found", convertedProducts));
    }

    // get product by the id
    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            Product product = productService.findById(productId);
            ProductDTO convertedProduct = productService.convertToDto(product);
            return ResponseEntity.ok(new ApiResponse("Found", convertedProduct));
        } catch (ProductNotFoundException e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new ApiResponse("Not found", null));
        }
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "product/add")
    // add product
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest product) {
        try {
            Product product1 = productService.addProuct(product);
            return ResponseEntity.ok(new ApiResponse("Add successfully", product1));
        } catch (AlreadyExistsException e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/product/update/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    //update product
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest request, @PathVariable Long productId) {
        try {
            Product updatedProduct = productService.updateProduct(request, productId);
            return ResponseEntity.ok(new ApiResponse("Updated successfully", updatedProduct));
        } catch (ProductNotFoundException e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new ApiResponse(e.getMessage(), null));
        }
    }

    // delete product by the id
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/product/delete/{productId}")
    public ResponseEntity<ApiResponse> deleteProductByTheId(@PathVariable Long productId) {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Delete successfully", null));
        } catch (ProductNotFoundException e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new ApiResponse("Failed", null));
        }

    }

    // get product by brand and name
    @GetMapping("/product/get/brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            List<Product> products = productService.getProductsByBrandAndName(name, brand);
            List<ProductDTO> convertedProducts = productService.getConvertedProducts(products);
            if (products == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(new ApiResponse("No product Found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Found", convertedProducts));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ApiResponse(e.getMessage(), null));
        }
    }

    // get product By category and brand
    @GetMapping("/product/get/{brand}/{category}")
    public ResponseEntity<ApiResponse> getProductByBrandAndCategory(@PathVariable String brand, @PathVariable String category) {
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(brand, category);
            List<ProductDTO> convertedProducts = productService.getConvertedProducts(products);
            if (products == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(new ApiResponse("No product found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Found", convertedProducts));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ApiResponse(e.getMessage(), null));
        }
    }

    // get product by name
    @GetMapping("/product/get/{name}")
    public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name) {
        try {
            List<Product> products = productService.getProductsByName(name);
            List<ProductDTO> convertedProducts = productService.getConvertedProducts(products);
            if (products == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(new ApiResponse("No product found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Found", convertedProducts));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ApiResponse(e.getMessage(), null));
        }
    }

    //get product by brand
    @GetMapping("/product/get/by-brand")
    public ResponseEntity<ApiResponse> getProductByBrand(@RequestParam String brand) {
        try {
            List<Product> products = productService.getProductsByBrand(brand);
            List<ProductDTO> convertedProducts = productService.getConvertedProducts(products);
            if (products == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(new ApiResponse("No product found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Found", convertedProducts));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ApiResponse(e.getMessage(), null));
        }
        

    }

    // get product by the category
    @GetMapping("/product/get/by-category/{category}")
    public ResponseEntity<ApiResponse> getProductByCategory(@PathVariable String category) {
        try {
            List<Product> products = productService.getProductsByCategory(category);
            List<ProductDTO> convertedProducts = productService.getConvertedProducts(products);
            if (products == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(new ApiResponse("No product found", null));
            }
            return ResponseEntity.ok(new ApiResponse("Found", convertedProducts));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ApiResponse(e.getMessage(), null));
        }
    }

    // count the product by brand and name
    @GetMapping("/product/get/by-brand-and-name")
    public ResponseEntity<ApiResponse> countProductByBrandAndName(@RequestParam String brand, @RequestParam String name) {
        try {
            Long productCount = productService.countProductsByBrandAndName(name, brand);
            return ResponseEntity.ok(new ApiResponse("Product Count", productCount));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ApiResponse(e.getMessage(), null));
        }
    }



}
