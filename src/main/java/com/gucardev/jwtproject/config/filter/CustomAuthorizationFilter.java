package com.gucardev.jwtproject.config.filter;

import com.gucardev.jwtproject.exception.GeneralException;
import com.gucardev.jwtproject.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final AuthService authService;

    public CustomAuthorizationFilter(AuthService authService) {
        this.authService = authService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {


        if (request.getServletPath().equals("/login"))
            filterChain.doFilter(request, response);

        else {
            String authHeader = request.getHeader(AUTHORIZATION);

            if (authHeader != null && authHeader.startsWith("Bearer ")) {

                String token = authHeader.substring(7);
                UsernamePasswordAuthenticationToken authToken;
                try {
                    authToken = authService.getAuthToken(token);
                } catch (Exception e) {
                    throw new GeneralException("Try to login again!", HttpStatus.UNAUTHORIZED);
                }


                SecurityContextHolder.getContext().setAuthentication(authToken);

                filterChain.doFilter(request, response);


            } else {
                filterChain.doFilter(request, response);
            }

        }


    }
}
