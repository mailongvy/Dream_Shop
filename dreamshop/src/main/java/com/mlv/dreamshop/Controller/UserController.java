package com.mlv.dreamshop.Controller;

import com.mlv.dreamshop.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mlv.dreamshop.Model.User;
import com.mlv.dreamshop.Response.ApiResponse;
import com.mlv.dreamshop.exceptions.AlreadyExistsException;
import com.mlv.dreamshop.exceptions.ResourceNotFound;
import com.mlv.dreamshop.request.CreateUserRequest;
import com.mlv.dreamshop.request.UserUpdateRequest;
import com.mlv.dreamshop.service.user.IUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/users")
public class UserController {
    private final IUserService userService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            User user = userService.getUserById(userId);
            UserDTO userDTO = userService.convertUserToDTO(user);
            return ResponseEntity.ok(new ApiResponse("User found", userDTO));
        } catch (ResourceNotFound e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/user/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
        try {
            User user = userService.createUser(request);
            UserDTO userDTO = userService.convertUserToDTO(user);
            return ResponseEntity.ok(new ApiResponse("Create User Success", userDTO));
        } catch (AlreadyExistsException e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/user/update/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UserUpdateRequest request, @PathVariable Long userId) {
        try {
            User user = userService.updateUser(request, userId);
            UserDTO userDTO = userService.convertUserToDTO(user);
            return ResponseEntity.ok(new ApiResponse("Update Success", userDTO));
        } catch (ResourceNotFound e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/user/delete/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("Delete User Success", null));
        } catch (ResourceNotFound e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(new ApiResponse(e.getMessage(), null));
        }
    }




}
