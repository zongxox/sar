package com.example.demo.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersIns0211Req {

    private String orderNo;

    private String amount;

    private String status;

    private String remark;

    private List<String> orderType;

    private String source;

    private String customer;

    private String createdAt;

    private String updatedAt;


}