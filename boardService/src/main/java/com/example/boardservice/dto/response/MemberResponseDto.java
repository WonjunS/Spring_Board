package com.example.boardservice.dto.response;

import com.example.boardservice.domain.Member;
import com.example.boardservice.domain.Role;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class MemberResponseDto implements Serializable {

    private final Long id;
    private final String nickname;
    private final String email;
    private final Role role;
    private final LocalDateTime modifiedAt;

    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname();
        this.email = member.getEmail();
        this.role = member.getRole();
        this.modifiedAt = member.getModifiedAt();
    }

}