package com.example.demo.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseQuery0202Res {

    private Long id;

    private String title;

    private String teacherName;

    private String description;

    private Integer price;

    private String level;

    private Integer maxStudents;

    private String isPublished;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String isPublishedName;
}
