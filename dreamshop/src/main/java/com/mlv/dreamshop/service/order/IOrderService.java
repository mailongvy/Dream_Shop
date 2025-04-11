package com.mlv.dreamshop.service.order;

import java.math.BigDecimal;
import java.util.List;

import com.mlv.dreamshop.Model.Order;
import com.mlv.dreamshop.Model.OrderItem;

public interface IOrderService {
    Order placeOrder(Long userId);

    Order getOrder(Long orderId);

    BigDecimal calculateTotalAmount(List<OrderItem> orderItemList);
}
