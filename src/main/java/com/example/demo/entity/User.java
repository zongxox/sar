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
    private String email;
    private String zipcode;
    private String address;
    private String creUser;
    private LocalDateTime creDate;
    private String updUser;
    private LocalDateTime updDate;
}
