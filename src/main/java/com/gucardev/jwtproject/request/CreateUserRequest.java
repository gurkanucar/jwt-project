package com.gucardev.jwtproject.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CreateUserRequest {

    @NotNull
    @Size(max = 50, min = 3)
    private String username;

    @NotNull
    @Email
    private String email;


    @NotNull
    @Size(max = 50, min = 8)
    private String password;


}
