package com.mlv.dreamshop.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mlv.dreamshop.Model.Order;
import com.mlv.dreamshop.Response.ApiResponse;
import com.mlv.dreamshop.dto.OrderDTO;
import com.mlv.dreamshop.exceptions.ResourceNotFound;
import com.mlv.dreamshop.service.order.IOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${apiPrefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;

    // tạo order
    @PostMapping("/order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
        try {
            Order order = orderService.placeOrder(userId);
            OrderDTO orderDTO = orderService.convertToDto(order);
            return ResponseEntity.ok(new ApiResponse("Item Order success", orderDTO));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
        try {
            OrderDTO order = orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Order found", order));
        } catch (ResourceNotFound e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/orderByUserId/{userId}")
    public ResponseEntity<ApiResponse> getUsersOrder(@PathVariable Long userId) {
        try {
            List<OrderDTO> order = orderService.getUserOrder(userId);
            return ResponseEntity.ok(new ApiResponse("Order found", order));
        } catch (ResourceNotFound e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new ApiResponse(e.getMessage(), null));
        }
    }

    






}
