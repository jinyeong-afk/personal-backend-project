package com.example.demo.controller;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.model.Boards;
import com.example.demo.model.Users;
import com.example.demo.repository.BoardsRepository;
import com.example.demo.repository.UsersRepository;
import com.example.demo.requestObject.RequestCreateBoards;
import com.example.demo.responseObject.ResponseReadAllBoards;
import com.example.demo.responseObject.ResponseReadAllBoards.BoardsData;
import com.example.demo.responseObject.ResponseReadOneBoards;
import com.example.demo.service.BoardsService;
import com.example.demo.service.BoardsServiceImpl;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
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

    @DisplayName("모든 게시글 조회")
    @Test
    public void testGetAllBoards() throws Exception {
        // 테스트용 데이터
        int pagination = 0;
        ReflectionTestUtils.setField(boardsService, "PAGE_SIZE", 10);
        Boards boards = new Boards(1L, "테스트 타이틀", "테스트 콘텐츠", Users.builder().id(2L).build());
        List<Boards> boardsDataList = new ArrayList<>();
        boardsDataList.add(boards);

        PageImpl<Boards> boardsPage = new PageImpl<>(boardsDataList);
        when(boardsRepository.findAll(PageRequest.of(pagination, 10))).thenReturn(boardsPage);
        // Mock 객체의 행동 설정
        when(boardsService.getAllBoards(pagination)).thenReturn(boardsPage);

        List<BoardsData> boardsList = boardsPage.stream()
            .map(BoardsData::new)
            .collect(Collectors.toList());

        ResponseEntity<ResponseReadAllBoards> responseEntity = boardsController.ReadAllBoards(pagination);

        assertEquals(responseEntity.getBody().toString(), ResponseReadAllBoards.builder()
            .contents(boardsList)
            .pageSize(boardsPage.getSize())
            .totalElements(boardsPage.getTotalElements())
            .totalPages(boardsPage.getTotalPages())
            .build().toString());

        // Mock 객체의 메서드 호출 검증
        verify(boardsRepository).findAll(any(Pageable.class));
    }

    @DisplayName("특정 게시글 조회 성공")
    @Test
    public void testGetOneBoardsSuccess() {
        long findBoardId = 0L;
        Boards boards = Boards.builder()
            .id(findBoardId)
            .users(Users.builder().email("test@test.com").build())
            .title("테스트 타이틀")
            .content("테스트 컨텐츠")
            .build();

        when(boardsRepository.findById(findBoardId)).thenReturn(Optional.ofNullable(boards));

        ResponseEntity<ResponseReadOneBoards> response = boardsController.readOneBoards(findBoardId);

        assertEquals(response.getBody().toString(), ResponseReadOneBoards.builder()
            .id(boards.getId())
            .userEmail(boards.getUsers().getEmail())
            .title(boards.getTitle())
            .content(boards.getContent())
            .build().toString());
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        verify(boardsRepository).findById(findBoardId);
    }
}