package com.gucardev.jwtproject.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gucardev.jwtproject.exception.GeneralException;
import com.gucardev.jwtproject.model.Role;
import com.gucardev.jwtproject.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtHelper {


    @Value("${jwt-variables.KEY}")
    private String KEY;

    @Value("${jwt-variables.ISSUER}")
    private String ISSUER;

    @Value("${jwt-variables.EXPIRES_ACCESS_TOKEN_MINUTE}")
    private Integer EXPIRES_ACCESS_TOKEN_MINUTE;


    public String createJwtToken(User user) {

        Algorithm algorithm = Algorithm.HMAC256(KEY.getBytes());

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + (EXPIRES_ACCESS_TOKEN_MINUTE * 60 * 1000)))
                .withIssuedAt(convertToDateViaSqlDate(LocalDate.now()))
                .withIssuer(ISSUER)
                .withClaim("roles", roles)
                .sign(algorithm);
    }


    public UsernamePasswordAuthenticationToken getAuthToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(KEY.getBytes());
        JWTVerifier verifier = JWT.require(algorithm)
                .acceptExpiresAt(EXPIRES_ACCESS_TOKEN_MINUTE * 60)
                .build();
        DecodedJWT decodedJWT;
        try {
            decodedJWT = verifier.verify(token);
        } catch (Exception e) {
            throw new GeneralException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        String username = decodedJWT.getSubject();
        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
        return new UsernamePasswordAuthenticationToken(username,
                null, authorities);

    }


    public String generateRefreshToken() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "");
    }

    public Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }


}
