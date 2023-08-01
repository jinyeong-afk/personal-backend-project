package com.example.demo.dto;

import com.example.demo.requestObject.RequestSignUpUsers;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class SignUpUsersDTO {
    private String email;
    private String password;

    public SignUpUsersDTO(RequestSignUpUsers requestSignUpUsers) {
        this.email = requestSignUpUsers.getEmail();
        this.password = requestSignUpUsers.getPassword();
    }
}
