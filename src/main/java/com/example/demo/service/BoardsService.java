package com.example.demo.service;

import com.example.demo.dto.CreateBoardsDTO;
import com.example.demo.requestObject.RequestCreateBoards;

public interface BoardsService {
    public void addBoard(CreateBoardsDTO createBoardsDTO);
}
