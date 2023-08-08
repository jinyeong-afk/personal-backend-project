package com.example.demo.repository;

import com.example.demo.model.Boards;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardsRepository extends JpaRepository<Boards, Long> {

}
