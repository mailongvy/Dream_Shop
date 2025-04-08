package com.mlv.dreamshop.service.order;

import com.mlv.dreamshop.Model.Order;

public interface IOrderService {
    Order placeOrder(Order userId);

    Order getOrder();
}
