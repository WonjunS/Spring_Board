package com.example.boardservice.controller;

import com.example.boardservice.dto.request.MemberRequestDto;
import com.example.boardservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/signup")
    public String signupPage(MemberRequestDto memberDto, Model model) {
        model.addAttribute("memberDto", memberDto);
        return "member/joinForm";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberRequestDto memberDto, BindingResult result) {
        if(result.hasErrors()) {
            return "member/joinForm";
        }
        try {
            memberService.save(memberDto);
        } catch(Exception e) {
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

}
