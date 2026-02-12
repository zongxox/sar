package com.example.demo.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrPdf0212Res {
    private Long id;
    private String receiptNo;
    private String payerName;
    private String payeeName;
    private Integer amount;
    private String status;
    private String statusName;
    private String payMethod;
    private String source;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private Integer pageGroup;

    private String createTimeStr;
    private String updateTimeStr;
}
