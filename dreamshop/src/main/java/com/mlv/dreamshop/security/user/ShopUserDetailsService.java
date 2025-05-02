package com.mlv.dreamshop.security.user;

import com.mlv.dreamshop.DAO.UserRepository;
import com.mlv.dreamshop.Model.User;
import com.mlv.dreamshop.exceptions.ResourceNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ShopUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ShopUserDetails shopUserDetails;

    @Override
    public UserDetails loadUserByUsername(String email)  {
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() -> new ResourceNotFound("User not found"));
        return shopUserDetails.buildUserDetail(user);
    }


}
