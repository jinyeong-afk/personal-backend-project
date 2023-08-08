package com.example.demo.repository;

import com.example.demo.model.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Boolean existsByEmail(String email);
    Optional<Users> findOneWithAuthoritiesByEmail(String email);
    Users findByEmail(String email);
}
