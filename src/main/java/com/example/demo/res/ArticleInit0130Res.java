package com.example.demo.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleInit0130Res {

    private String author;       // 作者

    private String category;     // 文章分類

    private String status;       // 文章狀態

    private String statusName;       // 文章狀態

}
