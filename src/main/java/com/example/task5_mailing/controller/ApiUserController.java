package com.example.task5_mailing.controller;

import com.example.task5_mailing.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ApiUserController {

    private final UserService userService;

    @GetMapping("users/get-user/{key}")
    public List<String> getUsers(
            @PathVariable String key
    ) {
        return userService.getUsernames(key);
    }

}
