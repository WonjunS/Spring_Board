package com.example.boardservice.dao;

import com.example.boardservice.domain.Posts;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostsMapper {

    void boardWrite(Posts posts);

    Posts readPosts();
}
