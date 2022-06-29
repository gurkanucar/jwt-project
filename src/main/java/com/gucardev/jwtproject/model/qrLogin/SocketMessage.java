package com.gucardev.jwtproject.model.qrLogin;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

public class SocketMessage {

    @Transient
    @Enumerated(EnumType.STRING)
    private LoginQRCodeType type;

    @Transient
    private String message;

}
