package com.mlv.dreamshop.service.Email;

import com.mlv.dreamshop.Model.Order;
import com.mlv.dreamshop.Model.User;
import com.mlv.dreamshop.dto.SimpleOrderDTO;

public interface IEmailService {
    void sendSimpleEmail(String mailTo, String subject, String text);
    void sendOrderConfirmationEmail(Order order, User user);
    void sendOrderConfirmationEmail(SimpleOrderDTO orderInfo);
}
