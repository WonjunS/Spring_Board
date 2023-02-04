package com.example.boardservice.service;

import com.example.boardservice.domain.BoardType;
import com.example.boardservice.domain.Member;
import com.example.boardservice.domain.Posts;
import com.example.boardservice.dto.request.PostsRequestDto;
import com.example.boardservice.dto.response.PostsResponseDto;
import com.example.boardservice.repository.MemberRepository;
import com.example.boardservice.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostsService {

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final PostsRepository postsRepository;

    // 게시글 올리기
    public Long post(PostsRequestDto postsDto, String email) throws Exception {
        Member member = memberRepository.findByEmail(email);

        if(member == null) {
            throw new EntityNotFoundException("User not found.");
        }

        postsDto.setBoardType(BoardType.FREE);
        postsDto.setMember(member);
        postsDto.setCreatedDate(LocalDateTime.now());
        postsDto.setModifiedDate(LocalDateTime.now());

        Posts posts = postsDto.toEntity();
        postsRepository.save(posts);

        return posts.getId();
    }

    // 게시물 전체 불러오기
    @Transactional(readOnly = true)
    public Page<PostsResponseDto> getAllPosts(Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10);

        Page<PostsResponseDto> posts = postsRepository.findAll(pageable).map(PostsResponseDto::new);
        return posts;
    }

    // 특정 게시물 불러오기
    public PostsResponseDto getPost(Long postsId) {
        Posts post = postsRepository.findById(postsId)
                .orElseThrow(EntityNotFoundException::new);

        return new PostsResponseDto(post);
    }

    // 게시물 조회수 업데이트
    @Transactional
    public int updateView(Long postsId) {
        return postsRepository.updateView(postsId);
    }

}
