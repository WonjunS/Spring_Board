package com.example.boardservice.dto;

import com.example.boardservice.domain.Member;
import com.example.boardservice.domain.Role;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {

    private Long id;
    private String nickname;
    private String password;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // Dto -> Entity
    public Member toEntity() {
        Member member = Member.builder()
                .id(id)
                .nickname(nickname)
                .password(password)
                .email(email)
                .role(role)
                .build();

        return member;
    }

}
