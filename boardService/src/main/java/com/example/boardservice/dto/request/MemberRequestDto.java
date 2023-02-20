package com.example.boardservice.dto.request;

import com.example.boardservice.domain.Member;
import com.example.boardservice.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberRequestDto {

    private Long id;

    @NotBlank
    private String nickname;

    @NotBlank
    private String password;

    @NotBlank
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
                .visits(0)
                .role(role)
                .build();

        return member;
    }

}
