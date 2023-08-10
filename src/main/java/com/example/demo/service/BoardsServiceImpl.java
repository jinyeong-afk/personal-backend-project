package com.example.demo.service;

import com.example.demo.dto.CreateBoardsDTO;
import com.example.demo.model.Boards;
import com.example.demo.model.Users;
import com.example.demo.repository.BoardsRepository;
import com.example.demo.repository.UsersRepository;
import com.example.demo.requestObject.RequestUpdateBoards;
import com.example.demo.responseObject.ResponseReadAllBoards.BoardsData;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public Page<Boards> getAllBoards(Integer pagination) {
        Pageable pageable = PageRequest.of(pagination, PAGE_SIZE);
        Page<Boards> boardsPage = boardsRepository.findAll(pageable);
        List<BoardsData> boardsList = boardsPage.stream()
            .map(BoardsData::new)
            .collect(Collectors.toList());
        return boardsPage;
    }

    @Override
    public Boards getOneBoards(long id) {
        return boardsRepository.findById(id).get();
    }

    @Override
    public void updateBoards(long id, RequestUpdateBoards requestUpdateBoards, Authentication authentication) {
        Boards boards = checkUpdateBoardsException(id, authentication);
        if (requestUpdateBoards.getTitle() == null) requestUpdateBoards.setTitle(boards.getTitle());
        if (requestUpdateBoards.getContent() == null) requestUpdateBoards.setContent(boards.getContent());
        boardsRepository.save(Boards.builder()
            .id(boards.getId())
            .users(boards.getUsers())
            .title(requestUpdateBoards.getTitle())
            .content(requestUpdateBoards.getContent())
            .build());
    }

    public Boards checkUpdateBoardsException(long id, Authentication authentication) {
        Optional<Boards> boards = boardsRepository.findById(id);
        if (!boards.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!authentication.getName().equals(boards.get().getUsers().getEmail())) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED);
        }
        return boards.get();
    }

}
