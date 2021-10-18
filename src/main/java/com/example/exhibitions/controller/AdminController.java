package com.example.exhibitions.controller;

import com.example.exhibitions.data.ExhibitionDTO;
import com.example.exhibitions.entity.Exhibition;
import com.example.exhibitions.entity.User;
import com.example.exhibitions.repository.ExhibitionRepository;
import com.example.exhibitions.repository.UserRepository;
import com.example.exhibitions.service.ExhibitionServiceImpl;
import com.example.exhibitions.service.UserService;
import com.example.exhibitions.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AdminController {
    @Autowired
    UserRepository userRepository;
//    @Autowired
//    ExhibitionRepository exhibitionRepository;
    @Autowired
    ExhibitionServiceImpl exhibitionService;
    @PostMapping("/users/enabled/{id}")
    public String changeUserStatus(@RequestParam Boolean checked, @PathVariable String id) {
        userRepository.changeEnabledStatus(checked, Long.valueOf(id));
        return "Success";
    }
    @PostMapping("/shows/add")
    public String addShow(@ModelAttribute ExhibitionDTO exhibition_data) {
        System.out.println(exhibition_data);
        exhibitionService.save(exhibition_data);
        return "Success";
    }
}
