package com.example.demo.controller;

import com.example.demo.requestObject.RequestSignUpUsers;
import com.example.demo.responseObject.ResponseSignUpUsers;
import com.example.demo.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UsersController {
    private final UsersService usersService;

    @PostMapping
    public ResponseEntity<ResponseSignUpUsers> signUp(@RequestBody @Validated RequestSignUpUsers requestSignUpUsers, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(usersService.addUser(requestSignUpUsers));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
