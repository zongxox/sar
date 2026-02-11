package com.example.demo.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersPdf0211Res {

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

    private String statusName;

    private Integer pageGroup;

    private String createdAtStr;

    private String updatedAtStr;

}