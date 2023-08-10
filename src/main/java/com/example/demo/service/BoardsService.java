package com.example.demo.service;

import com.example.demo.dto.CreateBoardsDTO;
import com.example.demo.model.Boards;
import com.example.demo.requestObject.RequestCreateBoards;
import com.example.demo.responseObject.ResponseReadAllBoards;
import org.springframework.data.domain.Page;

public interface BoardsService {
    public void addBoard(CreateBoardsDTO createBoardsDTO);
    public Page<Boards> getAllBoards(Integer pagination);
}
