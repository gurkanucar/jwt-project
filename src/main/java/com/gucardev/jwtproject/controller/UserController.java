package com.gucardev.jwtproject.controller;


import com.gucardev.jwtproject.dto.UserDto;
import com.gucardev.jwtproject.model.User;
import com.gucardev.jwtproject.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        var dto = mapper.map(userService.getUser(username), UserDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
