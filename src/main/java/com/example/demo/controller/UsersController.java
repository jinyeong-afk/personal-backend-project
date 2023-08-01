package com.example.demo.controller;

import com.example.demo.requestObject.RequestSignUpUsers;
import com.example.demo.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UsersController {
    private final UsersService usersService;

    @PostMapping
    public void signUp(@RequestBody @Validated RequestSignUpUsers requestSignUpUsers) {
        usersService.addUser(requestSignUpUsers);
    }
}
