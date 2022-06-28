package com.gucardev.jwtproject.repository;

import com.gucardev.jwtproject.model.LoginQRCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginQRCodeRepository extends CrudRepository<LoginQRCode, Long> {

    Optional<LoginQRCode> findLoginQRCodeByRoom(String code);

}
