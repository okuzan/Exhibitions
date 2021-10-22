package com.example.exhibitions.controller;

import com.example.exhibitions.data.ExhibitionDTO;
import com.example.exhibitions.entity.Exhibition;
import com.example.exhibitions.entity.Hall;
import com.example.exhibitions.error.CustomErrorException;
import com.example.exhibitions.repository.ExhibitionRepository;
import com.example.exhibitions.repository.HallRepository;
import com.example.exhibitions.repository.UserRepository;
import com.example.exhibitions.service.ExhibitionServiceImpl;
import com.example.exhibitions.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private HallRepository hallRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private ExhibitionServiceImpl exhibitionService;

    @Autowired
    private ExhibitionRepository exhibitionRepository;

    @ModelAttribute("exhibition")
    public ExhibitionDTO exhibitionDTO() {
        return new ExhibitionDTO();
    }

    @ModelAttribute("halls")
    public List<Hall> halls() {

//         hallRepo.findAll().contains();
        return hallRepo.findAll();
    }

    @GetMapping("/login")
    public String login() {
        return "views/unauthorized/login";
    }

    @GetMapping(value = {"/", "/welcome"})
    public String homepage() {
        return "views/authorized/welcome";
    }


    @PostMapping("api/shows/add")
    public String addExhibition(@ModelAttribute("exhibition")
                                @Validated ExhibitionDTO data,
                                BindingResult result) {
        if (result.hasErrors()) return "redirect:/api/shows/add?error";

        exhibitionService.save(data);
        return "redirect:/api/shows/add?success";
    }

    @PostMapping("api/shows/{id}/edit")
    public String editExhibition(@ModelAttribute("exhibition")
                                 @Validated ExhibitionDTO data,
                                 BindingResult result) {
        if (result.hasErrors()) return "redirect:/api/shows/{id}/edit?error";
        Exhibition updatedExhibition = exhibitionService.mapDataToExhibition(data);
        exhibitionRepository.save(updatedExhibition);
        return "redirect:/api/shows/{id}/edit?success";
    }

    @GetMapping("/api/shows/add")
    public String addShow(Model model) {
        return "views/authorized/admin/add_item";
    }

    @GetMapping("/api/shows")
    public String seeShows(Model model) {
        return "views/all/shows";
    }


    @GetMapping("/api/shows/{id}")
    public String show(Model model, @PathVariable String id) {
        return "views/all/show";
    }

    @GetMapping("/api/shows/{id}/buy")
    public String buy(Model model, @PathVariable String id) {
        return "views/authorized/user/buy";
    }

    @GetMapping("/api/shows/{id}/edit")
    public String edit(Model model, @PathVariable String id) {
        Exhibition exhibition = exhibitionRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new CustomErrorException(HttpStatus.NOT_FOUND, "Couldn't find that item!"));
//                .orElseThrow(ItemNotFound::new);
        model.addAttribute("data", exhibition);
        return "/views/authorized/admin/edit_item";
    }

    @ResponseBody
    @PostMapping("api/users/enabled/{id}")
    public String changeUserStatus(@RequestParam Boolean checked, @PathVariable String id) {
        userRepository.changeEnabledStatus(checked, Long.valueOf(id));
        return "Success";
    }

    @ResponseBody
    @DeleteMapping("/api/shows/delete/{id}")
    public ResponseEntity<?> deleteShow(@PathVariable String id) {
        exhibitionRepository.deleteById(Long.valueOf(id));
        return ResponseEntity.ok("Deleted");
    }

    @GetMapping(path = "/api/users")
    public String posts(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                        @RequestParam(value = "size", required = false, defaultValue = "10") int size, Model model
    ) {
        model.addAttribute("posts", userService.getPage(pageNumber, size));
        model.addAttribute("path", "/api/users");
        return "views/authorized/admin/users";
    }

}