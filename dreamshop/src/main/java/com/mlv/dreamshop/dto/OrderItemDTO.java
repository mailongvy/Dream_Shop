package com.mlv.dreamshop.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemDTO {
    private Long productId;

    private String productName;

    private int quantity;

    private BigDecimal price;

}
