package com.example.boardservice.service;

import com.example.boardservice.dao.MemberMapper;
import com.example.boardservice.dao.PostsMapper;
import com.example.boardservice.domain.Member;
import com.example.boardservice.domain.Posts;
import com.example.boardservice.dto.PostsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostsService {

    MemberMapper memberMapper;
    PostsMapper postsMapper;

    @Transactional
    public Long save(PostsDto postsDto, String email) {
        Member member = memberMapper.selectOne(email);
        postsDto.setMember(member);

        Posts posts = postsDto.toEntity();
        postsMapper.boardWrite(posts);

        return posts.getId();
    }
}
