package com.example.demo.entity;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Builder
@Data
public class User {
    private Integer id;
    private String name;
    private String account;
    private String password;
    private String phone;
    private String mail;
    private String creUser;
    private LocalDateTime creDate;
    private String updUser;
    private LocalDateTime updDate;
}
