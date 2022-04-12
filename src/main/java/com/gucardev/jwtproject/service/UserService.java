package com.gucardev.jwtproject.service;

import com.gucardev.jwtproject.exception.GeneralException;
import com.gucardev.jwtproject.model.ROLES;
import com.gucardev.jwtproject.model.Role;
import com.gucardev.jwtproject.model.User;
import com.gucardev.jwtproject.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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


    public User getUserByPermit(String username) {
        User user = getUserByUsername(username);
        if (!isAuthorized(user)) {
            throw new GeneralException("you can not make this operation!", HttpStatus.UNAUTHORIZED);
        }
        return user;
    }


    public User saveUser(User user) {

        if (userRepository.findUserByUsername(user.getUsername()).isPresent()) {
            throw new GeneralException("User already exists!", HttpStatus.CONFLICT);
        }

        var userRole = roleService.getRoleByName(ROLES.USER.toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of(userRole));
        User newUser;
        try {
            newUser = userRepository.save(user);
        } catch (Exception e) {
            throw new GeneralException("An error occurred while saving user!",
                    HttpStatus.BAD_REQUEST);
        }
        return newUser;
    }


    public void updateUser(User user) {
        // get the user if it is admin or himself
        //  User existing = getUserByPermit(user.getUsername());
        User existing = getUserByUsername(user.getUsername());

        existing.setPassword(user.getPassword());
        existing.setName(user.getName());
        existing.setSurname(user.getSurname());
        if (doesIncludesRoles(List.of(ROLES.ADMIN, ROLES.SUPERADMIN), user.getRoles())) {
            existing.setRoles(user.getRoles());
        }
        userRepository.save(existing);
    }


    public void deleteUser(String username) {
        // get the user if it is admin or himself
        User existing = getUserByPermit(username);
        userRepository.delete(existing);
    }


    protected User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new GeneralException("User not found!", HttpStatus.NOT_FOUND));
    }

    protected User checkLoginUser(String username, String password) {
        var user = getUserByUsername(username);
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new GeneralException("wrong password!", HttpStatus.NOT_FOUND);
        }
        return user;
    }


    private boolean isAuthorized(User unknownUser) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = getUserByUsername(auth.getName());

        var isAdmin = doesIncludesRoles(
                List.of(ROLES.SUPERADMIN, ROLES.ADMIN),
                user.getRoles());

        var isOwner = unknownUser.getId().equals(user.getId());

        return isAdmin || isOwner;
    }


    public User grantRole(String username, String roleName) {
        User user = getUserByUsername(username);
        Role role = roleService.getRoleByName(roleName);
        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            updateUser(user);
        } else {
            throw new GeneralException("Role already exists!", HttpStatus.NOT_ACCEPTABLE);
        }

        return user;
    }

    public User revokeRole(String username, String roleName) {
        User user = getUserByUsername(username);
        Role role = roleService.getRoleByName(roleName);
        var roles = user.getRoles();
        if (roles.size() > 1 && !role.getName().equals(ROLES.SUPERADMIN.toString())) {
            user.getRoles().remove(role);
            updateUser(user);
        } else if (role.getName().equals(ROLES.SUPERADMIN.toString())) {
            throw new GeneralException("Are you kidding :D", HttpStatus.NOT_ACCEPTABLE);
        } else {
            throw new GeneralException("Every user must have a role at least!", HttpStatus.NOT_ACCEPTABLE);
        }
        return user;
    }


    protected boolean doesIncludesRoles(List<ROLES> checkRoles, List<Role> roles) {
        return roles.stream()
                .anyMatch(role -> checkRoles.stream()
                        .anyMatch(x -> x.toString().equals(role.getName())));
    }

}
