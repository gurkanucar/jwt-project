package com.gucardev.jwtproject.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("LoginQRCode")
public class LoginQRCode implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String room;
    private String code;

    private List<String> connectedUsers;

    @Transient
    @Enumerated(EnumType.STRING)
    private LoginQRCodeType type;

    @Transient
    private String message;

}
