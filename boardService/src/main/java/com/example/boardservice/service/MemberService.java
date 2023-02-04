package com.example.boardservice.service;

import com.example.boardservice.dao.MemberMapper;
import com.example.boardservice.domain.Member;
import com.example.boardservice.domain.Role;
import com.example.boardservice.dto.MemberDto;
import com.example.boardservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public void save(MemberDto memberDto) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        memberDto.setRole(Role.USER);
        memberDto.setCreatedAt(LocalDateTime.now());
        memberDto.setModifiedAt(LocalDateTime.now());

        memberRepository.save(memberDto.toEntity());
    }

//    public Member save(Member member) {
//        validateDuplicatedMember(member);
//        return memberRepository.save(member);
//    }

    private void validateDuplicatedMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());

        if(findMember != null) {
            throw new IllegalStateException("Member already exists");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if(member == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

}
