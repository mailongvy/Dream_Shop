package com.mlv.dreamshop.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApiResponse {
    // return data for frontend
    private String message;
    private Object data;
}
