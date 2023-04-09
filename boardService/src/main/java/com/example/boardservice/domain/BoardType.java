package com.example.boardservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardType {

    FREE("자유게시판"),
    QUESTION("질문게시판"),
    NOTICE("공지게시판");

    @Getter
    private final String value;

}