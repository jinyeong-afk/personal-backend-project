package com.example.demo.service;

import com.example.demo.dto.CreateBoardsDTO;
import com.example.demo.model.Boards;
import com.example.demo.model.Users;
import com.example.demo.repository.BoardsRepository;
import com.example.demo.repository.UsersRepository;
import com.example.demo.responseObject.ResponseReadAllBoards;
import com.example.demo.responseObject.ResponseReadAllBoards.BoardsData;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardsServiceImpl implements BoardsService{

    @Value("${spring.data.web.pageable.default-page-size}")
    private int PAGE_SIZE;
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

    @Override
    public ResponseReadAllBoards getAllBoards(Integer pagination) {
        Pageable pageable = PageRequest.of(pagination, PAGE_SIZE);
        Page<Boards> boardsPage = boardsRepository.findAll(pageable);
        List<BoardsData> boardsList = boardsPage.stream()
            .map(BoardsData::new)
            .collect(Collectors.toList());
        return ResponseReadAllBoards.builder()
            .contents(boardsList)
            .pageSize(boardsPage.getSize())
            .totalElements(boardsPage.getTotalElements())
            .totalPages(boardsPage.getTotalPages())
            .build();
    }

}
