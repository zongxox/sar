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
public class ProductUpd0203Req {
    private String id;                 // 商品ID

    private String name;             // 商品名稱

    private String description;      // 商品描述

    private String price;           // 商品價格

    private String stock;           // 庫存數量

    private List<String> category;         // 商品分類

    private String brand;            // 商品品牌

    private String sku;              // 商品編號(SKU)

    private String status;           // 商品狀態

    private String createdTime; // 建立時間

    private String updatedTime; // 更新時間
}
