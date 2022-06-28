package com.gucardev.jwtproject.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("LoginQRCode")
public class LoginQRCode implements Serializable {

    @Id
    private Long id;

    private String room;
    private String code;

    @Enumerated(EnumType.STRING)
    private LoginQRCodeType type;
    private String message;

}
