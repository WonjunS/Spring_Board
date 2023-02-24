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

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostsController {

    @Autowired private final PostsService postsService;
    @Autowired private final MemberService memberService;
    @Autowired private final CommentService commentService;

    // 게시물 불러오기 (최신순)
    @GetMapping("/posts")
    public String main(@RequestParam(required = false, defaultValue = "id", value = "orderby") String orderCriteria,
                       Model model, Pageable pageable) {
        Page<PostsResponseDto> posts = postsService.getAllPosts(pageable, orderCriteria);
        model.addAttribute("boardList", posts);

        return "posts/mainBoard";
    }

    // 게시물 작성하기
    @GetMapping("/post/write")
    public String write(PostsRequestDto postsDto, Principal principal, Model model) {
        MemberResponseDto member = memberService.findMember(principal.getName());
        if(member != null) {
            model.addAttribute("member", member);
        }
        model.addAttribute("postsDto", postsDto);

        return "posts/postWrite";
    }

    // 작성된 게시물 저장
    @PostMapping("/post/write")
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

        int count = (comments == null) ? 0 : comments.size();

        if(comments != null && !comments.isEmpty()) {
            model.addAttribute("comments", comments);
        }

        model.addAttribute("count", count);
        model.addAttribute("postsDto", postsDto);
        postsService.updateView(postsId);

        return "posts/postDetail";
    }

    // 특정 회원이 작성한 게시물만 불러오기
    @GetMapping("/posts/all/{memberId}")
    public String getPostByMember(@PathVariable("memberId") Long memberId, Model model, Principal principal,
                                  @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("boardList", postsService.getAllPostsByWriter(principal.getName(), pageable));
        return "posts/memberBoard";
    }

    @GetMapping("/posts/free")
    public String getFreeBoard(@RequestParam(required = false, defaultValue = "id", value = "orderby") String orderCriteria,
                               Model model, Pageable pageable) {
        model.addAttribute("boardList", postsService.getAllFreePosts(pageable, orderCriteria));
        return "posts/freeBoard";
    }

    @GetMapping("/posts/question")
    public String getQuestionBoard(@RequestParam(required = false, defaultValue = "id", value = "orderby") String orderCriteria,
                                   Model model, Pageable pageable) {
        model.addAttribute("boardList", postsService.getAllQuestionPosts(pageable, orderCriteria));
        return "posts/questionBoard";
    }

    @GetMapping("/posts/notice")
    public String getNoticeBoard(@RequestParam(required = false, defaultValue = "id", value = "orderby") String orderCriteria,
                                 Model model, Pageable pageable) {
        model.addAttribute("boardList", postsService.getAllNoticePosts(pageable, orderCriteria));
        return "posts/noticeBoard";
    }

    // 게시물 수정 페이지
    @GetMapping("/post/{postId}/update")
    public String updatePage(@PathVariable("postId") Long postId, Model model) {
        PostsResponseDto postDto = postsService.getPost(postId);
        postsService.updateView(postId);
        model.addAttribute("postDto", postDto);
        return "posts/postUpdate";
    }

    // 게시물 수정하기
    @PutMapping("/post/{postId}/update")
    public String update(@PathVariable("postId") Long postId, @RequestParam("title") String title,
                         @RequestParam("boardType") String boardType, @RequestParam("content") String content) {
        PostsRequestDto postDto = PostsRequestDto.builder()
                .title(title)
                .content(content)
                .build();
        postsService.updatePost(postId, boardType, postDto);

        return "redirect:/";
    }

    // 게시물 삭제하기
    @DeleteMapping("/post/{postId}/delete")
    public String delete(@PathVariable("postId") Long postId) {
        commentService.deleteAllCommentsByPostId(postId);
        postsService.deletePost(postId);
        return "redirect:/";
    }

}
