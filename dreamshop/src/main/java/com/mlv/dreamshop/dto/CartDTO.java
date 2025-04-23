package com.mlv.dreamshop.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class CartDTO {
    private Long id;
    private Set<CartItemDTO> items;
    private BigDecimal totalAmount;
}
