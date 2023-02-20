package com.example.boardservice.controller;

import com.example.boardservice.dto.request.CommentRequestDto;
import com.example.boardservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/post/{postId}/write")
    public String writeComment(@PathVariable("postId") Long postId, CommentRequestDto dto, Principal principal) {
        commentService.save(postId, principal.getName(), dto);

        return "redirect:/post/read/{postId}";
    }
}
