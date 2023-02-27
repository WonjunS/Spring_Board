package com.example.boardservice.repository;

import com.example.boardservice.domain.Likes;
import com.example.boardservice.domain.Member;
import com.example.boardservice.domain.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    List<Likes> findByMemberAndPosts(Member member, Posts posts);

    void deleteAllByMember(Member member);
    void deleteAllByPosts(Posts posts);
    void deleteByMemberAndPosts(Member member, Posts posts);

}
