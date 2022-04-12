package com.gucardev.jwtproject.controller;

import com.gucardev.jwtproject.dto.UserDto;
import com.gucardev.jwtproject.request.CreateUserRequest;
import com.gucardev.jwtproject.request.LoginRequest;
import com.gucardev.jwtproject.request.RefreshTokenRequest;
import com.gucardev.jwtproject.service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final ModelMapper mapper;


    public AuthController(AuthService authService,
                          ModelMapper mapper) {
        this.authService = authService;
        this.mapper = mapper;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return ResponseEntity.ok().body(authService.registerUser(createUserRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body(authService.login(loginRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        authService.logout();
        return ResponseEntity.ok().build();
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        authService.refreshTokens(refreshTokenRequest.getToken());
        return ResponseEntity.ok().build();
    }


}
