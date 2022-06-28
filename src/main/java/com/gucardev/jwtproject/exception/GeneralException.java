package com.gucardev.jwtproject.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;


@Data
public class GeneralException extends RuntimeException {

    private final String message;
    private final HttpStatus status;
    private final LocalDate time;

    public GeneralException(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.time = LocalDate.now();
    }

    public GeneralException(String message, int status) {
        this.message = message;
        this.status = HttpStatus.valueOf(status);
        this.time = LocalDate.now();
    }


}
