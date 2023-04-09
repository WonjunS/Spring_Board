package com.example.boardservice.service;

import com.example.boardservice.domain.BoardType;
import com.example.boardservice.domain.Member;
import com.example.boardservice.domain.Role;
import com.example.boardservice.dto.request.MemberRequestDto;
import com.example.boardservice.dto.request.PostsRequestDto;
import com.example.boardservice.dto.response.MemberResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class PostsServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PostsService postsService;

    private void createUser() {
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
    @DisplayName("게시글 올리기 테스트")
    public void writePost() throws Exception {
        // given
        createUser();
        MemberResponseDto savedMember = memberService.findMember("abcd@gmail.com");
        PostsRequestDto postsDto = PostsRequestDto.builder()
                .id(1L)
                .title("No title")
                .content("This is a sample posting test")
                .likes(0)
                .view(0)
                .writer("Anonymous")
                .build();

        // when
        Long postId = postsService.post(postsDto, "자유게시판", savedMember.getEmail());

        // then
        assertEquals(postId, postsDto.getId());
    }

}