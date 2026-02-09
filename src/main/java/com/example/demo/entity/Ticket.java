package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ticket")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false, length = 50)
    private String userName;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content", length = 500)
    private String content;

    @Column(name = "category", length = 30)
    private String category;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "contact", length = 100)
    private String contact;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
