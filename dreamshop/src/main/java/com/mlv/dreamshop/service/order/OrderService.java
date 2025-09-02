package com.mlv.dreamshop.service.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.mlv.dreamshop.DAO.OrderRepository;
import com.mlv.dreamshop.DAO.ProductRepository;
import com.mlv.dreamshop.Model.Cart;
import com.mlv.dreamshop.Model.Order;
import com.mlv.dreamshop.Model.OrderItem;
import com.mlv.dreamshop.Model.Product;
import com.mlv.dreamshop.dto.OrderConfirmationEvent;
import com.mlv.dreamshop.dto.OrderDTO;
import com.mlv.dreamshop.dto.SimpleOrderDTO;
import com.mlv.dreamshop.enums.OrderStatus;
import com.mlv.dreamshop.exceptions.ResourceNotFound;
import com.mlv.dreamshop.service.Cart.ICartService;
import com.mlv.dreamshop.service.Email.EmailFallbackService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ICartService cartService;
    private final ModelMapper modelMapper;
    // private final EmailService emailService;
    private final KafkaTemplate<String, OrderConfirmationEvent> kafkaTemplate;

    private final EmailFallbackService emailFallbackService;

    private static final String ORDER_CONFIRMATION_TOPIC = "order-confirmation";

    @Override
    public OrderDTO getOrder(Long orderId) {
        // TODO Auto-generated method stub
        return orderRepository.findById(orderId)
                              .map(this::convertToDto)
                              .orElseThrow(() -> new ResourceNotFound("Order not found"));
    }

    // // Thêm method này vào OrderService class
    // @Override
    // public void testPartion2() {
    //     try {
    //         // Tạo test message
    //         SimpleOrderDTO orderInfo = new SimpleOrderDTO(
    //             3L,  // test order ID (Long)
    //             2L,  // test user ID (Long)
    //             LocalDate.now(),
    //             new BigDecimal("500.00"),
    //             "PENDING"
    //         );

    //         OrderConfirmationEvent event = new OrderConfirmationEvent(orderInfo);
            
    //         // Gửi đến partition 1 cụ thể (partition thứ 2)
    //         String messageKey = "partition-1-message";
    //         kafkaTemplate.send(ORDER_CONFIRMATION_TOPIC, "2", event);
            
    //         log.info("Sent message to partition 1 with key: {}", messageKey);
    //     } catch (Exception e) {
    //         log.error("Failed to send message to partition 1", e);
    //     }
    // }

    @Override
    @Transactional
    public Order placeOrder(Long userId) {
        // TODO Auto-generated method stub
        // get cart to make the order
        Cart cart = cartService.getCartsByUserId(userId);

        // after finding the cart make the order belong to cart
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);

        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(cart.getId());

        // gửi email đã đặt hàng thành công
        // sendOrderConfirmationEmail(savedOrder, cart.getUser());

        // thay vì gửi mail trực tiếp cho orderservice gửi event qua kafka từ kafka gửi mail 
        try {
            // trước kho gửi kafka thì manual setup cho simpleorderdto
            SimpleOrderDTO orderInfo = new SimpleOrderDTO(
                savedOrder.getId(),
                cart.getUser().getId(),
                savedOrder.getOrderDate(),
                savedOrder.getTotalAmount(),
                savedOrder.getOrderStatus().toString()
            );

            OrderConfirmationEvent event = new OrderConfirmationEvent(orderInfo);

            // gửi lần 1
            kafkaTemplate.send(ORDER_CONFIRMATION_TOPIC, event);
            // emailFallbackService.sendOrderConfirmationWithFallback(savedOrder, cart.getUser());
            log.info("Sent order confirmation event to Kafka for order: {}", savedOrder.getId());

            
            // Gửi lần 2 (mô phỏng retry)
            kafkaTemplate.send(ORDER_CONFIRMATION_TOPIC, "1", event);
            log.info("Sent message 2 (duplicate) for order: {}", savedOrder.getId());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Failed to send order confirmation event to Kafka for order: {}", savedOrder.getId(), e);
        }
        

        return savedOrder;
    }

    // private void sendOrderConfirmationEmail(Order order, User user) {
    //     try {
    //         // Send email asynchronously to not affect order processing
    //         emailService.sendOrderConfirmationEmail(order, user);
    //     } catch (Exception e) {
    //         // Log error but don't fail the order creation
    //         System.out.println("Failed to send order confirmation email for order");
    //     }
    // }

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

    @Override
    public OrderDTO convertToDto(Order order) {
        OrderDTO dto = modelMapper.map(order, OrderDTO.class);
        // Manually map orderItems to items
        if (order.getOrderItems() != null) {
            dto.setItems(order.getOrderItems().stream()
                .map(item -> {
                    com.mlv.dreamshop.dto.OrderItemDTO itemDTO = new com.mlv.dreamshop.dto.OrderItemDTO();
                    itemDTO.setProductId(item.getProduct().getId());
                    itemDTO.setProductName(item.getProduct().getName());
                    itemDTO.setProductBrand(item.getProduct().getBrand());
                    itemDTO.setQuantity(item.getQuantity());
                    itemDTO.setPrice(item.getPrice());
                    return itemDTO;
                })
                .collect(Collectors.toList()));
        }
        return dto;
    }
    

}
