package com.example.demo.service;

import com.example.demo.model.Authority;
import com.example.demo.model.Users;
import com.example.demo.repository.UsersRepository;
import com.example.demo.requestObject.RequestSignUpUsers;
import com.example.demo.responseObject.ResponseSignUpUsers;
import com.example.demo.utils.SecurityUtil;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceIml implements UsersService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseSignUpUsers addUser(RequestSignUpUsers requestSignUpUsers) {
        if (!checkDuplicationUser(requestSignUpUsers.getEmail())) {
            Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();
            Users user = Users.builder()
                .email(requestSignUpUsers.getEmail())
                .password(passwordEncode(requestSignUpUsers.getPassword()))
                .authorities(Collections.singleton(authority))
                .build();
            usersRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        return new ResponseSignUpUsers(requestSignUpUsers.getEmail());
    }

    public String passwordEncode(String password) {
        return passwordEncoder.encode(password);
    }

    public Boolean checkDuplicationUser(String email) {
        return usersRepository.existsByEmail(email);
    }

    // 유저,권한 정보를 가져오는 메소드
    @Transactional(readOnly = true)
    public Optional<Users> getUserWithAuthorities(String email) {
        return usersRepository.findOneWithAuthoritiesByEmail(email);
    }

    // 현재 securityContext에 저장된 username의 정보만 가져오는 메소드
    @Transactional(readOnly = true)
    public Optional<Users> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername()
            .flatMap(usersRepository::findOneWithAuthoritiesByEmail);
    }
}
