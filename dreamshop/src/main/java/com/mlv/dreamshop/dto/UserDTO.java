package com.mlv.dreamshop.dto;

import com.mlv.dreamshop.Model.Cart;
import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDTO> orders;
    private CartDTO cart;
}
