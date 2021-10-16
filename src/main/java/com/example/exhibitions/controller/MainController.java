package com.example.exhibitions.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MainController {
    @GetMapping("/welcome")
    public String welcome() {
        return "welcome";
    }
    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/")
    public String homepage(Model model){
        return "welcome";
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
    public String adminUsers(Model model, @PathVariable String id) {
        return "users";
    }
}
