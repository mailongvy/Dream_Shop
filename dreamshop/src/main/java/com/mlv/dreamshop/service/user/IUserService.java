package com.mlv.dreamshop.service.user;

import com.mlv.dreamshop.Model.User;
import com.mlv.dreamshop.dto.UserDTO;
import com.mlv.dreamshop.request.CreateUserRequest;
import com.mlv.dreamshop.request.UserUpdateRequest;

public interface IUserService  {
    User getUserById(Long userId);

    User createUser(CreateUserRequest request);

    User updateUser(UserUpdateRequest request, Long userId);

    void deleteUser(Long userId);

    UserDTO convertUserToDTO(User user);
}
