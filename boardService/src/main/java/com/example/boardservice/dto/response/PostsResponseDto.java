package com.example.boardservice.dto.response;

import com.example.boardservice.domain.BoardType;
import com.example.boardservice.domain.Posts;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostsResponseDto {

    private Long id;
    private String title;
    private String content;
    private String writer;
    private int view;
    private int likes;
    private BoardType boardType;
    private Long memberId;
    private LocalDateTime createdAt, modifiedAt;
    private List<CommentResponseDto> comments;

    public PostsResponseDto() {}

    // Entity -> Dto
    public PostsResponseDto(Posts posts) {
        this.id = posts.getId();
        this.title = posts.getTitle();
        this.writer = posts.getWriter();
        this.content = posts.getContent();
        this.view = posts.getView();
        this.likes = posts.getLikes();
        this.boardType = posts.getBoardType();
        this.createdAt = posts.getCreatedAt();
        this.modifiedAt = posts.getModifiedAt();
        this.memberId = posts.getMember().getId();
        this.comments = posts.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }

}
