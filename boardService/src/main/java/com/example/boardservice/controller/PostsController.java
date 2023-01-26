package com.example.boardservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PostsController {

    @GetMapping("/posts/write")
    public String write(Model model) {
        return "posts/postsWrite";
    }
}
