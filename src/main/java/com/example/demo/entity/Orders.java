package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Orders {

    private Long id;

    private String orderNo;

    private Integer amount;

    private String status;

    private String remark;

    private String orderType;

    private String source;

    private String customer;

    private Date createdAt;

    private Date updatedAt;


}