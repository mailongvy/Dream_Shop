package com.mlv.dreamshop.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto;
import org.springframework.stereotype.Service;

import com.mlv.dreamshop.DAO.OrderRepository;
import com.mlv.dreamshop.DAO.ProductRepository;
import com.mlv.dreamshop.Model.Cart;
import com.mlv.dreamshop.Model.Order;
import com.mlv.dreamshop.Model.OrderItem;
import com.mlv.dreamshop.Model.Product;
import com.mlv.dreamshop.dto.OrderDTO;
import com.mlv.dreamshop.enums.OrderStatus;
import com.mlv.dreamshop.exceptions.ResourceNotFound;
import com.mlv.dreamshop.service.Cart.ICartService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ICartService cartService;
    private final ModelMapper modelMapper;

    @Override
    public OrderDTO getOrder(Long orderId) {
        // TODO Auto-generated method stub
        return orderRepository.findById(orderId)
                              .map(this::convertToDto)
                              .orElseThrow(() -> new ResourceNotFound("Order not found"));
    }

    @Override
    @Transactional
    public Order placeOrder(Long userId) {
        // TODO Auto-generated method stub
        Cart cart = cartService.getCartsByUserId(userId);

        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);

        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(cart.getId());

        return savedOrder;
    }

    // tạo ra các order
    private Order createOrder(Cart cart) {
        Order order = new Order();

        // set the user
        order.setUser(cart.getUser());

        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }


    // tạo ra các order item thông qua các cart item

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(order, product, cartItem.getQuantity(), cartItem.getUnitPrice());
        }).collect(Collectors.toList());
    }
 

    @Override
    public BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return orderItemList.stream()
                            .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<OrderDTO> getUserOrder(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::convertToDto).toList();
    }

    
    public OrderDTO convertToDto(Order order) {
        return modelMapper.map(order, OrderDTO.class);
    }
    

}
