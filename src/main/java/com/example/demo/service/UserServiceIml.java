package com.example.demo.service;

import com.example.demo.model.Users;
import com.example.demo.repository.UsersRepository;
import com.example.demo.requestObject.RequestSignUpUsers;
import com.example.demo.responseObject.ResponseSignUpUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceIml implements UsersService {
    private final UsersRepository usersRepository;

    public ResponseSignUpUsers addUser(RequestSignUpUsers requestSignUpUsers) {
        Users users = new Users(requestSignUpUsers);
        if (!checkDuplicationUser(users.getEmail())) {
            usersRepository.save(users);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        return new ResponseSignUpUsers(requestSignUpUsers.getEmail());
    }

    public Boolean checkDuplicationUser(String email) {
        return usersRepository.existsByEmail(email);
    }
}
