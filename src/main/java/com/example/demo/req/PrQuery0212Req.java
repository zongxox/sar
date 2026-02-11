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
public class PrQuery0212Req {
    private String receiptNo;
    private String status;
    private List<String> payMethod;
    private String source;
}
