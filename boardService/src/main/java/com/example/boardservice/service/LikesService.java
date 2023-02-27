package com.example.boardservice.service;

import com.example.boardservice.domain.Likes;
import com.example.boardservice.domain.Member;
import com.example.boardservice.domain.Posts;
import com.example.boardservice.repository.LikesRepository;
import com.example.boardservice.repository.MemberRepository;
import com.example.boardservice.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LikesService {

    @Autowired private final MemberRepository memberRepository;
    @Autowired private final PostsRepository postsRepository;
    @Autowired private final LikesRepository likesRepository;

    public List<Likes> findByMemberAndPosts(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(EntityNotFoundException::new);

        Posts post = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        return likesRepository.findByMemberAndPosts(member, post);
    }

    public void saveLikes(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(EntityNotFoundException::new);

        Posts post = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        Likes like = Likes.builder()
                .member(member)
                .posts(post)
                .build();

        likesRepository.save(like);
    }

    public void deleteAllByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(EntityNotFoundException::new);
        likesRepository.deleteAllByMember(member);
    }

    public void deleteAllByPosts(Long postId) {
        Posts post = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);
        likesRepository.deleteAllByPosts(post);
    }

    public void deleteByMemberAndPosts(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(EntityNotFoundException::new);

        Posts post = postsRepository.findById(postId)
                .orElseThrow(EntityNotFoundException::new);

        likesRepository.deleteByMemberAndPosts(member, post);
    }

}
