package com.gucardev.jwtproject.service;

import com.gucardev.jwtproject.model.Role;
import com.gucardev.jwtproject.model.User;
import com.gucardev.jwtproject.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;


    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = getUserByUsername(username);

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), authorities);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public User getUser(String username) {
        User user = getUserByUsername(username);
        if (!isAuthorized(user)) {
            throw new RuntimeException("you can not make this operation!");
        }
        return user;
    }


    public User saveUser(User user) {
        var userRole = roleService.getRoleByName("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(userRole);
        return userRepository.save(user);
    }


    public void updateUser(User user) {
        // get the user if it is admin or himself
        User existing = getUser(user.getUsername());

        existing.setPassword(passwordEncoder.encode(user.getPassword()));
        existing.setName(user.getName());
        existing.setSurname(user.getSurname());
        existing.setRoles(user.getRoles());
        userRepository.save(existing);
    }


    public void deleteUser(String username) {
        // get the user if it is admin or himself
        User existing = getUser(username);
        userRepository.delete(existing);
    }


    protected User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found!"));
    }


    private boolean isAuthorized(User unknownUser) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = getUser(auth.getName());
        var isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        var isOwner = unknownUser.getId().equals(user.getId());
        return isAdmin || isOwner;
    }


    public void grantRole(String username, String roleName) {
        User user = getUserByUsername(username);
        Role role = roleService.getRoleByName(roleName);
        user.getRoles().add(role);
        updateUser(user);
    }

    public void revokeRole(String username, String roleName) {
        User user = getUserByUsername(username);
        Role role = roleService.getRoleByName(roleName);
        user.getRoles().remove(role);
        updateUser(user);
    }

}
