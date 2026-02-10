package com.example.demo.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersQuery0211Res {

    private Long id;

    private String orderNo;

    private Integer amount;

    private String status;

    private String remark;

    private String orderType;

    private String source;

    private String customer;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date updatedAt;

    private String statusName;


}