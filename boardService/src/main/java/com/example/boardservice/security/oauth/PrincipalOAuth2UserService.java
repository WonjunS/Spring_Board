package com.example.boardservice.security.oauth;

import com.example.boardservice.domain.Member;
import com.example.boardservice.domain.MemberGrade;
import com.example.boardservice.domain.Role;
import com.example.boardservice.repository.MemberRepository;
import com.example.boardservice.security.auth.PrincipalDetails;
import com.example.boardservice.security.oauth.provider.FaceBookUserInfo;
import com.example.boardservice.security.oauth.provider.GoogleUserInfo;
import com.example.boardservice.security.oauth.provider.NaverUserInfo;
import com.example.boardservice.security.oauth.provider.OAuth2UserInfo;
import com.example.boardservice.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    // userRequest는 code를 받아서 accessToken을 응답 받은 객체
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest); // google의 회원 프로필 조회

        // code를 통해 구성한 정보
        System.out.println("userRequest clientRegistration : " + userRequest.getClientRegistration());

        // token을 통해 응답받은 회원정보
        System.out.println("oAuth2User : " + oAuth2User);

        return processOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        // Attribute를 파싱해서 공통 객체로 묶는다. 관리가 편함.
        OAuth2UserInfo oAuth2UserInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            oAuth2UserInfo = new FaceBookUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            oAuth2UserInfo = new NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        } else {
            System.out.println("우리는 구글과 페이스북만 지원해요 ㅎㅎ");
        }

        Optional<Member> userOptional =
                memberRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(), oAuth2UserInfo.getProviderId());

        Member member;
        if (userOptional.isPresent()) {
            member = userOptional.get();
            memberService.updateVisits(member.getEmail());
            memberRepository.save(member);
        } else {
            member = Member.builder()
                    .nickname(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId().substring(0, 6))
                    .email(oAuth2UserInfo.getEmail())
                    .role(Role.SOCIAL)
                    .provider(oAuth2UserInfo.getProvider())
                    .providerId(oAuth2UserInfo.getProviderId())
                    .visits(0)
                    .activityScore(0)
                    .memberGrade(MemberGrade.BRONZE)
                    .build();
            memberService.updateVisits(oAuth2UserInfo.getEmail());
            memberRepository.save(member);
        }

        return new PrincipalDetails(member, oAuth2User.getAttributes());
    }
}