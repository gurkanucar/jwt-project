package com.gucardev.jwtproject.controller;


import com.gucardev.jwtproject.dto.RoleDto;
import com.gucardev.jwtproject.model.Role;
import com.gucardev.jwtproject.request.RoleGrantRevokeRequest;
import com.gucardev.jwtproject.request.RoleRequest;
import com.gucardev.jwtproject.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;
    private final ModelMapper mapper;

    public RoleController(RoleService roleService,
                          ModelMapper mapper) {
        this.roleService = roleService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<?> saveRole(@Valid @RequestBody RoleRequest roleRequest) {
        var role = mapper.map(roleRequest, Role.class);
        return ResponseEntity.status(201).body(roleService.saveRole(role));
    }


    @GetMapping("/all")
    public ResponseEntity<?> getRoles() {
        var list = roleService.getRoles()
                .stream().map(x -> mapper.map(x, RoleDto.class));
        return ResponseEntity.ok(list);
    }




}
