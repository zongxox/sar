package com.example.demo.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrQuery0212Res {
    private Long id;
    private String receiptNo;
    private String payerName;
    private String payeeName;
    private Integer amount;
    private String status;
    private String statusName;
    private String payMethod;
    private String source;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Taipei")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Taipei")
    private LocalDateTime updateTime;
}
