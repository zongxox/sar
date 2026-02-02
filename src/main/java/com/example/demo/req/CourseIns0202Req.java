package com.example.demo.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseIns0202Req {

    private String title;

    private String teacherName;

    private String description;

    private Integer price;

    private List<String> level;

    private Integer maxStudents;

    private String isPublished;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
