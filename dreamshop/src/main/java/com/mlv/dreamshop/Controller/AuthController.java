package com.mlv.dreamshop.Controller;

import com.mlv.dreamshop.Response.ApiResponse;
import com.mlv.dreamshop.Response.JwtResponse;
import com.mlv.dreamshop.request.LoginRequest;
import com.mlv.dreamshop.security.jwt.JwtUtils;
import com.mlv.dreamshop.security.user.ShopUserDetails;
import com.mlv.dreamshop.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // tạo jwt token cho người dùng
            String jwt = jwtUtils.generateTokenForUser(authentication);
            // lấy thông tin người dùng từ authentication
            ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();

            // trả về thông tin người dùng và jwt token
            JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);

            // trả về thông tin người dùng và jwt token
            return ResponseEntity.ok(new ApiResponse("Login success", jwtResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED) // Lỗi xác thực không thể đăng nhập
                    .body(new ApiResponse("Login failed", null));
        }




    }

}
