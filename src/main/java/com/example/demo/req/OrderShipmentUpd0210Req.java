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
public class OrderShipmentUpd0210Req {
    private String id;

    private String orderNo;

    private String customerName;

    private String productName;

    private List<String> quantity;

    private String totalPrice;

    private String shippingAddress;

    private String status;

    private String shippedAt;

    private String createdAt;
}
