package com.example.boardservice.dto.request;

import com.example.boardservice.domain.Comment;
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
public class CommentRequestDto {

    private Long id;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Member member;
    private Posts posts;

    // Dto -> Entity
    public Comment toEntity() {
        Comment comments = Comment.builder()
                .id(id)
                .comment(comment)
                .member(member)
                .posts(posts)
                .build();

        return comments;
    }
}
