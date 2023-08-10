package com.example.demo.responseObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseReadOneBoards {
    private long id;
    private String title;
    private String content;
    private String userEmail;
}
