package com.example.boardservice.dto;

import com.example.boardservice.domain.BoardType;
import com.example.boardservice.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    // Dto -> Entity

}
