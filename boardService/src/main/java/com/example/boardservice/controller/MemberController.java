package com.example.boardservice.controller;

import com.example.boardservice.dto.request.MemberRequestDto;
import com.example.boardservice.dto.response.MemberResponseDto;
import com.example.boardservice.dto.response.PostsResponseDto;
import com.example.boardservice.service.MemberService;
import com.example.boardservice.service.PostsService;
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

    @Autowired private MemberService memberService;
    @Autowired private PostsService postsService;

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

    @GetMapping("/info")
    public String memberDetails(Principal principal, Model model) {
        MemberResponseDto memberDto = memberService.findMember(principal.getName());
        if(memberDto != null) {
            model.addAttribute("memberDto", memberDto);
        }
        return "member/memberInfo";
    }

    @GetMapping("/{memberId}/update")
    public String memberUpdate(@PathVariable("memberId") Long memberId, Model model) {
        MemberResponseDto memberDto = memberService.findMember(memberId);
        if(memberDto != null) {
            model.addAttribute("memberDto", memberDto);
        }

        return "member/updateForm";
    }

    @PostMapping("/{memberId}/update")
    public String update(@RequestParam(value = "nickname") String nickname,
                         @RequestParam(value = "email") String email) {
        try {
            memberService.updateMemberNickname(email, nickname);
        } catch(Exception e) {
            System.out.println(e.getMessage());
            return "redirect:/member/{memberId}/update";
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/nicknameCheck", method = RequestMethod.GET)
    @ResponseBody
    public int checkNickname(@RequestParam("nickname") String nickname) throws Exception {
        boolean result = memberService.isDuplicateNickname(nickname);

        if(result) return 1;
        return 0;
    }

    @GetMapping("/{memberId}/delete")
    public String memberDelete(@PathVariable("memberId") Long memberId, Model model) {
        MemberResponseDto memberDto = memberService.findMember(memberId);
        if(memberDto != null) {
            model.addAttribute("memberDto", memberDto);
        }
        return "member/memberDelete";
    }

    @DeleteMapping("/{memberId}/delete")
    public String delete(@RequestParam(value = "nickname") String nickname,
                         @RequestParam(value = "email") String email) {
        memberService.deleteMember(email, nickname);
        return "redirect:/";
    }

    @GetMapping("/{memberId}")
    public String getMemberDetails(@PathVariable("memberId") Long memberId, Model model) {
        MemberResponseDto memberDto = memberService.findMember(memberId);
        if(memberDto != null) {
            model.addAttribute("memberDto", memberDto);
        }
        List<PostsResponseDto> postsDto = postsService.getAllPostsByWriter(memberId);
        model.addAttribute("postsDto", postsDto);

        return "member/memberDetails";
    }

}
