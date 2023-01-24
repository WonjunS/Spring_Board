package com.example.boardservice.dao;

import com.example.boardservice.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

@Mapper
public interface memberMapper {

    void insertMember(Member member);

    Member selectOne(String email);

    ArrayList<HashMap<String, Object>> findAll();

}
