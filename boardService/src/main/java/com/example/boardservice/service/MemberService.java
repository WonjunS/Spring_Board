package com.example.boardservice.service;

import com.example.boardservice.domain.Member;
import com.example.boardservice.domain.Role;
import com.example.boardservice.dto.request.MemberRequestDto;
import com.example.boardservice.dto.response.MemberResponseDto;
import com.example.boardservice.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostsService postsService;

    @Autowired
    private CommentService commentService;

    // 회원 정보 저장
    @Transactional
    public void save(MemberRequestDto memberDto) {
        validateDuplicatedEmail(memberDto.getEmail());
        validateDuplicatedNickname(memberDto.getNickname());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        memberDto.setRole(Role.USER);

        memberRepository.save(memberDto.toEntity());
    }

    // 이메일 중복 확인
    private void validateDuplicatedEmail(String email) {
        Member findMember = memberRepository.findByEmail(email);

        if(findMember != null) {
            throw new IllegalStateException("이미 사용중인 이메일 입니다.");
        }
    }

    // 닉네임 중복 확인
    private void validateDuplicatedNickname(String nickname) {
        Member findMember = memberRepository.findByNickname(nickname);

        if(findMember != null) {
            throw new IllegalStateException("이미 사용중인 닉네임 입니다.");
        }
    }

    public boolean isDuplicateNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    // 특정 회원 찾기
    public MemberResponseDto findMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return new MemberResponseDto(member);
    }

    public MemberResponseDto findMember(String email) {
        Member member = memberRepository.findByEmail(email);
        return new MemberResponseDto(member);
    }

    // 전체 회원 리스트 조회
    public List<MemberResponseDto> findAllMembers() {
        List<Member> members = memberRepository.findAll();
        List<MemberResponseDto> memberList = new ArrayList<>();

        for(Member member : members) {
            MemberResponseDto memberResponseDto = new MemberResponseDto(member);
            memberList.add(memberResponseDto);
        }

        return memberList;
    }

    // 회원 정보 수정
    public int updateMemberNickname(String email, String nickname) {
        validateDuplicatedNickname(nickname);
        Long memberId = memberRepository.findByEmail(email).getId();
        return memberRepository.updateNickname(memberId, nickname);
    }

    // 회원 탈퇴 기능
    public void deleteMember(String email, String nickname) {
        Long memberId = memberRepository.findByEmail(email).getId();

        commentService.deleteAllCommentsByMemberId(memberId);
        postsService.deleteAllPostsByMemberId(memberId);
        memberRepository.deleteById(memberId);
    }

}
