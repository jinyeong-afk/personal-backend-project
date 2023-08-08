package com.example.demo.service;

import com.example.demo.dto.CreateBoardsDTO;
import com.example.demo.model.Boards;
import com.example.demo.model.Users;
import com.example.demo.repository.BoardsRepository;
import com.example.demo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardsServiceImpl implements BoardsService{

    private final BoardsRepository boardsRepository;
    private final UsersRepository usersRepository;

    @Override
    public void addBoard(CreateBoardsDTO createBoardsDTO) {
        Users users = usersRepository.findByEmail(createBoardsDTO.getEmail());
        boardsRepository.save(Boards.builder()
            .users(users)
            .title(createBoardsDTO.getTitle())
            .content(createBoardsDTO.getContent())
            .build());
    }
}
