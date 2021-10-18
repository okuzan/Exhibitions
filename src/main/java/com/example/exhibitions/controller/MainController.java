package com.example.exhibitions.controller;

import com.example.exhibitions.data.ExhibitionDTO;
import com.example.exhibitions.data.HallDTO;
import com.example.exhibitions.repository.HallRepository;
import com.example.exhibitions.repository.UserRepository;
import com.example.exhibitions.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class MainController {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private HallRepository hallRepo;

    @Autowired
    private UserServiceImpl userService;

    @ModelAttribute("exhibition")
    public ExhibitionDTO userRegistrationDto() {
        return new ExhibitionDTO();
    }


    @GetMapping("/welcome")
    public String welcome() {
        return "views/authorized/welcome";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "views/unauthorized/login";
    }


    @GetMapping("/")
    public String homepage(Model model) {
        return "views/authorized/welcome";
    }

    @GetMapping("/api/shows/{id}/edit")
    public String edit(Model model, @PathVariable String id) {

        return "views/authorized/admin/add_item";
    }

    @GetMapping("/api/shows/add")
    public String add(Model model) {
        model.addAttribute("halls", hallRepo.findAll());
//        model.addAttribute("exhibition", new ExhibitionDTO());
        return "views/authorized/admin/add_item";
    }

    @PostMapping("api/halls/add")
    public String customerRegi3stration(@ModelAttribute("hall") @Valid HallDTO data, BindingResult result) {

        if (result.hasErrors()) {
            System.out.println("binding errors!");
            return "views/unauthorized/registration2";
        }
        System.out.println("gone thru");
//        userService.save(data);
        return "views/unauthorized/registration_confirmation";
    }
    @PostMapping("show/add")
    public String customerRegi3stratio3n(@ModelAttribute("exhibtion") @Valid ExhibitionDTO data, BindingResult result) {

        if (result.hasErrors()) {
            System.out.println("binding errors!");
            return "views/unauthorized/registration2";
        }
        System.out.println("gone thru");
//        userService.save(data);
        return "views/unauthorized/registration_confirmation";
    }

    @GetMapping("/api/shows/{id}")
    public String show(Model model, @PathVariable String id) {
        return "show";
    }

    @GetMapping("/api/halls/add")
    public String hall(Model model) {
        model.addAttribute("hall", new HallDTO());
        return "views/unauthorized/registration2";
    }

    @GetMapping("/api/shows/{id}/buy")
    public String buy(Model model, @PathVariable String id) {
        return "buy";
    }

//    @RequestMapping(path = "/testrequest", method = RequestMethod.POST)
//    public String testGetRequest(@RequestBody String request)  {
//        final byte[] requestContent;
//        requestContent = IOUtils.toByteArray(request.getReader());
//         String s = new String(requestContent, StandardCharsets.UTF_8);
//    }
//    @PostMapping(path = "/api/users/enabled/{id}")
//    @PostMapping(path = "/api/users/enabled/{id}")
//    public void fjdksjf(Model model, @PathVariable String id){
//        System.out.println(id);
//    }
    @GetMapping(path = "/api/users")
    public String posts(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
                        @RequestParam(value = "size", required = false, defaultValue = "10") int size, Model model
                        ) {
        model.addAttribute("posts", userService.getPage(pageNumber, size));
        model.addAttribute("path","/api/users");
        return "views/authorized/admin/users";
    }

//    @RequestMapping(value = "/api/users/paginated", method = RequestMethod.GET)
//    public String listBooks(
//            Model model,
//            @RequestParam("page") Optional<Integer> page,
//            @RequestParam("size") Optional<Integer> size) {
//        int currentPage = page.orElse(1);
//        int pageSize = size.orElse(5);
//
//        Page<User> userPage = userService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
//
//        model.addAttribute("userPage", userPage);
//
//        int totalPages = userPage.getTotalPages();
//        if (totalPages > 0) {
//            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
//                    .boxed()
//                    .collect(Collectors.toList());
//            model.addAttribute("pageNumbers", pageNumbers);
//        }
//
//        return "views/authorized/admin/users_paginated";
//    }
}
