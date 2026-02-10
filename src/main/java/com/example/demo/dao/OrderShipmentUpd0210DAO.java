package com.example.demo.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderShipmentUpd0210DAO {
    private String id;

    private String orderNo;

    private String customerName;

    private String productName;

    private String quantity;

    private String totalPrice;

    private String shippingAddress;

    private String status;

    private String shippedAt;

    private String createdAt;
}
