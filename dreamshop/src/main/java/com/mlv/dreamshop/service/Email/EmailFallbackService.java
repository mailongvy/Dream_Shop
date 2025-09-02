package com.mlv.dreamshop.service.Email;

import java.util.concurrent.TimeUnit;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.mlv.dreamshop.Model.Order;
import com.mlv.dreamshop.Model.User;
import com.mlv.dreamshop.dto.OrderConfirmationEvent;
import com.mlv.dreamshop.dto.SimpleOrderDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailFallbackService {

    private final EmailService emailService;
    private final KafkaTemplate<String, OrderConfirmationEvent> kafkaTemplate;

    public void sendOrderConfirmationWithFallback(Order order, User user) {
        // gửi qua kafka trước
        // kafka bth -> mail được chuyển qua bth 
        // kafka sâpk -> mail không được chuyển qua cho khách hàng -> chuyển trực típ mail
        try {
            SimpleOrderDTO orderInfo = new SimpleOrderDTO(
                order.getId(),
                user.getId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getOrderStatus().toString()           
            );

            OrderConfirmationEvent event = new OrderConfirmationEvent(orderInfo);

            // gửi qua kafka với timeout ngắn (5s)
            kafkaTemplate.send("order-confirmation", event).get(5, TimeUnit.SECONDS);
             log.info("Email sent via Kafka successfully for order: {}", order.getId());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // khi gửi thất bại -> fall back gửi mail trực tiếp 
            log.warn("Kafka failed! Sending email directly {}", e.getMessage());
            try {
                emailService.sendOrderConfirmationEmail(order, user);
                log.info("Email sent directly for order {} ", order.getId());
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                log.error("Both Kafka and direct email failed");
            }

        } 

    }

}
