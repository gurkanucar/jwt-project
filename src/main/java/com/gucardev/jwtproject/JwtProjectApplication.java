package com.gucardev.jwtproject;

import com.gucardev.jwtproject.model.Role;
import com.gucardev.jwtproject.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JwtProjectApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(JwtProjectApplication.class, args);
    }


    @Autowired
    RoleService roleService;

    @Override
    public void run(String... args) throws Exception {
        roleService.saveRole(Role.builder().name("ROLE_USER").detail("simple operations").build());
    }
}
