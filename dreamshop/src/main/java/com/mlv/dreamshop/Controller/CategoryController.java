package com.mlv.dreamshop.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mlv.dreamshop.Model.Category;
import com.mlv.dreamshop.Response.ApiResponse;
import com.mlv.dreamshop.exceptions.AlreadyExistsException;
import com.mlv.dreamshop.exceptions.CategoryNotFoundException;
import com.mlv.dreamshop.service.category.CategoryService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RequiredArgsConstructor
@RestController
@RequestMapping("${apiPrefix}/categories")
public class CategoryController {
    private final CategoryService categoryService;

    
    
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            // nếu tìm được sẽ trả về toàn bộ categories đó
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Found", categories));
        } catch (CategoryNotFoundException e) {
            // TODO Auto-generated catch block
            // nếu không tìm thây thì trả về status not found và data là null vì ko tìm thãy dữ liệu
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ApiResponse("Error", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // add category
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
        try {
            Category category1 = categoryService.addCategory(category);
            return ResponseEntity.ok(new ApiResponse("Add successfully", category1));
        } catch (AlreadyExistsException e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body(new ApiResponse(e.getMessage(), null));
        }
    }


    // get category by id
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long categoryId) {
        try {
            Category category = categoryService.getCategoryById(categoryId);
            return ResponseEntity.ok(new ApiResponse("Found", category));
        } catch (CategoryNotFoundException e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }

    }

    //get category by name
    @GetMapping("category/{name}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name) {
        try {
            Category category = categoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Found", category));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    // delete category by the id
    @DeleteMapping("category/delete/{categoryId}")
    public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long categoryId) {
        try {
            categoryService.deleteCategoryById(categoryId);
            return ResponseEntity.ok(new ApiResponse("Delete succesfully", null));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete Error", null));
        }

    }

    //update category by the id
    @PutMapping("/category/update/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategoryById(@PathVariable Long categoryId, @RequestBody Category category) {
        try {
            Category updatedCategory = categoryService.updateCategory(category, categoryId);
            return ResponseEntity.ok(new ApiResponse("Update succesfully", updatedCategory));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Failed", null));
        }
    }




    
}
