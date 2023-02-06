package com.example.boardservice.service;

import com.example.boardservice.domain.Comment;
import com.example.boardservice.domain.Member;
import com.example.boardservice.domain.Posts;
import com.example.boardservice.dto.request.CommentRequestDto;
import com.example.boardservice.repository.CommentRepository;
import com.example.boardservice.repository.MemberRepository;
import com.example.boardservice.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class CommentService {

    @Autowired
    private final CommentRepository commentRepository;

    @Autowired
    private final PostsRepository postsRepository;

    @Autowired
    private final MemberRepository memberRepository;

    @Transactional
    public Long save(Long postsId, String email, CommentRequestDto dto) {
        Member member = memberRepository.findByEmail(email);

        Posts posts = postsRepository.findById(postsId)
                .orElseThrow(EntityNotFoundException::new);

        dto.setMember(member);
        dto.setPosts(posts);

        Comment comment = dto.toEntity();
        commentRepository.save(comment);

        return comment.getId();
    }
}
