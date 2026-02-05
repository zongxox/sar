package com.example.demo.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderShipmentInit0205Res {
    private Integer quantity;

    private String shippingAddress;

    private String status;

    private String statusName;

}
