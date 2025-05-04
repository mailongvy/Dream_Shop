package com.mlv.dreamshop.service.user;

import java.util.Optional;

import com.mlv.dreamshop.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mlv.dreamshop.DAO.UserRepository;
import com.mlv.dreamshop.Model.User;
import com.mlv.dreamshop.exceptions.AlreadyExistsException;
import com.mlv.dreamshop.exceptions.ResourceNotFound;
import com.mlv.dreamshop.request.CreateUserRequest;
import com.mlv.dreamshop.request.UserUpdateRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    

    @Override
    public User createUser(CreateUserRequest request) {
        // TODO Auto-generated method stub   
        return Optional.of(request)
                       .filter((user) -> !userRepository.existsByEmail(request.getEmail()))
                       .map(req -> {
                            User user = new User(); 
                            user.setEmail(req.getEmail());
                            user.setPassword(passwordEncoder.encode(req.getPassword()));
                            user.setFirstName(req.getFirstName());
                            user.setLastName(req.getLastName());
                            return userRepository.save(user);
                       }).orElseThrow(() -> new AlreadyExistsException( request.getEmail() + " already exist"));
    }

    @Override
    public void deleteUser(Long userId) {
        // TODO Auto-generated method stub
        userRepository.findById(userId).ifPresentOrElse(user -> userRepository.delete(user), () -> {
            throw new ResourceNotFound("User not found");
        });
        
    }

    @Override
    public User getUserById(Long userId) {
        // TODO Auto-generated method stub
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFound("User not found"));
    }

    @Override
    public User updateUser(UserUpdateRequest request, Long userId) {
        // TODO Auto-generated method stub
        return userRepository.findById(userId).map(existingUser -> {
            existingUser.setFirstName(request.getFirstName());
            existingUser.setLastName(request.getLastName());
            return userRepository.save(existingUser); // luuw thông tin người dùng lại sau khi cập nhật
        }).orElseThrow(() -> new ResourceNotFound("User not found"));


    }

    @Override
    public UserDTO convertUserToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public User getAuthenticationUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }
}
