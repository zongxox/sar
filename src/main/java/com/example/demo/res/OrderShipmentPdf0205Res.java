package com.example.demo.res;

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
public class OrderShipmentPdf0205Res {

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

    private String shippedAtStr;

    private String createdAtStr;
}
