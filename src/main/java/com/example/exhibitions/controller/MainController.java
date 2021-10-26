package com.example.exhibitions.controller;

import com.example.exhibitions.data.ExhibitionDTO;
import com.example.exhibitions.data.ExhibitionSpecificationsBuilder;
import com.example.exhibitions.entity.Exhibition;
import com.example.exhibitions.entity.Hall;
import com.example.exhibitions.entity.Ticket;
import com.example.exhibitions.entity.User;
import com.example.exhibitions.error.CustomErrorException;
import com.example.exhibitions.repository.ExhibitionRepository;
import com.example.exhibitions.repository.HallRepository;
import com.example.exhibitions.repository.TicketRepository;
import com.example.exhibitions.repository.UserRepository;
import com.example.exhibitions.service.ExhibitionServiceImpl;
import com.example.exhibitions.service.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class MainController {
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

    @GetMapping("/login")
    public String login() {
        return "views/unauthorized/login";
    }

    @GetMapping("/snippet")
    public String login3() {
        return "views/authorized/user/snippet";
    }

    @GetMapping(value = {"/", "/welcome"})
    public String homepage() {
        return "views/authorized/welcome";
    }

    @GetMapping("/account")
    public String account(Model model, @CurrentSecurityContext(expression = "authentication?.name") String username) {
        User user = userRepository.findByEmail(username).orElseThrow();
        model.addAttribute("user", user);
        return "views/authorized/user/account";
    }

    @PostMapping("/api/shows/add")
    public String addExhibition(@ModelAttribute("exhibition")
                                @Validated ExhibitionDTO data,
                                BindingResult result) {
        if (result.hasErrors()) return "redirect:/api/shows/add?error";

        exhibitionService.save(data);
        return "redirect:/api/shows/add?success";
    }

    @PostMapping("/account")
    public String replenishAccount(@ModelAttribute("balance") BigDecimal balance,
                                   BindingResult result, @CurrentSecurityContext(expression = "authentication?.name") String username) {
        if (result.hasErrors()) return "redirect:/account?error";
        userRepository.replenishAccount(balance, username);
        return "redirect:/account?success";
    }

    @PostMapping("/api/shows/{id}/edit")
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
        model.addAttribute("data", exhibition);
        return "/views/authorized/admin/edit_item";
    }

    @ResponseBody
    @PostMapping("/api/users/enabled/{id}")
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

    @ResponseBody
    @PostMapping("/api/shows/buy")
    public ResponseEntity<?> buyShow(@RequestParam Long id,
                                     @CurrentSecurityContext(expression = "authentication?.name") String username) {
        userService.buyTicket(username, id);
        return ResponseEntity.ok("Bought!");
    }

    private static final Logger logger = LogManager.getLogger(MainController.class);

    @GetMapping("/api/shows")
    public String seeShows(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "start", required = false) String startHash,
            @RequestParam(value = "end", required = false) String endHash,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size, Model model) {
        ExhibitionSpecificationsBuilder builder = new ExhibitionSpecificationsBuilder();
        logger.info(exhibitionRepository.getMaxPrice());
        logger.info(exhibitionRepository.getMinPrice());
        model.addAttribute("minPrice", exhibitionRepository.getMinPrice());
        model.addAttribute("maxPrice", exhibitionRepository.getMaxPrice());
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }
        Specification<Exhibition> spec = builder.build();
        if (startHash != null && !startHash.isEmpty()) {
            model.addAttribute("posts", exhibitionService.getPageFiltered(pageNumber, size, startHash, endHash));
        } else {
            model.addAttribute("posts", exhibitionService.getPage(pageNumber, size, spec));
        }
        model.addAttribute("path", "/api/shows");
        return "views/all/shows";
    }

    @GetMapping("/api/tickets")
    public String seeTickets(Model model, @CurrentSecurityContext(expression = "authentication?.name") String username) {
        List<Ticket> tickets = ticketRepository.findAllByUser(userRepository.findByEmail(username).get());
        model.addAttribute("tickets", tickets);
        return "views/authorized/user/tickets";
    }

    @GetMapping(path = "/api/users")
    public String posts(
            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size, Model model
    ) {
        model.addAttribute("posts", userService.getPage(pageNumber, size));
        model.addAttribute("path", "/api/users");
        return "views/authorized/admin/users";
    }

}