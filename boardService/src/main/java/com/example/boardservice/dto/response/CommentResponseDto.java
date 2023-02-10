package com.example.boardservice.dto.response;

import com.example.boardservice.domain.Comment;
import com.example.boardservice.domain.Member;
import com.example.boardservice.domain.Posts;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentResponseDto {

    private Long id;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String nickname;
    private Long memberId;
    private Long postsId;

    // Entity -> Dto
    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.nickname = comment.getMember().getNickname();
        this.memberId = comment.getMember().getId();
        this.postsId = comment.getPosts().getId();
    }

}
