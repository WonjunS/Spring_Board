package com.example.boardservice.repository;

import com.example.boardservice.domain.BoardType;
import com.example.boardservice.domain.Member;
import com.example.boardservice.domain.Posts;
import com.querydsl.core.annotations.QueryInit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Modifying
    @Query("update Posts p set p.likes = p.likes - 1 where p.id = :id")
    int deleteLikes(Long id);

    @Transactional
    @Query("select p from Posts p where p.boardType = :boardType")
    Page<Posts> findPostsByBoardType(BoardType boardType, Pageable pageable);
    Page<Posts> findAllByMember(Member member, Pageable pageable);
    Page<Posts> findByTitleContaining(String keyword, Pageable pageable);

    @Transactional
    @Query("select p from Posts p where p.member = :member order by p.id desc")
    List<Posts> getAllByMemberAndOrderByIdDesc(Member member);

    @Transactional
    @Query("select p from Posts p where p.boardType = :boardType order by p.id desc")
    List<Posts> getAllByBoardType(BoardType boardType);

    @Transactional
    @Query("select p from Posts p order by p.view desc")
    List<Posts> getAllAndOrderByView();

    void delete(Posts post);

    void deleteById(Long id);

    void deleteAllByMember(Member member);

}
