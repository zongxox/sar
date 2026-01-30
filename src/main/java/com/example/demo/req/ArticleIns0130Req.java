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
public class ArticleIns0130Req {

    private String title;        // 文章標題

    private String content;      // 文章內容

    private String summary;      // 文章摘要

    private String author;       // 作者

    private List<String> category;     // 文章分類

    private String status;       // 文章狀態

    private String views;       // 瀏覽次數

    private String createdAt;  // 建立時間

    private String updatedAt;  // 更新時間
}
