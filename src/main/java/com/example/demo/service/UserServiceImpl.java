package com.example.demo.service;

import com.example.demo.model.Users;
import com.example.demo.repository.UsersRepository;
import com.example.demo.requestObject.RequestSignUpUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {
    private final UsersRepository usersRepository;

    public void addUser(RequestSignUpUsers requestSignUpUsers) {
        Users users = new Users(requestSignUpUsers);
        usersRepository.save(users);
    }
}
