package com.example.boardservice.service;

import com.example.boardservice.domain.Member;
import com.example.boardservice.domain.MemberGrade;
import com.example.boardservice.domain.Role;
import com.example.boardservice.dto.request.MemberRequestDto;
import com.example.boardservice.dto.response.MemberResponseDto;
import com.example.boardservice.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
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

    void createUser() {
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
    }

    @Test
    @DisplayName("유저 불러오기")
    void loadUser() {
        // given
        createUser();

        // when
        MemberResponseDto dto = memberService.findMember("abcd@gmail.com");

        // then
        assertEquals("Test User", dto.getNickname());
        assertEquals("abcd@gmail.com", dto.getEmail());
    }

    @Test
    @DisplayName("중복 이메일 테스트")
    void createDuplicatedEmailTest() {
        createUser();

        try {
            MemberRequestDto memberDto = MemberRequestDto.builder()
                    .id(1L)
                    .email("abcd@gmail.com")
                    .nickname("Test User2")
                    .password("123456")
                    .role(Role.USER)
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .build();
            memberService.save(memberDto);
        } catch(Exception e) {
            assertEquals("이미 사용중인 이메일 입니다." ,e.getMessage());
        }
    }

    @Test
    @DisplayName("중복 닉네임 테스트")
    void createDuplicatedNicknameTest() {
        createUser();

        try {
            MemberRequestDto memberDto = MemberRequestDto.builder()
                    .id(1L)
                    .email("abcd4@gmail.com")
                    .nickname("Test User")
                    .password("123456")
                    .role(Role.USER)
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .build();
            memberService.save(memberDto);
        } catch(Exception e) {
            assertEquals("이미 사용중인 닉네임 입니다." ,e.getMessage());
        }
    }

    @Test
    @DisplayName("회원 정보 수정 테스트")
    void updateMemberDetails() {
        createUser();

        MemberResponseDto findMember = memberService.findMember("abcd@gmail.com");

        memberService.updateMemberNickname(findMember.getEmail(), "Test User3");

        MemberResponseDto dto = memberService.findMember("abcd@gmail.com");

        assertEquals("abcd@gmail.com", dto.getEmail());
        assertEquals("Test User3", dto.getNickname());
    }

    @Test
    @DisplayName("회원 탈퇴 테스트")
    void deleteMember() {
        createUser();

        memberService.deleteMember("abcd@gmail.com", "Test User");
    }

}