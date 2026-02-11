package com.example.demo.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersUpd0211DAO {
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