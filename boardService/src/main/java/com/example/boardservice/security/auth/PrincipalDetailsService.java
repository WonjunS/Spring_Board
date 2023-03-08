package com.example.boardservice.security.auth;

import com.example.boardservice.domain.Member;
import com.example.boardservice.repository.MemberRepository;
import com.example.boardservice.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired private MemberRepository memberRepository;
    @Autowired private MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username);

        if(member == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        // 방문수 업데이트
        memberService.updateVisits(member.getId());

        // 활동 점수 업데이트
        memberService.updateActivityScore(username, 1);

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}