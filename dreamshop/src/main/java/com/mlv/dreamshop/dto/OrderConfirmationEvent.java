package com.mlv.dreamshop.dto;

// import com.mlv.dreamshop.Model.Order;
// import com.mlv.dreamshop.Model.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderConfirmationEvent {
    private SimpleOrderDTO orderInfo;
}
