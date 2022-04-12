package com.gucardev.jwtproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok("Hello!");
    }

    @PreAuthorize("hasAnyAuthority('SUPERADMIN') || hasAnyAuthority('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<?> admin() {
        return ResponseEntity.ok("Only admin can see this!");
    }


}
