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
public class PrQuery0212DAO {
    private String receiptNo;
    private String status;
    private List<String> payMethod;
    private String source;
}
