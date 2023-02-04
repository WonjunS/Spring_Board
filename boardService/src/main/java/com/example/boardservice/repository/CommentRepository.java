package com.example.boardservice.repository;

import com.example.boardservice.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> getCommentByMemberId(Long memberId);

}
