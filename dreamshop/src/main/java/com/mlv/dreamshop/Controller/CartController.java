package com.mlv.dreamshop.Controller;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mlv.dreamshop.Model.Cart;
import com.mlv.dreamshop.Response.ApiResponse;
import com.mlv.dreamshop.exceptions.ResourceNotFound;
import com.mlv.dreamshop.service.Cart.ICartService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${apiPrefix}/carts")
public class CartController {
    private final ICartService cartService;
    

    // public ResponseEntity getCart
    @GetMapping("/{cartId}/my-cart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long cartId) {
        try {
            Cart cart = cartService.getCart(cartId);
            return ResponseEntity.ok(new ApiResponse("Success", cart));
        } catch (ResourceNotFound e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new ApiResponse(e.getMessage(), null));
        }
    }

    // clear cart
    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.ok(new ApiResponse("Clear Cart Success", null));
        
    }

    // get total amount for the cart
    @GetMapping("{cartId}/cart/total-price")
    public ResponseEntity<ApiResponse> getTotalAmount(@PathVariable Long cartId) {
        BigDecimal totalPrice = cartService.getTotalPrice(cartId);
        return ResponseEntity.ok(new ApiResponse("Total price", totalPrice));
    }


}
