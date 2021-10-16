package com.example.exhibitions.service;

import com.example.exhibitions.data.UserDTO;
import com.example.exhibitions.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService  extends UserDetailsService {

    User findByEmail(String email);
    User save (UserDTO dto);
}
