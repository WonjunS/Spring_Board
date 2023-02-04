package com.example.boardservice.controller;

import com.example.boardservice.dto.MemberDto;
import com.example.boardservice.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    /// TODO: https://daegom.com/main/spring-post11/
    @Autowired
    private MemberService memberService;

    @GetMapping("/signup")
    public String signupPage(MemberDto memberDto, Model model) {
        model.addAttribute("memberDto", memberDto);
        return "member/joinForm";
    }

    @PostMapping("/signup")
    public String signup(@Valid MemberDto memberDto, BindingResult result) {
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

    @RequestMapping(value = "/emailCheck", method = RequestMethod.POST)
    @ResponseBody
    public int checkEmailDuplicate(@RequestParam("email") String email) {
        boolean isExist = memberService.validateDuplicatedEmail(email);
        if(isExist) return 1;
        return 0;
    }

    @GetMapping("/user-nickname/{nickname}/exists")
    public ResponseEntity<Boolean> checkNicknameDuplicate(@PathVariable String nickname) {
        return ResponseEntity.ok(memberService.validateDuplicatedNickname(nickname));
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
