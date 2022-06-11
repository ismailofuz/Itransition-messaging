package com.example.task5_mailing.controller;

import com.example.task5_mailing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller

@RequestMapping("/login")
@RequiredArgsConstructor
public class UserAuthController {

    private final UserService userService;

    @GetMapping
    public String loginPage(){
        return "login";
    }

    @PostMapping
    public String login(
            @RequestParam String username,
            HttpSession session
    ){
        userService.saveUser(username);
        session.setAttribute("username", username);
        return "redirect:/";
    }


}
