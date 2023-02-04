package com.example.boardservice.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardType {

    FREE("FREE_BOARD"),
    ANNOUNCEMENT("ANNOUNCEMENT_BOARD");

    private final String value;

}