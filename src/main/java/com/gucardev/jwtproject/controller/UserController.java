package com.gucardev.jwtproject.controller;


import com.gucardev.jwtproject.dto.UserDto;
import com.gucardev.jwtproject.model.User;
import com.gucardev.jwtproject.request.RoleGrantRevokeRequest;
import com.gucardev.jwtproject.request.UpdateUserRequest;
import com.gucardev.jwtproject.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {


    private final UserService userService;
    private final ModelMapper mapper;


    public UserController(UserService userService, ModelMapper mapper) {
        this.userService = userService;
        this.mapper = mapper;
    }


    @PreAuthorize("hasAnyAuthority('SUPERADMIN') || hasAnyAuthority('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        //convert to dto
        var list = userService.getAllUsers()
                .stream().map(x -> mapper.map(x, UserDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }


    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        var dto = mapper.map(userService.getUserByPermit(username), UserDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserRequest user) {
        userService.updateUser(user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @PutMapping("/role")
    public ResponseEntity<?> grantRole(@Valid @RequestBody RoleGrantRevokeRequest request) {
        var dto = mapper.map(userService.grantRole(request.getUsername(), request.getRole()), UserDto.class);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/role")
    public ResponseEntity<?> revokeRole(@Valid @RequestBody RoleGrantRevokeRequest request) {
        var dto = mapper.map(userService.revokeRole(request.getUsername(), request.getRole()), UserDto.class);
        return ResponseEntity.ok().body(dto);
    }

}
