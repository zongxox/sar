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
public class ArticleQuery0130Req {

    private String title;        // 文章標題

    private String author;       // 作者

    private List<String> category;     // 文章分類

    private String status;       // 文章狀態


}
