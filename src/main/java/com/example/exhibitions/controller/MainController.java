package com.example.exhibitions.controller;

import com.example.exhibitions.entity.User;
import com.example.exhibitions.repository.UserRepository;
import com.example.exhibitions.service.UserService;
import com.example.exhibitions.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class MainController {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/welcome")
    public String welcome() {
        return "views/authorized/welcome";
    }
    @GetMapping("/login")
    public String login(Model model) {
        return "views/unauthorized/login";
    }


    @GetMapping("/")
    public String homepage(Model model){
        return "views/authorized/welcome";
    }
    @GetMapping("/api/shows/{id}/edit")
    public String edit(Model model, @PathVariable String id) {
        return "edit";
    }
    @GetMapping("/api/shows/{id}")
    public String show(Model model, @PathVariable String id) {
        return "show";
    }
    @GetMapping("/api/shows/{id}/buy")
    public String buy(Model model, @PathVariable String id) {
        return "buy";
    }

    @GetMapping("/api/users")
    public ModelAndView adminUsers(Model model) {
        ModelAndView mav = new ModelAndView("views/authorized/admin/users");
        mav.addObject("users", userRepo.findAll());
        return mav;
    }
    @RequestMapping(value = "/api/users/paginated", method = RequestMethod.GET)
    public String listBooks(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<User> userPage = userService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("userPage", userPage);

        int totalPages = userPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "views/authorized/admin/users_paginated";
    }
}
