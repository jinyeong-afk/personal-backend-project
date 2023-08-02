package com.example.demo.service;

import com.example.demo.dto.SignUpUsersDTO;
import com.example.demo.model.Users;
import com.example.demo.repository.UsersRepository;
import com.example.demo.requestObject.RequestSignUpUsers;
import com.example.demo.responseObject.ResponseSignUpUsers;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceIml implements UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseSignUpUsers addUser(RequestSignUpUsers requestSignUpUsers) {
        if (!checkDuplicationUser(requestSignUpUsers.getEmail())) {
            usersRepository.save(passwordEncode(requestSignUpUsers));
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        return new ResponseSignUpUsers(requestSignUpUsers.getEmail());
    }

    public Users passwordEncode(RequestSignUpUsers requestSignUpUsers) {
        SignUpUsersDTO signUpUsersDTO = new SignUpUsersDTO(requestSignUpUsers);
        signUpUsersDTO.setPassword(passwordEncoder.encode(signUpUsersDTO.getPassword()));
        return new Users(signUpUsersDTO);
    }

    public Boolean checkDuplicationUser(String email) {
        return usersRepository.existsByEmail(email);
    }
}
