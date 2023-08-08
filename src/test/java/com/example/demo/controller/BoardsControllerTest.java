package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.model.Boards;
import com.example.demo.model.Users;
import com.example.demo.repository.BoardsRepository;
import com.example.demo.repository.UsersRepository;
import com.example.demo.requestObject.RequestCreateBoards;
import com.example.demo.service.BoardsService;
import com.example.demo.service.BoardsServiceImpl;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class BoardsControllerTest {

    @InjectMocks
    private BoardsController boardsController;
    @Mock
    private BoardsService boardsService;
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private BoardsRepository boardsRepository;
    @Mock
    private Authentication authentication;
    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        boardsService = new BoardsServiceImpl(boardsRepository, usersRepository);
        boardsController = new BoardsController(boardsService);
        mockMvc = MockMvcBuilders.standaloneSetup(boardsController).build();
    }

    @DisplayName("게시글 작성")
    @Test
    public void testCreateBoard() throws Exception {

        // 테스트용 데이터
        String userEmail = "test@example.com";
        String title = "Test Title";
        String content = "Test Content";
        RequestCreateBoards requestCreateBoards = new RequestCreateBoards(title, content);

        // Mock 객체의 행동 설정
        when(usersRepository.findByEmail(userEmail)).thenReturn(new Users());
        when(authentication.getName()).thenReturn(userEmail);

        boardsController.createBoard(requestCreateBoards, authentication);

        // Mock 객체의 메서드 호출 검증
        verify(boardsRepository).save(any(Boards.class));
    }
}