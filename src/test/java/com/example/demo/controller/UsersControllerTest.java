package com.example.demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.repository.UsersRepository;
import com.example.demo.requestObject.RequestSignUpUsers;
import com.example.demo.responseObject.ResponseSignUpUsers;
import com.example.demo.service.UserServiceIml;
import com.example.demo.service.UsersService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {
    @InjectMocks
    private UsersController usersController;

    @Mock
    private UsersService usersService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(usersController).build();
    }

    @DisplayName("회원 가입 성공")
    @Test
    void signUpSuccess() throws Exception {
        // given
        RequestSignUpUsers request = RequestSignUpUsers.builder()
            .email("test@test.test")
            .password("test1234")
            .build();
        ResponseSignUpUsers response = new ResponseSignUpUsers("test@test.test");

        doReturn(response).when(usersService)
            .addUser(any(RequestSignUpUsers.class));

        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(request))
        );

        // then
        MvcResult mvcResult = resultActions.andExpect(status().isCreated())
            .andExpect(jsonPath("email", response.getEmail()).exists())
            .andReturn();
    }

    @DisplayName("올바르지 않은 이메일 회원가입 실패")
    @Test
    void signUpFailByInvalidEmail() throws Exception {
        // given
        RequestSignUpUsers request = RequestSignUpUsers
            .builder()
            .email("test1234") // 올바르지 않은 이메일 형식
            .password("test1234")
            .build();

        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(request))
        );

        // then
        resultActions.andExpect(status().isBadRequest());

        // 이메일 형식이 아닌 경우에는 서비스 메소드 addUser가 호출되지 않아야 함
        verify(usersService, never()).addUser(any(RequestSignUpUsers.class));
    }

    @DisplayName("올바르지 않은 비밀번호 회원가입 실패")
    @Test
    void signUpFailByInvalidPassword() throws Exception {
        // given
        RequestSignUpUsers request = RequestSignUpUsers
            .builder()
            .email("test@test.com")
            .password("test") // 8자 이하 비밀번호
            .build();

        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(request))
        );

        // then
        resultActions.andExpect(status().isBadRequest());

        // 이메일 형식이 아닌 경우에는 서비스 메소드 addUser가 호출되지 않아야 함
        verify(usersService, never()).addUser(any(RequestSignUpUsers.class));
    }

    @DisplayName("중복 이메일 회원가입 실패")
    @Test
    public void testSignUpDuplicateEmail() {
        // given
        String duplicateEmail = "duplicate@example.com";
        RequestSignUpUsers request = RequestSignUpUsers.builder()
            .email(duplicateEmail)
            .build();

        // when
        when(usersService.addUser(request)).thenThrow(new ResponseStatusException(HttpStatus.CONFLICT));

        ResponseEntity<ResponseSignUpUsers> responseEntity = usersController.signUp(request);

        // then
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody()); // Since an exception is thrown, the response body should be null

        verify(usersService).addUser(request);
    }
}