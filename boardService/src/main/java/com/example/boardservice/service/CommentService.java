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

    @Autowired private final CommentRepository commentRepository;
    @Autowired private final PostsRepository postsRepository;
    @Autowired private final MemberRepository memberRepository;

    // 댓글 작성
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

    // 해당 게시물에 작성된 모든 댓글 삭제
    @Transactional
    public void deleteAllCommentsByPostId(Long postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);
        commentRepository.deleteAllByPosts(post);
    }

    // 탈퇴한 회원이 작성한 모든 댓글 삭제
    @Transactional
    public void deleteAllCommentsByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(EntityNotFoundException::new);
        commentRepository.deleteAllByMember(member);
    }

}
