package com.mlv.dreamshop.Controller;

// import com.mlv.dreamshop.Model.Cart;
// import com.mlv.dreamshop.Model.User;
import com.mlv.dreamshop.Model.Cart;
import com.mlv.dreamshop.Model.User;
import com.mlv.dreamshop.service.user.IUserService;
import io.jsonwebtoken.JwtException;
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
import com.mlv.dreamshop.service.Cart.ICartService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/cartItems")
@RestController
public class CartItemController {
    private final ICartItemService cartItemService;
    private final ICartService cartService;
    private final IUserService userService;


//    @PostMapping("/item/add")
//    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam(required = false) Long cartId,
//                                                    @RequestParam Long productId,
//                                                    @RequestParam Integer quantity) {
//            try {
//                if (cartId == null) {
//                    cartId = cartService.initializeNewCart();
//                }
//                cartItemService.addItemToCart(cartId, productId, quantity);
//                return ResponseEntity.ok(new ApiResponse("Add item successfully", null));
//            } catch (ResourceNotFound e) {
//                // TODO Auto-generated catch block
//                return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                              .body(new ApiResponse(e.getMessage(), null));
//            }
//    }

    @PostMapping("/item/add")
    public ResponseEntity<ApiResponse> addItemToCart(
                                                     @RequestParam Long productId,
                                                     @RequestParam Integer quantity) {
        try {
            User user = userService.getAuthenticationUser();
            Cart cart = cartService.initializeNewCart(user);

            cartItemService.addItemToCart(cart.getId(), productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Add item successfully", null));
        } catch (ResourceNotFound e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (JwtException e) {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }



    // remove item from the cart
    @DeleteMapping("/cart/{cartId}/item/{itemId}/remove")
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
