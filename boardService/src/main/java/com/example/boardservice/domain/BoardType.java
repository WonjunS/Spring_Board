package com.example.boardservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardType {

    FREE("free"),
    QUESTION("question"),
    NOTICE("notice");

    @Getter
    private final String value;

}