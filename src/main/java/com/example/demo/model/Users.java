package com.example.demo.model;

import com.example.demo.dto.SignUpUsersDTO;
import com.example.demo.requestObject.RequestSignUpUsers;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;

    public Users(SignUpUsersDTO signUpUsersDTO){
        this.email = signUpUsersDTO.getEmail();
        this.password = signUpUsersDTO.getPassword();
    }
}
