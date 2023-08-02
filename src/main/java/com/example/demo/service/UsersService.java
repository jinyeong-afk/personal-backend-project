package com.example.demo.service;

import com.example.demo.requestObject.RequestSignUpUsers;
import com.example.demo.responseObject.ResponseSignUpUsers;

public interface UsersService {
    public ResponseSignUpUsers addUser(RequestSignUpUsers requestSignUpUsers);
}
