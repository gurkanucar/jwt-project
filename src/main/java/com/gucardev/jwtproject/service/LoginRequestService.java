package com.gucardev.jwtproject.service;

import com.gucardev.jwtproject.exception.GeneralException;
import com.gucardev.jwtproject.model.LoginQRCode;
import com.gucardev.jwtproject.repository.LoginQRCodeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LoginRequestService {

    private final LoginQRCodeRepository repository;

    public LoginRequestService(LoginQRCodeRepository repository) {
        this.repository = repository;
    }

    public LoginQRCode get(Long id) {
        return repository.findById(id).orElseThrow(() -> new GeneralException("Not Found!", 404));
    }

    public LoginQRCode update(LoginQRCode loginQRCode) {
        var existing = repository.findById(loginQRCode.getId()).orElseThrow(() -> new GeneralException("Not Found!", 404));
        existing.setCode(loginQRCode.getCode());
        return repository.save(existing);
    }

    public LoginQRCode create(LoginQRCode loginQRCode) {
        repository.findById(loginQRCode.getId()).ifPresent((s) -> {
            throw new GeneralException("Already Exists!", HttpStatus.CONFLICT);
        });
        return repository.save(loginQRCode);
    }

    public void delete(Long id) {
        var existing = repository.findById(id).orElseThrow(() -> new GeneralException("Not Found!", 404));
        repository.delete(existing);
    }


    public LoginQRCode getByRoom(String room) {
        return repository.findLoginQRCodeByRoom(room).orElseThrow(() -> new GeneralException("Not Found!", 404));
    }

}
