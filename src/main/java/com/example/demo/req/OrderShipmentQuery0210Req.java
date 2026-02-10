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
public class OrderShipmentQuery0210Req {


    private String productName;


    private List<Integer> quantity;


    private String shippingAddress;


    private String status;

}
