package com.example.boardservice.repository;

import com.example.boardservice.domain.Likes;
import com.example.boardservice.domain.Member;
import com.example.boardservice.domain.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Transactional
    @Query("select l from Likes l where l.member = :member and l.posts = :posts")
    Likes findByMemberAndPosts(Member member, Posts posts);

    boolean existsLikesByMemberAndPosts(Member member, Posts posts);

    void deleteAllByMember(Member member);
    void deleteAllByPosts(Posts posts);
    void deleteByMemberAndPosts(Member member, Posts posts);

}
