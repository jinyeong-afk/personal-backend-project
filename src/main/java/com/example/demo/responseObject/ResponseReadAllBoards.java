package com.example.demo.responseObject;

import com.example.demo.model.Boards;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseReadAllBoards {
    private List<BoardsData> contents;
    private int pageSize;
    private long totalElements;
    private int totalPages;

    @Getter
    @NoArgsConstructor
    public static class BoardsData{
        private Long id;
        private Long userId;
        private String title;
        private String content;

        public BoardsData(Boards boards){
            this.id = boards.getId();
            this.userId = boards.getUsers().getId();
            this.title = boards.getTitle();
            this.content = boards.getContent();
        }
    }
}
