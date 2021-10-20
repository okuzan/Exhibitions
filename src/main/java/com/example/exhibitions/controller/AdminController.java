package com.example.exhibitions.controller;

import com.example.exhibitions.repository.HallRepository;
import com.example.exhibitions.repository.UserRepository;
import com.example.exhibitions.service.ExhibitionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AdminController {

//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    HallRepository hallRepository;
//    @Autowired
//    ExhibitionServiceImpl exhibitionService;
//
//    @PostMapping("/users/enabled/{id}")
//    public String changeUserStatus(@RequestParam Boolean checked, @PathVariable String id) {
//        userRepository.changeEnabledStatus(checked, Long.valueOf(id));
//        return "Success";
//    }
}
