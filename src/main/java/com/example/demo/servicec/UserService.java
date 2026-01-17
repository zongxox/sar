package com.example.demo.servicec;

import com.example.demo.mapper.UserMapper;
import com.example.demo.req.UserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void insert(UserReq userReq){
        userMapper.insert(userReq);
    }
}
