package com.example.boardservice.repository;

import com.example.boardservice.domain.Comment;
import com.example.boardservice.domain.Member;
import com.example.boardservice.domain.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> getCommentByMemberId(Long memberId);

    void delete(Comment comment);
    void deleteAllByPosts(Posts post);
    void deleteAllByMember(Member member);

}
