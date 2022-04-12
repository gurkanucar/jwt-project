package com.gucardev.jwtproject.service;

import com.gucardev.jwtproject.exception.GeneralException;
import com.gucardev.jwtproject.model.Role;
import com.gucardev.jwtproject.model.User;
import com.gucardev.jwtproject.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;


    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;

    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }


    public Role getRoleByName(String name) {
        return roleRepository.findRoleByName(name)
                .orElseThrow(() -> new GeneralException("Role not found!", HttpStatus.NOT_FOUND));
    }


    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }



}
