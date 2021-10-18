package com.example.exhibitions.controller;

import com.example.exhibitions.data.ExhibitionDTO;
import com.example.exhibitions.repository.HallRepository;
import com.example.exhibitions.repository.UserRepository;
import com.example.exhibitions.service.ExhibitionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AdminController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    HallRepository hallRepository;
    @Autowired
    ExhibitionServiceImpl exhibitionService;
    @PostMapping("/users/enabled/{id}")
    public String changeUserStatus(@RequestParam Boolean checked, @PathVariable String id) {
        userRepository.changeEnabledStatus(checked, Long.valueOf(id));
        return "Success";
    }
    @PostMapping("/shows/add")
    public String addShow(@Valid ExhibitionDTO data, Model model, BindingResult result) {
        System.out.println("HERE!!!!!!!!!!!!!");
        if (result.hasErrors()) {
            System.out.println("Binding errors");
            model.addAttribute("exhibition_data", new ExhibitionDTO());
            model.addAttribute("halls", hallRepository.findAll());
            return "views/authorized/admin/add_item";
        }
        exhibitionService.save(data);

        return "Success";
    }
}
