package com.gucardev.jwtproject.repository;

import com.gucardev.jwtproject.model.RefreshToken;
import com.gucardev.jwtproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);

    void deleteRefreshTokenByUser(User user);

}
