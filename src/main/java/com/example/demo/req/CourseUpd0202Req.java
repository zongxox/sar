package com.example.demo.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseUpd0202Req {
    private String id;

    private String title;

    private String teacherName;

    private String description;

    private String price;

    private List<String> level;

    private String maxStudents;

    private String isPublished;

    private String createdAt;

    private String updatedAt;
}
