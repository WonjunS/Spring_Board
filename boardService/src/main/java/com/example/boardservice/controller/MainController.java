package com.example.boardservice.controller;

import com.example.boardservice.domain.BoardType;
import com.example.boardservice.dto.response.PostsResponseDto;
import com.example.boardservice.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    @Autowired
    private final PostsService postsService;

    @GetMapping("/")
    public String main(Model model) {
        List<PostsResponseDto> popularList = postsService.getAllPostsByView();
        List<PostsResponseDto> freeList = postsService.getAllPostsByBoardType(BoardType.FREE);
        List<PostsResponseDto> questionList = postsService.getAllPostsByBoardType(BoardType.QUESTION);
        List<PostsResponseDto> noticeList = postsService.getAllPostsByBoardType(BoardType.NOTICE);

        model.addAttribute("popularList", popularList);
        model.addAttribute("freeList", freeList);
        model.addAttribute("questionList", questionList);
        model.addAttribute("noticeList", noticeList);

        return "main";
    }
}
