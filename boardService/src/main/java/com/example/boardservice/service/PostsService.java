package com.example.boardservice.service;

import com.example.boardservice.domain.BoardType;
import com.example.boardservice.domain.Likes;
import com.example.boardservice.domain.Member;
import com.example.boardservice.domain.Posts;
import com.example.boardservice.dto.request.PostsRequestDto;
import com.example.boardservice.dto.response.PostsResponseDto;
import com.example.boardservice.repository.MemberRepository;
import com.example.boardservice.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostsService {

    @Autowired private final MemberRepository memberRepository;
    @Autowired private final PostsRepository postsRepository;
    @Autowired private final CommentService commentService;
    @Autowired private final LikesService likesService;

    // 게시글 올리기
    public Long post(PostsRequestDto postsDto, String boardType, String email) throws Exception {
        Member member = memberRepository.findByEmail(email);

        if(member == null) {
            throw new EntityNotFoundException("User not found.");
        }

        if(boardType.equals("자유게시판")) postsDto.setBoardType(BoardType.FREE);
        if(boardType.equals("공지사항")) postsDto.setBoardType(BoardType.NOTICE);
        if(boardType.equals("질문게시판")) postsDto.setBoardType(BoardType.QUESTION);

        postsDto.setMember(member);

        Posts posts = postsDto.toEntity();
        postsRepository.save(posts);

        return posts.getId();
    }

    // 게시물 1개 불러오기
    public PostsResponseDto getPost(Long postsId) {
        Posts post = postsRepository.findById(postsId)
                .orElseThrow(EntityNotFoundException::new);

        return new PostsResponseDto(post);
    }

    // 게시물 전체 불러오기
    @Transactional(readOnly = true)
    public Page<PostsResponseDto> getAllPosts(Pageable pageable, String orderCriteria) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, orderCriteria));

        Page<PostsResponseDto> posts = postsRepository.findAll(pageable).map(PostsResponseDto::new);

        return posts;
    }

    // 특정 회원이 작성한 게시물 불러오기
    @Transactional(readOnly = true)
    public Page<PostsResponseDto> getAllPostsByWriter(String email, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by("id").descending());

        Member member = memberRepository.findByEmail(email);

        Page<PostsResponseDto> posts = null;
        if(member != null) {
            posts = postsRepository.findAllByMember(member, pageable).map(PostsResponseDto::new);
        }

        return posts;
    }

    // 검색한 게시물 불러오기
    @Transactional(readOnly = true)
    public Page<PostsResponseDto> searchPosts(String keyword, Pageable pageable) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by("id").descending());

        Page<PostsResponseDto> posts = postsRepository.findByTitleContaining(keyword, pageable).map(PostsResponseDto::new);
        return posts;
    }

    // 자유 게시판에 있는 게시글 가져오기
    @Transactional(readOnly = true)
    public Page<PostsResponseDto> getAllFreePosts(Pageable pageable, String orderCriteria) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, orderCriteria));

        Page<PostsResponseDto> posts =
                postsRepository.findPostsByBoardType(BoardType.FREE, pageable).map(PostsResponseDto::new);

        return posts;
    }

    // 질문 게시판에 있는 게시글 가져오기
    public Object getAllQuestionPosts(Pageable pageable, String orderCriteria) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, orderCriteria));

        Page<PostsResponseDto> posts =
                postsRepository.findPostsByBoardType(BoardType.QUESTION, pageable).map(PostsResponseDto::new);

        return posts;
    }

    // 공지 게시판에 있는 게시글 가져오기
    public Object getAllNoticePosts(Pageable pageable, String orderCriteria) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
        pageable = PageRequest.of(page, 10, Sort.by(Sort.Direction.DESC, orderCriteria));

        Page<PostsResponseDto> posts =
                postsRepository.findPostsByBoardType(BoardType.NOTICE, pageable).map(PostsResponseDto::new);

        return posts;
    }

    // 특정 게시판에 있는 게시글 불러오기
    public List<PostsResponseDto> getAllPostsByBoardType(BoardType boardType) {
        List<Posts> posts = postsRepository.getAllByBoardType(boardType);
        List<PostsResponseDto> postsList = new ArrayList<>();

        int count = 0;
        while(count < 10 && count < posts.size()) {
            Posts p = posts.get(count);
            PostsResponseDto postsDto = new PostsResponseDto(p);
            postsList.add(postsDto);
            count++;
        }

        return postsList;
    }

    // 조회수 순으로 게시글 불러오기
    public List<PostsResponseDto> getAllPostsByView() {
        List<Posts> posts = postsRepository.getAllAndOrderByView();
        List<PostsResponseDto> postsList = new ArrayList<>();

        int count = 0;
        while(count < 10 && count < posts.size()) {
            Posts p = posts.get(count);
            PostsResponseDto postsDto = new PostsResponseDto(p);
            postsList.add(postsDto);
            count++;
        }

        return postsList;
    }

    // 게시물 수정하기
    @Transactional
    public void updatePost(Long id, String boardType, PostsRequestDto postsDto) {
        Posts post = postsRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id = " + id));

        if(boardType.equals("자유게시판")) postsDto.setBoardType(BoardType.FREE);
        if(boardType.equals("공지사항")) postsDto.setBoardType(BoardType.NOTICE);
        if(boardType.equals("질문게시판")) postsDto.setBoardType(BoardType.QUESTION);

        post.update(postsDto.getTitle(), postsDto.getBoardType(), postsDto.getContent());
    }

    // 게시물 조회수 업데이트
    @Transactional
    public int updateView(Long postId) {
        return postsRepository.updateView(postId);
    }

    // 저장된 좋아요가 있는지 조회
    public int findLike(Long memberId, Long postId) {
        Likes findLike = likesService.findByMemberAndPosts(memberId, postId);

        if(findLike == null) {
            return 0;
        }

        return 1;
    }

    // 게시물 좋아요 수 업데이트
    @Transactional
    public int updateLikes(Long memberId, Long postId) {
        Likes findLike = likesService.findByMemberAndPosts(memberId, postId);

        if(findLike == null) {
            likesService.saveLikes(memberId, postId);
            postsRepository.updateLikes(postId);
            return 1;
        } else {
            likesService.deleteByMemberAndPosts(memberId, postId);
            return 0;
        }
    }

    // 게시물 삭제하기
    @Transactional
    public void deletePost(Long id) {
        likesService.deleteAllByPosts(id);
        commentService.deleteAllCommentsByPostId(id);
        postsRepository.deleteById(id);
    }

    // 특정 회원이 작성한 모든 게시물 삭제
    @Transactional
    public void deleteAllPostsByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(EntityNotFoundException::new);
        likesService.deleteAllByMember(memberId);
        commentService.deleteAllCommentsByMemberId(member.getId());
        postsRepository.deleteAllByMember(member);
    }

}
