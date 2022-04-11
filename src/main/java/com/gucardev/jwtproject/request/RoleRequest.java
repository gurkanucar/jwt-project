package com.gucardev.jwtproject.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoleRequest {


    private Long id;
    private String name;
    private String detail;


}
