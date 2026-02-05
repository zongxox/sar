package com.example.demo.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderShipmentUpd0205DAO {
    private Integer id;

    private String orderNo;

    private String customerName;

    private String productName;

    private Integer quantity;

    private BigDecimal totalPrice;

    private String shippingAddress;

    private String status;

    private LocalDateTime shippedAt;

    private LocalDateTime createdAt;
}
