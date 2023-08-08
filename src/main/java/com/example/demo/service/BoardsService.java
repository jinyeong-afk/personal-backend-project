package com.example.demo.service;

import com.example.demo.dto.CreateBoardsDTO;
import com.example.demo.requestObject.RequestCreateBoards;
import com.example.demo.responseObject.ResponseReadAllBoards;

public interface BoardsService {
    public void addBoard(CreateBoardsDTO createBoardsDTO);
    public ResponseReadAllBoards getAllBoards(Integer pagination);
}
