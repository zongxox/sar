package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "COURSE")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE", nullable = false, length = 100)
    private String title;

    @Column(name = "TEACHER_NAME", nullable = false, length = 50)
    private String teacherName;

    @Column(name = "DESCRIPTION", length = 255)
    private String description;

    @Column(name = "PRICE", nullable = false)
    private Integer price;

    @Column(name = "LEVEL", length = 20)
    private String level;

    @Column(name = "MAX_STUDENTS")
    private Integer maxStudents;

    @Column(name = "IS_PUBLISHED")
    private String isPublished;

    @Column(name = "CREATED_AT", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT", nullable = false)
    private LocalDateTime updatedAt;
}
