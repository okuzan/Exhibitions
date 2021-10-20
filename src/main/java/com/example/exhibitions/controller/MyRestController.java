package com.example.exhibitions.controller;
import com.example.exhibitions.data.ExhibitionDTO;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpResponse;

@RestController
public class MyRestController {

//    @PostMapping("show/add")
//    public String customerRegistration(
//            @ModelAttribute("exhibition") @Validated ExhibitionDTO data,
//             BindingResult result, Model model) throws IOException {
//        System.out.println("iḿ in");
//        if (result.hasErrors()) {
//            System.out.println("binding errors!");
////            return "redirect:/show/add?success";
////            httpResponse.sendRedirect("/show/add?success");
//
//            return "redirect:/show/add?success";
//        }
////        httpResponse.sendRedirect("/");
//        System.out.println("gone thru");
//        model.addAttribute("message", "Success");
////        userService.save(data);
//        return "redirect:/show/add?success";
//    }

}
