package com.gucardev.jwtproject;

import com.gucardev.jwtproject.model.ROLES;
import com.gucardev.jwtproject.model.Role;
import com.gucardev.jwtproject.model.User;
import com.gucardev.jwtproject.service.RoleService;
import com.gucardev.jwtproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class JwtProjectApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(JwtProjectApplication.class, args);
    }


    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    @Override
    public void run(String... args) throws Exception {

        roleService.saveRole(Role.builder().name("USER").detail("simple operations").build());
        roleService.saveRole(Role.builder().name("ADMIN").detail("admin operations").build());
        roleService.saveRole(Role.builder().name("SUPERADMIN").detail("super operations").build());
        User superadmin = userService.saveUser(User.builder().username("gurkan").email("g@mail.com").password("password").build());
        userService.saveUser(User.builder().username("metin").email("m@mail.com").password("password").build());
        userService.saveUser(User.builder().username("ali").email("a@mail.com").password("password").build());
        userService.saveUser(User.builder().username("kartal").email("k@mail.com").password("password").build());
        User admin = userService.saveUser(User.builder().username("sezai").email("s@mail.com").password("password").build());


        var superAdminRole = roleService.getRoleByName(ROLES.SUPERADMIN.toString());
        superadmin.setRoles(List.of(superAdminRole));
        userService.updateUser(superadmin);

        var adminRole = roleService.getRoleByName(ROLES.ADMIN.toString());
        admin.setRoles(List.of(adminRole));
        userService.updateUser(admin);


    }
}
