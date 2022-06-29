package com.gucardev.jwtproject.model.qrLogin;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("LoginQRCode")
public class LoginQRCode implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    @NotEmpty
    private String room;

    @NotNull
    @NotEmpty
    private String code;

    @Transient
    @Enumerated(EnumType.STRING)
    private LoginQRCodeType type;

    @Transient
    private String message;

}
