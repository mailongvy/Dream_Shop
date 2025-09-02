package com.mlv.dreamshop.Controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mlv.dreamshop.Model.Product;
import com.mlv.dreamshop.service.category.CategoryService;
import com.mlv.dreamshop.service.product.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WebController {
    
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/")
    public String index() {
        return "redirect:/shop";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/shop")
    public String shopPage(Model model, 
                          @RequestParam(required = false) String category,
                          @RequestParam(required = false) String search) {
        try {
            List<Product> products;
            
            if (category != null && !category.isEmpty()) {
                products = productService.getProductsByCategory(category);
            } else if (search != null && !search.isEmpty()) {
                products = productService.getProductsByName(search);
            } else {
                products = productService.getAllProducts();
            }
            
            model.addAttribute("products", products);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("selectedCategory", category);
            model.addAttribute("searchQuery", search);
            
        } catch (Exception e) {
            model.addAttribute("products", List.of());
            model.addAttribute("categories", List.of());
            model.addAttribute("error", "Không thể tải sản phẩm");
        }
        
        return "shop";
    }

    @GetMapping("/cart")
    public String cartPage() {
        return "cart";
    }

    @GetMapping("/orders")
    public String ordersPage() {
        return "orders";
    }

    @GetMapping("/upload")
    public String uploadPage() {
        return "upload";
    }

    @GetMapping("/debug")
    public String debugPage() {
        return "debug";
    }
}
