package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.requestObject.RequestSignUpUsers;
import com.example.demo.responseObject.ResponseSignUpUsers;
import com.example.demo.service.UsersService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
        RequestSignUpUsers request = signUpRequest();
        ResponseSignUpUsers response = userResponse();

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

    private RequestSignUpUsers signUpRequest() {
        return RequestSignUpUsers.builder()
            .email("test@test.test")
            .password("test1234")
            .build();
    }

    private ResponseSignUpUsers userResponse() {
        return new ResponseSignUpUsers("test@test.test");
    }
}