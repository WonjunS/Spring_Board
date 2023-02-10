package com.example.boardservice.repository;

import com.example.boardservice.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Transactional
    @Modifying
    @Query("update Member m set m.nickname = :nickname where m.id = :id")
    int updateNickname(Long id, String nickname);

    Member findByEmail(String email);
    Member findByNickname(String nickname);

    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

}
