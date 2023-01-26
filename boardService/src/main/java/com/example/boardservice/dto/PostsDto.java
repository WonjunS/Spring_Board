package com.example.boardservice.dto;

import com.example.boardservice.domain.BoardType;
import com.example.boardservice.domain.Member;
import com.example.boardservice.domain.Posts;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostsDto {

    private Long id;
    private String title;
    private String content;
    private String writer;
    private int view;
    private int likes;
    private BoardType boardType;
    private Member member;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // Dto -> Entity
    public Posts toEntity() {
        Posts post = Posts.builder()
                .id(id)
                .title(title)
                .content(content)
                .writer(writer)
                .view(0)
                .likes(0)
                .boardType(boardType)
                .build();

        return post;
    }

}
