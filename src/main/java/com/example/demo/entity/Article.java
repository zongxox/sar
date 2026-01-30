package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@Table(name = "articles")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;          // 文章ID

    @Column(nullable = false, length = 150)
    private String title;        // 文章標題

    @Lob
    private String content;      // 文章內容

    @Column(length = 255)
    private String summary;      // 文章摘要

    @Column(length = 50)
    private String author;       // 作者

    @Column(length = 50)
    private String category;     // 文章分類

    @Column(length = 20)
    private String status;       // 文章狀態

    private Integer views;       // 瀏覽次數

    @Column(name = "created_at")
    private LocalDateTime createdAt;  // 建立時間

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;  // 更新時間
}
