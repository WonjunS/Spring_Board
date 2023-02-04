package com.example.boardservice.dto.response;

import com.example.boardservice.domain.BoardType;
import com.example.boardservice.domain.Posts;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostsResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final String writer;
    private final int view;
    private final int likes;
    private final BoardType boardType;
    private final Long memberId;
    private LocalDateTime createdDate, modifiedDate;
    private final List<CommentResponseDto> comments;

    // Entity -> Dto
    public PostsResponseDto(Posts posts) {
        this.id = posts.getId();
        this.title = posts.getTitle();
        this.writer = posts.getWriter();
        this.content = posts.getContent();
        this.view = posts.getView();
        this.likes = posts.getLikes();
        this.boardType = posts.getBoardType();
        this.memberId = posts.getMember().getId();
        this.comments = posts.getComments().stream().map(CommentResponseDto::new).collect(Collectors.toList());
    }

}
