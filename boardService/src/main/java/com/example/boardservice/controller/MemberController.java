package com.example.boardservice.controller;

import com.example.boardservice.dto.request.MemberRequestDto;
import com.example.boardservice.dto.response.MemberResponseDto;
import com.example.boardservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    // TODO: 카카오, 네이버 로그인 구현
    // https://lotuus.tistory.com/78

    @Autowired
    private MemberService memberService;

    @GetMapping("/signup")
    public String signupPage(MemberRequestDto memberDto, Model model) {
        model.addAttribute("memberDto", memberDto);
        return "member/joinForm";
    }

    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute MemberRequestDto memberDto, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "member/joinForm";
        }
        try {
            memberService.save(memberDto);
        } catch(Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/joinForm";
        }
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "member/loginForm";
    }

    @GetMapping("/login/fail")
    public String loginFail(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 혹은 비밀번호를 확인해주세요.");
        return "member/loginForm";
    }

    @GetMapping("/list")
    public String memberList(Model model) {
        List<MemberResponseDto> members = memberService.findAllMembers();
        model.addAttribute("members", members);

        return "member/memberList";
    }

    @GetMapping("/update")
    public String updatePage(Principal principal, Model model) {
        MemberResponseDto memberDto = memberService.findMember(principal.getName());
        model.addAttribute("memberDto", memberDto);

        return "member/updateForm";
    }

    @PostMapping("/update")
    public String memberUpdate(@RequestParam(value = "nickname") String nickname,
                               @RequestParam(value = "email") String email) {
        try {
            memberService.updateMemberNickname(email, nickname);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return "redirect:/member/update";
        }
        return "redirect:/";
    }

}
