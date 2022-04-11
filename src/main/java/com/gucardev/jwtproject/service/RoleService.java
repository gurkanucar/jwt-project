package com.gucardev.jwtproject.service;

import com.gucardev.jwtproject.model.Role;
import com.gucardev.jwtproject.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRoleByName(String name) {

        return roleRepository.findRoleByName(name)
                .orElseThrow(()->new RuntimeException("Role not found!"));
    }
}
