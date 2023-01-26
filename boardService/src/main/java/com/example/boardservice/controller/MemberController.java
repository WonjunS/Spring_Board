package com.example.boardservice.controller;

import com.example.boardservice.dto.MemberDto;
import com.example.boardservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    /// TODO: https://daegom.com/main/spring-post11/
    @Autowired
    private MemberService userService;

    @GetMapping("/signup")
    public String signupPage() {
        return "member/joinForm";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberDto memberDto, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "member/joinForm";
        }
        try {
            userService.save(memberDto);
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
