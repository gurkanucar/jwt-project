package com.gucardev.jwtproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RefreshTokenDto {

    private String accessToken;
    private String refreshToken;

}
