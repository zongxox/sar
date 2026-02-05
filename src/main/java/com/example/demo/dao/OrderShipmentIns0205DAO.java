package com.example.demo.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderShipmentIns0205DAO {

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
