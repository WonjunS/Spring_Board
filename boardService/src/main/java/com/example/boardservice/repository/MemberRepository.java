package com.example.boardservice.repository;

import com.example.boardservice.domain.Member;
import com.example.boardservice.domain.MemberGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Transactional
    @Modifying
    @Query("update Member m set m.nickname = :nickname where m.id = :id")
    int updateNickname(Long id, String nickname);

    @Transactional
    @Modifying
    @Query("update Member m set m.visits = m.visits + 1 where m.id = :id")
    int updateVisits(Long id);

    @Transactional
    @Modifying
    @Query("update Member m set m.visits = m.visits + 1 where m.email = :email")
    int updateVisits(String email);

    @Transactional
    @Modifying
    @Query("update Member m set m.memberGrade = :grade where m.id = :id")
    int updateMemberGrade(Long id, MemberGrade grade);

    @Transactional
    @Modifying
    @Query("update Member m set m.activityScore = m.activityScore + :score where m.id = :id")
    int updateActivityScore(Long id, int score);

    Member findByEmail(String email);
    Member findByNickname(String nickname);

    Optional<Member> findByProviderAndProviderId(String provider, String providerId);

    boolean existsByNickname(String nickname);

    void deleteById(Long id);

}
