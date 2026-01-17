package com.example.demo.controller;

import com.example.demo.req.UserReq;
import com.example.demo.servicec.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/insert")
    public void insert(@RequestBody UserReq userReq) {
        System.out.println("name=" + userReq.getName());
        userService.insert(userReq);
    }
}
