package com.example.demo.controller;

import com.example.demo.dto.CreateBoardsDTO;
import com.example.demo.model.Boards;
import com.example.demo.requestObject.RequestCreateBoards;
import com.example.demo.responseObject.ResponseReadAllBoards;
import com.example.demo.responseObject.ResponseReadAllBoards.BoardsData;
import com.example.demo.responseObject.ResponseReadOneBoards;
import com.example.demo.service.BoardsService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/boards")
@RequiredArgsConstructor
public class BoardsController {

    private final BoardsService boardsService;

    @PostMapping
    public ResponseEntity<Void> createBoard(@RequestBody RequestCreateBoards requestCreateBoards,
        Authentication authentication) {
        boardsService.addBoard(CreateBoardsDTO.builder()
            .email(authentication.getName())
            .title(requestCreateBoards.getTitle())
            .content(requestCreateBoards.getContent())
            .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<ResponseReadAllBoards> ReadAllBoards(@RequestParam(required = false) Integer pagination) {
        if (pagination == null) pagination = 0;
        Page<Boards> boardsPage = boardsService.getAllBoards(pagination);
        return ResponseEntity.ok(ResponseReadAllBoards.builder()
            .contents(boardsPage.stream()
                .map(BoardsData::new)
                .collect(Collectors.toList()))
            .pageSize(boardsPage.getSize())
            .totalElements(boardsPage.getTotalElements())
            .totalPages(boardsPage.getTotalPages())
            .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseReadOneBoards> readOneBoards(@PathVariable long id) {
        try{
            Boards boards = boardsService.getOneBoards(id);
            return ResponseEntity.ok(ResponseReadOneBoards.builder()
                .id(boards.getId())
                .userEmail(boards.getUsers().getEmail())
                .title(boards.getTitle())
                .content(boards.getContent())
                .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
