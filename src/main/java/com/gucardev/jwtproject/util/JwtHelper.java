package com.gucardev.jwtproject.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.gucardev.jwtproject.model.Role;
import com.gucardev.jwtproject.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtHelper {


    @Value("${jwt-variables.KEY}")
    private String KEY;

    @Value("${jwt-variables.ISSUER}")
    private String ISSUER;

    @Value("${jwt-variables.EXPIRES_ACCESS_TOKEN}")
    private String EXPIRES_ACCESS_TOKEN;



    private final Algorithm algorithm = Algorithm.HMAC256(KEY.getBytes());

    private final JWTVerifier verifier = JWT.require(algorithm).build();

    public String createJwtToken(User user) {

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRES_ACCESS_TOKEN))
                .withIssuer(ISSUER)
                .withClaim("roles", roles)
                .sign(algorithm);
    }


    public UsernamePasswordAuthenticationToken getAuthToken(String token) {
        DecodedJWT decodedJWT = verifier.verify(token);
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



}
