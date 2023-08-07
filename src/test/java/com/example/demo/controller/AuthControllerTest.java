package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.jwt.TokenProvider;
import com.example.demo.model.Users;
import com.example.demo.requestObject.RequestLoginUsers;
import com.example.demo.requestObject.RequestSignUpUsers;
import com.example.demo.service.CustomUserDetailsService;
import com.example.demo.service.UsersService;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @DisplayName("로그인 성공")
    @Test
    public void testLogin() throws Exception {
        // Arrange
        String username = "test@test.com";
        String password = "test1234";
        RequestLoginUsers requestLoginUsers = RequestLoginUsers.builder()
            .email(username)
            .password(password)
            .build();

        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        when(authenticationManager.authenticate(any())).thenReturn(
            new UsernamePasswordAuthenticationToken(username, password)
        );
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);

        String jwtToken = "mocked-jwt-token";
        when(tokenProvider.createToken(any())).thenReturn(jwtToken);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        ResponseEntity<Void> responseEntity = authController.authorize(requestLoginUsers);

        assertEquals(authentication.getName(), username);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Bearer " + jwtToken, responseEntity.getHeaders().getFirst("Authorization"));
    }

    @DisplayName("로그인 실패 - 잘못된 인증 정보")
    @Test
    public void testInvalidLogin() throws Exception {

        String failedUsername = "failed@test.com";
        String failedPassword = "failedPassword";
        RequestLoginUsers failedUsers = RequestLoginUsers.builder()
            .email(failedUsername)
            .password(failedPassword)
            .build();

        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);

        String jwtToken = "mocked-jwt-token";
        when(tokenProvider.createToken(any())).thenReturn(jwtToken);
        when(authController.authorize(failedUsers)).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        ResponseEntity<Void> responseEntity = authController.authorize(failedUsers);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @DisplayName("로그인 실패 - 올바르지 않은 이메일")
    @Test
    void signUpFailByInvalidEmail() throws Exception {
        // given
        RequestLoginUsers request = RequestLoginUsers
            .builder()
            .email("test1234") // 올바르지 않은 이메일 형식
            .password("test1234")
            .build();

        // when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(request))
        );

        // then
        resultActions.andExpect(status().isBadRequest());

        // 이메일 형식이 아닌 경우에는 서비스 메소드 addUser가 호출되지 않아야 함
        verify(customUserDetailsService, never()).loadUserByUsername(any());
    }
}