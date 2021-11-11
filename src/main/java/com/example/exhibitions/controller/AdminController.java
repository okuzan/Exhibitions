package com.example.exhibitions.controller;

import com.example.exhibitions.data.ExhibitionDTO;
import com.example.exhibitions.entity.Exhibition;
import com.example.exhibitions.entity.Hall;
import com.example.exhibitions.entity.Ticket;
import com.example.exhibitions.error.CustomErrorException;
import com.example.exhibitions.repository.ExhibitionRepository;
import com.example.exhibitions.repository.HallRepository;
import com.example.exhibitions.repository.TicketRepository;
import com.example.exhibitions.repository.UserRepository;
import com.example.exhibitions.service.ExhibitionServiceImpl;
import com.example.exhibitions.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
public class AdminController {

    @Autowired
    private HallRepository hallRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private TicketRepository ticketRepository;

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
        return hallRepo.findAll();
    }

    @GetMapping("/shows/add")
    public String addShow(Model model) {
        return "views/authorized/admin/add_item";
    }

    @GetMapping("/shows/{id}/edit")
    public String edit(Model model, @PathVariable String id) {
        Exhibition exhibition = exhibitionRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new CustomErrorException(HttpStatus.NOT_FOUND, "Couldn't find that item!"));
        model.addAttribute("data", exhibition);
        return "/views/authorized/admin/edit_item";
    }

    @GetMapping(path = "/users")
    public String posts(
            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size, Model model
    ) {
        model.addAttribute("posts", userService.getPage(pageNumber, size));
        model.addAttribute("path", "/api/users");
        return "views/authorized/admin/users";
    }

    @PostMapping("/shows/add")
    public String addExhibition(@ModelAttribute("exhibition")
                                @Validated ExhibitionDTO data,
                                BindingResult result) {
        if (result.hasErrors()) return "redirect:/api/shows/add?error";

        exhibitionService.save(data);
        return "redirect:/api/shows/add?success";
    }


    @ResponseBody
    @PostMapping("/users/enabled/{id}")
    public String changeUserStatus(@RequestParam Boolean checked, @PathVariable String id) {
        userRepository.changeEnabledStatus(checked, Long.valueOf(id));
        return "Success";
    }

    @ResponseBody
    @DeleteMapping("/shows/delete/{id}")
    public ResponseEntity<?> deleteShow(@PathVariable String id) {
        exhibitionRepository.deleteById(Long.valueOf(id));

        return ResponseEntity.ok("Deleted");
    }

}
