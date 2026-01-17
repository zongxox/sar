package com.example.demo.req;

import lombok.Data;

@Data
public class UserReq {
    private String id;
    private String name;
    private String account;
    private String password;
    private String phone;
    private String mail;
    private String creUser;
    private String creDate;
    private String updUser;
}
