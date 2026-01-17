package com.example.demo.mapper;

import com.example.demo.req.UserReq;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    int insert(UserReq userReq);
}
