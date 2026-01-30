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
public class ArticleDon0130Res {
    private Integer id;          // 文章ID

    private String title;        // 文章標題

    private String content;      // 文章內容

    private String summary;      // 文章摘要

    private String author;       // 作者

    private String category;     // 文章分類

    private String status;       // 文章狀態

    private Integer views;       // 瀏覽次數

    private LocalDateTime createdAt;  // 建立時間

    private LocalDateTime updatedAt;  // 更新時間

}
