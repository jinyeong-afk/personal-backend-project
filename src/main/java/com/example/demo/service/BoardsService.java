package com.example.demo.service;

import com.example.demo.dto.CreateBoardsDTO;
import com.example.demo.model.Boards;
import com.example.demo.requestObject.RequestUpdateBoards;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface BoardsService {
    public void addBoard(CreateBoardsDTO createBoardsDTO);
    public Page<Boards> getAllBoards(Integer pagination);
    public Boards getOneBoards(long id);
    public void updateBoards(long id, RequestUpdateBoards requestUpdateBoards, Authentication authentication);
    public void deleteBoards(long id, Authentication authentication);
}
