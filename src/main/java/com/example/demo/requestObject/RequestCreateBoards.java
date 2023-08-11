package com.example.demo.requestObject;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateBoards {
    @NotEmpty(message = "제목 입력은 필수입니다.")
    private String title;
    @NotEmpty(message = "내용 입력은 필수입니다.")
    private String content;
}
