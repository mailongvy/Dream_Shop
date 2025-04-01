package com.mlv.dreamshop.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mlv.dreamshop.Response.ApiResponse;
import com.mlv.dreamshop.exceptions.ResourceNotFound;
import com.mlv.dreamshop.service.Cart.ICartItemService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/cartItems")
@RestController
public class CartItemController {
    private final ICartItemService cartItemService;

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam Long cartId, 
                                                    @RequestParam Long productId, 
                                                    @RequestParam Integer quantity) {
            try {
                cartItemService.addItemToCart(cartId, productId, quantity);
                return ResponseEntity.ok(new ApiResponse("Add item successfully", null));
            } catch (ResourceNotFound e) {
                // TODO Auto-generated catch block
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                              .body(new ApiResponse(e.getMessage(), null));
            }
    }

    // remove item from the cart
    @DeleteMapping("/{cartId}/item/{itemId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        try {
            cartItemService.removeItemFormCart(cartId, itemId);
            return ResponseEntity.ok(new ApiResponse("Remove the item successfully", null));
        } catch (ResourceNotFound e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new ApiResponse(e.getMessage(), null));
        }
        
    }

    //update item quantity 
    @PutMapping("/cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                        @PathVariable Long itemId, 
                                                        @RequestParam Integer quantity) {
            try {
                cartItemService.updateItemQuantity(cartId, itemId, quantity);
                return ResponseEntity.ok(new ApiResponse("Update item successfully", null));
            } catch (ResourceNotFound e) {
                // TODO Auto-generated catch block
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body(new ApiResponse(e.getMessage(), null));
            }
    }
}
