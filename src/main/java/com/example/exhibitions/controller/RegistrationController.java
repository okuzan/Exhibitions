package com.example.exhibitions.controller;

import com.example.exhibitions.data.UserDTO;
import com.example.exhibitions.entity.User;
import com.example.exhibitions.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserDTO userRegistrationDto() {
        return new UserDTO();
    }

    @GetMapping
    public String getRegistrationView() {
        return "views/unauthorized/registration";
    }

    @PostMapping
    public String userRegistration(@ModelAttribute("user") @Valid UserDTO userDto, BindingResult result) {
        User existing = userService.findByEmail(userDto.getEmail());
        if (existing != null)
            result.rejectValue("email", null, "There is already an account registered with that email");

        if (result.hasErrors()) return "redirect:/register?error";

        userService.save(userDto);
        return "redirect:/register?success";
    }
}
