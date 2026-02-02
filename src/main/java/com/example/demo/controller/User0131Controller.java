package com.example.demo.controller;

import com.example.demo.req.User0131Req;
import com.example.demo.service.User0131Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/0130")
public class User0131Controller {

    private final User0131Service userService;


    @PostMapping("/register")
    public String register(@RequestBody User0131Req req) {

        userService.register(req.getUsername(), req.getPassword());

        return "ok";
    }



}

