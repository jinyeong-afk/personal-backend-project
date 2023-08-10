package com.example.demo.service;

import com.example.demo.dto.CreateBoardsDTO;
import com.example.demo.model.Boards;
import com.example.demo.requestObject.RequestUpdateBoards;
import org.springframework.data.domain.Page;

public interface BoardsService {
    public void addBoard(CreateBoardsDTO createBoardsDTO);
    public Page<Boards> getAllBoards(Integer pagination);
    public Boards getOneBoards(long id);
    public void updateBoards(long id, RequestUpdateBoards requestUpdateBoards);
}
