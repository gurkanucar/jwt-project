package com.gucardev.jwtproject.service;


import com.gucardev.jwtproject.model.User;
import com.gucardev.jwtproject.request.CreateUserRequest;
import com.gucardev.jwtproject.request.LoginRequest;
import com.gucardev.jwtproject.request.RefreshTokenRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public User registerUser(CreateUserRequest createUserRequest) {
        return null;
    }


    public void login(LoginRequest loginRequest) {
    }

    public void createRefreshToken(RefreshTokenRequest refreshTokenRequest) {
    }


    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByUsername(auth.getName());
    }

}
