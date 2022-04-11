package com.gucardev.jwtproject.service;


import com.gucardev.jwtproject.exception.GeneralException;
import com.gucardev.jwtproject.model.RefreshToken;
import com.gucardev.jwtproject.model.User;
import com.gucardev.jwtproject.repository.RefreshTokenRepository;
import com.gucardev.jwtproject.request.CreateUserRequest;
import com.gucardev.jwtproject.request.LoginRequest;
import com.gucardev.jwtproject.request.RefreshTokenRequest;
import com.gucardev.jwtproject.util.JwtHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@Transactional
public class AuthService {

    @Value("${jwt-variables.EXPIRES_ACCESS_TOKEN}")
    private String EXPIRES_REFRESH_TOKEN;

    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtHelper jwtHelper;

    public AuthService(UserService userService, RefreshTokenRepository refreshTokenRepository, JwtHelper jwtHelper) {
        this.userService = userService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtHelper = jwtHelper;
    }

    /*
    public void createRefreshToken(RefreshTokenRequest refreshTokenRequest) {

    }
     */

    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        String token;
        do {

            token = jwtHelper.generateRefreshToken();

        } while (refreshTokenRepository.findByToken(token).isPresent());

        refreshToken.setUser(user);
        refreshToken.setToken(token);
        refreshToken.setExpiryDate(LocalDateTime.now().plusMinutes(Long.parseLong(EXPIRES_REFRESH_TOKEN)));
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }


    public Map<String, String> createTokens(User user) {

        String access_token = jwtHelper.createJwtToken(user);
        RefreshToken refreshToken = createRefreshToken(user);

        return Map.of("access_token", access_token,
                "refresh_token", refreshToken.getToken());

    }

    public Map<String, String> createTokens(String username) {

        var user = userService.getUserByUsername(username);

        String access_token = jwtHelper.createJwtToken(user);
        RefreshToken refreshToken = createRefreshToken(user);

        return Map.of("access_token", access_token,
                "refresh_token", refreshToken.getToken());

    }


    public User registerUser(CreateUserRequest createUserRequest) {
        return null;
    }


    public void login(LoginRequest loginRequest) {
    }


    public User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getUserByUsername(auth.getName());
    }

    public UsernamePasswordAuthenticationToken getAuthToken(String token) {
        return jwtHelper.getAuthToken(token);
    }


    public Map<String, String> refreshTokens(String refreshToken) {

        RefreshToken token = getRefreshToken(refreshToken);

        if (token != null) {
            deleteRefreshToken(token.getToken());

            return createTokens(token.getUser());
        } else
            throw new RuntimeException("Invalid refresh token!");

    }

    public void deleteRefreshToken(String token) {
        RefreshToken refreshToken = getRefreshToken(token);
        refreshTokenRepository.delete(refreshToken);
    }


    protected RefreshToken getRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("token not found"));
    }


}
