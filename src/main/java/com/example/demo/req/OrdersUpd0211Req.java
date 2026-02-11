package com.example.demo.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersUpd0211Req {
    private String id;

    private String orderNo;

    private String amount;

    private String status;

    private String remark;

    private String orderType;

    private String source;

    private String customer;

    private String createdAt;

    private String updatedAt;
}