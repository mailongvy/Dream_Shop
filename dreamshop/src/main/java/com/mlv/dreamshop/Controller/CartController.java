package com.mlv.dreamshop.Controller;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mlv.dreamshop.Model.Cart;
import com.mlv.dreamshop.Response.ApiResponse;
import com.mlv.dreamshop.exceptions.ResourceNotFound;
import com.mlv.dreamshop.service.cart.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/carts")
public class CartController {
    private final CartService cartService;

    @GetMapping("cart/{cartId}")
    public ResponseEntity<ApiResponse> getCartById(@PathVariable Long cartId) {
        try {
            Cart cart = cartService.getCartById(cartId);
            return ResponseEntity.ok(new ApiResponse("Found", cart));
        } catch (ResourceNotFound e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("clear/{cartId}")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {
        try {
            cartService.clearCart(cartId);
            return ResponseEntity.ok(new ApiResponse("Clear Cart Successfully", null));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ApiResponse(e.getMessage(), null));
        }
    }


    @GetMapping("cart/total-price/{cartId}")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId) {
        try {
            BigDecimal totalAmount = cartService.getTotalPrice(cartId);
            return ResponseEntity.ok(new ApiResponse("Get Total Amount Success", totalAmount));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ApiResponse(e.getMessage(), null));
        }
    }

    

}
