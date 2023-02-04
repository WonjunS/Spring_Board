package com.example.boardservice.controller;

import com.example.boardservice.domain.Member;
import com.example.boardservice.dto.request.PostsRequestDto;
import com.example.boardservice.dto.response.CommentResponseDto;
import com.example.boardservice.dto.response.PostsResponseDto;
import com.example.boardservice.service.MemberService;
import com.example.boardservice.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostsController {

    @Autowired
    private final PostsService postsService;

    @Autowired
    private final MemberService memberService;

    @GetMapping("/posts")
    public String main(Model model, @PageableDefault Pageable pageable) {
        model.addAttribute("boardList", postsService.getAllPosts(pageable));

        return "posts/mainBoard";
    }

    @GetMapping("/posts/write")
    public String write(PostsRequestDto postsDto, Principal principal, Model model) {
        Member member = memberService.findMember(principal.getName());
        if(member != null) {
            model.addAttribute("member", member);
        }
        model.addAttribute("postsDto", postsDto);

        return "posts/postsWrite";
    }

    @PostMapping("/posts/write")
    public String insertData(@Valid PostsRequestDto postsDto, Principal principal) throws Exception {
        postsService.post(postsDto, principal.getName());

        return "redirect:/";
    }

    @GetMapping("/posts/read/{postsId}")
    public String getPost(@PathVariable("postsId") Long postsId, Model model) {
        PostsResponseDto postsDto = postsService.getPost(postsId);
        List<CommentResponseDto> comments = postsDto.getComments();

        if(comments != null && !comments.isEmpty()) {
            model.addAttribute("comments", comments);
        }

        model.addAttribute("postsDto", postsDto);
        postsService.updateView(postsId);

        return "posts/postsDetail";
    }

}
