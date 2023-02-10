package com.example.boardservice.controller;

import com.example.boardservice.dto.request.PostsRequestDto;
import com.example.boardservice.dto.response.CommentResponseDto;
import com.example.boardservice.dto.response.MemberResponseDto;
import com.example.boardservice.dto.response.PostsResponseDto;
import com.example.boardservice.service.CommentService;
import com.example.boardservice.service.MemberService;
import com.example.boardservice.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PreUpdate;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class PostsController {

    @Autowired private final PostsService postsService;
    @Autowired private final MemberService memberService;
    @Autowired private final CommentService commentService;

    // 게시물 불러오기 (최신순)
    @GetMapping("/posts")
    public String main(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("boardList", postsService.getAllPosts(pageable));

        return "posts/mainBoard";
    }

    // 게시물 작성하기
    @GetMapping("/posts/write")
    public String write(PostsRequestDto postsDto, Principal principal, Model model) {
        MemberResponseDto member = memberService.findMember(principal.getName());
        if(member != null) {
            model.addAttribute("member", member);
        }
        model.addAttribute("postsDto", postsDto);

        return "posts/postsWrite";
    }

    // 작성된 게시물 저장
    @PostMapping("/posts/write")
    public String insertData(@RequestParam("title") String title, @RequestParam("boardType") String boardType,
                             @RequestParam("writer") String writer, @RequestParam("content") String content,
                             Principal principal) throws Exception {

        PostsRequestDto postsDto = PostsRequestDto.builder()
                .title(title)
                .writer(writer)
                .content(content)
                .build();
        postsService.post(postsDto, boardType, principal.getName());

        return "redirect:/posts";
    }

    @GetMapping("/posts/search")
    public String search(@RequestParam("keyword") String keyword, Pageable pageable, Model model) {
        Page<PostsResponseDto> posts = postsService.searchPosts(keyword, pageable);
        model.addAttribute("boardList", posts);

        return "posts/mainBoard";
    }

    // 특정 게시물 불러오기
    @GetMapping("/post/read/{postsId}")
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

    // 특정 회원이 작성한 게시물만 불러오기
    @GetMapping("/posts/all/{memberId}")
    public String getPostByMember(@PathVariable("memberId") Long memberId, Model model, Principal principal,
                                  @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("boardList", postsService.getAllPostsByWriter(principal.getName(), pageable));
        return "posts/memberBoard";
    }

    @GetMapping("/posts/free")
    public String getFreeBoard(Model model, Pageable pageable) {
        model.addAttribute("boardList", postsService.getAllFreePosts(pageable));
        return "posts/freeBoard";
    }

    @GetMapping("/posts/question")
    public String getQuestionBoard(Model model, Pageable pageable) {
        model.addAttribute("boardList", postsService.getAllQuestionPosts(pageable));
        return "posts/questionBoard";
    }

    @GetMapping("/posts/notice")
    public String getNoticeBoard(Model model, Pageable pageable) {
        model.addAttribute("boardList", postsService.getAllNoticePosts(pageable));
        return "posts/noticeBoard";
    }

    // 게시물 수정 페이지
    @GetMapping("/post/update/{postId}")
    public String updatePage(@PathVariable("postId") Long postId, Model model) {
        PostsResponseDto postDto = postsService.getPost(postId);
        postsService.updateView(postId);
        model.addAttribute("postDto", postDto);
        return "posts/postsUpdate";
    }

    // 게시물 수정하기
    @PutMapping("/post/update/{postId}")
    public String update(@PathVariable("postId") Long postId, @RequestParam("title") String title,
                         @RequestParam("boardType") String boardType, @RequestParam("content") String content) {
        PostsRequestDto postDto = PostsRequestDto.builder()
                .title(title)
                .content(content)
                .build();
        postsService.updatePost(postId, boardType, postDto);
        return "redirect:/posts";
    }

    // 게시물 삭제하기
    @DeleteMapping("/post/delete/{postId}")
    public String delete(@PathVariable("postId") Long postId) {
        commentService.deleteComments(postId);
        postsService.deletePost(postId);
        return "redirect:/posts";
    }

}
