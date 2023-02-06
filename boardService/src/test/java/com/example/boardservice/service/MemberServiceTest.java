package com.example.boardservice.service;

import com.example.boardservice.domain.Member;
import com.example.boardservice.domain.Role;
import com.example.boardservice.dto.request.MemberRequestDto;
import com.example.boardservice.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("유저 불러오기")
    public void loadUser() {
        // given
        MemberRequestDto memberDto = MemberRequestDto.builder()
                .id(1L)
                .email("abcd@gmail.com")
                .nickname("Test User")
                .password("123456")
                .role(Role.USER)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        memberService.save(memberDto);

        // when
        Member savedMember = memberRepository.findByEmail("abcd@gmail.com");

        // then
        assertEquals(memberDto.getNickname(), savedMember.getNickname());
        assertEquals(memberDto.getEmail(), savedMember.getEmail());
    }
}