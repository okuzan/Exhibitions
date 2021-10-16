package com.example.exhibitions.service;

import com.example.exhibitions.data.UserDTO;
import com.example.exhibitions.entity.Role;
import com.example.exhibitions.entity.User;
import com.example.exhibitions.repository.RoleRepository;
import com.example.exhibitions.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    private User mapDataToUser(final UserDTO userData) {
        User user = new User();
        user.setUsername(userData.getUsername());
        user.setEmail(userData.getEmail());
        user.setName(userData.getName());
        user.setPassword(passwordEncoder.encode(userData.getPassword()));
        Role role = roleRepository.findByName("ROLE_USER");
        user.setRoles(List.of(role));
        return user;
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(UserDTO dto) {
        User customerModel = mapDataToUser(dto);
        return userRepository.save(customerModel);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(s);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }
}
