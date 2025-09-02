package com.mlv.dreamshop.KafkaConsumer;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.mlv.dreamshop.DAO.OrderRepository;
import com.mlv.dreamshop.DAO.UserRepository;
import com.mlv.dreamshop.Model.Order;
import com.mlv.dreamshop.Model.User;
import com.mlv.dreamshop.dto.OrderConfirmationEvent;
import com.mlv.dreamshop.exceptions.ResourceNotFound;
import com.mlv.dreamshop.service.Email.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderConfirmationConsumer {
    private final EmailService emailService;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @KafkaListener(topics = "order-confirmation", groupId="order-confirmation-group")
    public void handleOrderConfirmation(OrderConfirmationEvent event, ConsumerRecord<String, OrderConfirmationEvent> record) {
        int partition = record.partition();
        try {
            Order order = orderRepository.findById(event.getOrderInfo().getId())
                                            .orElseThrow(() -> new ResourceNotFound("Order Not Found"));
            User user = userRepository.findById(event.getOrderInfo().getUserId())
                                        .orElseThrow(() -> new ResourceNotFound("User Not Found"));
            emailService.sendOrderConfirmationEmail(order, user);
            log.info("Consumer1: Processed order confirmation from {} and sent email for order: {}", partition, order.getId());
        } catch (Exception e) {
            log.error("Failed to process order confirmation from {} event for order ", partition);
            // Có thể thêm retry mechanism nếu cần (sử dụng Spring Retry hoặc Kafka retry topic)
        }
    }

    // @KafkaListener(topics = "order-confirmation", groupId="order-confirmation")
    // public void handleOrderConfirmation2(OrderConfirmationEvent event, ConsumerRecord<String, OrderConfirmationEvent> record) {
    //     int partition = record.partition();
    //     try {
    //         Order order = orderRepository.findById(event.getOrderInfo().getId())
    //                                         .orElseThrow(() -> new ResourceNotFound("Order Not Found"));
    //         User user = userRepository.findById(event.getOrderInfo().getUserId())
    //                                     .orElseThrow(() -> new ResourceNotFound("User Not Found"));
    //         emailService.sendOrderConfirmationEmail(order, user);
    //         log.info("Consumer 2: Processed order confirmation from {} and sent email for order: {}", partition, order.getId());
    //     } catch (Exception e) {
    //         log.error("Consumer 2: Failed to process order confirmation from {} event for order ", partition);
    //         // Có thể thêm retry mechanism nếu cần (sử dụng Spring Retry hoặc Kafka retry topic)
    //     }
    // }

    
}
