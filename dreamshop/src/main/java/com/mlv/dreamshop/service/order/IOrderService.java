package com.mlv.dreamshop.service.order;

import java.math.BigDecimal;
import java.util.List;

import com.mlv.dreamshop.Model.Order;
import com.mlv.dreamshop.Model.OrderItem;
import com.mlv.dreamshop.dto.OrderDTO;

public interface IOrderService {
    Order placeOrder(Long userId);

    OrderDTO getOrder(Long orderId);

    BigDecimal calculateTotalAmount(List<OrderItem> orderItemList);

    List<OrderDTO> getUserOrder(Long userId);

    OrderDTO convertToDto(Order order);

    // void testPartion2();
}
