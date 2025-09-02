package com.mlv.dreamshop.service.Email;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.mlv.dreamshop.Model.Order;
import com.mlv.dreamshop.Model.OrderItem;
import com.mlv.dreamshop.Model.User;
import com.mlv.dreamshop.dto.SimpleOrderDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements IEmailService {
    private final JavaMailSender javaMailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    @Value("${app.mail.name}")
    private String fromName;


    @Override
    public void sendSimpleEmail(String mailTo, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(mailTo);
            message.setSubject(subject);
            message.setText(text);

            javaMailSender.send(message);
            log.info("Simple email sent successfully to {} ", mailTo);
            System.out.println("Send Email Successfully");
        } catch (MailException e) {
            // TODO Auto-generated catch block
            log.error("Failed to send simple email to: {}", mailTo, e);
            throw new RuntimeException("Failed to send email", e);
        }

    }

    @Override
    public void sendOrderConfirmationEmail(SimpleOrderDTO orderInfo) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    

    @Override
    public void sendOrderConfirmationEmail(Order order, User user) {
        // tiêu đề của gmail
        try {
            String subject = "Đặt hàng thành công - Đơn hàng #" + order.getId();
            // nội dung email
            String emailContent = buildOrderConfirmationContent(order, user);

            sendSimpleEmail("longvy070804@gmail.com" , subject, emailContent);

            // ghi log để thông báo đã gửi mail thành công
            log.info("Order confirmation email sent successfully to: {} for order: {}", 
                        user.getEmail(), order.getId());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Failed to send order confirmation email to: {} for order: {}", 
                    user.getEmail(), order.getId(), e);
        }
    }

    // trước khi gửi email thì phải xây dựng content trước khi gửi 
    private String buildOrderConfirmationContent(Order order, User user) {
        StringBuilder emailContent = new StringBuilder();

        // lời chào
        emailContent.append("Xin chào ").append(user.getFirstName()).append(" ").append(user.getLastName()).append("!\n\n");

        // lời cảm ơn 
        emailContent.append("Cảm ơn bạn đã đặt hàng tại hệ thống ").append(fromName).append(" \n\n");

        // thông tin đơn hàng
        emailContent.append("THÔNG TIN ĐƠN HÀNG: \n");
        emailContent.append("===============================================================================\n");
        emailContent.append("Mã đơn hàng: #").append(order.getId()).append("\n");
        emailContent.append("Ngày đặt hàng: ").append(order.getOrderDate()).append("\n");
        emailContent.append("Trạng thái: ").append(getOrderStatusInVietnamese(order.getOrderStatus().toString())).append("\n");
        emailContent.append("Khách hàng: ").append(user.getFirstName()).append(" ").append(user.getLastName()).append("\n");
        emailContent.append("Email: ").append(user.getEmail()).append("\n");

        emailContent.append("CHI TIẾT SẢN PHẨM:\n");
        emailContent.append("===============================================================================\n");

        // đánh dấu stt của order items
        int itemNumber = 1;
        for (OrderItem item : order.getOrderItems()) {
            emailContent.append(itemNumber++).append(". ").append(item.getProduct().getName()).append("\n");
            emailContent.append("   - Thương hiệu: ").append(item.getProduct().getBrand()).append("\n");
            emailContent.append("   - Số lượng: ").append(item.getQuantity()).append("\n");
            emailContent.append("   - Đơn giá: $").append(item.getPrice()).append("\n");
            emailContent.append("   - Thành tiền: $").append(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))).append("\n\n");
        }

        // Tổng kết đơn hàng
        BigDecimal shipping = order.getTotalAmount().compareTo(new BigDecimal("100")) >= 0 ? BigDecimal.ZERO : new BigDecimal("2");
        BigDecimal finalTotal = order.getTotalAmount().add(shipping);
        
        emailContent.append("===============================================================================\n");
        emailContent.append("TỔNG KẾT ĐƠN HÀNG:\n");
        emailContent.append("Tạm tính: $").append(order.getTotalAmount()).append("\n");
        emailContent.append("Phí giao hàng: $").append(shipping).append("\n");
        emailContent.append("Tổng cộng: $").append(finalTotal).append("\n\n");
        
        emailContent.append("Cảm ơn bạn đã tin tướng và đặt hàng tại ").append(fromName).append("!\n");
        emailContent.append("Đơn hàng sẽ được xử lý và giao đến bạn trong thời gian sớm nhất.\n\n");
        emailContent.append("Trân trọng,\n");
        emailContent.append("Đội ngũ ").append(fromName);
        
        return emailContent.toString();


    }

    private String getOrderStatusInVietnamese(String status) {
        return switch (status) {
            case "PENDING" -> "Đang xử lý";
            case "PROCESSING" -> "Đang chuẩn bị";
            case "SHIPPED" -> "Đã giao vận";
            case "DELIVERED" -> "Đã giao hàng";
            case "CANCELLED" -> "Đã hủy";
            default -> status;
        };
    }

    
    

    

}
