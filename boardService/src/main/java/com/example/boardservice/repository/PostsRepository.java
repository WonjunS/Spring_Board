package com.example.boardservice.repository;

import com.example.boardservice.domain.BoardType;
import com.example.boardservice.domain.Member;
import com.example.boardservice.domain.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Transactional
    @Modifying
    @Query("update Posts p set p.view = p.view + 1 where p.id = :id")
    int updateView(Long id);

    @Transactional
    @Modifying
    @Query("update Posts p set p.likes = p.likes + 1 where p.id = :id")
    int updateLikes(Long id);

    @Transactional
    @Query("select p from Posts p where p.boardType = :boardType")
    Page<Posts> findPostsByBoardType(BoardType boardType, Pageable pageable);

    Page<Posts> findAllByMember(Member member, Pageable pageable);
    Page<Posts> findByTitleContaining(String keyword, Pageable pageable);

    void deleteById(Long id);

    void deleteAllByMember(Member member);

}
